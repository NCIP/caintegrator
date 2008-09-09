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
package gov.nih.nci.caintegrator2.web.action.query;

import java.util.HashSet;
//import java.util.List;
import java.util.Set;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

//import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyManagementService;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;

// import things needed for creating test results
//import gov.nih.nci.caintegrator2.web.action.query.StudyHelper;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeriesAcquisition;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.application.ResultValue;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractAnnotationValue;

/**
 * Edits a study (new or existing).
 */
public class EditQueryAction extends ActionSupport implements Preparable {

    private static final long serialVersionUID = 1L;
    
    private StudyManagementService studyManagementService;   
    private QueryResult queryResult = new QueryResult();
    private String injectTest = "no";
    
    /**
     * The 'prepare' interceptor will look for this method enabling 
     * preprocessing.
     */
    public void prepare() {
        EditQueryHelper editQueryHelper = new EditQueryHelper();
        editQueryHelper.prepopulateAnnotationSelectLists(studyManagementService);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String execute()  {
        
        // declarations and such
        final Long id;
        final Query query = new Query();
        
        // obtain Query results
        
        //check if test data is requested
        if (getInjectTest().equals("yes")) {
            //create test results for now
            id = Long.parseLong("123");
            queryResult.setId(id);
            query.setName("cai2 Test Query - basic");
            query.setDescription("This is test query composed for testing inside the action class");
            queryResult.setQuery(query);
            queryResult.setRowCollection(getTestResultRows());     
        } else if (getInjectTest().equals("vasari")) {
            // create and run more realistic vasari query           
            query.setName("cai2 Test Query - vasari");
            query.setDescription("This is test query composed for testing inside the action class");
            queryResult.setQuery(query);            
        }
        
        // write query result object into the session scope
        
        return SUCCESS;
    }
    
    /**
     * Struts Setter method for the QueryResult which will be displayed.
     * @param qR the query result to be set
     */
    public void setQueryResult(QueryResult qR) {
        this.queryResult = qR;
    }
    
    /**
     * Gets the QueryResult to be handled by struts.
     * @return final result from the rows.
     */
    public QueryResult getQueryResult() {
        return queryResult;
    }
    
    /**
     * Struts Setter method for the test injection parameter.
     * This parameter determines if test values are used for query results.
     * @param iP the test selection parameter.
     */
    public void setInjectTest(String iP) {
        this.injectTest = iP;
    }
    
    /**
     * Struts getter method for the test injection parameter.
     * This parameter determines if test values are used for query results.
     * @return the test selection parameter.
     */
    public String getInjectTest() {
        return injectTest;
    }
    
    /**
     * @return resultRows
     */
    public final Set<ResultRow> getTestResultRows() {
        
        //Create dummy results rows
        Set<ResultRow> resultRows = new HashSet<ResultRow>();
        
        ResultRow row1 = new ResultRow();       
        ImageSeriesAcquisition imageSeriesAcquisition1 = new ImageSeriesAcquisition();
        StudySubjectAssignment studySubjectAssignment1 = new StudySubjectAssignment();
        SampleAcquisition sampleAcquisition1 = new SampleAcquisition();
        Sample sample1 = new Sample();
        ResultValue resultValue1 = new ResultValue();
        ResultColumn resultColumn1 = new ResultColumn();
        AbstractAnnotationValue abstractAnnotationValue1 = new AbstractAnnotationValue();
        Set<ResultValue> resultValuesCollection1 = new HashSet<ResultValue>();
        
        row1.setImageSeriesAcquisition(imageSeriesAcquisition1);
        studySubjectAssignment1.setIdentifier("SubjectID1");
        row1.setSubjectAssignment(studySubjectAssignment1);
        sample1.setName("SampleName1");
        sampleAcquisition1.setSample(sample1);
        row1.setSampleAcquisition(sampleAcquisition1);
        row1.getSampleAcquisition().getSample().setName("SampleId1");
        resultColumn1.setAnnotationDefinition(new AnnotationDefinition());
        resultValue1.setValue(abstractAnnotationValue1);
        row1.setValueCollection(resultValuesCollection1);
        row1.getValueCollection().add(resultValue1);
        resultRows.add(row1);
        
        return resultRows;
    }
    
    /**
     * @return the studyManagementService
     */
    public StudyManagementService getStudyManagementService() {
        return studyManagementService;
    }

    /**
     * @param studyManagementService the studyManagementService to set
     */
    public void setStudyManagementService(StudyManagementService studyManagementService) {
        this.studyManagementService = studyManagementService;
    }
    
}
