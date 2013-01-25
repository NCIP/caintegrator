/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */

package edu.mit.broad.genepattern.gp.services;

@SuppressWarnings({ "unchecked", "serial" })
public class FileWrapper  implements java.io.Serializable {
    private javax.activation.DataHandler dataHandler;
    private java.lang.String filename;
    private long lastModified;
    private long length;

    public FileWrapper() {
    }

    public FileWrapper(
           javax.activation.DataHandler dataHandler,
           java.lang.String filename,
           long lastModified,
           long length) {
           this.dataHandler = dataHandler;
           this.filename = filename;
           this.lastModified = lastModified;
           this.length = length;
    }


    /**
     * Gets the dataHandler value for this FileWrapper.
     * 
     * @return dataHandler
     */
    public javax.activation.DataHandler getDataHandler() {
        return dataHandler;
    }


    /**
     * Sets the dataHandler value for this FileWrapper.
     * 
     * @param dataHandler
     */
    public void setDataHandler(javax.activation.DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }


    /**
     * Gets the filename value for this FileWrapper.
     * 
     * @return filename
     */
    public java.lang.String getFilename() {
        return filename;
    }


    /**
     * Sets the filename value for this FileWrapper.
     * 
     * @param filename
     */
    public void setFilename(java.lang.String filename) {
        this.filename = filename;
    }


    /**
     * Gets the lastModified value for this FileWrapper.
     * 
     * @return lastModified
     */
    public long getLastModified() {
        return lastModified;
    }


    /**
     * Sets the lastModified value for this FileWrapper.
     * 
     * @param lastModified
     */
    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }


    /**
     * Gets the length value for this FileWrapper.
     * 
     * @return length
     */
    public long getLength() {
        return length;
    }


    /**
     * Sets the length value for this FileWrapper.
     * 
     * @param length
     */
    public void setLength(long length) {
        this.length = length;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FileWrapper)) return false;
        FileWrapper other = (FileWrapper) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.dataHandler==null && other.getDataHandler()==null) || 
             (this.dataHandler!=null &&
              this.dataHandler.equals(other.getDataHandler()))) &&
            ((this.filename==null && other.getFilename()==null) || 
             (this.filename!=null &&
              this.filename.equals(other.getFilename()))) &&
            this.lastModified == other.getLastModified() &&
            this.length == other.getLength();
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
        if (getDataHandler() != null) {
            _hashCode += getDataHandler().hashCode();
        }
        if (getFilename() != null) {
            _hashCode += getFilename().hashCode();
        }
        _hashCode += new Long(getLastModified()).hashCode();
        _hashCode += new Long(getLength()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FileWrapper.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("Analysis", "FileWrapper"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataHandler");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataHandler"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "DataHandler"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("filename");
        elemField.setXmlName(new javax.xml.namespace.QName("", "filename"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastModified");
        elemField.setXmlName(new javax.xml.namespace.QName("", "lastModified"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("length");
        elemField.setXmlName(new javax.xml.namespace.QName("", "length"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
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
