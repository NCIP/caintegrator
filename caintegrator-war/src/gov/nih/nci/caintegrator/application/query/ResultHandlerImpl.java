/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.query;

import gov.nih.nci.caintegrator.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator.domain.annotation.SubjectAnnotation;
import gov.nih.nci.caintegrator.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.QueryResult;
import gov.nih.nci.caintegrator.domain.application.ResultColumn;
import gov.nih.nci.caintegrator.domain.application.ResultRow;
import gov.nih.nci.caintegrator.domain.application.ResultValue;
import gov.nih.nci.caintegrator.domain.application.SortTypeEnum;
import gov.nih.nci.caintegrator.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator.domain.translational.StudySubjectAssignment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;


/**
 * Creates the actual results for the Query and Subjects that passed the criterion checks.
 */
public class ResultHandlerImpl implements ResultHandler {

    /**
     * {@inheritDoc}
     */
    @Override
    public QueryResult createResults(Query query, Set<ResultRow> resultRows, CaIntegrator2Dao dao) {
        QueryResult queryResult = new QueryResult();
        queryResult.setRowCollection(resultRows);
        queryResult.setQuery(query);
        if (CollectionUtils.isNotEmpty(resultRows)) {
            addColumns(queryResult, dao);
            sortRows(queryResult);
        }
        return queryResult;
    }

    /**
     * This function assumes a QueryResult with no columns, just rows, and it fills in the columns
     * and values for each row.
     * @param queryResult - object that contains the rows.
     * @param dao
     */
    private void addColumns(QueryResult queryResult, CaIntegrator2Dao dao) {
        Query query = queryResult.getQuery();
        Collection<ResultColumn> columns = query.retrieveVisibleColumns();
        columns = removeUnauthorizedColumns(columns, query, dao);
        Collection<ResultRow> resultRows = queryResult.getRowCollection();
        for (ResultRow row : resultRows) {
            List<ResultValue> valueList = new ArrayList<ResultValue>();
            for (int i = 0; i < columns.size(); i++) {
                 valueList.add(null);
            }
            for (ResultColumn column : columns) {
                EntityTypeEnum entityType = column.getEntityType();
                ResultValue resultValue = new ResultValue();
                resultValue.setColumn(column);
                resultValue.setValue(null);
                valueList.set(column.getColumnIndex(), resultValue);
                switch(entityType) {
                case IMAGESERIES:
                    resultValue.setValue(handleImageSeriesRow(row, column));
                break;
                case SAMPLE:
                    resultValue.setValue(handleSampleRow(row, column));
                break;
                case SUBJECT:
                    resultValue.setValue(handleSubjectRow(row, column));
                break;
                default:
                    // Might need to throw some sort of error in this case?
                    resultValue.setValue(null);
                break;
                }
            }
            row.setValueCollection(valueList);
        }
    }

    /**
     * Takes an input collection of ResultColumns, and removes any column for
     * which the corresponding AnnotationFieldDescriptor is restricted by the
     * users AuthorizedStudyElementsGroups.
     */
    private Collection<ResultColumn> removeUnauthorizedColumns(Collection<ResultColumn> columns,
            Query query, CaIntegrator2Dao dao) {
        List<AnnotationFieldDescriptor> authorizedAfdList = new ArrayList<AnnotationFieldDescriptor>();
        UserWorkspace userWorkspace = query.getSubscription().getUserWorkspace();

        if (userWorkspace != null) {
            String username = userWorkspace.getUsername();
            StudyConfiguration studyConfig = query.getSubscription().getStudy().getStudyConfiguration();
            authorizedAfdList = dao.getAuthorizedAnnotationFieldDescriptors(username, studyConfig);
        }

        if (CollectionUtils.isNotEmpty(authorizedAfdList)) {
            List<ResultColumn> restrictedColumns = new ArrayList<ResultColumn>();
            restrictedColumns.addAll(columns);

            for (ResultColumn column : columns) {
                if (!authorizedAfdList.contains(column.getAnnotationFieldDescriptor())) {
                    restrictedColumns.remove(column);
                }
            }
            reindexColumns(restrictedColumns);
            return restrictedColumns;
        }

        return columns;
    }

    private void reindexColumns(List<ResultColumn> columns) {
        Collections.sort(columns);
        for (int i = 0; i < columns.size(); i++) {
            columns.get(i).setColumnIndex(i);
        }
    }

    private AbstractAnnotationValue handleImageSeriesRow(ResultRow row, ResultColumn column) {
        ImageSeries imageSeries = row.getImageSeries();
        if (imageSeries != null) {
            for (AbstractAnnotationValue annotationValue : imageSeries.getAnnotationCollection()) {
                if (annotationValue.getAnnotationDefinition().equals(column.getAnnotationDefinition())) {
                    return retrieveValue(column.getAnnotationFieldDescriptor(), annotationValue);
                }
            }
        }
        return null;
    }

    private AbstractAnnotationValue handleSampleRow(ResultRow row, ResultColumn column) {
        SampleAcquisition sampleAcquisition = row.getSampleAcquisition();
        if (sampleAcquisition != null
                && sampleAcquisition.getAnnotationCollection() != null) {
            for (AbstractAnnotationValue annotationValue : sampleAcquisition.getAnnotationCollection()) {
                if (annotationValue.getAnnotationDefinition().equals(column.getAnnotationDefinition())) {
                    return retrieveValue(column.getAnnotationFieldDescriptor(), annotationValue);
                }
            }
        }
        return null;
    }

    private AbstractAnnotationValue handleSubjectRow(ResultRow row, final ResultColumn column) {
        StudySubjectAssignment studySubjectAssignment = row.getSubjectAssignment();
        if (studySubjectAssignment != null
                && CollectionUtils.isNotEmpty(studySubjectAssignment.getSubjectAnnotationCollection())) {
            for (SubjectAnnotation subjectAnnotation : studySubjectAssignment.getSubjectAnnotationCollection()) {
                if (subjectAnnotation.getAnnotationValue().getAnnotationDefinition()
                        .equals(column.getAnnotationDefinition())) {
                    return retrieveValue(column.getAnnotationFieldDescriptor(),
                            subjectAnnotation.getAnnotationValue());
                }
            }
        }
        return null;
    }

    /**
     * Sort the rows in the Query Result object.
     * @param queryResult - Object that needs the results to be sorted.
     */
    private void sortRows(QueryResult queryResult) {
        Collection<ResultColumn> columnsCollection = queryResult.getQuery().getColumnCollection();
        if (!columnsCollection.isEmpty()) {
            Collection <ResultRow> rowsCollection = queryResult.getRowCollection();
            List<ResultColumn> sortColumns = new ArrayList<ResultColumn>();
            for (ResultColumn column : columnsCollection) {
                if (!SortTypeEnum.UNSORTED.equals(column.getSortType())) {
                    // Sort Order and Column Index are the same for now, can be changed later
                    column.setSortOrder(column.getColumnIndex());
                    sortColumns.add(column);
                }
            }
            if (!sortColumns.isEmpty()) { // Sort only if there's a specified sort column.
                Collections.sort(sortColumns, new ResultColumnComparator());
                queryResult.setRowCollection(ResultRowComparator.sort(rowsCollection, sortColumns));
            }
        }
    }

    private AbstractAnnotationValue retrieveValue(AnnotationFieldDescriptor fieldDescriptor,
            AbstractAnnotationValue value) {
        return fieldDescriptor.getAnnotationMasks().isEmpty() ? value
            : AbstractAnnotationMaskHandler.retrieveMaskedValue(fieldDescriptor.getAnnotationMasks(), value);
    }
}
