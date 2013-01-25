/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.ajax;

import java.util.HashMap;
import java.util.Map;

import org.directwebremoting.proxy.dwr.Util;

/**
 * Application scope factory which will store and return Dwr Util objects for specific users on different
 * <code>PersistedJob</code>types.
 */
public class DwrUtilFactory {

    private final Map <String, Util> analysisUsernameUtilityMap = new HashMap<String, Util>();
    private final Map <String, Util> studyConfigurationUtilityMap = new HashMap<String, Util>();
    private final Map <String, Util> genomicDataSourceUtilityMap = new HashMap<String, Util>();
    private final Map <String, Util> imagingDataSourceUtilityMap = new HashMap<String, Util>();
    private final Map <String, Util> platformConfigurationUtilityMap = new HashMap<String, Util>();
    
    /**
     * Retrieves the DWR utility object for an Analysis Job.
     * @param username to retrieve utility for.
     * @return DWR Utility
     */
    public Util retrieveAnalysisJobUtil(String username) {
        return retrieveDwrUtil(username, analysisUsernameUtilityMap);
    }
    
    /**
     * Retrieves the DWR utility object for a Study Configuration job.
     * @param username to retrieve utility for.
     * @return DWR Utility
     */
    public Util retrieveStudyConfigurationUtil(String username) {
        return retrieveDwrUtil(username, studyConfigurationUtilityMap);
    }
    
    /**
     * Retrieves the DWR utility object for a Platform Configuration job.
     * @param username to retrieve utility for.
     * @return DWR Utility
     */
    public Util retrievePlatformConfigurationUtil(String username) {
        return retrieveDwrUtil(username, platformConfigurationUtilityMap);
    }
    
    /**
     * Retrieves the DWR utility object for a GenomicDataSourceConfiguration job.
     * @param username to retrieve utility for.
     * @return DWR Utility
     */
    public Util retrieveGenomicDataSourceUtil(String username) {
        return retrieveDwrUtil(username, genomicDataSourceUtilityMap);
    }
    
    /**
     * Retrieves the DWR utility object for an ImageDataSourceConfiguration job.
     * @param username to retrieve utility for.
     * @return DWR Utility
     */
    public Util retrieveImagingDataSourceUtil(String username) {
        return retrieveDwrUtil(username, imagingDataSourceUtilityMap);
    }
    
    private Util retrieveDwrUtil(String username, Map<String, Util> map) {
        return (map.get(username) == null) 
            ? new Util() : map.get(username);
    }
    
    /**
     * Associates a username with a session for the <code>AbstractPersistedAnalysisJob</code>. 
     * @param username for current user.
     * @param util dwr object.
     */
    public void associateAnalysisJobWithSession(String username, Util util) {
        analysisUsernameUtilityMap.put(username, util);
    }
    
    
    /**
     * Associates a username with a session for the <code>StudyDeploymentJob</code>. 
     * @param username for current user.
     * @param util dwr object.
     */
    public void associateStudyConfigurationJobWithSession(String username, Util util) {
        studyConfigurationUtilityMap.put(username, util);
    }
    
    /**
     * Associates a username with a session for the <code>PlatformDeploymentJob</code>. 
     * @param username for current user.
     * @param util dwr object.
     */
    public void associatePlatformConfigurationJobWithSession(String username, Util util) {
        platformConfigurationUtilityMap.put(username, util);
    }
    
    /**
     * Associates a username with a session for the <code>GenomicDataSourceConfiguration</code>. 
     * @param username for current user.
     * @param util dwr object.
     */
    public void associateGenomicDataSourceWithSession(String username, Util util) {
        genomicDataSourceUtilityMap.put(username, util);
    }
    
    /**
     * Associates a username with a session for the <code>ImageDataSourceConfiguration</code>. 
     * @param username for current user.
     * @param util dwr object.
     */
    public void associateImagingDataSourceWithSession(String username, Util util) {
        imagingDataSourceUtilityMap.put(username, util);
    }
    

}
