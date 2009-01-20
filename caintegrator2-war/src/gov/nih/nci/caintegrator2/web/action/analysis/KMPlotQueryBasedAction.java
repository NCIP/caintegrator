/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The caArray
 * Software was developed in conjunction with the National Cancer Institute 
 * (NCI) by NCI employees, 5AM Solutions, Inc. (5AM), ScenPro, Inc. (ScenPro)
 * and Science Applications International Corporation (SAIC). To the extent 
 * government employees are authors, any rights in such works shall be subject 
 * to Title 17 of the United States Code, section 105. 
 *
 * This caArray Software License (the License) is between NCI and You. You (or 
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
 * its rights in the caArray Software to (i) use, install, access, operate, 
 * execute, copy, modify, translate, market, publicly display, publicly perform,
 * and prepare derivative works of the caArray Software; (ii) distribute and 
 * have distributed to and by third parties the caIntegrator Software and any 
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
package gov.nih.nci.caintegrator2.web.action.analysis;


import gov.nih.nci.caintegrator2.application.analysis.KMQueryBasedParameters;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlot;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlotTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.web.SessionHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Action dealing with Kaplan-Meier Query Based plotting.
 */
@SuppressWarnings("PMD.CyclomaticComplexity") // See retrieveFormSelectedValues()
public class KMPlotQueryBasedAction extends AbstractKaplanMeierAction {

    private static final long serialVersionUID = 1L;

    private KMQueryBasedParameters kmPlotParameters = new KMQueryBasedParameters();
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        super.prepare();
        setDisplayTab(QUERY_TAB);
        retrieveFormValues();
        refreshObjectInstances();
        populateQueries();
    }
    
    
    private void retrieveFormValues() {
        kmPlotParameters.setExclusiveGroups(getForm().isExclusiveGroups());
        kmPlotParameters.setAddPatientsNotInQueriesGroup(getForm().isAddPatientsNotInQueriesGroup());
        if (!getForm().getSelectedQueryIDs().isEmpty()) {
            kmPlotParameters.getQueries().clear();
            for (String id : getForm().getSelectedQueryIDs()) {
                Query query = new Query();
                query.setId(Long.valueOf(id));
                kmPlotParameters.getQueries().add(query);
            }
        }
    }
    
    private void refreshObjectInstances() {
        if (!kmPlotParameters.getQueries().isEmpty()) {
            List <Query> newValues = new ArrayList<Query>();
            for (Query value : kmPlotParameters.getQueries()) {
                Query newValue = getStudyManagementService().getRefreshedStudyEntity(value);
                newValues.add(newValue);
            }
            kmPlotParameters.getQueries().clear();
            kmPlotParameters.getQueries().addAll(newValues);
        }
    }
    
    private void populateQueries() {
        initialize();
        loadSelectedQueries();
    }

    private void initialize() {
        if (getStudySubscription() != null 
            && getStudySubscription().getQueryCollection() != null
            && getKmPlotForm().getQueryBasedForm().getSelectedQueries().isEmpty() 
            && getKmPlotForm().getQueryBasedForm().getUnselectedQueries().isEmpty()) {
            getKmPlotForm().getQueryBasedForm().setUnselectedQueries(new HashMap<String, Query>());
            for (Query query 
                    : getStudySubscription().getQueryCollection()) {
                getKmPlotForm().getQueryBasedForm().getUnselectedQueries().
                                                    put(query.getId().toString(), query);
            }
        }
    }
    
    private void loadSelectedQueries() {
        if (!kmPlotParameters.getQueries().isEmpty()) {
            getKmPlotForm().getQueryBasedForm().getSelectedQueries().clear();
            Set<Query> usedQueries = new HashSet<Query>();
            for (Query query : kmPlotParameters.getQueries()) {
                getKmPlotForm().getQueryBasedForm().getSelectedQueries().put(query.getId().toString(), query);
                usedQueries.add(query);
            }
            loadAvailableQueries(usedQueries);
        }
    }

    private void loadAvailableQueries(Set<Query> usedQueries) {
        getKmPlotForm().getQueryBasedForm().getUnselectedQueries().clear();
        for (Query query 
                : getStudySubscription().getQueryCollection()) {
            if (!usedQueries.contains(query)) {
                getKmPlotForm().getQueryBasedForm().getUnselectedQueries().
                                                put(query.getId().toString(), query);
            }
        }
    }


    /**
     * Clears all input values and km plots on the session.
     * @return Struts return value.
     */
    public String reset() {
        clearQueryBasedKmPlot();
        getForm().clear();
        kmPlotParameters.clear();
        return SUCCESS;
    }

    private void clearQueryBasedKmPlot() {
        SessionHelper.setKmPlot(KMPlotTypeEnum.QUERY_BASED, null);
    }

    /**
     * Used to bring up the input form.
     * @return Struts return value.
     */
    public String input() {
        setDisplayTab(QUERY_TAB);
        return SUCCESS;
    }

    /**
     * When the form is filled out and the user clicks "Create Plot" this calls the
     * analysis service to generate a KMPlot object.
     * @return Struts return value.
     */
    public String createPlot() {
        if (!kmPlotParameters.validate()) {
            for (String errorMessages : kmPlotParameters.getErrorMessages()) {
                addActionError(errorMessages);
            }
            return INPUT;
        }
        KMPlot plot = getAnalysisService().createKMPlot(getStudySubscription(), kmPlotParameters);
        SessionHelper.setKmPlot(KMPlotTypeEnum.QUERY_BASED, plot);
        return SUCCESS;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Map<String, String>> getAllStringPValues() {
        if (SessionHelper.getQueryBasedKmPlot() != null) {
            return retrieveAllStringPValues(SessionHelper.getQueryBasedKmPlot());
        }
        return new HashMap<String, Map<String, String>>();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCreatable() {
        return true;
    }


    /**
     * @return
     */
    private KMPlotQueryBasedActionForm getForm() {
        return getKmPlotForm().getQueryBasedForm();
    }

    /**
     * @return the kmPlotParameters
     */
    @SuppressWarnings("unchecked")
    @Override
    public KMQueryBasedParameters getKmPlotParameters() {
        return kmPlotParameters;
    }

    /**
     * @param kmPlotParameters the kmPlotParameters to set
     */
    public void setKmPlotParameters(KMQueryBasedParameters kmPlotParameters) {
        this.kmPlotParameters = kmPlotParameters;
    }

}
