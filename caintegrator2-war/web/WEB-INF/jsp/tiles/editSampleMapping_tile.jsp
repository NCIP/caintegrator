<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script type="text/javascript">
    
    function saveSampleMapping() {
        if (document.sampleMappingForm.genomicSourceCurrentlyMapped.value == "true" 
            && document.sampleMappingForm.sampleMappingFile.value != null 
            && document.sampleMappingForm.sampleMappingFile.value != "") {
                if (confirm('You are adding a new sample mapping file.  All previous sample mappings will be deleted and re-mapped based on the new file.  Please click OK or Cancel.')) {
                    showBusyDialog();
                    document.sampleMappingForm.submit();                
                }
        } else {
            showBusyDialog();
            document.sampleMappingForm.submit();
        }
    }
    
</script>
            
<div id="content">                      
            
    
    <h1 style="color: #FFFFFF; background: #263D6B; padding: 5px;">editing: <strong><s:property value="studyConfiguration.study.shortTitleText" /></strong></h1>    

    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('edit_sample_mappings_help')" class="help">
   &nbsp;</a>
    </div>

    <!--/Page Help-->  
    <h1><s:property value="#subTitleText" /></h1>
    <p>Upload mapping files and click <strong>Map Samples</strong>.</p>
    <div class="form_wrapper_outer">
 
    <table class="form_wrapper_table">
        <tr>
            <th class="title" style="height: 2.5em;">Data Source</th>
            <th class="alignright">&nbsp;</th>
        </tr>
            <tr>
                <td colspan="2" style="padding: 5px;">    

                <s:actionerror />
                <s:form id="sampleMappingForm" name="sampleMappingForm"
                    action="saveSampleMapping" method="post" enctype="multipart/form-data" >
                    <s:hidden name="studyConfiguration.id" />
                    <s:hidden name="genomicSource.id" />
                    <s:if test="%{genomicSource.mappedSamples.isEmpty}">
                        <s:hidden name="genomicSourceCurrentlyMapped" value="false" />
                    </s:if>
                    <s:else>
                        <s:hidden name="genomicSourceCurrentlyMapped" value="true" />
                    </s:else>
                    
                <s:if test="%{studyConfiguration.hasLoadedClinicalDataSource()}">
                    <s:textfield label="caArray Server Hostname" name="genomicSource.serverProfile.hostname" readonly="true" cssClass="readonly" />
                    <s:textfield label="caArray Server JNDI Port" name="genomicSource.serverProfile.port" readonly="true" cssClass="readonly" />
                    <!-- NOTE - using custom struts theme to turn off autocomplete -->
                    <s:textfield label="caArray Username" name="genomicSource.serverProfile.username" readonly="true" cssClass="readonly" theme="cai2xhtml" />
                    <!--/NOTE -->       
                    <s:textfield label="caArray Experiment Id" name="genomicSource.experimentIdentifier" readonly="true" cssClass="readonly" />
                    <s:file id="sampleMappingFile" name="sampleMappingFile" label="Subject to Sample Mapping File" size="35" />
                    
                    <tr>
                        <td class="tdLabel">
                            <s:if test="genomicSource.useSupplementalFiles">
                                (For using CaArray supplemental file use 6 column format mapping file)
                            </s:if>
                            <s:else>
                                (For parsed CaArray experiment use 2 column format mapping file)
                            </s:else>            
                        </td>
                    </tr>
                    <s:checkbox name="genomicSource.singleDataFile" label="Multiple Samples Per Data File"
                        labelposition="left" /><br>
                    <tr>
                        <td class="tdLabel">
                            (Default is 1 sample per data file)          
                        </td>
                    </tr>
                    <s:textfield label="Control Sample Set Name" name="controlSampleSetName" required="true" theme="cai2xhtml" size="35"/>
                    <s:file name="controlSampleFile" label="Control Samples File" size="35" />
                </s:if>
                    <tr> 
	                    <td></td>
	                    <td>
	                    <button type="button" 
	                            onclick="document.sampleMappingForm.action = 'cancelGenomicSource.action';
	                            document.sampleMappingForm.submit();"> Cancel 
	                    </button>
                    <s:if test="%{studyConfiguration.hasLoadedClinicalDataSource()}">
	                    <button type="button" onclick="saveSampleMapping()"> Map Samples </button>
                    </s:if>
	                    </td> 
	                </tr>
                </s:form>
                </td>
            </tr>
    </table>
    
    <table class="form_wrapper_table">
        <tr>
            <th class="title" style="height: 2.5em;">Control Sample Sets</th>
            <th class="alignright">&nbsp;</th>
        </tr>
        <tr>
            <td colspan="2" style="padding: 5px;"> 
                       
            <table class="data">
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
            </td>
        </tr>
    </table>
                  
    <table class="form_wrapper_table">
            <tr>
                <th class="title" style="height: 2.5em;">Sample Mappings</th>
                <th class="alignright">&nbsp;</th>
            </tr>
            <tr>
                <td colspan="2" style="padding: 5px;"> 
                
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
                   
                    <br><br>
                    
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
            
                </td>
            </tr>
    </table>

    </div>            
</div>

<div class="clear"><br /></div>
