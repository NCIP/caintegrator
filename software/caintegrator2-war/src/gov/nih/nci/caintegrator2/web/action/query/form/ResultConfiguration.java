/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.ResultsOrientationEnum;
import gov.nih.nci.caintegrator2.domain.application.SortTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.translational.Study;

/**
 * Contains query form configuration for the result rows to be displayed.
 */
public class ResultConfiguration {

    private final QueryForm form;
    private final ColumnSelectionList subjectColumns;
    private final ColumnSelectionList imageSeriesColumns;

    ResultConfiguration(QueryForm form) {
        this.form = form;
        subjectColumns = new ColumnSelectionList(this,
                getStudy().getStudyConfiguration()
                    .getVisibleSubjectAnnotationCollection(), EntityTypeEnum.SUBJECT);
        imageSeriesColumns = new ColumnSelectionList(this,
                getStudy().getStudyConfiguration()
                    .getVisibleImageSeriesAnnotationCollection(), EntityTypeEnum.IMAGESERIES);
    }

    Study getStudy() {
        return getQuery().getSubscription().getStudy();
    }

    /**
     * @return the resultType
     */
    public String getResultType() {
        if (getQuery().getResultType() == null) {
            return "";
        } else {
            return getQuery().getResultType().getValue();
        }
    }

    /**
     * @param resultType the resultType to set
     */
    public void setResultType(String resultType) {
        if (StringUtils.isBlank(resultType)) {
            getQuery().setResultType(null);
        } else {
            getQuery().setResultType(ResultTypeEnum.getByValue(resultType));
        }
        if (ResultTypeEnum.GENOMIC.equals(getQuery().getResultType())) {
            getQuery().getColumnCollection().clear();
            if (StringUtils.isBlank(getReporterType())) {
                setReporterType(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET.getValue());
            }
        }
    }

    /**
     * @return the orientation
     */
    public String getOrientation() {
        if (getQuery().getOrientation() == null) {
            return "";
        } else {
            return getQuery().getOrientation().getValue();
        }
    }

    /**
     * @param orientation the orientation to set
     */
    public void setOrientation(String orientation) {
        if (StringUtils.isBlank(orientation)) {
            getQuery().setOrientation(null);
        } else {
            getQuery().setOrientation(ResultsOrientationEnum.getByValue(orientation));
        }
    }

    /**
     * @return the reporterType
     */
    public String getReporterType() {
        if (getQuery().getReporterType() != null) {
            return getQuery().getReporterType().getValue();
        } else {
            return "";
        }
    }

    /**
     * @param reporterType the reporterType to set
     */
    public void setReporterType(String reporterType) {
        if (StringUtils.isBlank(reporterType)) {
            getQuery().setReporterType(null);
        } else {
            getQuery().setReporterType(ReporterTypeEnum.getByValue(reporterType));
        }
    }

    /**
     * @return the form
     */
    private QueryForm getForm() {
        return form;
    }

    /**
     * @return the query
     */
    Query getQuery() {
        return getForm().getQuery();
    }

    /**
     * @return the subjectColumns
     */
    public ColumnSelectionList getSubjectColumns() {
        return subjectColumns;
    }

    /**
     * @return the imageSeriesColumns
     */
    public ColumnSelectionList getImageSeriesColumns() {
        return imageSeriesColumns;
    }
    
    /**
     * @return the list of columns in order by columnIndex
     */
    public List<ResultColumn> getSelectedColumns() {
        List<ResultColumn> selectedColumns = new ArrayList<ResultColumn>();
        selectedColumns.addAll(getQuery().getColumnCollection());
        final Comparator<ResultColumn> columnIndexComparator = new Comparator<ResultColumn>() {
            /**
             * {@inheritDoc}
             */
            public int compare(ResultColumn column1, ResultColumn column2) {
                return column1.getColumnIndex() - column2.getColumnIndex();
            }
        };
        Collections.sort(selectedColumns, columnIndexComparator);
        return selectedColumns;
    }

    /**
     * @param columnName get index of this column
     * @return the index
     */
    public int getColumnIndex(String columnName) {
        return getColumn(columnName).getColumnIndex() + 1;
    }

    /**
     * @return the allowable indexes
     */
    public int[] getColumnIndexOptions() {
        int[] indexes = new int[getQuery().getColumnCollection().size()];
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = i + 1;
        }
        return indexes;
    }
    
    /**
     * @param columnName get index of this column
     * @param index the index
     */
    public void setColumnIndex(String columnName, int index) {
        getColumn(columnName).setColumnIndex(index - 1);
    }

    private ResultColumn getColumn(String columnName) {
        for (ResultColumn column : getQuery().getColumnCollection()) {
            if (column.getAnnotationDefinition().getDisplayName().equals(columnName)) {
                return column;
            }
        }
        return null;
    }
    
    /**
     * For getting the sortType as a string (for JSP purposes).
     * @param columnName get index of this column.
     * @return sortType.
     */
    public String getSortType(String columnName) {
        if (getColumn(columnName).getSortType() != null) {
            return getColumn(columnName).getSortType().getValue();
        } else {
            return "";
        }
    }
    
    /**
     * Sets sort type based on string input.
     * @param columnName get index of this column.
     * @param sortType to set sortType.
     */
    public void setSortType(String columnName, String sortType) {
        if (StringUtils.isBlank(sortType)) {
            getColumn(columnName).setSortType(null);
        } else {
            getColumn(columnName).setSortType(SortTypeEnum.getByValue(sortType));
        }
    }

    /**
     * Revises the result column indexes to ensure there are no duplicates.
     */
    public void reindexColumns() {
        List<ResultColumn> selectedColumns = getSelectedColumns();
        for (int i = 0; i < selectedColumns.size(); i++) {
            selectedColumns.get(i).setColumnIndex(i);
        }
    }

}
