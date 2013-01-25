/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */

package edu.mit.broad.genepattern.gp.services;

@SuppressWarnings({ "unchecked", "serial" })
public class ParameterInfo  implements java.io.Serializable {
    private java.util.HashMap attributes;
    private java.util.HashMap choices;
    private java.lang.String defaultValue;
    private java.lang.String description;
    private boolean inputFile;
    private java.lang.String label;
    private java.lang.String name;
    private boolean optional;
    private boolean outputFile;
    private boolean password;
    private java.lang.String value;

    public ParameterInfo() {
    }

    public ParameterInfo(
           java.util.HashMap attributes,
           java.util.HashMap choices,
           java.lang.String defaultValue,
           java.lang.String description,
           boolean inputFile,
           java.lang.String label,
           java.lang.String name,
           boolean optional,
           boolean outputFile,
           boolean password,
           java.lang.String value) {
           this.attributes = attributes;
           this.choices = choices;
           this.defaultValue = defaultValue;
           this.description = description;
           this.inputFile = inputFile;
           this.label = label;
           this.name = name;
           this.optional = optional;
           this.outputFile = outputFile;
           this.password = password;
           this.value = value;
    }


    /**
     * Gets the attributes value for this ParameterInfo.
     * 
     * @return attributes
     */
    public java.util.HashMap getAttributes() {
        return attributes;
    }


    /**
     * Sets the attributes value for this ParameterInfo.
     * 
     * @param attributes
     */
    public void setAttributes(java.util.HashMap attributes) {
        this.attributes = attributes;
    }


    /**
     * Gets the choices value for this ParameterInfo.
     * 
     * @return choices
     */
    public java.util.HashMap getChoices() {
        return choices;
    }


    /**
     * Sets the choices value for this ParameterInfo.
     * 
     * @param choices
     */
    public void setChoices(java.util.HashMap choices) {
        this.choices = choices;
    }


    /**
     * Gets the defaultValue value for this ParameterInfo.
     * 
     * @return defaultValue
     */
    public java.lang.String getDefaultValue() {
        return defaultValue;
    }


    /**
     * Sets the defaultValue value for this ParameterInfo.
     * 
     * @param defaultValue
     */
    public void setDefaultValue(java.lang.String defaultValue) {
        this.defaultValue = defaultValue;
    }


    /**
     * Gets the description value for this ParameterInfo.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this ParameterInfo.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the inputFile value for this ParameterInfo.
     * 
     * @return inputFile
     */
    public boolean isInputFile() {
        return inputFile;
    }


    /**
     * Sets the inputFile value for this ParameterInfo.
     * 
     * @param inputFile
     */
    public void setInputFile(boolean inputFile) {
        this.inputFile = inputFile;
    }


    /**
     * Gets the label value for this ParameterInfo.
     * 
     * @return label
     */
    public java.lang.String getLabel() {
        return label;
    }


    /**
     * Sets the label value for this ParameterInfo.
     * 
     * @param label
     */
    public void setLabel(java.lang.String label) {
        this.label = label;
    }


    /**
     * Gets the name value for this ParameterInfo.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this ParameterInfo.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the optional value for this ParameterInfo.
     * 
     * @return optional
     */
    public boolean isOptional() {
        return optional;
    }


    /**
     * Sets the optional value for this ParameterInfo.
     * 
     * @param optional
     */
    public void setOptional(boolean optional) {
        this.optional = optional;
    }


    /**
     * Gets the outputFile value for this ParameterInfo.
     * 
     * @return outputFile
     */
    public boolean isOutputFile() {
        return outputFile;
    }


    /**
     * Sets the outputFile value for this ParameterInfo.
     * 
     * @param outputFile
     */
    public void setOutputFile(boolean outputFile) {
        this.outputFile = outputFile;
    }


    /**
     * Gets the password value for this ParameterInfo.
     * 
     * @return password
     */
    public boolean isPassword() {
        return password;
    }


    /**
     * Sets the password value for this ParameterInfo.
     * 
     * @param password
     */
    public void setPassword(boolean password) {
        this.password = password;
    }


    /**
     * Gets the value value for this ParameterInfo.
     * 
     * @return value
     */
    public java.lang.String getValue() {
        return value;
    }


    /**
     * Sets the value value for this ParameterInfo.
     * 
     * @param value
     */
    public void setValue(java.lang.String value) {
        this.value = value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ParameterInfo)) return false;
        ParameterInfo other = (ParameterInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.attributes==null && other.getAttributes()==null) || 
             (this.attributes!=null &&
              this.attributes.equals(other.getAttributes()))) &&
            ((this.choices==null && other.getChoices()==null) || 
             (this.choices!=null &&
              this.choices.equals(other.getChoices()))) &&
            ((this.defaultValue==null && other.getDefaultValue()==null) || 
             (this.defaultValue!=null &&
              this.defaultValue.equals(other.getDefaultValue()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            this.inputFile == other.isInputFile() &&
            ((this.label==null && other.getLabel()==null) || 
             (this.label!=null &&
              this.label.equals(other.getLabel()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            this.optional == other.isOptional() &&
            this.outputFile == other.isOutputFile() &&
            this.password == other.isPassword() &&
            ((this.value==null && other.getValue()==null) || 
             (this.value!=null &&
              this.value.equals(other.getValue())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getAttributes() != null) {
            _hashCode += getAttributes().hashCode();
        }
        if (getChoices() != null) {
            _hashCode += getChoices().hashCode();
        }
        if (getDefaultValue() != null) {
            _hashCode += getDefaultValue().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        _hashCode += (isInputFile() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getLabel() != null) {
            _hashCode += getLabel().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        _hashCode += (isOptional() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isOutputFile() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isPassword() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getValue() != null) {
            _hashCode += getValue().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ParameterInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("Analysis", "ParameterInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attributes");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attributes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Map"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("choices");
        elemField.setXmlName(new javax.xml.namespace.QName("", "choices"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Map"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("defaultValue");
        elemField.setXmlName(new javax.xml.namespace.QName("", "defaultValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("", "description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inputFile");
        elemField.setXmlName(new javax.xml.namespace.QName("", "inputFile"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("label");
        elemField.setXmlName(new javax.xml.namespace.QName("", "label"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("optional");
        elemField.setXmlName(new javax.xml.namespace.QName("", "optional"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("outputFile");
        elemField.setXmlName(new javax.xml.namespace.QName("", "outputFile"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("password");
        elemField.setXmlName(new javax.xml.namespace.QName("", "password"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("value");
        elemField.setXmlName(new javax.xml.namespace.QName("", "value"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
