package gov.nih.nci.caintegrator2.domain.annotation;

import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;


/**
 * 
 */
public class StringAnnotationValue extends AbstractAnnotationValue {

    private static final long serialVersionUID = 1L;
    
    private String stringValue;

    /**
     * @return the stringValue
     */
    public String getStringValue() {
        return stringValue;
    }

    /**
     * @param stringValue the stringValue to set
     */
    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return stringValue;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotationTypeEnum getValidAnnotationType() {
        return AnnotationTypeEnum.STRING;
    }

}