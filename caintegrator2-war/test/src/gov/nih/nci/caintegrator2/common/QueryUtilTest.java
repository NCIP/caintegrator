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
package gov.nih.nci.caintegrator2.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.domain.annotation.DateAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.NumericAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.StringAnnotationValue;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.CopyNumberAlterationCriterion;
import gov.nih.nci.caintegrator2.domain.application.ExpressionLevelCriterion;
import gov.nih.nci.caintegrator2.domain.application.FoldChangeCriterion;
import gov.nih.nci.caintegrator2.domain.application.GeneNameCriterion;
import gov.nih.nci.caintegrator2.domain.application.GenomicCriterionTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultRow;
import gov.nih.nci.caintegrator2.domain.application.StringComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.SubjectListCriterion;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;


public class QueryUtilTest {


    @Test
    public void testResultRowSetContainsResultRow() {
        
        Set<ResultRow> rowSet = new HashSet<ResultRow>();
        ResultRow row1 = new ResultRow();
        StudySubjectAssignment subjectAssignment = new StudySubjectAssignment();
        subjectAssignment.setId(Long.valueOf(1));
        row1.setSubjectAssignment(subjectAssignment);
        rowSet.add(row1);
        ResultRow rowToTest = new ResultRow();
        rowToTest.setSubjectAssignment(subjectAssignment);
        
        assertTrue(QueryUtil.resultRowSetContainsResultRow(rowSet, rowToTest, false) != null);
        
    }

    @Test
    public void testAnnotationValueBelongToPermissibleValue() {
        StringAnnotationValue stringValue = new StringAnnotationValue();
        stringValue.setStringValue("TeSt");
        PermissibleValue stringPermissibleValue = new PermissibleValue();
        stringPermissibleValue.setValue("tEsT");
        assertTrue(QueryUtil.annotationValueBelongToPermissibleValue(stringValue, stringPermissibleValue));
        stringPermissibleValue.setValue("Not Equals");
        assertFalse(QueryUtil.annotationValueBelongToPermissibleValue(stringValue, stringPermissibleValue));
        
        NumericAnnotationValue numericValue = new NumericAnnotationValue();
        numericValue.setNumericValue(50.0);
        PermissibleValue numericPermissibleValue = new PermissibleValue();
        numericPermissibleValue.setValue("50.0");
        assertTrue(QueryUtil.annotationValueBelongToPermissibleValue(numericValue, numericPermissibleValue));
        DateAnnotationValue dateValue = new DateAnnotationValue();
        long currentTime = System.currentTimeMillis();
        dateValue.setDateValue(new Date(currentTime));
        
        PermissibleValue datePermissibleValue = new PermissibleValue();
        datePermissibleValue.setValue(DateUtil.toString(new Date(currentTime)));
        
        assertTrue(QueryUtil.annotationValueBelongToPermissibleValue(dateValue, datePermissibleValue));
        
    }
    
    
    @Test
    public void testIsCompoundCriterionGenomic() {
        CompoundCriterion compoundCriterion1 = new CompoundCriterion();
        compoundCriterion1.setCriterionCollection(new HashSet<AbstractCriterion>());
        CompoundCriterion compoundCriterion2 = new CompoundCriterion();
        compoundCriterion2.setCriterionCollection(new HashSet<AbstractCriterion>());
        compoundCriterion2.getCriterionCollection().add(new StringComparisonCriterion());
        compoundCriterion2.getCriterionCollection().add(new StringComparisonCriterion());
        compoundCriterion1.getCriterionCollection().add(compoundCriterion2);
        assertFalse(QueryUtil.isCompoundCriterionGeneExpression(compoundCriterion1));
        assertFalse(QueryUtil.isCompoundCriterionCopyNumber(compoundCriterion1));
        GeneNameCriterion geneNameCriterion = new GeneNameCriterion();
        compoundCriterion1.getCriterionCollection().add(geneNameCriterion);
        geneNameCriterion.setGenomicCriterionType(GenomicCriterionTypeEnum.GENE_EXPRESSION);
        assertTrue(QueryUtil.isCompoundCriterionGeneExpression(compoundCriterion1));
        assertFalse(QueryUtil.isCompoundCriterionCopyNumber(compoundCriterion1));
        geneNameCriterion.setGenomicCriterionType(GenomicCriterionTypeEnum.COPY_NUMBER);
        assertFalse(QueryUtil.isCompoundCriterionGeneExpression(compoundCriterion1));
        assertTrue(QueryUtil.isCompoundCriterionCopyNumber(compoundCriterion1));
        
        compoundCriterion1.setCriterionCollection(new HashSet<AbstractCriterion>());
        compoundCriterion1.getCriterionCollection().add(compoundCriterion2);
        compoundCriterion1.getCriterionCollection().add(new StringComparisonCriterion());
        assertFalse(QueryUtil.isCompoundCriterionGeneExpression(compoundCriterion1));
        assertFalse(QueryUtil.isCompoundCriterionCopyNumber(compoundCriterion1));
        compoundCriterion2.getCriterionCollection().add(new FoldChangeCriterion());
        assertTrue(QueryUtil.isCompoundCriterionGeneExpression(compoundCriterion1));
        assertFalse(QueryUtil.isCompoundCriterionCopyNumber(compoundCriterion1));

        compoundCriterion1 = new CompoundCriterion();
        compoundCriterion1.setCriterionCollection(new HashSet<AbstractCriterion>());
        compoundCriterion2 = new CompoundCriterion();
        compoundCriterion2.setCriterionCollection(new HashSet<AbstractCriterion>());
        compoundCriterion1.getCriterionCollection().add(compoundCriterion2);
        compoundCriterion1.getCriterionCollection().add(new StringComparisonCriterion());
        assertFalse(QueryUtil.isCompoundCriterionGeneExpression(compoundCriterion1));
        assertFalse(QueryUtil.isCompoundCriterionCopyNumber(compoundCriterion1));
        compoundCriterion2.getCriterionCollection().add(new CopyNumberAlterationCriterion());
        assertFalse(QueryUtil.isCompoundCriterionGeneExpression(compoundCriterion1));
        assertTrue(QueryUtil.isCompoundCriterionCopyNumber(compoundCriterion1));
        
        compoundCriterion1 = new CompoundCriterion();
        compoundCriterion2 = new CompoundCriterion();
        CompoundCriterion compoundCriterion3 = new CompoundCriterion();
        compoundCriterion1.getCriterionCollection().add(compoundCriterion2);
        compoundCriterion1.getCriterionCollection().add(compoundCriterion3);
        compoundCriterion2.getCriterionCollection().add(new StringComparisonCriterion());
        compoundCriterion3.getCriterionCollection().add(geneNameCriterion);
        geneNameCriterion.setGenomicCriterionType(GenomicCriterionTypeEnum.GENE_EXPRESSION);
        assertTrue(QueryUtil.isCompoundCriterionGeneExpression(compoundCriterion1));
        
        compoundCriterion1 = new CompoundCriterion();
        compoundCriterion1.setCriterionCollection(new HashSet<AbstractCriterion>());
        compoundCriterion1.getCriterionCollection().add(new ExpressionLevelCriterion());
        assertTrue(QueryUtil.isCompoundCriterionGeneExpression(compoundCriterion1));
    }
    
    @Test
    public void testGetCriterionTypeFromQuery() {
        Query query = new Query();
        CompoundCriterion compoundCriterion1 = new CompoundCriterion();
        CompoundCriterion compoundCriterion2 = new CompoundCriterion();
        compoundCriterion1.getCriterionCollection().add(new GeneNameCriterion());
        compoundCriterion1.getCriterionCollection().add(compoundCriterion2);
        compoundCriterion2.getCriterionCollection().add(new SubjectListCriterion());
        compoundCriterion2.getCriterionCollection().add(new SubjectListCriterion());
        query.setCompoundCriterion(compoundCriterion1);
        assertEquals(1, QueryUtil.getCriterionTypeFromQuery(query, GeneNameCriterion.class).size());
        assertEquals(2, QueryUtil.getCriterionTypeFromQuery(query, SubjectListCriterion.class).size());
        assertEquals(0, QueryUtil.getCriterionTypeFromQuery(query, StringComparisonCriterion.class).size());
    }
}
