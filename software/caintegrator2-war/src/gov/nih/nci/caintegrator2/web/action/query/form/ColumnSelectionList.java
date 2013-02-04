/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Allows a user to select columns to be shown in query results.
 */
public class ColumnSelectionList {
    
    private final OptionList<AnnotationDefinition> columnList;
    private final EntityTypeEnum entityType;
    private final ResultConfiguration resultConfiguration;
    
    ColumnSelectionList(ResultConfiguration resultConfiguration,
            Collection<AnnotationDefinition> annotationDefinitions, EntityTypeEnum entityType) {
        this.resultConfiguration = resultConfiguration;
        this.entityType = entityType;
        columnList = createColumnList(annotationDefinitions);
    }

    private OptionList<AnnotationDefinition> createColumnList(Collection<AnnotationDefinition> annotationDefinitions) {
        OptionList<AnnotationDefinition> options = new OptionList<AnnotationDefinition>();
        for (AnnotationDefinition annotationDefinition : sortDefinitions(annotationDefinitions)) {
            options.addOption(annotationDefinition.getDisplayName(), annotationDefinition);
        }
        return options;
    }

    private List<AnnotationDefinition> sortDefinitions(Collection<AnnotationDefinition> definitions) {
        List<AnnotationDefinition> sortedDefinitions = new ArrayList<AnnotationDefinition>();
        sortedDefinitions.addAll(definitions);
        Comparator<AnnotationDefinition> comparator = new Comparator<AnnotationDefinition>() {
            public int compare(AnnotationDefinition definition1, AnnotationDefinition definition2) {
                return definition1.getDisplayName().compareTo(definition2.getDisplayName());
            }
        };
        Collections.sort(sortedDefinitions, comparator);
        return sortedDefinitions;
    }

    OptionList<AnnotationDefinition> getColumnList() {
        return columnList;
    }
    
    /**
     * @return the column options.
     */
    public List<Option<AnnotationDefinition>> getOptions() {
        return getColumnList().getOptions();
    }
    
    /**
     * Returns the selected column names.
     * 
     * @return the column names.
     */
    public String[] getValues() {
        List<String> columnNames = new ArrayList<String>();
        for (ResultColumn column : resultConfiguration.getQuery().getColumnCollection()) {
            if (entityType.equals(column.getEntityType())) {
                columnNames.add(column.getAnnotationDefinition().getDisplayName());
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
        Set<AnnotationDefinition> selectedColumns = new HashSet<AnnotationDefinition>();
        selectedColumns.addAll(getColumnList().getActualValues(values));
        handleExistingAndRemovedColumns(resultConfiguration.getQuery().getColumnCollection(), selectedColumns);
        addNewSelectedColumns(resultConfiguration.getQuery().getColumnCollection(), selectedColumns);
    }

    private void addNewSelectedColumns(Collection<ResultColumn> columnCollection,
            Set<AnnotationDefinition> selectedColumns) {
        for (AnnotationDefinition annotationDefinition : selectedColumns) {
            ResultColumn column = new ResultColumn();
            column.setEntityType(entityType);
            column.setAnnotationDefinition(annotationDefinition);
            column.setColumnIndex(columnCollection.size());
            columnCollection.add(column);
        }
    }

    private void handleExistingAndRemovedColumns(Collection<ResultColumn> columnCollection,
            Set<AnnotationDefinition> selectedColumns) {
        Iterator<ResultColumn> columnIterator = columnCollection.iterator();
        while (columnIterator.hasNext()) {
            ResultColumn nextColumn = columnIterator.next();
            if (entityType.equals(nextColumn.getEntityType())) {
                if (selectedColumns.contains(nextColumn.getAnnotationDefinition())) {
                    selectedColumns.remove(nextColumn.getAnnotationDefinition());
                } else {
                    columnIterator.remove();
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
    

}
