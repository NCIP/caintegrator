<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('edit_sample_mappings_help')" class="help">
   (draft)</a>
    </div>

    <!--/Page Help-->         
    
    <h1><s:property value="#subTitleText" /></h1>
    <s:actionerror />
    <s:form action="saveSampleMapping" method="post" enctype="multipart/form-data" >
        <s:hidden name="studyConfiguration.id" />
        <s:hidden name="genomicSource.id" />
        <s:textfield label="caArray Server Hostname" name="genomicSource.serverProfile.hostname" readonly="true" cssClass="readonly" />
        <s:textfield label="caArray server JNDI Port" name="genomicSource.serverProfile.port" readonly="true" cssClass="readonly" />
        <!-- NOTE - using custom struts theme to turn off autocomplete -->
        <s:textfield label="caArrayUsername" name="genomicSource.serverProfile.username" readonly="true" cssClass="readonly" theme="cai2xhtml" />
        <s:password label="caArrayPassword" name="genomicSource.serverProfile.password" readonly="true" cssClass="readonly" theme="cai2xhtml"/>
        <!--/NOTE -->       
        <s:textfield label="caArray Experiment Id" name="genomicSource.experimentIdentifier" readonly="true" cssClass="readonly" />
        <s:file name="sampleMappingFile" label="Subject to Sample Mapping File" />
        
        <s:textfield label="Control Sample Set Name" name="controlSampleSetName" required="true" theme="cai2xhtml"/>
        <s:file name="controlSampleFile" label="Control Samples File" />
        <s:submit value="Map Samples" />
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

<br><br>
               
    <table class="data">
        <tr>
            <th colspan="2">Control Sample Sets</th>
        </tr>
        <tr>
            <th>Set Name</th><th>Sample Name</th>
        </tr>

        <s:iterator value="genomicSource.controlSampleSetCollection" status="status">
            <tr><td colspan="2" style="font-weight:bold; border-top: 1px solid #aaaaaa; border-bottom: 0px;; border-left: 1px solid #aaaaaa;"><s:property value="name" /></td></tr>

            <s:iterator value="samples" status="status">
                    <tr style="border-top: 0px; border-bottom: 0px;"><td style="border-bottom: 0px; border-left: 1px solid #aaaaaa;">&nbsp;</td>
                    <s:if test="#status.odd == true">
                      <td class="odd">
                    </s:if>
                    <s:else>
                      <td class="even">
                    </s:else> 
                    <s:div title="cai2 ID = %{id}"><s:property value="name" /></s:div></td>
                    </tr>
            </s:iterator>

        </s:iterator>

   </table>
            
</div>

<div class="clear"><br /></div>
