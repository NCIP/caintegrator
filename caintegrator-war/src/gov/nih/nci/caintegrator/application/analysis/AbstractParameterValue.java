/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.analysis;

import java.io.Serializable;

/**
 * Base class for parameter values.
 */
public abstract class AbstractParameterValue implements Serializable, Cloneable {

    /**
     * Default serialize.
     */
    private static final long serialVersionUID = 1L;

    private AnalysisParameter parameter;

    /**
     * Instantiates a new instance.
     */
    protected AbstractParameterValue() {
        super();
    }

    /**
     * @return the parameter
     */
    public AnalysisParameter getParameter() {
        return parameter;
    }

    /**
     * @param parameter the parameter to set
     */
    public void setParameter(AnalysisParameter parameter) {
        this.parameter = parameter;
    }

    /**
     * Sets the value from a <code>String</code>.
     *
     * @param stringValue the value
     */
    public abstract void setValueFromString(String stringValue);

    /**
     * Returns the value as a <code>String</code>.
     *
     * @return the value
     */
    public abstract String getValueAsString();

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getValueAsString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Unexpected CloneNotSupportedException", e);
        }
    }

}
