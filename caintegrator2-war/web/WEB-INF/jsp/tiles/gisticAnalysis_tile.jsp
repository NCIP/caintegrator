<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<script type="text/javascript" src="/caintegrator/common/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="/caintegrator/common/js/jquery.editable-select.js"></script>
<script type="text/javascript">
    jQuery.noConflict();

    jQuery(function() {
      jQuery('.editable-select').editableSelect(
        {
          bg_iframe: true,
          onSelect: false,
          case_sensitive: false, // If set to true, the user has to type in an exact
                                 // match for the item to get highlighted
          items_then_scroll: 10 // If there are more than 10 items, display a scrollbar
        }
      );
    });

    // This function is called at body onload because to get editable-select to work it needs to show the div first and then hide it by default.
    function initializeJsp() {
        var div = document.getElementById('gridServiceInputParams');
        div.style.display = "none";
    }
    
    function checkDisplaySelectPlatform(entry) {
        if (document.getElementById("selectedQuery").value == "") {
            document.getElementById('platformSelectorDiv').style.display = 'block'
        } else {   
            document.getElementById('platformSelectorDiv').style.display = 'none'
        }
    }
    
    function selectService(service) {
        if (service == "WEB") {
            document.getElementById('gridServiceInputParams').style.display = 'none';
            document.getElementById('webServiceInputParams').style.display = 'block';
        } else {
            document.getElementById('webServiceInputParams').style.display = 'none';
            document.getElementById('gridServiceInputParams').style.display = 'block';
        } 
    }
    
</script>   

<div id="content">                      
    
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openWikiHelp('DYDnAg', '6-AnalyzingStudies-GISTICSupportedAnalysis')" class="help">
   &nbsp;</a>
    </div>

    <!--/Page Help-->        
    
    <h1><s:property value="#subTitleText" /></h1>
    <div class="box2">   
    <div class="whitebg">   
        <s:actionerror/>
            <s:div name="commentdiv" cssClass="inlinehelp_form_top" cssStyle="margin-left: 0px;height:130px">
                <div class="wwlbl">&nbsp;</div >
                <div class="wwctrl" style="width: 300px; white-space:normal; text-align: left; padding-bottom: 1em;">
                    This form submits a job which analyzes samples using the GenePattern GISTIC module.<br>
                    <br>
                    <s:div cssStyle="text-align:left; width: 550px; margin-left: auto; margin-right: auto;">
                    <span style="font-weight:bold;">Job Name</span> - Please enter a job name.<br>
                    <span style="font-weight:bold;">GISTIC Service Type</span> -  Select whether to use the GISTIC web service or grid service and provide or select the service address. If the web service is selected, authentication information is also required<br>
                    <span style="font-weight:bold;">Annotation Query or List</span> - (Optional) Select a saved Annotation query or list to specify which samples will be processed.<br>
                    <span style="font-weight:bold;">Exclude Sample Control Set</span> - (Optional) Select a Control Sample Set to be excluded from the Annotation Query.<br>
                    </s:div>
                </div>
            </s:div>
        
        <s:form id="gisticAnalysisForm" action="gisticAnalysis" method="post" enctype="multipart/form-data" theme="css_xhtml">
        
           <s:hidden name="selectedAction" />
            
            <s:textfield name="currentGisticAnalysisJob.name" label="Job Name" size="50" required="true" />
            <br />
            <s:div cssStyle="padding: 1em 0 0 0;">
                <s:div cssClass="wwlbl"><label class="label">GISTIC Service Type:&nbsp;</label></s:div>
                <s:div>
                    <s:radio theme="css_xhtml" name="useWebService" list="#{true:'Use GenePattern GISTIC Web Service'}" onclick="selectService('WEB'); " />
                    <s:radio theme="css_xhtml" name="useWebService" list="#{false:'Use GISTIC Grid Service'}" onclick="selectService('GRID'); " />
                </s:div>
            </s:div>
            <br />
            <s:div id="gridServiceInputParams">
            <s:select id="gridServiceUrl" name="gridServiceUrl"
                list="gisticServices" label="GISTIC Grid Server" required="true" disabled="true" cssClass="editable-select"/>
            </s:div>
            <br/>
            <s:div id="webServiceInputParams" cssStyle="%{useWebServiceOn}">
                <s:textfield id="webServiceUrl" name="webServiceUrl" label="GenePattern Web Service URL" size="50" required="true" /> <br />
                <s:textfield id="username" name="gisticParameters.server.username" label="GenePattern Username" size="50" required="true" /> <br />
                <s:password id="password" name="gisticParameters.server.password" label="GenePattern Password" size="50" showPassword="true" />
            </s:div>
            <br />
            <s:div name="commentdiv" cssClass="inlinehelp_form_top" cssStyle="margin: 3em 0 0 0px; height:130px; padding-bottom: 0;">
                <div class="wwlbl">&nbsp;</div >
                <div class="wwctrl" style="width: 300px; white-space:normal; text-align: left; padding-top: 1em; padding-bottom: 1em;">For the
                    Annotation Query / List parameter below, choose either "All Samples" or a annotation query or list.  If "All Samples" is selected, then all samples will be used.  If a annotation query or list is selected, only those samples which map to the subjects in the annotation query/list results will be used.  The annotation queries and lists in this list have been previously saved by the user.  Control samples can be excluded from this processing by selecting a control set name in the Exclude Sample Control Set dropdown.
                </div>
            </s:div>
            <br />
            <s:select id="selectedQuery" name="gisticAnalysisForm.selectedQuery"
                headerKey="" headerValue="All Samples"
                list="gisticAnalysisForm.clinicalQueries" listValue="value.displayName" label="Annotation Queries and Lists"
                onchange="if (%{studyHasMultiplePlatforms}) {checkDisplaySelectPlatform()}" />
            <br />
            <s:if test="%{studyHasMultiplePlatforms}">
                <s:div id="platformSelectorDiv">
                    <s:select name="gisticAnalysisForm.selectedPlatformName" multiple="false"
                        list="platformsInStudy" label="Select Platform"/>
                        <br/>
                </s:div>
            </s:if>
            <s:select name="gisticAnalysisForm.excludeControlSampleSetName"
                headerKey="" headerValue="None"
                list="controlSampleSets" label="Exclude Sample Control Set"
                required="true" theme="css_xhtml"
                title="Samples in this set will be excluded."/>
            <br/>
            <s:textfield name="gisticParameters.amplificationsThreshold" label="Amplifications Threshold" size="50" required="true" />
            <br />
            <s:textfield name="gisticParameters.deletionsThreshold" label="Deletions Threshold" size="50" required="true" />
            <br />
            <s:textfield name="gisticParameters.joinSegmentSize" label="Join Segment Size" size="50" required="true" />
            <br />
            <s:textfield name="gisticParameters.qvThresh" label="QV Thresh" size="50" required="true" />
            <br />
            <s:select name="gisticParameters.removeX" label="Remove X"
                list="gisticParameters.removeXOptions" required="true" />
            <br />
            <s:file name="gisticParameters.cnvSegmentsToIgnoreFile" label="cnv File" />
            <br />
            <br/>
            <s:div cssClass="wwgrp" cssStyle="padding: 1em 0 1em 0; display:inline-block" >
                <s:div cssClass="wwlbl">&nbsp;</s:div>
                <s:div cssClass="wwctrl">
                    <s:submit value="Perform Analysis" align="center" onclick="this.form.selectedAction.value = 'execute'; return true;" theme="simple" />
                    <s:submit value="Cancel" action="cancelGenePatternAnalysis" theme="simple"/>
                </s:div>
            </s:div>
             <br />
        </s:form>
    </div>                                                                                                      
    </div>                                                                                                      
            
</div>

<div class="clear"><br /></div>
