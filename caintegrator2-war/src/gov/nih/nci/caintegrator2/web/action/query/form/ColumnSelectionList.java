/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AnnotationGroup;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

/**
 * Allows a user to select columns to be shown in query results.
 */
public class ColumnSelectionList implements Comparable<ColumnSelectionList> {
    private final AnnotationGroup annotationGroup;
    private final OptionList<AnnotationFieldDescriptor> columnList;
    private final ResultConfiguration resultConfiguration;

    ColumnSelectionList(ResultConfiguration resultConfiguration, AnnotationGroup annotationGroup,
            Set<AnnotationFieldDescriptor> fieldDescriptors) {
        this.resultConfiguration = resultConfiguration;
        this.annotationGroup = annotationGroup;
        Collection<AnnotationFieldDescriptor> filterDescriptors = Collections2.filter(fieldDescriptors,
                new Predicate<AnnotationFieldDescriptor>() {
                    @Override
                    public boolean apply(AnnotationFieldDescriptor adf) {
                        return adf.getDefinition() != null;
                    }
        });
        columnList = createColumnList(filterDescriptors);
    }

    private OptionList<AnnotationFieldDescriptor> createColumnList(
            Collection<AnnotationFieldDescriptor> annotationFieldDescriptors) {
        OptionList<AnnotationFieldDescriptor> options = new OptionList<AnnotationFieldDescriptor>();
        for (AnnotationFieldDescriptor annotationFieldDescriptor : sortFieldDescriptors(annotationFieldDescriptors)) {
            options.addOption(annotationFieldDescriptor.getDefinition().getDisplayName(), annotationFieldDescriptor);
        }
        return options;
    }

    private List<AnnotationFieldDescriptor> sortFieldDescriptors(Collection<AnnotationFieldDescriptor> definitions) {
        List<AnnotationFieldDescriptor> sortedDefinitions = new ArrayList<AnnotationFieldDescriptor>();
        sortedDefinitions.addAll(definitions);
        Comparator<AnnotationFieldDescriptor> comparator = new Comparator<AnnotationFieldDescriptor>() {
            public int compare(AnnotationFieldDescriptor definition1, AnnotationFieldDescriptor definition2) {
                return definition1.getDefinition().getDisplayName().compareTo(
                        definition2.getDefinition().getDisplayName());
            }
        };
        Collections.sort(sortedDefinitions, comparator);
        return sortedDefinitions;
    }

    OptionList<AnnotationFieldDescriptor> getColumnList() {
        return columnList;
    }

    /**
     * @return the column options.
     */
    public List<Option<AnnotationFieldDescriptor>> getOptions() {
        return getColumnList().getOptions();
    }

    /**
     * Returns the selected column names.
     *
     * @return the column names.
     */
    public String[] getValues() {
        List<String> columnNames = new ArrayList<String>();
        for (ResultColumn column : resultConfiguration.getQuery().retrieveVisibleColumns()) {
            if (annotationGroup.equals(column.getAnnotationFieldDescriptor().getAnnotationGroup())) {
                columnNames.add(column.getAnnotationFieldDescriptor().getDefinition().getDisplayName());
            }
        }
        return columnNames.toArray(new String[columnNames.size()]);
    }

    /**
     * Sets the selected columns by column names.
     *
     * @param values the array of selected column names.
     */
    public void setValues(String[] values) {
        Set<AnnotationFieldDescriptor> selectedColumns = new HashSet<AnnotationFieldDescriptor>();
        selectedColumns.addAll(getColumnList().getActualValues(values));
        handleExistingAndRemovedColumns(resultConfiguration.getQuery(), selectedColumns);
        addNewSelectedColumns(resultConfiguration.getQuery().getColumnCollection(), selectedColumns);
    }

    /**
     * Seelect all columns.
     */
    public void selectAllValues() {
        Set<AnnotationFieldDescriptor> selectedColumns = new HashSet<AnnotationFieldDescriptor>();
        selectedColumns.addAll(getColumnList().getAllActualValues());
        handleExistingAndRemovedColumns(resultConfiguration.getQuery(), selectedColumns);
        addNewSelectedColumns(resultConfiguration.getQuery().getColumnCollection(), selectedColumns);

    }

    private void addNewSelectedColumns(Collection<ResultColumn> columnCollection,
            Set<AnnotationFieldDescriptor> selectedColumns) {
        for (AnnotationFieldDescriptor annotationFieldDescriptor : selectedColumns) {
            ResultColumn column = new ResultColumn();
            column.setAnnotationFieldDescriptor(annotationFieldDescriptor);
            column.setColumnIndex(columnCollection.size());
            columnCollection.add(column);
        }
    }

    private void handleExistingAndRemovedColumns(Query query,
            Set<AnnotationFieldDescriptor> selectedColumns) {
        for (ResultColumn column : query.retrieveVisibleColumns()) {
            if (annotationGroup.equals(column.getAnnotationFieldDescriptor().getAnnotationGroup())) {
                if (selectedColumns.contains(column.getAnnotationFieldDescriptor())) {
                    selectedColumns.remove(column.getAnnotationFieldDescriptor());
                } else {
                    query.getColumnCollection().remove(column);
                }
            }
        }
    }

    /**
     * @return true if there are no associated columns.
     */
    public boolean isEmpty() {
        return getColumnList().getOptions().isEmpty();
    }

    /**
     * @return the annotationGroup
     */
    public AnnotationGroup getAnnotationGroup() {
        return annotationGroup;
    }

    /**
     * {@inheritDoc}
     */
    public int compareTo(ColumnSelectionList o) {
        if (annotationGroup != null) {
            return annotationGroup.getName().compareTo(o.getAnnotationGroup().getName());
        }
        return 0;
    }



}
