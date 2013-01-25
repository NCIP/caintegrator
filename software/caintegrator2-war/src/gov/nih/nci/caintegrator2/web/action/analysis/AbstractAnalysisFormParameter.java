/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import gov.nih.nci.caintegrator2.application.analysis.AbstractParameterValue;
import gov.nih.nci.caintegrator2.application.analysis.AnalysisParameter;
import gov.nih.nci.caintegrator2.application.analysis.AnalysisParameterType;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.application.query.QueryManagementService;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ValidationAware;

/**
 * Represents a single parameter and value on an <code>AnalysisForm</code>.
 */
public abstract class AbstractAnalysisFormParameter {

    private static final String FILE_PARAMETER_SUFFIX_REGEX = "\\.filename";
    private final GenePatternAnalysisForm form;
    private AbstractParameterValue parameterValue;

    AbstractAnalysisFormParameter(GenePatternAnalysisForm form, AbstractParameterValue parameterValue) {
        if (parameterValue == null) {
            throw new IllegalArgumentException("Null value for parameterValue");
        }
        this.form = form;
        setParameterValue(parameterValue);
    }

    /**
     * @return the name
     */
    public final String getName() {
        return getParameter().getName().replaceAll(FILE_PARAMETER_SUFFIX_REGEX, "");
    }

    /**
     * @return the description
     */
    public final String getDescription() {
        return getParameter().getDescription();
    }

    /**
     * @return the required
     */
    public final boolean isRequired() {
        return getParameter().isRequired();
    }
    
    /**
     * @return the type of display element to use.
     */
    public abstract String getDisplayType();
    
    /**
     * @return the value
     */
    public abstract String getValue();

    /**
     * @param value the value to set
     */
    public abstract void setValue(String value);

    AbstractParameterValue getParameterValue() {
        return parameterValue;
    }

    final void setParameterValue(AbstractParameterValue parameterValue) {
        this.parameterValue = parameterValue;
    }

    final void validate(String fieldName, ValidationAware action) {
        if (StringUtils.isBlank(getValue())) {
            validateEmptyField(fieldName, action);
        } else {
            validateValue(fieldName, action);
        }
    }

    @SuppressWarnings("PMD.EmptyMethodInAbstractClassShouldBeAbstract") // empty default implementation
    void validateValue(String fieldName, ValidationAware action) {
        // default implementation is no-op; override if appropriate
    }

    private void validateEmptyField(String fieldName, ValidationAware action) {
        if (getParameter().isRequired()) {
            action.addFieldError(fieldName, getParameter().getName() + " is required.");
        }   
    }

    AnalysisParameter getParameter() {
        return getParameterValue().getParameter();
    }

    static AbstractAnalysisFormParameter create(GenePatternAnalysisForm form, AbstractParameterValue parameterValue) {
        if (!parameterValue.getParameter().getChoiceKeys().isEmpty()) {
            return new SelectListFormParameter(form, parameterValue);
        } else if (parameterValue.getParameter().getType().equals(AnalysisParameterType.GENOMIC_DATA)) {
            return new GenomicDataFormParameter(form, parameterValue);
        } else if (parameterValue.getParameter().getType().equals(AnalysisParameterType.SAMPLE_CLASSIFICATION)) {
            return new SampleClassificationFormParameter(form, parameterValue);
        } else {
            return new TextFieldFormParameter(form, parameterValue);
        }
    }

    GenePatternAnalysisForm getForm() {
        return form;
    }

    /**
     * Configures the underlying parameter value to prepare it to be used in an analysis method
     * invocation.
     * 
     * @param studySubscription current study subscription
     * @param queryManagementService used to retrieve any necessary data.
     * @throws InvalidCriterionException if criterion is not valid.
     */
    @SuppressWarnings("PMD.EmptyMethodInAbstractClassShouldBeAbstract") // empty default implementation
    public void configureForInvocation(StudySubscription studySubscription, 
                                       QueryManagementService queryManagementService) throws InvalidCriterionException {
        // default implementation is no-op; override as necessary
    }
    

}
