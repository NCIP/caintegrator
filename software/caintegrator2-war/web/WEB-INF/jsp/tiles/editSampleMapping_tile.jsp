<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->
    
    <div class="pagehelp"><a href="#" class="help"></a></div>
    
    <!--/Page Help-->           
    
    <h1>Edit Sample Mappings</h1>
    <s:form action="saveSampleMapping" method="post" enctype="multipart/form-data">
        <s:hidden name="studyConfiguration.id" />
        <s:hidden name="genomicSource.id" />
        <s:textfield label="caArray Server Hostname" name="genomicSource.serverProfile.hostname" readonly="true" />
        <s:textfield label="caArray server JNDI Port" name="genomicSource.serverProfile.port" readonly="true" />
        <s:textfield label="caArray Username" name="genomicSource.serverProfile.username" readonly="true" />
        <s:textfield label="caArray Password" name="genomicSource.serverProfile.password" readonly="true" />
        <s:textfield label="caArray Experiment Id" name="genomicSource.experimentIdentifier" readonly="true" />
        <s:file name="clinicalFile" label="File" />
        <s:submit value="Upload Mapping File" />
    </s:form>
    
    <table class="data">
        <tr>
            <th colspan="2">Unmapped Samples</th>
        </tr>
        <tr>
            <th>Sample ID</th>
            <th>Sample Name</th>
        </tr>
        <s:iterator value="genomicSource.unmappedSamples" status="status">
            <s:if test="#status.odd == true">
              <tr class="odd">
            </s:if>
            <s:else>
              <tr class="even">
            </s:else>            
            <td><s:property value="id" /></td>
            <td><s:property value="name" /></td>
        </s:iterator>
    
    <table class="data">
        <tr>
            <th colspan="3">Mapped Samples</th>
        </tr>
        <tr>
            <th>Sample ID</th>
            <th>Sample Name</th>
            <th>Subject Identifier</th>
        </tr>
        <s:iterator value="genomicSource.mappedSamples" status="status">
            <s:if test="#status.odd == true">
              <tr class="odd">
            </s:if>
            <s:else>
              <tr class="even">
            </s:else>            
            <td><s:property value="id" /></td>
            <td><s:property value="name" /></td>
        </s:iterator>
    </table>
            
</div>

<div class="clear"><br /></div>
