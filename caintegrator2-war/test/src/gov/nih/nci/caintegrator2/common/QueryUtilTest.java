/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
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
    public void getMatchingRows() {
        Set<ResultRow> rows = new HashSet<ResultRow>();

        ResultRow rowOne = new ResultRow();
        rowOne.setSubjectAssignment(new StudySubjectAssignment());
        rowOne.getSubjectAssignment().setIdentifier("1");
        rows.add(rowOne);

        ResultRow rowTwo = new ResultRow();
        rowTwo.setSubjectAssignment(new StudySubjectAssignment());
        rowTwo.getSubjectAssignment().setIdentifier("2");
        rows.add(rowTwo);

        ResultRow existingRow = new ResultRow();
        existingRow.setSubjectAssignment(rowOne.getSubjectAssignment());

        ResultRow nonExistentRow = new ResultRow();
        nonExistentRow.setSubjectAssignment(new StudySubjectAssignment());
        nonExistentRow.getSubjectAssignment().setIdentifier("3");

        assertEquals(rowOne, QueryUtil.getMatchingRow(rows, existingRow, false));
        assertEquals(rowOne, QueryUtil.getMatchingRow(rows, existingRow, true));
        assertNull(QueryUtil.getMatchingRow(rows, nonExistentRow, false));
        assertNull(QueryUtil.getMatchingRow(rows, nonExistentRow, true));
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
