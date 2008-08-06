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
package gov.nih.nci.caintegrator2.data;

import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AnnotationFieldType;
import gov.nih.nci.caintegrator2.application.study.EntityTypeEnum;
import gov.nih.nci.caintegrator2.application.study.NumericComparisonOperatorEnum;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.DateAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.NumericAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.StringAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.SubjectAnnotation;
import gov.nih.nci.caintegrator2.domain.application.NumericComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.StringComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeriesAcquisition;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.springframework.test.AbstractTransactionalSpringContextTests;

public final class CaIntegrator2DaoTestIntegration extends AbstractTransactionalSpringContextTests {
    
    private CaIntegrator2Dao dao;
    private SessionFactory sessionFactory;
    private AnnotationDefinition sampleAnnotationDefinition;
    private AnnotationDefinition imageSeriesAnnotationDefinition;
    private AnnotationDefinition subjectAnnotationDefinition;
   
    protected String[] getConfigLocations() {
        return new String[] {"classpath*:/**/dao-test-config.xml"};
    }

    @Test
    public void testGetWorkspace() {
        UserWorkspace workspace = new UserWorkspace();
        dao.save(workspace);

        UserWorkspace workspace2 = this.dao.getWorkspace("Anything.");
        assertEquals(workspace.getId(), workspace2.getId());
        
    }

    @Test
    public void testSave() {
        StudyConfiguration studyConfiguration1 = new StudyConfiguration(); 
        Study study1 = studyConfiguration1.getStudy();
        study1.setLongTitleText("longTitleText");
        study1.setShortTitleText("shortTitleText");
        assertNull(studyConfiguration1.getId());
        assertNull(study1.getId());
        dao.save(studyConfiguration1);
        assertNotNull(studyConfiguration1.getId());
        assertNotNull(study1.getId());
        
        StudyConfiguration studyConfiguration2 = dao.get(studyConfiguration1.getId(), StudyConfiguration.class);
        Study study2 = studyConfiguration2.getStudy();
        
        assertEquals(study1.getShortTitleText(), study2.getShortTitleText());
        assertEquals(study1.getLongTitleText(), study2.getLongTitleText());
        assertEquals(study1, study2);
        assertEquals(studyConfiguration1, studyConfiguration2);
    }
    
    @Test 
    @SuppressWarnings({"PMD.ExcessiveMethodLength"})
    public void testFindMatches() {
        // First load 2 AnnotationFieldDescriptors.
        AnnotationFieldDescriptor afd = new AnnotationFieldDescriptor();
        afd.setKeywords("congestive heart failure");
        afd.setName("Congestive Heart Failure");
        afd.setType(AnnotationFieldType.CHOICE);
        dao.save(afd);
        
        AnnotationFieldDescriptor afd2 = new AnnotationFieldDescriptor();
        afd2.setKeywords("congestive");
        afd2.setName("Congestive");
        afd2.setType(AnnotationFieldType.CHOICE);
        dao.save(afd2);
        
        AnnotationFieldDescriptor afd3 = new AnnotationFieldDescriptor();
        afd3.setKeywords("congestive failure");
        afd3.setName("Congestive Failure");
        afd3.setType(AnnotationFieldType.CHOICE);
        dao.save(afd3);
        
        // Now search for our item on the string "congestive"
        List<String> searchWords = new ArrayList<String>();
        searchWords.add("CoNgeStiVe");
        searchWords.add("HearT");
        searchWords.add("failure");
        List<AnnotationFieldDescriptor> afds1 = dao.findMatches(searchWords);
        
        assertNotNull(afds1);
        // Make sure it sorted them properly.
        assertEquals(afds1.get(0).getName(), "Congestive Heart Failure");
        assertEquals(afds1.get(1).getName(), "Congestive Failure");
        assertEquals(afds1.get(2).getName(), "Congestive");
        
        List<String> searchWords2 = new ArrayList<String>();
        searchWords2.add("afdsefda");
        List<AnnotationFieldDescriptor> afds2 = dao.findMatches(searchWords2);
        assertEquals(0, afds2.size());
    }
    
    @Test
    @SuppressWarnings({"PMD.ExcessiveMethodLength"})
    public void testFindMatchingSamples() {
        
        Study study = populateAndRetrieveStudy();
        dao.save(study);
        
        // Now need to create the criterion items and see if we can retrieve back the proper values.
        NumericComparisonCriterion criterion = new NumericComparisonCriterion();
        criterion.setNumericValue(12.0);
        criterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.GREATEROREQUAL.getValue());
        criterion.setAnnotationDefinition(sampleAnnotationDefinition);
        criterion.setEntityType(EntityTypeEnum.SAMPLE.getValue());
        List<SampleAcquisition> matchingSamples = dao.findMatchingSamples(criterion, study);
        
        assertEquals(3, matchingSamples.size());
        
        // Try a different number combination to test a different operator
        NumericComparisonCriterion criterion2 = new NumericComparisonCriterion();
        criterion2.setNumericValue(11.0);
        criterion2.setNumericComparisonOperator(NumericComparisonOperatorEnum.LESSOREQUAL.getValue());
        criterion2.setAnnotationDefinition(sampleAnnotationDefinition);
        criterion2.setEntityType(EntityTypeEnum.SAMPLE.getValue());
        List<SampleAcquisition> matchingSamples2 = dao.findMatchingSamples(criterion2, study);
        
        assertEquals(2, matchingSamples2.size());
        
        // Try using a different Annotation Definition and verify that it returns 0 from that.
        NumericComparisonCriterion criterion3 = new NumericComparisonCriterion();
        criterion3.setNumericValue(13.0);
        criterion3.setNumericComparisonOperator(NumericComparisonOperatorEnum.GREATEROREQUAL.getValue());
        criterion3.setAnnotationDefinition(imageSeriesAnnotationDefinition);
        criterion3.setEntityType(EntityTypeEnum.SAMPLE.getValue());
        List<SampleAcquisition> matchingSamples3 = dao.findMatchingSamples(criterion3, study);
        
        assertEquals(0, matchingSamples3.size());
    }
    
    @Test
    public void testFindMatchingImageSeries() {
        Study study = populateAndRetrieveStudy();
        dao.save(study);
        
        StringComparisonCriterion criterion1 = new StringComparisonCriterion();
        criterion1.setStringValue("string1");
        criterion1.setEntityType(EntityTypeEnum.IMAGESERIES.getValue());
        criterion1.setAnnotationDefinition(imageSeriesAnnotationDefinition);
        List<ImageSeriesAcquisition> matchingImageSeriesAcquisitions = dao.findMatchingImageSeries(criterion1, study);
        
        assertEquals(1, matchingImageSeriesAcquisitions.size());
        
        // Change only the annotation definition and see if it returns 0.
        StringComparisonCriterion criterion2 = new StringComparisonCriterion();
        criterion2.setStringValue("string1");
        criterion2.setEntityType(EntityTypeEnum.IMAGESERIES.getValue());
        criterion2.setAnnotationDefinition(sampleAnnotationDefinition);
        List<ImageSeriesAcquisition> matchingImageSeriesAcquisitions2 = dao.findMatchingImageSeries(criterion2, study);
        assertEquals(0, matchingImageSeriesAcquisitions2.size());
    }
    
    @SuppressWarnings({"PMD.ExcessiveMethodLength"})
    private Study populateAndRetrieveStudy() {
        Study myStudy = new Study();
        myStudy.setShortTitleText("Test Study");
        sampleAnnotationDefinition = new AnnotationDefinition();
        sampleAnnotationDefinition.setDisplayName("SampleAnnotation");
        
        imageSeriesAnnotationDefinition = new AnnotationDefinition();
        imageSeriesAnnotationDefinition.setDisplayName("ImageSeriesAnnotation");
        
        subjectAnnotationDefinition = new AnnotationDefinition();
        subjectAnnotationDefinition.setDisplayName("SubjectAnnotation");
        
        Collection<AnnotationDefinition> sampleDefinitions = new HashSet<AnnotationDefinition>();
        sampleDefinitions.add(sampleAnnotationDefinition);
        
        Collection<AnnotationDefinition> imageSeriesDefinitions = new HashSet<AnnotationDefinition>();
        imageSeriesDefinitions.add(imageSeriesAnnotationDefinition);
        
        Collection<AnnotationDefinition> subjectDefinitions = new HashSet<AnnotationDefinition>();
        subjectDefinitions.add(subjectAnnotationDefinition);
        
        // Setup everything to use the same definition collection for simplicity.
        myStudy.setSampleAnnotationCollection(sampleDefinitions);
        myStudy.setImageSeriesAnnotationCollection(imageSeriesDefinitions);
        myStudy.setSubjectAnnotationCollection(subjectDefinitions);
        
        NumericAnnotationValue numval1 = new NumericAnnotationValue();
        NumericAnnotationValue numval2 = new NumericAnnotationValue();
        NumericAnnotationValue numval3 = new NumericAnnotationValue();
        NumericAnnotationValue numval4 = new NumericAnnotationValue();
        NumericAnnotationValue numval5 = new NumericAnnotationValue();
        
        StringAnnotationValue stringval1 = new StringAnnotationValue();
        StringAnnotationValue stringval2 = new StringAnnotationValue();
        StringAnnotationValue stringval3 = new StringAnnotationValue();
        
        DateAnnotationValue date1 = new DateAnnotationValue();
        DateAnnotationValue date2 = new DateAnnotationValue();
        DateAnnotationValue date3 = new DateAnnotationValue();
        
        
        SampleAcquisition sampleAcquisition1 = new SampleAcquisition();
        sampleAcquisition1.setAnnotationCollection(new HashSet<AbstractAnnotationValue>());
        
        SampleAcquisition sampleAcquisition2 = new SampleAcquisition();
        sampleAcquisition2.setAnnotationCollection(new HashSet<AbstractAnnotationValue>());
        
        SampleAcquisition sampleAcquisition3 = new SampleAcquisition();
        sampleAcquisition3.setAnnotationCollection(new HashSet<AbstractAnnotationValue>());
       
        SampleAcquisition sampleAcquisition4 = new SampleAcquisition();
        sampleAcquisition4.setAnnotationCollection(new HashSet<AbstractAnnotationValue>());
       
        SampleAcquisition sampleAcquisition5 = new SampleAcquisition();
        sampleAcquisition5.setAnnotationCollection(new HashSet<AbstractAnnotationValue>());
        
        ImageSeries imageSeries1 = new ImageSeries();
        imageSeries1.setAnnotationCollection(new HashSet<AbstractAnnotationValue>());
        
        ImageSeries imageSeries2 = new ImageSeries();
        imageSeries2.setAnnotationCollection(new HashSet<AbstractAnnotationValue>());
        
        ImageSeries imageSeries3 = new ImageSeries();
        imageSeries3.setAnnotationCollection(new HashSet<AbstractAnnotationValue>());
        
        SubjectAnnotation subjectAnnotation1 = new SubjectAnnotation();
        subjectAnnotation1.setAnnotationValue(date1);
        
        numval1.setAnnotationDefinition(sampleAnnotationDefinition);
        numval1.setNumericValue(10.0);
        sampleAcquisition1.getAnnotationCollection().add(numval1);
        
        numval2.setAnnotationDefinition(sampleAnnotationDefinition);
        numval2.setNumericValue(11.0);
        sampleAcquisition2.getAnnotationCollection().add(numval2);
        
        numval3.setAnnotationDefinition(sampleAnnotationDefinition);
        numval3.setNumericValue(12.0);
        sampleAcquisition3.getAnnotationCollection().add(numval3);
        
        numval4.setAnnotationDefinition(sampleAnnotationDefinition);
        numval4.setNumericValue(13.0);
        sampleAcquisition4.getAnnotationCollection().add(numval4);
        
        numval5.setAnnotationDefinition(sampleAnnotationDefinition);
        numval5.setNumericValue(14.0);
        sampleAcquisition5.getAnnotationCollection().add(numval5);
        
        stringval1.setAnnotationDefinition(imageSeriesAnnotationDefinition);
        stringval1.setStringValue("string1");
        imageSeries1.getAnnotationCollection().add(stringval1);
        
        stringval2.setAnnotationDefinition(imageSeriesAnnotationDefinition);
        stringval2.setStringValue("string2");
        imageSeries2.getAnnotationCollection().add(stringval2);
        
        stringval3.setAnnotationDefinition(imageSeriesAnnotationDefinition);
        stringval3.setStringValue("string3");
        imageSeries3.getAnnotationCollection().add(stringval3);
        
        
        StudySubjectAssignment studySubjectAssignment = new StudySubjectAssignment();
        
        Collection<SampleAcquisition> saCollection = new HashSet<SampleAcquisition>();
        saCollection.add(sampleAcquisition1);
        saCollection.add(sampleAcquisition2);
        saCollection.add(sampleAcquisition3);
        saCollection.add(sampleAcquisition4);
        saCollection.add(sampleAcquisition5);
        
        Collection<ImageSeries> isCollection = new HashSet<ImageSeries>();
        isCollection.add(imageSeries1);
        isCollection.add(imageSeries2);
        isCollection.add(imageSeries3);
        
        Collection<ImageSeriesAcquisition> isaCollection = new HashSet<ImageSeriesAcquisition>();
        ImageSeriesAcquisition isAcquisition = new ImageSeriesAcquisition();
        isAcquisition.setSeriesCollection(isCollection);
        isaCollection.add(isAcquisition);
        
        Collection<SubjectAnnotation> subjectAnnotationCollection = new HashSet<SubjectAnnotation>();
        subjectAnnotationCollection.add(subjectAnnotation1);
        
        studySubjectAssignment.setSampleAcquisitionCollection(saCollection);
        studySubjectAssignment.setImageStudyCollection(isaCollection);
        studySubjectAssignment.setSubjectAnnotationCollection(subjectAnnotationCollection);
        
        Collection<StudySubjectAssignment> ssaCollection = new HashSet<StudySubjectAssignment>();
        ssaCollection.add(studySubjectAssignment);
        myStudy.setAssignmentCollection(ssaCollection);
        return myStudy;
    }

    /**
     * @param caIntegrator2Dao the caIntegrator2Dao to set
     */
    public void setDao(CaIntegrator2Dao caIntegrator2Dao) {
        this.dao = caIntegrator2Dao;
    }

    /**
     * @return the sessionFactory
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * @param sessionFactory the sessionFactory to set
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

}
