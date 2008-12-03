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
package gov.nih.nci.caintegrator2.web.action.query.form;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.application.study.BooleanOperatorEnum;
import gov.nih.nci.caintegrator2.application.study.EntityTypeEnum;
import gov.nih.nci.caintegrator2.application.study.NumericComparisonOperatorEnum;
import gov.nih.nci.caintegrator2.application.study.WildCardTypeEnum;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractPermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.FoldChangeCriterion;
import gov.nih.nci.caintegrator2.domain.application.GeneNameCriterion;
import gov.nih.nci.caintegrator2.domain.application.NumericComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.StringComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

public class QueryFormTest {
    
    private long nextId = 0;
    private QueryForm queryForm = new QueryForm();
    private StudySubscription subscription;
    private AnnotationDefinition stringClinicalAnnotation1;
    private AnnotationDefinition stringClinicalAnnotation2;
    private AnnotationDefinition numericClinicalAnnotation;
    private AnnotationDefinition testImageSeriesAnnotation;

    @Before
    public void setUp() {
        subscription = new StudySubscription();
        subscription.setId(1L);
        Study study = new Study();
        study.setSubjectAnnotationCollection(new HashSet<AnnotationDefinition>());
        study.setImageSeriesAnnotationCollection(new HashSet<AnnotationDefinition>());
        subscription.setStudy(study);
        stringClinicalAnnotation1 = createDefinition("stringClinicalAnnotation1", AnnotationTypeEnum.STRING);
        stringClinicalAnnotation2 = createDefinition("stringClinicalAnnotation2", AnnotationTypeEnum.STRING);
        numericClinicalAnnotation = createDefinition("numericClinicalAnnotation", AnnotationTypeEnum.NUMERIC);
        study.getSubjectAnnotationCollection().add(stringClinicalAnnotation1);
        study.getSubjectAnnotationCollection().add(stringClinicalAnnotation2);
        study.getSubjectAnnotationCollection().add(numericClinicalAnnotation);
        testImageSeriesAnnotation = createDefinition("testImageSeriesAnnotation", AnnotationTypeEnum.STRING);
        study.getImageSeriesAnnotationCollection().add(testImageSeriesAnnotation);
    }

    private AnnotationDefinition createDefinition(String name, AnnotationTypeEnum type) {
        AnnotationDefinition definition = new AnnotationDefinition();
        definition.setDisplayName(name);
        definition.setId(nextId++);
        definition.setType(type.getValue());
        definition.setPermissibleValueCollection(new HashSet<AbstractPermissibleValue>());
        return definition;
    }

    @Test
    public void testCreateQuery() {
        queryForm.createQuery(subscription);
        assertNotNull(queryForm.getQuery());
        assertNotNull(queryForm.getCriteriaGroup());
        assertEquals(subscription, queryForm.getQuery().getSubscription());
        assertEquals(3, queryForm.getClinicalAnnotations().getNames().size());
        assertEquals("numericClinicalAnnotation", queryForm.getClinicalAnnotations().getNames().get(0));
        assertEquals(stringClinicalAnnotation1, queryForm.getClinicalAnnotations().getDefinition("stringClinicalAnnotation1"));
    }
    
    @Test
    public void testCriteriaGroup() {
        queryForm.createQuery(subscription);
        CriteriaGroup group = queryForm.getCriteriaGroup();
        assertEquals(BooleanOperatorEnum.AND.getValue(), group.getBooleanOperatorName());
        group.setBooleanOperatorName("or");
        assertEquals(BooleanOperatorEnum.OR.getValue(), group.getBooleanOperatorName());
        group.setCriterionTypeName("");
        assertEquals("", group.getCriterionTypeName());
        group.setCriterionTypeName(CriterionTypeEnum.CLINICAL.getValue());
        assertEquals(CriterionTypeEnum.CLINICAL.getValue(), group.getCriterionTypeName());
        assertEquals(0, group.getCriteria().size());
        group.addCriterion();
        assertEquals(1, group.getCriteria().size());
    }
    
    @Test
    public void testCriterionHolder() {
        queryForm.createQuery(subscription);
        CriteriaGroup group = queryForm.getCriteriaGroup();
        group.setCriterionTypeName(CriterionTypeEnum.CLINICAL.getValue());
        group.addCriterion();
        ClinicalCriterionHolder criterionHolder = (ClinicalCriterionHolder) group.getCriteria().get(0);
        checkNewCriterionHolder(group, criterionHolder);

        checkSetNewHolderToStringField(criterionHolder);
        checkSetToEqualsOperator(criterionHolder);
        checkSetStringOperandValue(criterionHolder);
        checkChangeStringFieldOperator(criterionHolder);
        checkChangeToDifferentStringField(criterionHolder);
        checkChangeToNumericField(criterionHolder);
        checkChangeNumericOperator(criterionHolder);
        checkAddImageSeriesCriterion(group);
        checkAddGeneExpressionCriterion(group);
    }


    private void checkChangeNumericOperator(ClinicalCriterionHolder criterionHolder) {
        NumericComparisonCriterion criterion = (NumericComparisonCriterion) criterionHolder.getAnnotationCriterion();
        criterionHolder.setOperatorName(CriterionOperatorEnum.GREATER_THAN.getValue());
        assertEquals(NumericComparisonOperatorEnum.GREATER.getValue(), criterion.getNumericComparisonOperator());
        criterionHolder.setOperatorName(CriterionOperatorEnum.GREATER_THAN_OR_EQUAL_TO.getValue());
        assertEquals(NumericComparisonOperatorEnum.GREATEROREQUAL.getValue(), criterion.getNumericComparisonOperator());
        criterionHolder.setOperatorName(CriterionOperatorEnum.LESS_THAN.getValue());
        assertEquals(NumericComparisonOperatorEnum.LESS.getValue(), criterion.getNumericComparisonOperator());
        criterionHolder.setOperatorName(CriterionOperatorEnum.LESS_THAN_OR_EQUAL_TO.getValue());
        assertEquals(NumericComparisonOperatorEnum.LESSOREQUAL.getValue(), criterion.getNumericComparisonOperator());
        criterionHolder.setOperatorName(CriterionOperatorEnum.EQUALS.getValue());
        assertEquals(NumericComparisonOperatorEnum.EQUAL.getValue(), criterion.getNumericComparisonOperator());
    }

    private void checkAddGeneExpressionCriterion(CriteriaGroup group) {
        group.setCriterionTypeName(CriterionTypeEnum.GENE_EXPRESSION.getValue());
        group.addCriterion();
        GeneExpressionCriterionHolder criterionHolder = (GeneExpressionCriterionHolder) group.getCriteria().get(2);
        assertEquals(2, criterionHolder.getAvailableFieldNames().size());
        assertTrue(criterionHolder.getAvailableFieldNames().contains("Gene Name"));
        assertTrue(criterionHolder.getAvailableFieldNames().contains("Fold Change"));
        assertEquals(group, criterionHolder.getGroup());
        criterionHolder.setFieldName("Gene Name");
        assertEquals("Gene Name", criterionHolder.getFieldName());
        assertEquals(1, criterionHolder.getAvailableOperatorNames().length);
        criterionHolder.setOperatorName(CriterionOperatorEnum.EQUALS.getValue());
        assertEquals(CriterionOperatorEnum.EQUALS.getValue(), criterionHolder.getOperatorName());
        assertTrue(criterionHolder.getCriterion() instanceof GeneNameCriterion);
        criterionHolder.getOperands().get(0).setValue("EGFR");
        assertEquals("EGFR", ((GeneNameCriterion) criterionHolder.getCriterion()).getGeneSymbol());

        criterionHolder.setFieldName("Fold Change");
        assertEquals(1, criterionHolder.getAvailableOperatorNames().length);
        criterionHolder.setOperatorName(CriterionOperatorEnum.EQUALS.getValue());
        assertTrue(criterionHolder.getCriterion() instanceof FoldChangeCriterion);
        assertEquals(2, criterionHolder.getOperands().size());
        criterionHolder.getOperands().get(0).setValue("Up");
        criterionHolder.getOperands().get(1).setValue("2.0");
        FoldChangeCriterion foldChangeCriterion = (FoldChangeCriterion) criterionHolder.getCriterion();
        assertEquals("Up", foldChangeCriterion.getRegulationType());
        assertEquals(2.0, foldChangeCriterion.getFolds(), 0.0);
    }

    private void checkAddImageSeriesCriterion(CriteriaGroup group) {
        group.setCriterionTypeName(CriterionTypeEnum.IMAGE_SERIES.getValue());
        group.addCriterion();
        ImageSeriesCriterionHolder imageSeriesCriterionHolder = (ImageSeriesCriterionHolder) group.getCriteria().get(1);
        assertEquals(1, imageSeriesCriterionHolder.getAvailableFieldNames().size());
        assertTrue(imageSeriesCriterionHolder.getAvailableFieldNames().contains("testImageSeriesAnnotation"));
        assertEquals(group, imageSeriesCriterionHolder.getGroup());
    }

    private void checkChangeToNumericField(ClinicalCriterionHolder criterionHolder) {
        criterionHolder.setFieldName("numericClinicalAnnotation");
        assertEquals(5 , criterionHolder.getAvailableOperatorNames().length);
        assertEquals("", criterionHolder.getOperatorName());
        criterionHolder.setOperatorName(CriterionOperatorEnum.EQUALS.getValue());
        assertEquals(CriterionOperatorEnum.EQUALS.getValue(), criterionHolder.getOperatorName());
        assertTrue(criterionHolder.getCriterion() instanceof NumericComparisonCriterion);
        assertEquals(1, criterionHolder.getGroup().getCompoundCriterion().getCriterionCollection().size());
        assertTrue(criterionHolder.getGroup().getCompoundCriterion().getCriterionCollection().iterator().next() instanceof NumericComparisonCriterion);
    }


    private void checkChangeToDifferentStringField(ClinicalCriterionHolder criterionHolder) {
        criterionHolder.setFieldName("stringClinicalAnnotation2");
        assertEquals("stringClinicalAnnotation2", criterionHolder.getFieldName());
        StringComparisonCriterion criterion = (StringComparisonCriterion) criterionHolder.getAnnotationCriterion();
        assertEquals(WildCardTypeEnum.WILDCARD_BEFORE_AND_AFTER_STRING.getValue(), criterion.getWildCardType());
        assertEquals("value", criterionHolder.getOperands().get(0).getValue());
        assertEquals("value", criterion.getStringValue());
        assertEquals(stringClinicalAnnotation2, criterion.getAnnotationDefinition());
    }

    private void checkChangeStringFieldOperator(ClinicalCriterionHolder criterionHolder) {
        StringComparisonCriterion criterion = (StringComparisonCriterion) criterionHolder.getAnnotationCriterion();
        criterionHolder.setOperatorName(CriterionOperatorEnum.BEGINS_WITH.getValue());
        assertEquals(WildCardTypeEnum.WILDCARD_AFTER_STRING.getValue(), criterion.getWildCardType());
        criterionHolder.setOperatorName(CriterionOperatorEnum.ENDS_WITH.getValue());
        assertEquals(WildCardTypeEnum.WILDCARD_BEFORE_STRING.getValue(), criterion.getWildCardType());
        criterionHolder.setOperatorName(CriterionOperatorEnum.EQUALS.getValue());
        assertEquals(WildCardTypeEnum.WILDCARD_OFF.getValue(), criterion.getWildCardType());
        criterionHolder.setOperatorName(CriterionOperatorEnum.CONTAINS.getValue());
        assertEquals(WildCardTypeEnum.WILDCARD_BEFORE_AND_AFTER_STRING.getValue(), criterion.getWildCardType());
        assertEquals("value", criterionHolder.getOperands().get(0).getValue());
        assertEquals("value", criterion.getStringValue());
    }

    private void checkSetStringOperandValue(ClinicalCriterionHolder criterionHolder) {
        criterionHolder.getOperands().get(0).setValue("value");
        assertEquals("value", criterionHolder.getOperands().get(0).getValue());
        StringComparisonCriterion criterion = (StringComparisonCriterion) criterionHolder.getAnnotationCriterion();
        assertEquals("value", criterion.getStringValue());
    }

    private void checkNewCriterionHolder(CriteriaGroup group, ClinicalCriterionHolder criterionHolder) {
        assertEquals(3, criterionHolder.getAvailableFieldNames().size());
        assertTrue(criterionHolder.getAvailableFieldNames().contains("stringClinicalAnnotation1"));
        assertEquals(group, criterionHolder.getGroup());
        assertEquals(0 , criterionHolder.getAvailableOperatorNames().length);
        assertEquals("", criterionHolder.getFieldName());
        assertEquals("", criterionHolder.getOperatorName());
    }

    private void checkSetToEqualsOperator(ClinicalCriterionHolder criterionHolder) {
        criterionHolder.setOperatorName(CriterionOperatorEnum.EQUALS.getValue());
        assertEquals(CriterionOperatorEnum.EQUALS.getValue(), criterionHolder.getOperatorName());
        assertEquals(CriterionOperatorEnum.EQUALS, criterionHolder.getOperator());
        StringComparisonCriterion criterion = (StringComparisonCriterion) criterionHolder.getAnnotationCriterion();
        assertNotNull(criterion);
        assertEquals(WildCardTypeEnum.WILDCARD_OFF.getValue(), criterion.getWildCardType());
        assertEquals(stringClinicalAnnotation1, criterion.getAnnotationDefinition());
        assertEquals(EntityTypeEnum.SUBJECT.getValue(), criterion.getEntityType());
        assertEquals(1, criterionHolder.getOperands().size());
        assertTrue(criterionHolder.getOperands().get(0) instanceof StringOperand);
    }

    private void checkSetNewHolderToStringField(ClinicalCriterionHolder criterionHolder) {
        criterionHolder.setFieldName("stringClinicalAnnotation1");
        assertEquals("stringClinicalAnnotation1", criterionHolder.getFieldName());
        assertEquals(4 , criterionHolder.getAvailableOperatorNames().length);
    }

    @Test
    public void testSetQuery() {
        Query query = new Query();
        query.setSubscription(subscription);
        CompoundCriterion compoundCriterion = new CompoundCriterion();
        compoundCriterion.setId(1L);
        compoundCriterion.setBooleanOperator(BooleanOperatorEnum.OR.getValue());
        query.setCompoundCriterion(compoundCriterion);
        StringComparisonCriterion criterion = new StringComparisonCriterion();
        criterion.setAnnotationDefinition(stringClinicalAnnotation1);
        criterion.setEntityType(EntityTypeEnum.SUBJECT.getValue());
        criterion.setStringValue("value");
        criterion.setWildCardType(WildCardTypeEnum.WILDCARD_BEFORE_AND_AFTER_STRING.getValue());
        compoundCriterion.setCriterionCollection(new HashSet<AbstractCriterion>());
        compoundCriterion.getCriterionCollection().add(criterion);
        
        queryForm.setQuery(query);
        assertNotNull(queryForm.getQuery());
        CriteriaGroup group = queryForm.getCriteriaGroup();
        assertNotNull(group);
        assertEquals(compoundCriterion, group.getCompoundCriterion());
        assertEquals(BooleanOperatorEnum.OR.getValue(), group.getBooleanOperatorName());
        assertEquals(1, group.getCriteria().size());
//        ClinicalCriterionHolder criterionHolder = (ClinicalCriterionHolder) group.getCriteria().get(0);
//        assertEquals(criterion, criterionHolder.getCriterion());
//        assertEquals(stringClinicalAnnotation1.getDisplayName(), criterionHolder.getFieldName());
    }

}
