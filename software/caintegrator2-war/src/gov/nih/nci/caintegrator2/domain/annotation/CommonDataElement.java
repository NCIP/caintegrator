/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.annotation;


import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

/**
 * 
 */
public class CommonDataElement extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private String contextName;
    private String definition;
    private String longName;
    private String preferredName;
    private Long publicID;
    private String registrationStatus;
    private String version;
    private String workflowStatus;
    
    private ValueDomain valueDomain = new ValueDomain();

    /**
     * @return the contextName
     */
    public String getContextName() {
        return contextName;
    }

    /**
     * @param contextName the contextName to set
     */
    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    /**
     * @return the definition
     */
    public String getDefinition() {
        return definition;
    }

    /**
     * @param definition the definition to set
     */
    public void setDefinition(String definition) {
        this.definition = definition;
    }

    /**
     * @return the longName
     */
    public String getLongName() {
        return longName;
    }

    /**
     * @param longName the longName to set
     */
    public void setLongName(String longName) {
        this.longName = longName;
    }

    /**
     * @return the preferredName
     */
    public String getPreferredName() {
        return preferredName;
    }

    /**
     * @param preferredName the preferredName to set
     */
    public void setPreferredName(String preferredName) {
        this.preferredName = preferredName;
    }

    /**
     * @return the publicID
     */
    public Long getPublicID() {
        return publicID;
    }

    /**
     * @param publicID the publicID to set
     */
    public void setPublicID(Long publicID) {
        this.publicID = publicID;
    }

    /**
     * @return the registrationStatus
     */
    public String getRegistrationStatus() {
        return registrationStatus;
    }

    /**
     * @param registrationStatus the registrationStatus to set
     */
    public void setRegistrationStatus(String registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the workflowStatus
     */
    public String getWorkflowStatus() {
        return workflowStatus;
    }

    /**
     * @param workflowStatus the workflowStatus to set
     */
    public void setWorkflowStatus(String workflowStatus) {
        this.workflowStatus = workflowStatus;
    }

    /**
     * @return the valueDomain
     */
    public ValueDomain getValueDomain() {
        return valueDomain;
    }

    /**
     * @param valueDomain the valueDomain to set
     */
    public void setValueDomain(ValueDomain valueDomain) {
        this.valueDomain = valueDomain;
    }
    
}
