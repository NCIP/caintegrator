<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      

    <h1 style="color: #FFFFFF; background: #263D6B; padding: 5px;">editing: <strong><s:property value="studyConfiguration.study.shortTitleText" /></strong></h1>
    
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('edit_genomic_data_help')" class="help">
   &nbsp;</a>
    </div>

    <!--/Page Help-->
    
    <script type="text/javascript">
        function CheckPlatformVendor(vendor) {
            if (vendor != "Agilent") {
                document.getElementById("platformName").disabled = true;
            } else {
                document.getElementById("platformName").disabled = false;
            }
        }
        
        function showConfirmMessage() {
            if (document.genomicSourceForm.genomicSourceId.value != null && document.genomicSourceForm.genomicSourceId.value != "") {
                if (confirm('You are about to update the configuration information for this data source.  Doing so will require you to remap your samples. Please click OK to update the data source or click Cancel to go back.')) {
                    document.genomicSourceForm.submit();                
                }
            } else {
                document.genomicSourceForm.submit();
            }
        }
    </script>
    
    <h1><s:property value="#subTitleText" /></h1>
    
    <p>Enter data source parameters and click <strong>Save</strong>.</p>
    <div class="form_wrapper_outer">
 
    <table class="form_wrapper_table">
            <tr>
                <th class="title" style="height: 2.5em;">Data Source</th>
                <th class="alignright">&nbsp;</th>
            </tr>
            <tr>
                <td colspan="2" style="padding: 5px;">    
    
    
                <s:actionerror/>
                <s:form id="genomicSourceForm" name="genomicSourceForm" action="saveGenomicSource">
                    <s:hidden name="studyConfiguration.id" />
                    <s:hidden name="genomicSource.id" id="genomicSourceId"/>
                    <s:if test="genomicSource.statusDescription != null && genomicSource.statusDescription.length() > 0">
            	        <tr>
            	            <td class="tdLabel" align="right">
            	                <label class="label">Status Description:</label>
            	            </td>
            	            <td>
            	                <s:property value="genomicSource.statusDescription"/>
            	            </td>
            	        </tr>
                    </s:if>
                    <s:textfield label="caArray Server Hostname" name="genomicSource.serverProfile.hostname" />
                    <s:textfield label="caArray Server JNDI Port" name="genomicSource.serverProfile.port" />
                    <!-- NOTE - using custom struts theme to turn off autocomplete -->
                    <s:textfield label="caArray Username" name="genomicSource.serverProfile.username" theme="cai2xhtml" />
                    <s:password label="caArray Password" name="genomicSource.serverProfile.password" theme="cai2xhtml"/>
                    <!--/NOTE --> 
                    <s:textfield label="caArray Experiment Id" name="genomicSource.experimentIdentifier" />
                    <s:select id="platformVendor" name="genomicSource.platformVendor" label="Vendor"
                        list="@gov.nih.nci.caintegrator2.application.arraydata.PlatformVendorEnum@getValuesToDisplay()"
                        onchange="CheckPlatformVendor(this.form.platformVendor.value);"/>
                    <s:select id="dataType" name="genomicSource.dataTypeString" label="Data Type"
                        list="@gov.nih.nci.caintegrator2.application.study.GenomicDataSourceDataTypeEnum@getStringValues()"/>
                    <s:select id="platformName" name="genomicSource.platformName" label="Platform (only needed for Agilent)"
                        list="agilentPlatformNames" disabled="platformNameDisable"/>
                    
                    <tr> 
                    <td></td>
                    <td>
                    <button type="button" 
                            onclick="document.genomicSourceForm.action = 'cancelGenomicSource.action';
                            document.genomicSourceForm.submit();"> Cancel 
                    </button>
                    <button type="button" onclick="showConfirmMessage()"> Save </button>
                    </td> 
                    </tr>
                </s:form>
                </td>
            </tr>
    </table>            
    </div>
</div>
<div class="clear"><br /></div>
