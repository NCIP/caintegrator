<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->
    
    <div class="pagehelp"><span class="help"><a href="#">&nbsp;</a></span></div>
    
    <!--/Page Help-->           
    
    <h1>Edit Sample Mappings</h1>
    <s:form action="saveSampleMapping" method="post" enctype="multipart/form-data">
        <s:hidden name="studyConfiguration.id" />
        <s:hidden name="genomicSource.id" />
        <s:textfield label="caArray Server Hostname" name="genomicSource.serverProfile.hostname" readonly="true" />
        <s:textfield label="caArray server JNDI Port" name="genomicSource.serverProfile.port" readonly="true" />
        <s:textfield label="caArray Username" name="genomicSource.serverProfile.username" readonly="true" />
        <s:password label="caArray Password" name="genomicSource.serverProfile.password" readonly="true" />
        <s:textfield label="caArray Experiment Id" name="genomicSource.experimentIdentifier" readonly="true" />
        <s:file name="sampleMappingFile" label="Subject to Sample Mapping File" />
        <s:submit value="Upload Mapping File" />
        <s:file name="controlSampleFile" label="Control Samples File" />
        <s:submit value="Upload Control Samples File" action="saveControlSamples" />
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
            </tr>
        </s:iterator>
   </table>
    
    <table class="data">
        <tr>
            <th colspan="3">Samples Mapped to Subjects</th>
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
            <td><s:property value="sampleAcquisition.assignment.identifier" /></td>
            </tr>
        </s:iterator>
    </table>
               
    <table class="data">
        <tr>
            <th colspan="2">Control Samples</th>
        </tr>
        <tr>
            <th>Sample ID</th>
            <th>Sample Name</th>
        </tr>
        <s:iterator value="genomicSource.controlSamples" status="status">
            <s:if test="#status.odd == true">
              <tr class="odd">
            </s:if>
            <s:else>
              <tr class="even">
            </s:else>            
            <td><s:property value="id" /></td>
            <td><s:property value="name" /></td>
            </tr>
        </s:iterator>
   </table>
            
</div>

<div class="clear"><br /></div>
