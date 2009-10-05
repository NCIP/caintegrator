package gov.nih.nci.caintegrator2.domain.annotation;

import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 */
public class ValueDomain extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private String longName;
    private Long publicID;
    private Long highValueNumber;
    private Long lowValueNumber;
    private Integer maximumLength;
    private Integer minimumLength;
    private AnnotationTypeEnum dataType;
    private Set<PermissibleValue> permissibleValueCollection = new HashSet<PermissibleValue>();
    
    /**
     * @return the highValueNumber
     */
    public Long getHighValueNumber() {
        return highValueNumber;
    }

    /**
     * @param highValueNumber the highValueNumber to set
     */
    public void setHighValueNumber(Long highValueNumber) {
        this.highValueNumber = highValueNumber;
    }

    /**
     * @return the lowValueNumber
     */
    public Long getLowValueNumber() {
        return lowValueNumber;
    }

    /**
     * @param lowValueNumber the lowValueNumber to set
     */
    public void setLowValueNumber(Long lowValueNumber) {
        this.lowValueNumber = lowValueNumber;
    }

    /**
     * @return the maximumLength
     */
    public Integer getMaximumLength() {
        return maximumLength;
    }

    /**
     * @param maximumLength the maximumLength to set
     */
    public void setMaximumLength(Integer maximumLength) {
        this.maximumLength = maximumLength;
    }

    /**
     * @return the minimumLength
     */
    public Integer getMinimumLength() {
        return minimumLength;
    }

    /**
     * @param minimumLength the minimumLength to set
     */
    public void setMinimumLength(Integer minimumLength) {
        this.minimumLength = minimumLength;
    }


    /**
     * @return the dataType
     */
    public AnnotationTypeEnum getDataType() {
        return dataType;
    }

    /**
     * @param dataType the dataType to set
     */
    public void setDataType(AnnotationTypeEnum dataType) {
        this.dataType = dataType;
    }

    /**
     * @return the dataType
     */
    public String getDataTypeString() {
        return dataType == null ? null : dataType.getValue();
    }

    /**
     * @param dataTypeString the dataType to set
     */
    public void setDataTypeString(String dataTypeString) {
        if (dataTypeString == null) {
            this.dataType = null;
        } else {
            this.dataType = AnnotationTypeEnum.getByValue(dataTypeString);
        }
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
    public boolean isEnumerated() {
        return permissibleValueCollection.isEmpty() ? false : true;
    }

    /**
     * @return the permissibleValueCollection
     */
    public Set<PermissibleValue> getPermissibleValueCollection() {
        return permissibleValueCollection;
    }
    
    /**
     * @param permissibleValueCollection the permissibleValueCollection to set
     */
    @SuppressWarnings("unused")     // required by Hibernate
    private void setPermissibleValueCollection(Set<PermissibleValue> permissibleValueCollection) {
        this.permissibleValueCollection = permissibleValueCollection;
    }

}