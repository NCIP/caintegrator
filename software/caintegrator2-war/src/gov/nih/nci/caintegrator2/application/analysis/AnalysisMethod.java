/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a type of analysis that can be performed.
 */
public class AnalysisMethod implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String name;
    private String description;
    private AnalysisServiceType serviceType;
    private List<AnalysisParameter> parameters = new ArrayList<AnalysisParameter>();
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * @return the serviceType
     */
    public AnalysisServiceType getServiceType() {
        return serviceType;
    }
    
    /**
     * @param serviceType the serviceType to set
     */
    public void setServiceType(AnalysisServiceType serviceType) {
        this.serviceType = serviceType;
    }
    
    /**
     * @return the parameters
     */
    public List<AnalysisParameter> getParameters() {
        return parameters;
    }
    
    /**
     * @param parameters the parameters to set
     */
    @SuppressWarnings("unused") // For use by Hibernate
    private void setParameters(List<AnalysisParameter> parameters) {
        this.parameters = parameters;
    }
    
    /**
     * Creates a configured invocation object for this method.
     * 
     * @return the invocation object.
     */
    public AnalysisMethodInvocation createInvocation() {
        AnalysisMethodInvocation invocation = new AnalysisMethodInvocation();
        invocation.setMethod(this);
        for (AnalysisParameter parameter : parameters) {
            if (parameter.getDefaultValue() != null) {
                invocation.setParameterValue(parameter, parameter.getDefaultValueCopy());
            } else {
                invocation.setParameterValue(parameter, parameter.createValue());
            }
        }
        return invocation;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getName();
    }

}
