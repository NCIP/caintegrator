/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis.geneexpression;

import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;

import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.lang.StringUtils;

/**
 * Parameters used for creating an Annotation Based Gene Expression plot. 
 */
public class GEPlotAnnotationBasedParameters extends AbstractGEPlotParameters {

    private AnnotationDefinition selectedAnnotation = new AnnotationDefinition();
    private final Collection <PermissibleValue> selectedValues = new HashSet<PermissibleValue>();    
    private EntityTypeEnum entityType;
    private boolean addPatientsNotInQueriesGroup = false;
    private ReporterTypeEnum reporterType;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate() {
        getErrorMessages().clear();
        boolean isValid = true;
        if (StringUtils.isBlank(getGeneSymbol())) {
            getErrorMessages().add("Must enter a gene symbol");
            isValid = false;
        }
        if (getSelectedAnnotation() == null) {
            getErrorMessages().add("Selected Annotation is null, please select a valid Selected Annotation.");
            isValid = false;
        }
        if (getSelectedValues().size() < 1) {
            getErrorMessages().add("Must select at least 1 grouping value");
            isValid = false;
        }
        return isValid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        setSelectedAnnotation(new AnnotationDefinition());
        getSelectedValues().clear();
        addPatientsNotInQueriesGroup = false;
    }
    
    /**
     * @return the selectedAnnotation
     */
    public AnnotationDefinition getSelectedAnnotation() {
        return selectedAnnotation;
    }

    /**
     * @param selectedAnnotation the selectedAnnotation to set
     */
    public void setSelectedAnnotation(AnnotationDefinition selectedAnnotation) {
        this.selectedAnnotation = selectedAnnotation;
    }

    /**
     * @return the selectedValues
     */
    public Collection<PermissibleValue> getSelectedValues() {
        return selectedValues;
    }

    /**
     * @return the entityType
     */
    public EntityTypeEnum getEntityType() {
        return entityType;
    }

    /**
     * @param entityType the entityType to set
     */
    public void setEntityType(EntityTypeEnum entityType) {
        this.entityType = entityType;
    }

    /**
     * @return the reporterType
     */
    public ReporterTypeEnum getReporterType() {
        return reporterType;
    }

    /**
     * @param reporterType the reporterType to set
     */
    public void setReporterType(ReporterTypeEnum reporterType) {
        this.reporterType = reporterType;
    }

    /**
     * @return the addPatientsNotInQueriesGroup
     */
    public boolean isAddPatientsNotInQueriesGroup() {
        return addPatientsNotInQueriesGroup;
    }

    /**
     * @param addPatientsNotInQueriesGroup the addPatientsNotInQueriesGroup to set
     */
    public void setAddPatientsNotInQueriesGroup(boolean addPatientsNotInQueriesGroup) {
        this.addPatientsNotInQueriesGroup = addPatientsNotInQueriesGroup;
    }

}
