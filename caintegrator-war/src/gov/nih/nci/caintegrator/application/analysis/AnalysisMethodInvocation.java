/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.analysis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains the configuration for invoking an analysis job.
 */
public class AnalysisMethodInvocation implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private AnalysisMethod method;
    private final Map<AnalysisParameter, AbstractParameterValue> parameterValues = 
        new HashMap<AnalysisParameter, AbstractParameterValue>();

    /**
     * @return the method
     */
    public AnalysisMethod getMethod() {
        return method;
    }
    
    /**
     * @param method the method to set
     */
    public void setMethod(AnalysisMethod method) {
        this.method = method;
    }

    /**
     * Gets the value for a given parameter.
     * 
     * @param parameter the parameter to retrieve
     * @return the value
     */
    public AbstractParameterValue getParameterValue(AnalysisParameter parameter) {
        return parameterValues.get(parameter);
    }

    /**
     * Sets the value for a given parameter.
     * 
     * @param parameter the parameter to set
     * @param value the value
     */
    public void setParameterValue(AnalysisParameter parameter, AbstractParameterValue value) {
        parameterValues.put(parameter, value);
    }

    /**
     * Returns an unmodifiable list of the parameter values.
     * 
     * @return the parameterValues
     */
    public List<AbstractParameterValue> getParameterValues() {
        List<AbstractParameterValue> values = new ArrayList<AbstractParameterValue>(parameterValues.size());
        for (AnalysisParameter parameter : method.getParameters()) {
            values.add(parameterValues.get(parameter));
        }
        return Collections.unmodifiableList(values);
    }
    
}
