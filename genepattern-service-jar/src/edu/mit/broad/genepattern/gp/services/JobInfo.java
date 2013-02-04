/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */

package edu.mit.broad.genepattern.gp.services;

@SuppressWarnings({ "unchecked", "serial" })
public class JobInfo  implements java.io.Serializable {
    private java.util.Calendar dateCompleted;
    private java.util.Calendar dateSubmitted;
    private java.lang.Boolean deleted;
    private java.lang.Long elapsedTimeMillis;
    private int jobNumber;
    private java.lang.String parameterInfo;
    private edu.mit.broad.genepattern.gp.services.ParameterInfo[] parameterInfoArray;
    private java.lang.String status;
    private int taskID;
    private java.lang.String taskLSID;
    private java.lang.String taskName;
    private java.lang.String userId;

    public JobInfo() {
    }

    public JobInfo(
           java.util.Calendar dateCompleted,
           java.util.Calendar dateSubmitted,
           int jobNumber,
           java.lang.String parameterInfo,
           edu.mit.broad.genepattern.gp.services.ParameterInfo[] parameterInfoArray,
           java.lang.String status,
           int taskID,
           java.lang.String taskLSID,
           java.lang.String taskName,
           java.lang.String userId) {
           this.dateCompleted = dateCompleted;
           this.dateSubmitted = dateSubmitted;
           this.jobNumber = jobNumber;
           this.parameterInfo = parameterInfo;
           this.parameterInfoArray = parameterInfoArray;
           this.status = status;
           this.taskID = taskID;
           this.taskLSID = taskLSID;
           this.taskName = taskName;
           this.userId = userId;
    }


    /**
     * Gets the dateCompleted value for this JobInfo.
     * 
     * @return dateCompleted
     */
    public java.util.Calendar getDateCompleted() {
        return dateCompleted;
    }


    /**
     * Sets the dateCompleted value for this JobInfo.
     * 
     * @param dateCompleted
     */
    public void setDateCompleted(java.util.Calendar dateCompleted) {
        this.dateCompleted = dateCompleted;
    }


    /**
     * Gets the dateSubmitted value for this JobInfo.
     * 
     * @return dateSubmitted
     */
    public java.util.Calendar getDateSubmitted() {
        return dateSubmitted;
    }


    /**
     * Sets the dateSubmitted value for this JobInfo.
     * 
     * @param dateSubmitted
     */
    public void setDateSubmitted(java.util.Calendar dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }


    /**
     * Gets the jobNumber value for this JobInfo.
     * 
     * @return jobNumber
     */
    public int getJobNumber() {
        return jobNumber;
    }


    /**
     * Sets the jobNumber value for this JobInfo.
     * 
     * @param jobNumber
     */
    public void setJobNumber(int jobNumber) {
        this.jobNumber = jobNumber;
    }


    /**
     * Gets the parameterInfo value for this JobInfo.
     * 
     * @return parameterInfo
     */
    public java.lang.String getParameterInfo() {
        return parameterInfo;
    }


    /**
     * Sets the parameterInfo value for this JobInfo.
     * 
     * @param parameterInfo
     */
    public void setParameterInfo(java.lang.String parameterInfo) {
        this.parameterInfo = parameterInfo;
    }


    /**
     * Gets the parameterInfoArray value for this JobInfo.
     * 
     * @return parameterInfoArray
     */
    public edu.mit.broad.genepattern.gp.services.ParameterInfo[] getParameterInfoArray() {
        return parameterInfoArray;
    }


    /**
     * Sets the parameterInfoArray value for this JobInfo.
     * 
     * @param parameterInfoArray
     */
    public void setParameterInfoArray(edu.mit.broad.genepattern.gp.services.ParameterInfo[] parameterInfoArray) {
        this.parameterInfoArray = parameterInfoArray;
    }


    /**
     * Gets the status value for this JobInfo.
     * 
     * @return status
     */
    public java.lang.String getStatus() {
        return status;
    }


    /**
     * Sets the status value for this JobInfo.
     * 
     * @param status
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }


    /**
     * Gets the taskID value for this JobInfo.
     * 
     * @return taskID
     */
    public int getTaskID() {
        return taskID;
    }


    /**
     * Sets the taskID value for this JobInfo.
     * 
     * @param taskID
     */
    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }


    /**
     * Gets the taskLSID value for this JobInfo.
     * 
     * @return taskLSID
     */
    public java.lang.String getTaskLSID() {
        return taskLSID;
    }


    /**
     * Sets the taskLSID value for this JobInfo.
     * 
     * @param taskLSID
     */
    public void setTaskLSID(java.lang.String taskLSID) {
        this.taskLSID = taskLSID;
    }


    /**
     * Gets the taskName value for this JobInfo.
     * 
     * @return taskName
     */
    public java.lang.String getTaskName() {
        return taskName;
    }


    /**
     * Sets the taskName value for this JobInfo.
     * 
     * @param taskName
     */
    public void setTaskName(java.lang.String taskName) {
        this.taskName = taskName;
    }


    /**
     * Gets the userId value for this JobInfo.
     * 
     * @return userId
     */
    public java.lang.String getUserId() {
        return userId;
    }


    /**
     * Sets the userId value for this JobInfo.
     * 
     * @param userId
     */
    public void setUserId(java.lang.String userId) {
        this.userId = userId;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof JobInfo)) return false;
        JobInfo other = (JobInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.dateCompleted==null && other.getDateCompleted()==null) || 
             (this.dateCompleted!=null &&
              this.dateCompleted.equals(other.getDateCompleted()))) &&
            ((this.dateSubmitted==null && other.getDateSubmitted()==null) || 
             (this.dateSubmitted!=null &&
              this.dateSubmitted.equals(other.getDateSubmitted()))) &&
            this.jobNumber == other.getJobNumber() &&
            ((this.parameterInfo==null && other.getParameterInfo()==null) || 
             (this.parameterInfo!=null &&
              this.parameterInfo.equals(other.getParameterInfo()))) &&
            ((this.parameterInfoArray==null && other.getParameterInfoArray()==null) || 
             (this.parameterInfoArray!=null &&
              java.util.Arrays.equals(this.parameterInfoArray, other.getParameterInfoArray()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            this.taskID == other.getTaskID() &&
            ((this.taskLSID==null && other.getTaskLSID()==null) || 
             (this.taskLSID!=null &&
              this.taskLSID.equals(other.getTaskLSID()))) &&
            ((this.taskName==null && other.getTaskName()==null) || 
             (this.taskName!=null &&
              this.taskName.equals(other.getTaskName()))) &&
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
        if (getDateCompleted() != null) {
            _hashCode += getDateCompleted().hashCode();
        }
        if (getDateSubmitted() != null) {
            _hashCode += getDateSubmitted().hashCode();
        }
        _hashCode += getJobNumber();
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
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        _hashCode += getTaskID();
        if (getTaskLSID() != null) {
            _hashCode += getTaskLSID().hashCode();
        }
        if (getTaskName() != null) {
            _hashCode += getTaskName().hashCode();
        }
        if (getUserId() != null) {
            _hashCode += getUserId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(JobInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("Analysis", "JobInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateCompleted");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dateCompleted"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateSubmitted");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dateSubmitted"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("", "jobNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("", "status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("taskID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "taskID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("taskLSID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "taskLSID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("taskName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "taskName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
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
     * @return the deleted
     */
    public java.lang.Boolean getDeleted() {
        return deleted;
    }

    /**
     * @param deleted the deleted to set
     */
    public void setDeleted(java.lang.Boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * @return the elapsedTimeMillis
     */
    public java.lang.Long getElapsedTimeMillis() {
        return elapsedTimeMillis;
    }

    /**
     * @param elapsedTimeMillis the elapsedTimeMillis to set
     */
    public void setElapsedTimeMillis(java.lang.Long elapsedTimeMillis) {
        this.elapsedTimeMillis = elapsedTimeMillis;
    }

}
