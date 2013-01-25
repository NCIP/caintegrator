/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.external.cadsr;

import gov.nih.nci.cadsr.domain.EnumeratedValueDomain;
import gov.nih.nci.cadsr.domain.ValueDomainPermissibleValue;
import gov.nih.nci.cadsr.domain.ValueMeaning;
import gov.nih.nci.cadsr.freestylesearch.util.Search;
import gov.nih.nci.cadsr.freestylesearch.util.SearchAC;
import gov.nih.nci.cadsr.freestylesearch.util.SearchResults;
import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.domain.annotation.CommonDataElement;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.ValueDomain;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;


/**
 * Implements the CaDSRFacade interface.
 */
public class CaDSRFacadeImpl implements CaDSRFacade {


    private Search search;
    private String dataDescription;
    private CaDSRApplicationServiceFactory caDsrApplicationServiceFactory;
    private String caDsrUrl;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CommonDataElement> retreiveCandidateDataElements(List<String> keywords) {

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < keywords.size(); ++i) {
            sb.append(keywords.get(i));
            if (i != keywords.size() - 1) {
                sb.append(' ');
            }
        }

        String keywordsString = sb.toString();
        search.setDataDescription(dataDescription);
        // Restrict results to only Data Elements
        search.restrictResultsByType(SearchAC.DE);
        search.excludeWorkflowStatusRetired(true);
        search.excludeTest(true);
        search.excludeTraining(true);
        return convertSearchResultsToDataElements(search.findReturningSearchResults(keywordsString));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommonDataElement retrieveDataElement(Long dataElementId, Float dataElementVersion)
    throws ConnectionException {
        // Satish Patel informed me that the cadsrApi clears out the SecurityContext after they're done with it,
        // they are working on a fix but as a workaround I need to store the context and set it again after
        // I am done with all of the caDSR domain objects.  --TJ
        SecurityContext originalContext = SecurityContextHolder.getContext();
        try {
            List<Object> cadsrDataElements = searchDataElement(dataElementId, dataElementVersion);
            gov.nih.nci.cadsr.domain.DataElement latestCadsrDataElement = getLatestDataElement(cadsrDataElements);
            return convertCadsrDataElementToDataElements(latestCadsrDataElement);
        } catch (Exception e) {
            throw new ConnectionException("Couldn't connect to the caDSR server", e);
        } finally {
            // Restore context as described above.
            SecurityContextHolder.setContext(originalContext);
        }
    }

    private List<Object> searchDataElement(Long dataElementId, Float dataElementVersion)
            throws ConnectionException, ApplicationException {
        // Won't have to provide a URL when it goes from stage to production.
        ApplicationService cadsrApi = caDsrApplicationServiceFactory.retrieveCaDsrApplicationService(caDsrUrl);

        gov.nih.nci.cadsr.domain.DataElement cadsrDataElement = new gov.nih.nci.cadsr.domain.DataElement();
        cadsrDataElement.setPublicID(dataElementId);
        cadsrDataElement.setVersion(dataElementVersion);
        List<Object> cadsrDataElements = cadsrApi.search(
                gov.nih.nci.cadsr.domain.DataElement.class, cadsrDataElement);
        return cadsrDataElements;
    }

    private gov.nih.nci.cadsr.domain.DataElement getLatestDataElement(List<Object> cadsrDataElements) {
        gov.nih.nci.cadsr.domain.DataElement latestCadsrDataElement = null;
        for (Object object : cadsrDataElements) {
            gov.nih.nci.cadsr.domain.DataElement dataElement = (gov.nih.nci.cadsr.domain.DataElement) object;
            if (latestCadsrDataElement == null
                    || dataElement.getVersion() > latestCadsrDataElement.getVersion()) {
                latestCadsrDataElement = dataElement;
            }
        }
        return latestCadsrDataElement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ValueDomain retrieveValueDomainForDataElement(Long dataElementId, Float dataElementVersion)
    throws ConnectionException {
        ValueDomain valueDomain = new ValueDomain();
        // Satish Patel informed me that the cadsrApi clears out the SecurityContext after they're done with it,
        // they are working on a fix but as a workaround I need to store the context and set it again after
        // I am done with all of the caDSR domain objects.  --TJ
        SecurityContext originalContext = SecurityContextHolder.getContext();
        try {
            List<Object> cadsrDataElements = searchDataElement(dataElementId, dataElementVersion);

            if (!cadsrDataElements.isEmpty()) {
                gov.nih.nci.cadsr.domain.ValueDomain cadsrValueDomain =
                    ((gov.nih.nci.cadsr.domain.DataElement) cadsrDataElements.get(0)).getValueDomain();
                // Default will be String, Character, Alphanumeric, and Alpha DVG
                AnnotationTypeEnum annotationType = AnnotationTypeEnum.STRING;
                String cadsrDataType = cadsrValueDomain.getDatatypeName();
                if (cadsrDataType.matches("DATE")) {
                    annotationType = AnnotationTypeEnum.DATE;
                }
                if (cadsrDataType.matches("NUMBER")) {
                    annotationType = AnnotationTypeEnum.NUMERIC;
                }
                valueDomain.setDataType(annotationType);
                valueDomain.setLongName(cadsrValueDomain.getLongName());
                valueDomain.setPublicID(cadsrValueDomain.getPublicID());
                if (cadsrValueDomain instanceof EnumeratedValueDomain) {
                    retrievePermissibleValues(valueDomain, cadsrValueDomain);
                }
            }
            return valueDomain;
        } catch (Exception e) {
            throw new ConnectionException("Couldn't connect to the caDSR server", e);
        } finally {
            // Restore context as described above.
            SecurityContextHolder.setContext(originalContext);
        }
    }

    private void retrievePermissibleValues(ValueDomain valueDomain,
                                           gov.nih.nci.cadsr.domain.ValueDomain cadsrValueDomain) {
        EnumeratedValueDomain enumeratedValueDomain = (EnumeratedValueDomain) cadsrValueDomain;
        Collection<ValueDomainPermissibleValue> valueDomainPermissibleValues =
                                enumeratedValueDomain.getValueDomainPermissibleValueCollection();
        for (ValueDomainPermissibleValue valueDomainPermissibleValue : valueDomainPermissibleValues) {
            gov.nih.nci.cadsr.domain.PermissibleValue cadsrPermissibleValue =
                valueDomainPermissibleValue.getPermissibleValue();
            String stringValue = cadsrPermissibleValue.getValue();
            PermissibleValue permissibleValue = new PermissibleValue();
            permissibleValue.setValue(stringValue);

            if (cadsrPermissibleValue.getValueMeaning() != null) {
                ValueMeaning valueMeaning = cadsrPermissibleValue.getValueMeaning();
                permissibleValue.setValueMeaning(valueMeaning.getDescription());
            }
            valueDomain.getPermissibleValueCollection().add(permissibleValue);
        }
    }

    private CommonDataElement convertCadsrDataElementToDataElements(
            gov.nih.nci.cadsr.domain.DataElement de) {
        CommonDataElement cde = null;
        if (de != null) {
            cde = new CommonDataElement();
            cde.setDefinition(de.getPreferredDefinition());
            cde.setLongName(de.getLongName());
            cde.setPublicID(Long.valueOf(de.getPublicID()));
            cde.setContextName(de.getContext().getName());
            cde.setPreferredName(de.getPreferredName());
            cde.setRegistrationStatus(de.getRegistrationStatus());
            cde.setVersion(de.getVersion().toString());
            cde.setWorkflowStatus(de.getWorkflowStatusName());
        }
        return cde;
    }

    private List<CommonDataElement> convertSearchResultsToDataElements(List<SearchResults> searchResults) {
        List<CommonDataElement> dataElements = new ArrayList<CommonDataElement>();
        CommonDataElement de;
        for (SearchResults sr : searchResults) {
            de = new CommonDataElement();
            de.setDefinition(sr.getPreferredDefinition());
            de.setLongName(sr.getLongName());
            de.setPublicID(Long.valueOf(sr.getPublicID()));
            de.setContextName(sr.getContextName());
            de.setPreferredName(sr.getPreferredName());
            de.setRegistrationStatus(sr.getRegistrationStatus());
            de.setVersion(sr.getVersion());
            de.setWorkflowStatus(sr.getWorkflowStatus());
            dataElements.add(de);
        }
        return dataElements;
    }

    /**
     * @param search the search to set
     */
    public void setSearch(Search search) {
        this.search = search;
    }

    /**
     * @param dataDescription the dataDescription to set
     */
    public void setDataDescription(String dataDescription) {
        this.dataDescription = dataDescription;
    }

    /**
     * @param caDsrUrl the caDsrUrl to set
     */
    public void setCaDsrUrl(String caDsrUrl) {
        this.caDsrUrl = caDsrUrl;
    }

    /**
     * @param caDsrApplicationServiceFactory the caDsrApplicationServiceFactory to set
     */
    public void setCaDsrApplicationServiceFactory(CaDSRApplicationServiceFactory caDsrApplicationServiceFactory) {
        this.caDsrApplicationServiceFactory = caDsrApplicationServiceFactory;
    }
}
