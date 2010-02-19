/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The caIntegrator2
 * Software was developed in conjunction with the National Cancer Institute 
 * (NCI) by NCI employees, 5AM Solutions, Inc. (5AM), ScenPro, Inc. (ScenPro)
 * and Science Applications International Corporation (SAIC). To the extent 
 * government employees are authors, any rights in such works shall be subject 
 * to Title 17 of the United States Code, section 105. 
 *
 * This caIntegrator2 Software License (the License) is between NCI and You. You (or 
 * Your) shall mean a person or an entity, and all other entities that control, 
 * are controlled by, or are under common control with the entity. Control for 
 * purposes of this definition means (i) the direct or indirect power to cause 
 * the direction or management of such entity, whether by contract or otherwise,
 * or (ii) ownership of fifty percent (50%) or more of the outstanding shares, 
 * or (iii) beneficial ownership of such entity. 
 *
 * This License is granted provided that You agree to the conditions described 
 * below. NCI grants You a non-exclusive, worldwide, perpetual, fully-paid-up, 
 * no-charge, irrevocable, transferable and royalty-free right and license in 
 * its rights in the caIntegrator2 Software to (i) use, install, access, operate, 
 * execute, copy, modify, translate, market, publicly display, publicly perform,
 * and prepare derivative works of the caIntegrator2 Software; (ii) distribute and 
 * have distributed to and by third parties the caIntegrator2 Software and any 
 * modifications and derivative works thereof; and (iii) sublicense the 
 * foregoing rights set out in (i) and (ii) to third parties, including the 
 * right to license such rights to further third parties. For sake of clarity, 
 * and not by way of limitation, NCI shall have no right of accounting or right 
 * of payment from You or Your sub-licensees for the rights granted under this 
 * License. This License is granted at no charge to You.
 *
 * Your redistributions of the source code for the Software must retain the 
 * above copyright notice, this list of conditions and the disclaimer and 
 * limitation of liability of Article 6, below. Your redistributions in object 
 * code form must reproduce the above copyright notice, this list of conditions 
 * and the disclaimer of Article 6 in the documentation and/or other materials 
 * provided with the distribution, if any. 
 *
 * Your end-user documentation included with the redistribution, if any, must 
 * include the following acknowledgment: This product includes software 
 * developed by 5AM, ScenPro, SAIC and the National Cancer Institute. If You do 
 * not include such end-user documentation, You shall include this acknowledgment 
 * in the Software itself, wherever such third-party acknowledgments normally 
 * appear.
 *
 * You may not use the names "The National Cancer Institute", "NCI", "ScenPro",
 * "SAIC" or "5AM" to endorse or promote products derived from this Software. 
 * This License does not authorize You to use any trademarks, service marks, 
 * trade names, logos or product names of either NCI, ScenPro, SAID or 5AM, 
 * except as required to comply with the terms of this License. 
 *
 * For sake of clarity, and not by way of limitation, You may incorporate this 
 * Software into Your proprietary programs and into any third party proprietary 
 * programs. However, if You incorporate the Software into third party 
 * proprietary programs, You agree that You are solely responsible for obtaining
 * any permission from such third parties required to incorporate the Software 
 * into such third party proprietary programs and for informing Your a
 * sub-licensees, including without limitation Your end-users, of their 
 * obligation to secure any required permissions from such third parties before 
 * incorporating the Software into such third party proprietary software 
 * programs. In the event that You fail to obtain such permissions, You agree 
 * to indemnify NCI for any claims against NCI by such third parties, except to 
 * the extent prohibited by law, resulting from Your failure to obtain such 
 * permissions. 
 *
 * For sake of clarity, and not by way of limitation, You may add Your own 
 * copyright statement to Your modifications and to the derivative works, and 
 * You may provide additional or different license terms and conditions in Your 
 * sublicenses of modifications of the Software, or any derivative works of the 
 * Software as a whole, provided Your use, reproduction, and distribution of the
 * Work otherwise complies with the conditions stated in this License.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, 
 * (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY, 
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO 
 * EVENT SHALL THE NATIONAL CANCER INSTITUTE, 5AM SOLUTIONS, INC., SCENPRO, INC.,
 * SCIENCE APPLICATIONS INTERNATIONAL CORPORATION OR THEIR 
 * AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.nih.nci.caintegrator2.external.cadsr;

import gov.nih.nci.cadsr.domain.AdministeredComponent;
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
@SuppressWarnings("PMD.CyclomaticComplexity")
public class CaDSRFacadeImpl implements CaDSRFacade {
    
    
    private Search search;
    private String dataDescription;
    private CaDSRApplicationServiceFactory caDsrApplicationServiceFactory;
    private String caDsrUrl;

    /**
     * {@inheritDoc}
     */
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
    @SuppressWarnings({ "PMD.ExcessiveMethodLength", "PMD.CyclomaticComplexity" }) // Type checking and casting
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
    
    @SuppressWarnings({"PMD.UnusedPrivateMethod", "unused" }) // This might be used later, but might not.
    private List<CommonDataElement> convertAdministeredComponentsToDataElements(List<AdministeredComponent> acs) {
        List<CommonDataElement> dataElements = new ArrayList<CommonDataElement>();
        CommonDataElement de;
        for (AdministeredComponent ac : acs) {
            de = new CommonDataElement();
            de.setDefinition(ac.getPreferredDefinition());
            de.setLongName(ac.getLongName());
            de.setPublicID(Long.valueOf(ac.getPublicID()));
            de.setContextName(ac.getContext().getName());
            de.setPreferredName(ac.getPreferredName());
            de.setRegistrationStatus(ac.getRegistrationStatus());
            de.setVersion(String.valueOf(ac.getVersion()));
            de.setWorkflowStatus(ac.getWorkflowStatusName());
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
