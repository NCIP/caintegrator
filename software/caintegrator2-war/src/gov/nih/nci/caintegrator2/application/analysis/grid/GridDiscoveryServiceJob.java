/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis.grid;

import gov.nih.nci.cagrid.metadata.exceptions.ResourcePropertyRetrievalException;
import gov.nih.nci.caintegrator2.common.ConfigurationHelper;
import gov.nih.nci.caintegrator2.common.ConfigurationParameter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Grid discovery job to query available services.
 */
@SuppressWarnings("PMD.CyclomaticComplexity") // Check for multiple grid services
public class GridDiscoveryServiceJob extends QuartzJobBean {

    private static final Logger LOGGER = Logger.getLogger(GridDiscoveryServiceJob.class);

    private static GridDiscoveryClient gridDiscoveryClient;
    private static ConfigurationHelper configurationHelper;
    private static Map<String, String> gridNbiaServices
        = Collections.synchronizedMap(new HashMap<String, String>());
    private static Map<String, String> gridPreprocessServices
        = Collections.synchronizedMap(new HashMap<String, String>());
    private static Map<String, String> gridCmsServices
        = Collections.synchronizedMap(new HashMap<String, String>());
    private static Map<String, String> gridPcaServices
        = Collections.synchronizedMap(new HashMap<String, String>());
    private static Map<String, String> gridCaDnaCopyServices 
        = Collections.synchronizedMap(new HashMap<String, String>());
    private static Map<String, String> gridGisticServices 
        = Collections.synchronizedMap(new HashMap<String, String>());

    /**
     * {@inheritDoc}
     */
    @Override
    protected void executeInternal(JobExecutionContext context)
        throws JobExecutionException {
        queryGridServices();
    }
    
    private static void queryGridServices() {
        try {
            // Get all services
            EndpointReferenceType[] searchedServices = gridDiscoveryClient.getServices();
            if (searchedServices != null) {
                clearServices();
                for (EndpointReferenceType epr : searchedServices) {
                    extractSelectedServices(epr);
                }
            }
            setDefaultServices();
        } catch (MalformedURIException e) {
            LOGGER.error("Error getting directory from GridDiscoveryClient", e);
            setDefaultServices();
        } catch (ResourcePropertyRetrievalException e) {
            LOGGER.error("Error getting directory from GridDiscoveryClient", e);
            setDefaultServices();
        }
    }

    private static void extractSelectedServices(EndpointReferenceType epr)  {
        String url = gridDiscoveryClient.getAddress(epr);
        String hostingCenter = "Unknown hosting center";
        try {
            hostingCenter = gridDiscoveryClient.getHostinCenter(epr);
        } catch (ResourcePropertyRetrievalException e) {
            LOGGER.warn("Error getting hosting center for grid service URL" + url + ": " + e.getMessage());
        }
        extractSelectedServices(hostingCenter, url);
    }
    
    private static void clearServices() {
        gridNbiaServices.clear();
        gridPreprocessServices.clear();
        gridCmsServices.clear();
        gridPcaServices.clear();
        gridCaDnaCopyServices.clear();
        gridGisticServices.clear();
    }
    
    private static void setDefaultServices() {
        setDefaultNbiaService();
        setDefaultPreprocessService();
        setDefaultCmsService();
        setDefaultPcaService();
        setDefaultCaDnaCopyService();
        setDefaultGisticService();
    }
    
    private static void setDefaultNbiaService() {
        if (gridNbiaServices.isEmpty()) {
            String defaultUrl = configurationHelper.getString(ConfigurationParameter.NBIA_URL);
            gridNbiaServices.put(defaultUrl, "Default NBIA service - " + defaultUrl);
        }
    }
    
    private static void setDefaultPreprocessService() {
        if (gridPreprocessServices.isEmpty()) {
            String defaultUrl = configurationHelper.getString(ConfigurationParameter.PREPROCESS_DATASET_URL);
            gridPreprocessServices.put(defaultUrl, "Default Broad service - " + defaultUrl);
        }
    }
    
    private static void setDefaultCmsService() {
        if (gridCmsServices.isEmpty()) {
            String defaultUrl = configurationHelper.getString(ConfigurationParameter.COMPARATIVE_MARKER_SELECTION_URL);
            gridCmsServices.put(defaultUrl, "Default Broad service - " + defaultUrl);
        }
    }

    private static void setDefaultPcaService() {
        if (gridPcaServices.isEmpty()) {
            String defaultUrl = configurationHelper.getString(ConfigurationParameter.PCA_URL);
            gridPcaServices.put(defaultUrl, "Default Broad service - " + defaultUrl);
        }
    }
    
    private static void setDefaultCaDnaCopyService() {
        if (gridCaDnaCopyServices.isEmpty()) {
            String defaultUrl = configurationHelper.getString(ConfigurationParameter.CA_DNA_COPY_URL);
            gridCaDnaCopyServices.put(defaultUrl, "Default Bioconductor service - " + defaultUrl);
        }
    }
    
    private static void setDefaultGisticService() {
        if (gridGisticServices.isEmpty()) {
            String defaultUrl = configurationHelper.getString(ConfigurationParameter.GISTIC_URL);
            gridGisticServices.put(defaultUrl, "Default GISTIC service - " + defaultUrl);
        }
    }

    @SuppressWarnings("PMD.CyclomaticComplexity") // Check for multiple grid services
    private static void extractSelectedServices(String hostingCenter, String url) {
        if (url.contains("MAGES")) {
            extractCmsServices(hostingCenter, url);
        } else if (shouldAdd(url, "PCA", gridPcaServices)) {
            gridPcaServices.put(url, buildDisplayName(hostingCenter, url));
        } else if (shouldAdd(url, "CaDNAcopy", gridCaDnaCopyServices)) {
            gridCaDnaCopyServices.put(url, buildDisplayName(hostingCenter, url));
        } else if (shouldAdd(url, "Gistic", gridGisticServices)) {
            gridGisticServices.put(url, buildDisplayName(hostingCenter, url));
        } else if (shouldAdd(url, "NCIA", gridNbiaServices)) {
            gridNbiaServices.put(url, buildDisplayName(hostingCenter, url));
        }
    }

    private static String buildDisplayName(String hostingCenter, String url) {
        return hostingCenter + " - " + url;
    }

    private static boolean shouldAdd(String url, String serviceName, Map<String, String> serviceMap) {
        return url.contains(serviceName) && !serviceMap.containsKey(url);
    }
    
    private static void extractCmsServices(String hostingCenter, String url) {
        if (url.contains("Comparative")
                && !gridCmsServices.containsKey(url)) {
            gridCmsServices.put(url, buildDisplayName(hostingCenter, url));
        } else if (url.contains("Preprocess")
                && !gridPreprocessServices.containsKey(url)) {
            gridPreprocessServices.put(url, buildDisplayName(hostingCenter, url));
        }
    }

    /**
     * @return the gridNbiaServices
     */
    public static Map<String, String> getGridNbiaServices() {
        if (gridNbiaServices.isEmpty()) {
            setDefaultNbiaService();
        }
        return gridNbiaServices;
    }

    /**
     * @return the gridPreprocessServices
     */
    public static Map<String, String> getGridPreprocessServices() {
        if (gridPreprocessServices.isEmpty()) {
            setDefaultPreprocessService();
        }
        return gridPreprocessServices;
    }

    /**
     * @return the gridCmsServices
     */
    public static Map<String, String> getGridCmsServices() {
        if (gridCmsServices.isEmpty()) {
            setDefaultCmsService();
        }
        return gridCmsServices;
    }

    /**
     * @return the gridPcaServices
     */
    public static Map<String, String> getGridPcaServices() {
        if (gridPcaServices.isEmpty()) {
            setDefaultPcaService();
        }
        return gridPcaServices;
    }

    /**
     * @return the gridCaDnaCopyServices
     */
    public static Map<String, String> getGridCaDnaCopyServices() {
        if (gridCaDnaCopyServices.isEmpty()) {
            setDefaultCaDnaCopyService();
        }
        return gridCaDnaCopyServices;
    }

    /**
     * @return the gridGisticServices
     */
    public static Map<String, String> getGridGisticServices() {
        if (gridGisticServices.isEmpty()) {
            setDefaultGisticService();
        }
        return gridGisticServices;
    }

    /**
     * @param gridDiscoveryClient the gridDiscoveryClient to set
     */
    public void setGridDiscoveryClient(GridDiscoveryClient gridDiscoveryClient) {
        GridDiscoveryServiceJob.gridDiscoveryClient = gridDiscoveryClient;
    }

    /**
     * @param configurationHelper the configurationHelper to set
     */
    public void setConfigurationHelper(ConfigurationHelper configurationHelper) {
        GridDiscoveryServiceJob.configurationHelper = configurationHelper;
    }

}
