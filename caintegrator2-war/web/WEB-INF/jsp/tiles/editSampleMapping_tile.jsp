<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cai2" uri="/WEB-INF/taglib/cai2taglib.tld"%>
            
<div id="content">                      
    
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('edit_sample_mappings_help')" class="help">
   (draft)</a>
    </div>

    <!--/Page Help-->         
    
    <h1>Edit Sample Mappings</h1>
    <s:form action="saveSampleMapping" method="post" enctype="multipart/form-data" >
        <s:hidden name="studyConfiguration.id" />
        <s:hidden name="genomicSource.id" />
        <s:textfield label="caArray Server Hostname" name="genomicSource.serverProfile.hostname" readonly="true" cssClass="readonly" />
        <s:textfield label="caArray server JNDI Port" name="genomicSource.serverProfile.port" readonly="true" cssClass="readonly" />
        
        <!-- Begin autocomplete workaround -->
        <!-- The following table rows are being formatted outside of struts to allow -->
        <!-- for inclusion of the autocomplete attribute, which is not supported by  -->
        <!-- the struts tag library.                                                 -->
        <tr>
            <td class="tdLabel"><label class="label" for="saveSampleMapping_genomicSource_serverProfile_username">caArrayusername:</label></td>
            <td>
            <input label="caArrayUsername" name="genomicSource.serverProfile.username" readonly="true" class="readonly" autocomplete="off" />
            </td>
        </tr>
        
        <tr>
            <td class="tdLabel"><label class="label" for="saveSampleMapping_genomicSource_serverProfile_password">caArraypassword:</label></td>
            <td>        
            <input type="password" class="readonly" id="saveSampleMapping_genomicSource_serverProfile_password" readonly="readonly" name="genomicSource.serverProfile.password" autocomplete="off" />
            </td>
        </tr>
        <!-- End autocomplete workaround -->
        
        <s:textfield label="caArray Experiment Id" name="genomicSource.experimentIdentifier" readonly="true" cssClass="readonly" />
        <s:file name="sampleMappingFile" label="Subject to Sample Mapping File" />
        <s:submit value="Upload Mapping File" />
        <s:file name="controlSampleFile" label="Control Samples File" />
        <s:submit value="Upload Control Samples File" action="saveControlSamples" />
    </s:form>
    
    <table class="data">
        <tr>
            <th>Unmapped Samples</th>
        </tr>
        <tr>
            <th>Sample Name</th>
        </tr>
        <s:iterator value="genomicSource.unmappedSamples" status="status">
            <s:if test="#status.odd == true">
              <tr class="odd">
            </s:if>
            <s:else>
              <tr class="even">
            </s:else>            
            <td><s:div title="cai2 ID = %{id}"><s:property value="name" /></s:div></td>
            </tr>
        </s:iterator>
   </table>
    
    <table class="data">
        <tr>
            <th colspan="2">Samples Mapped to Subjects</th>
        </tr>
        <tr>
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
            <td><s:div title="cai2 ID = %{id}"><s:property value="name" /></s:div></td>
            <td><s:property value="sampleAcquisition.assignment.identifier" /></td>
            </tr>
        </s:iterator>
    </table>
               
    <table class="data">
        <tr>
            <th colspan="2">Control Samples</th>
        </tr>
        <tr>
            <th>Sample Name</th>
        </tr>
        <s:iterator value="genomicSource.controlSamples" status="status">
            <s:if test="#status.odd == true">
              <tr class="odd">
            </s:if>
            <s:else>
              <tr class="even">
            </s:else>            
            <td><s:div title="cai2 ID = %{id}"><s:property value="name" /></s:div></td>
            </tr>
        </s:iterator>
   </table>
            
</div>

<div class="clear"><br /></div>
