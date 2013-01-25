/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */

package edu.mit.broad.genepattern.gp.services;

@SuppressWarnings({ "unchecked", "serial" })
public class WebServiceException  extends org.apache.axis.AxisFault  implements java.io.Serializable {
    private java.lang.Object rootCause;

    public WebServiceException() {
    }

    public WebServiceException(
           java.lang.Object rootCause) {
        this.rootCause = rootCause;
    }


    /**
     * Gets the rootCause value for this WebServiceException.
     * 
     * @return rootCause
     */
    public java.lang.Object getRootCause() {
        return rootCause;
    }


    /**
     * Sets the rootCause value for this WebServiceException.
     * 
     * @param rootCause
     */
    public void setRootCause(java.lang.Object rootCause) {
        this.rootCause = rootCause;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof WebServiceException)) return false;
        WebServiceException other = (WebServiceException) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.rootCause==null && other.getRootCause()==null) || 
             (this.rootCause!=null &&
              this.rootCause.equals(other.getRootCause())));
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
        if (getRootCause() != null) {
            _hashCode += getRootCause().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(WebServiceException.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://webservice.genepattern.org", "WebServiceException"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rootCause");
        elemField.setXmlName(new javax.xml.namespace.QName("", "rootCause"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
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


    /**
     * Writes the exception data to the faultDetails
     */
    public void writeDetails(javax.xml.namespace.QName qname, org.apache.axis.encoding.SerializationContext context) throws java.io.IOException {
        context.serialize(qname, null, this);
    }
}
