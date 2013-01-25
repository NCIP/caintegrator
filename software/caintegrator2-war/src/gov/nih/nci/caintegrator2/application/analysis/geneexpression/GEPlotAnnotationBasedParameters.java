/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.analysis.geneexpression;

import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;

import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.lang.StringUtils;

/**
 * Parameters used for creating an Annotation Based Gene Expression plot. 
 */
public class GEPlotAnnotationBasedParameters extends AbstractGEPlotParameters {

    private AnnotationFieldDescriptor selectedAnnotation = new AnnotationFieldDescriptor();
    private final Collection <PermissibleValue> selectedValues = new HashSet<PermissibleValue>();    
    private boolean addPatientsNotInQueriesGroup = false;
    private ReporterTypeEnum reporterType;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate() {
        getErrorMessages().clear();
        boolean isValid = true;
        isValid &= validateMultiplePlatforms();
        isValid &= validateGeneName();
        isValid &= validateSelectedAnnotation();
        isValid &= validateSelectedValues();
        return isValid;
    }

    private boolean validateSelectedValues() {
        if (getSelectedValues().size() < 1) {
            getErrorMessages().add("Must select at least 1 sample group.");
            return false;
        }
        return true;
    }

    private boolean validateSelectedAnnotation() {
        if (getSelectedAnnotation() == null) {
            getErrorMessages().add("Selected Annotation is null, please select a valid annotation.");
            return false;
        }
        return true;
    }

    private boolean validateGeneName() {
        if (StringUtils.isBlank(getGeneSymbol())) {
            getErrorMessages().add("Must enter a gene symbol");
            return false;
        }
        return true;
    }

    private boolean validateMultiplePlatforms() {
        if (isMultiplePlatformsInStudy() && StringUtils.isBlank(getPlatformName())) {
            getErrorMessages().add("Must select a platform");
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        setSelectedAnnotation(new AnnotationFieldDescriptor());
        getSelectedValues().clear();
        addPatientsNotInQueriesGroup = false;
    }
    
    /**
     * @return the selectedAnnotation
     */
    public AnnotationFieldDescriptor getSelectedAnnotation() {
        return selectedAnnotation;
    }

    /**
     * @param selectedAnnotation the selectedAnnotation to set
     */
    public void setSelectedAnnotation(AnnotationFieldDescriptor selectedAnnotation) {
        this.selectedAnnotation = selectedAnnotation;
    }

    /**
     * @return the selectedValues
     */
    public Collection<PermissibleValue> getSelectedValues() {
        return selectedValues;
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
