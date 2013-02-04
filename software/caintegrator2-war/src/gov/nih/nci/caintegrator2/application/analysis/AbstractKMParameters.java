/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import java.util.ArrayList;
import java.util.List;

import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;

/**
 * Abstract class that represents input parameters for a Kaplan-Meier plot.
 */
public abstract class AbstractKMParameters {
    private SurvivalValueDefinition survivalValueDefinition = new SurvivalValueDefinition();
    private final List<String> errorMessages = new ArrayList<String>();
    
    /**
     * Validates that all parameters are set.
     * @return T/F value.
     */
    public abstract boolean validate();
    
    /**
     * Clears all values.
     */
    public abstract void clear();
    
    
    /**
     * Validates the survival value definition.
     * @param currentValidation current status of validation.
     * @return T/F value if it is valid or not.
     */
    protected boolean validateSurvivalValueDefinition(boolean currentValidation) {
        boolean isValid = currentValidation;
        if (getSurvivalValueDefinition().getId() == null) {
            getErrorMessages().add("Must select a valid Survival Value Definition.");
            isValid = false;
        } else {
            try {
                Cai2Util.validateSurvivalValueDefinition(getSurvivalValueDefinition());
            } catch (InvalidSurvivalValueDefinitionException e) {
                getErrorMessages().add(e.getMessage());
                isValid = false;
            }
        }
        return isValid;
    }
    
    
    /**
     * @return the survivalValueDefinition
     */
    public SurvivalValueDefinition getSurvivalValueDefinition() {
        return survivalValueDefinition;
    }

    /**
     * @param survivalValueDefinition the survivalValueDefinition to set
     */
    public void setSurvivalValueDefinition(SurvivalValueDefinition survivalValueDefinition) {
        this.survivalValueDefinition = survivalValueDefinition;
    }

    /**
     * @return the errorMessages
     */
    public List<String> getErrorMessages() {
        return errorMessages;
    }

}
