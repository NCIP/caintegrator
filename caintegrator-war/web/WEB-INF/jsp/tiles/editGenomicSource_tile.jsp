<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      

    <h1 style="color: #FFFFFF; background: #263D6B; padding: 5px;">editing: <strong><s:property value="studyConfiguration.study.shortTitleText" /></strong></h1>
    
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openWikiHelp('CIDnAg', '2-CreatingaNewStudy-AddingorEditingGenomicData')" class="help">
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
                var confirmationMessage = "You are about to update the configuration information for this data source.  "
                    + "Doing so will require you to remap your samples. "
                    + "Please click OK to update the data source or click Cancel to go back.";
                 if (document.genomicSourceForm.dataType.value == "Copy Number") {
                	 confirmationMessage = "You are about to update the configuration information for this data source.  "
                         + "Doing so will require you to remap your samples. Any copy number analysis jobs associated with samples in this source will also be deleted.  "
                         + "Please click OK to update the data source or click Cancel to go back.";
                 }
                if (confirm(confirmationMessage)) {
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

        // This function is called at body onload because IE 7 puts the footer in the middle of the page
        // sporadically, and this toggles it to go to the proper position.
        function initializeJsp() {
            var tbody = document.getElementById('varianceInputParams');
            tbody.style.display = "none";
            tbody.style.display = "";
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
                    <s:token />
                    <s:hidden name="studyConfiguration.id" />
                    <s:hidden name="genomicSource.id" id="genomicSourceId"/>
                    <s:if test="tempGenomicSource.statusDescription != null && tempGenomicSource.statusDescription.length() > 0">
            	        <s:div cssStyle="padding: 1em 0 0 0;">
                            <s:div cssClass="wwlbl">
                            	<label class="label">Status Description: </label></s:div>
	                        <s:div>
	                            <s:property value="tempGenomicSource.statusDescription"/>
	                        </s:div>
	                    </s:div>
	                    <br />
                    </s:if>
                    <s:textfield label="caArray Web URL" name="tempGenomicSource.serverProfile.url" id="caArrayUrl" />
                    <br/>
                    <s:textfield label="caArray Server Hostname" name="tempGenomicSource.serverProfile.hostname" id="caArrayHost" />
                    <br/>
                    <s:div cssClass="wwlbl label">
                    	(Note:  caArray v 2.3 or newer is required)
                    </s:div>
                    <br />
                    <s:textfield label="caArray Server JNDI Port" name="tempGenomicSource.serverProfile.port" id="caArrayPort" />
                    <br/>
                    <!-- NOTE - using custom struts theme to turn off autocomplete -->
                    <s:textfield label="caArray Username" name="tempGenomicSource.serverProfile.username" id="caArrayUsername" theme="cai2_css_xhtml" />
                    <br/>
                    <s:password showPassword="true" label="caArray Password" name="tempGenomicSource.serverProfile.password" id="caArrayPassword" theme="cai2_css_xhtml"/>
                    <br/>
                    <!--/NOTE --> 
                    <s:textfield label="caArray Experiment Id" name="tempGenomicSource.experimentIdentifier" id="experimentId" />
                    <br/>
                    <s:select id="platformVendor" name="tempGenomicSource.platformVendorString" label="Vendor"
                        list="@gov.nih.nci.caintegrator.application.arraydata.PlatformVendorEnum@getValuesToDisplay()"
                        onchange="document.genomicSourceForm.action = 'refreshGenomicSource.action';
                                document.genomicSourceForm.submit();"/>
                    <br/>
                    <s:select id="dataType" name="tempGenomicSource.dataTypeString" label="Data Type"
                        list="dataTypes"
                        onchange="document.genomicSourceForm.action = 'refreshGenomicSource.action';
                                document.genomicSourceForm.submit();"/>
                    <br/>
                    <s:select id="platformName" 
                        name="tempGenomicSource.platformName" 
                        label="Platform"
                        list="filterPlatformNames"/>
                    <br/>
                    <s:select id="loadingType" 
                        name="tempGenomicSource.loadingTypeString" 
                        label="Array Data Loading Type"
                        list="loadingTypes"/>
                    <br/>
                    <s:select id="technicalReplicatesCentralTendency" name="tempGenomicSource.technicalReplicatesCentralTendencyString" label="Central Tendency for Technical Replicates"
                        list="@gov.nih.nci.caintegrator.application.study.CentralTendencyTypeEnum@getStringValues()"
                        />
                    <br/>    
                    <div class="wwgrp" id="wwgrp_isUseHighVarianceCalculation">
                        <div class="wwlbl" id="wwlbl_isUseHighVarianceCalculation">
                            <label class="checkboxLabel" for="isUseHighVarianceCalculation">Indicate if Technical Replicates have statistical variability:</label>
                            <s:checkbox id="isUseHighVarianceCalculation" 
                                        name="tempGenomicSource.useHighVarianceCalculation"
                                        onclick="checkVarianceInputParams(this);"
                                        theme="simple"
                                        title="Denote in the search results if a sample has high statistical variability among technical replicates"
                                        />
                        <br/> 
                        </div> 
                    </div>
                    <s:div id="varianceInputParams" cssStyle="%{varianceInputCssStyle}">
                    <s:select id="highVarianceCalculationType" name="tempGenomicSource.highVarianceCalculationTypeString" label="Standard Deviation Type" 
                        list="@gov.nih.nci.caintegrator.application.study.HighVarianceCalculationTypeEnum@getStringValues()"/>
                    <br/>
                    <s:textfield label="Standard Deviation Threshold" name="tempGenomicSource.highVarianceThreshold" id="highVarianceThreshold" />
                    <br/>
                    </s:div>
                    <br/>
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
                </s:form>
                </td>
            </tr>
    </table>            
    </div>
</div>
<div class="clear"><br /></div>
