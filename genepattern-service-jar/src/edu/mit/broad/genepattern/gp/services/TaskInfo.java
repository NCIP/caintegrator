/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */

package edu.mit.broad.genepattern.gp.services;

@SuppressWarnings({ "unchecked", "serial" })
public class TaskInfo  implements java.io.Serializable {
    private int ID;
    private int accessId;
    private java.util.HashMap attributes;
    private java.lang.String description;
    private java.lang.String lsid;
    private java.lang.String name;
    private java.lang.String parameterInfo;
    private edu.mit.broad.genepattern.gp.services.ParameterInfo[] parameterInfoArray;
    private boolean pipeline;
    private java.lang.String shortName;
    private java.lang.Object taskInfoAttributes;
    private java.lang.String userId;
    private java.lang.Boolean visualizer;

    public TaskInfo() {
    }

    public TaskInfo(
           int ID,
           int accessId,
           java.util.HashMap attributes,
           java.lang.String description,
           java.lang.String lsid,
           java.lang.String name,
           java.lang.String parameterInfo,
           edu.mit.broad.genepattern.gp.services.ParameterInfo[] parameterInfoArray,
           boolean pipeline,
           java.lang.String shortName,
           java.lang.Object taskInfoAttributes,
           java.lang.String userId) {
           this.ID = ID;
           this.accessId = accessId;
           this.attributes = attributes;
           this.description = description;
           this.lsid = lsid;
           this.name = name;
           this.parameterInfo = parameterInfo;
           this.parameterInfoArray = parameterInfoArray;
           this.pipeline = pipeline;
           this.shortName = shortName;
           this.taskInfoAttributes = taskInfoAttributes;
           this.userId = userId;
    }


    /**
     * Gets the ID value for this TaskInfo.
     * 
     * @return ID
     */
    public int getID() {
        return ID;
    }


    /**
     * Sets the ID value for this TaskInfo.
     * 
     * @param ID
     */
    public void setID(int ID) {
        this.ID = ID;
    }


    /**
     * Gets the accessId value for this TaskInfo.
     * 
     * @return accessId
     */
    public int getAccessId() {
        return accessId;
    }


    /**
     * Sets the accessId value for this TaskInfo.
     * 
     * @param accessId
     */
    public void setAccessId(int accessId) {
        this.accessId = accessId;
    }


    /**
     * Gets the attributes value for this TaskInfo.
     * 
     * @return attributes
     */
    public java.util.HashMap getAttributes() {
        return attributes;
    }


    /**
     * Sets the attributes value for this TaskInfo.
     * 
     * @param attributes
     */
    public void setAttributes(java.util.HashMap attributes) {
        this.attributes = attributes;
    }


    /**
     * Gets the description value for this TaskInfo.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this TaskInfo.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the lsid value for this TaskInfo.
     * 
     * @return lsid
     */
    public java.lang.String getLsid() {
        return lsid;
    }


    /**
     * Sets the lsid value for this TaskInfo.
     * 
     * @param lsid
     */
    public void setLsid(java.lang.String lsid) {
        this.lsid = lsid;
    }


    /**
     * Gets the name value for this TaskInfo.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this TaskInfo.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the parameterInfo value for this TaskInfo.
     * 
     * @return parameterInfo
     */
    public java.lang.String getParameterInfo() {
        return parameterInfo;
    }


    /**
     * Sets the parameterInfo value for this TaskInfo.
     * 
     * @param parameterInfo
     */
    public void setParameterInfo(java.lang.String parameterInfo) {
        this.parameterInfo = parameterInfo;
    }


    /**
     * Gets the parameterInfoArray value for this TaskInfo.
     * 
     * @return parameterInfoArray
     */
    public edu.mit.broad.genepattern.gp.services.ParameterInfo[] getParameterInfoArray() {
        return parameterInfoArray;
    }


    /**
     * Sets the parameterInfoArray value for this TaskInfo.
     * 
     * @param parameterInfoArray
     */
    public void setParameterInfoArray(edu.mit.broad.genepattern.gp.services.ParameterInfo[] parameterInfoArray) {
        this.parameterInfoArray = parameterInfoArray;
    }


    /**
     * Gets the pipeline value for this TaskInfo.
     * 
     * @return pipeline
     */
    public boolean isPipeline() {
        return pipeline;
    }


    /**
     * Sets the pipeline value for this TaskInfo.
     * 
     * @param pipeline
     */
    public void setPipeline(boolean pipeline) {
        this.pipeline = pipeline;
    }


    /**
     * Gets the shortName value for this TaskInfo.
     * 
     * @return shortName
     */
    public java.lang.String getShortName() {
        return shortName;
    }


    /**
     * Sets the shortName value for this TaskInfo.
     * 
     * @param shortName
     */
    public void setShortName(java.lang.String shortName) {
        this.shortName = shortName;
    }


    /**
     * Gets the taskInfoAttributes value for this TaskInfo.
     * 
     * @return taskInfoAttributes
     */
    public java.lang.Object getTaskInfoAttributes() {
        return taskInfoAttributes;
    }


    /**
     * Sets the taskInfoAttributes value for this TaskInfo.
     * 
     * @param taskInfoAttributes
     */
    public void setTaskInfoAttributes(java.lang.Object taskInfoAttributes) {
        this.taskInfoAttributes = taskInfoAttributes;
    }


    /**
     * Gets the userId value for this TaskInfo.
     * 
     * @return userId
     */
    public java.lang.String getUserId() {
        return userId;
    }


    /**
     * Sets the userId value for this TaskInfo.
     * 
     * @param userId
     */
    public void setUserId(java.lang.String userId) {
        this.userId = userId;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TaskInfo)) return false;
        TaskInfo other = (TaskInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.ID == other.getID() &&
            this.accessId == other.getAccessId() &&
            ((this.attributes==null && other.getAttributes()==null) || 
             (this.attributes!=null &&
              this.attributes.equals(other.getAttributes()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.lsid==null && other.getLsid()==null) || 
             (this.lsid!=null &&
              this.lsid.equals(other.getLsid()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.parameterInfo==null && other.getParameterInfo()==null) || 
             (this.parameterInfo!=null &&
              this.parameterInfo.equals(other.getParameterInfo()))) &&
            ((this.parameterInfoArray==null && other.getParameterInfoArray()==null) || 
             (this.parameterInfoArray!=null &&
              java.util.Arrays.equals(this.parameterInfoArray, other.getParameterInfoArray()))) &&
            this.pipeline == other.isPipeline() &&
            ((this.shortName==null && other.getShortName()==null) || 
             (this.shortName!=null &&
              this.shortName.equals(other.getShortName()))) &&
            ((this.taskInfoAttributes==null && other.getTaskInfoAttributes()==null) || 
             (this.taskInfoAttributes!=null &&
              this.taskInfoAttributes.equals(other.getTaskInfoAttributes()))) &&
            ((this.userId==null && other.getUserId()==null) || 
             (this.userId!=null &&
              this.userId.equals(other.getUserId())));
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
        _hashCode += getID();
        _hashCode += getAccessId();
        if (getAttributes() != null) {
            _hashCode += getAttributes().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getLsid() != null) {
            _hashCode += getLsid().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getParameterInfo() != null) {
            _hashCode += getParameterInfo().hashCode();
        }
        if (getParameterInfoArray() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getParameterInfoArray());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getParameterInfoArray(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += (isPipeline() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getShortName() != null) {
            _hashCode += getShortName().hashCode();
        }
        if (getTaskInfoAttributes() != null) {
            _hashCode += getTaskInfoAttributes().hashCode();
        }
        if (getUserId() != null) {
            _hashCode += getUserId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TaskInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("Analysis", "TaskInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accessId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "accessId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attributes");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attributes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Map"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("", "description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lsid");
        elemField.setXmlName(new javax.xml.namespace.QName("", "lsid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("parameterInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "parameterInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("parameterInfoArray");
        elemField.setXmlName(new javax.xml.namespace.QName("", "parameterInfoArray"));
        elemField.setXmlType(new javax.xml.namespace.QName("Analysis", "ParameterInfo"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pipeline");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pipeline"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shortName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "shortName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("taskInfoAttributes");
        elemField.setXmlName(new javax.xml.namespace.QName("", "taskInfoAttributes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "userId"));
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

    /**
     * @return the visualizer
     */
    public java.lang.Boolean getVisualizer() {
        return visualizer;
    }

    /**
     * @param visualizer the visualizer to set
     */
    public void setVisualizer(java.lang.Boolean visualizer) {
        this.visualizer = visualizer;
    }

}
