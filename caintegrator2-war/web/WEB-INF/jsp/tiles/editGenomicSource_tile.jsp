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
        function CheckPlatformVendor(vendor, dataType) {
            if (vendor == "Agilent"
                    || (vendor == "Affymetrix" && dataType == "Expression")) {
                document.getElementById("platformName").disabled = false;
            } else {
                document.getElementById("platformName").disabled = true;
            }
        }
        
        function saveGenomicSource() {
            if (document.genomicSourceForm.genomicSourceId.value != null
                    && document.genomicSourceForm.genomicSourceId.value != "") {
                if ((origHostname != document.getElementById("caArrayHost").value)
                    || (origPort != document.getElementById("caArrayPort").value)
                    || (origUsername != document.getElementById("caArrayUsername").value)
                    || (origPassword != document.getElementById("caArrayPassword").value)
                    || (origExperimentId != document.getElementById("experimentId").value)
                    || (origPlatformVendor != document.getElementById("platformVendor").value)
                    || (origDataType != document.getElementById("dataType").value)
                    || (origPlatformName != document.getElementById("platformName").value)
                    || (origTechnicalReplicatesCentralTendency != document.getElementById("technicalReplicatesCentralTendency").value)
                    || (origIsUseHighVarianceCalculation != document.getElementById("isUseHighVarianceCalculation").checked)
                    || (origHighVarianceCalculationType != document.getElementById("highVarianceCalculationType").value)
                    || (origHighVarianceThreshold != document.getElementById("highVarianceThreshold").value)) {
                    if (confirm("You are about to update the configuration information for this data source.  "
                        + "Doing so will require you to remap your samples. "
                        + "Please click OK to update the data source or click Cancel to go back.")) {
                	    showBusyDialog();
                        document.genomicSourceForm.submit();
                    }
                } else {
                    document.genomicSourceForm.mappingData.value = "false";
                    showBusyDialog();
                    document.genomicSourceForm.submit();          
                }
            } else {
            	showBusyDialog();
                document.genomicSourceForm.submit();
            }
        }

        function checkVarianceInputParams(chk){
            if (chk.checked == 1)
            {
              document.getElementById('varianceInputParams').style.display = 'block';
            }
            else
            {
              document.getElementById('varianceInputParams').style.display = 'none';
              chk.checked = 0;
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
                <s:form id="genomicSourceForm" name="genomicSourceForm" action="saveGenomicSource" theme="css_xhtml">
                    <s:hidden name="studyConfiguration.id" />
                    <s:hidden name="genomicSource.id" id="genomicSourceId"/>
                    <s:hidden name="mappingData" value="true"/>
                    <s:if test="genomicSource.statusDescription != null && genomicSource.statusDescription.length() > 0">
            	        <s:div cssStyle="padding: 1em 0 0 0;">
                            <s:div cssClass="wwlbl"><label class="label">Status Description: </label></s:div>
	                        <s:div>
	                            <s:property value="genomicSource.statusDescription"/>                        
	                        </s:div>
	                    </s:div>
	                    <br />
                    </s:if>
                    <s:textfield label="caArray Web URL" name="genomicSource.serverProfile.url" id="caArrayUrl" />
                    <s:textfield label="caArray Server Hostname" name="genomicSource.serverProfile.hostname" id="caArrayHost" />
                    <s:div cssClass="wwlbl"><label class="label">(Note:  caArray v 2.3 or newer is required) </label></s:div>
                    <br />
                    <s:textfield label="caArray Server JNDI Port" name="genomicSource.serverProfile.port" id="caArrayPort" />
                    <!-- NOTE - using custom struts theme to turn off autocomplete -->
                    <s:textfield label="caArray Username" name="genomicSource.serverProfile.username" id="caArrayUsername" theme="cai2_css_xhtml" />
                    <s:password showPassword="true" label="caArray Password" name="genomicSource.serverProfile.password" id="caArrayPassword" theme="cai2_css_xhtml"/>
                    <!--/NOTE --> 
                    <s:textfield label="caArray Experiment Id" name="genomicSource.experimentIdentifier" id="experimentId" />
                    <s:select id="platformVendor" name="genomicSource.platformVendor" label="Vendor"
                        list="@gov.nih.nci.caintegrator2.application.arraydata.PlatformVendorEnum@getValuesToDisplay()"
                        onchange="document.genomicSourceForm.action = 'refreshGenomicSource.action';
                                document.genomicSourceForm.submit();"/>
                    <s:select id="dataType" name="genomicSource.dataTypeString" label="Data Type"
                        list="dataTypes"
                        onchange="document.genomicSourceForm.action = 'refreshGenomicSource.action';
                                document.genomicSourceForm.submit();"/>
                    <s:select id="platformName" 
                        name="genomicSource.platformName" 
                        label="Platform (needed for Agilent and Affy Expression)"
                        list="filterPlatformNames" 
                        disabled="platformNameDisable"/>
                    <s:select id="technicalReplicatesCentralTendency" name="genomicSource.technicalReplicatesCentralTendencyString" label="Central Tendency for Technical Replicates"
                        list="@gov.nih.nci.caintegrator2.application.study.CentralTendencyTypeEnum@getStringValues()"
                        />
                    <s:checkbox id="isUseHighVarianceCalculation" 
                    name="genomicSource.useHighVarianceCalculation"
                    label="Calculate high variance among replicates: " 
                    title="Denote in the search results if a sample has high variance among replicates"
                    labelposition="left"
                    onclick="checkVarianceInputParams(this);"
                    />
                    <s:div id="varianceInputParams" cssStyle="%{varianceInputCssStyle}">
                    <s:textfield label="Variance Threshold" name="genomicSource.highVarianceThreshold" id="highVarianceThreshold" />
                    <s:select id="highVarianceCalculationType" name="genomicSource.highVarianceCalculationTypeString" label="Variance Calculation Type" 
                        list="@gov.nih.nci.caintegrator2.application.study.HighVarianceCalculationTypeEnum@getStringValues()"/>
                    </s:div>
                    
                    <s:div cssClass="wwgrp">
                        <s:div cssClass="wwlbl"></s:div>
                        <s:div cssClass="wwctrl">
                        <center>
                        <button type="button" 
                                onclick="document.genomicSourceForm.action = 'cancelGenomicSource.action';
                                document.genomicSourceForm.submit();"> Cancel 
                            </button>
                            <button type="button" onclick="saveGenomicSource()"> Save </button>
                            </center>
                         </s:div>
                    </s:div>

                    <script type="text/javascript">
                        var origHostname = document.genomicSourceForm.caArrayHost.value;
                        var origPort = document.genomicSourceForm.caArrayPort.value;
                        var origUsername = document.genomicSourceForm.caArrayUsername.value;
                        var origPassword = document.genomicSourceForm.caArrayPassword.value;
                        var origExperimentId = document.genomicSourceForm.experimentId.value;
                        var origPlatformVendor = document.genomicSourceForm.platformVendor.value;
                        var origDataType = document.genomicSourceForm.dataType.value;
                        var origPlatformName = document.genomicSourceForm.platformName.value;
                        var origTechnicalReplicatesCentralTendency = document.genomicSourceForm.technicalReplicatesCentralTendency.value;
                        var origIsUseHighVarianceCalculation = document.genomicSourceForm.isUseHighVarianceCalculation.checked;
                        var origHighVarianceCalculationType = document.genomicSourceForm.highVarianceCalculationType.value;
                        var origHighVarianceThreshold = document.genomicSourceForm.highVarianceThreshold.value;
                    </script>
                </s:form>
                </td>
            </tr>
    </table>            
    </div>
</div>
<div class="clear"><br /></div>
