package gov.nih.nci.caintegrator2.domain.annotation;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import java.util.Collection;

/**
 * 
 */
public class ValueDomain extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private String dataType;
    private String longName;
    private Long publicID;
    private String valueDomainType;
    
    private Collection<AbstractPermissibleValue> permissibleValueCollection;

    /**
     * @return the dataType
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * @param dataType the dataType to set
     */
    public void setDataType(String dataType) {
        this.dataType = dataType;
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
     * @return the valueDomainType
     */
    public String getValueDomainType() {
        return valueDomainType;
    }

    /**
     * @param valueDomainType the valueDomainType to set
     */
    public void setValueDomainType(String valueDomainType) {
        this.valueDomainType = valueDomainType;
    }

    /**
     * @return the permissibleValueCollection
     */
    public Collection<AbstractPermissibleValue> getPermissibleValueCollection() {
        return permissibleValueCollection;
    }

    /**
     * @param permissibleValueCollection the permissibleValueCollection to set
     */
    public void setPermissibleValueCollection(Collection<AbstractPermissibleValue> permissibleValueCollection) {
        this.permissibleValueCollection = permissibleValueCollection;
    }

}