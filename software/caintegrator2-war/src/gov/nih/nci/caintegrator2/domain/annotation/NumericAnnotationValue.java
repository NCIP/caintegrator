package gov.nih.nci.caintegrator2.domain.annotation;

import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.application.study.ValidationException;

import java.text.DecimalFormat;

/**
 * 
 */
public class NumericAnnotationValue extends AbstractAnnotationValue {

    private static final long serialVersionUID = 1L;
    /**
     * The global decimal format that will be used by Cai2 application.
     */
    public static final String DECIMAL_FORMAT = "0.####################"; // Up to 20 dec places.
    private Double numericValue;
    
    /**
     * Empty Constructor.
     */
    public NumericAnnotationValue() { 
        // Empty Constructor
    }
    
    /**
     * Converts the given annotation value to a a new object, as well as moves it to the new 
     * annotationDefinition.
     * @param value to use to update this object.
     * @param annotationDefinition is the new definition to move value to.
     */
    public NumericAnnotationValue(AbstractAnnotationValue value, AnnotationDefinition annotationDefinition) {
        super(value, annotationDefinition);
    }

    /**
     * @return the numericValue
     */
    public Double getNumericValue() {
        return numericValue;
    }

    /**
     * @param numericValue the numericValue to set
     */
    public void setNumericValue(Double numericValue) {
        this.numericValue = numericValue;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return numericValue != null ? new DecimalFormat(DECIMAL_FORMAT).format(numericValue) : "";
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotationTypeEnum getValidAnnotationType() {
        return AnnotationTypeEnum.NUMERIC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void convertAnnotationValue(AnnotationDefinition annotationDefinition) throws ValidationException {
        switch (annotationDefinition.getDataType()) {
            case NUMERIC:
                if (annotationDefinition.equals(this.getAnnotationDefinition())) {
                    return;
                }
                NumericAnnotationValue numericAnnotationValue = new NumericAnnotationValue(this, annotationDefinition);
                numericAnnotationValue.setNumericValue(numericValue);
                break;
            case STRING:
                StringAnnotationValue stringAnnotationValue = new StringAnnotationValue(this, annotationDefinition);
                stringAnnotationValue.setStringValue(this.toString());
                break;
            case DATE:
                throw new ValidationException("Cannot convert Numeric value '" + numericValue + "' to a date");
            default:
                throw new IllegalArgumentException("Must input valid annotationType.");
        }
    }
}