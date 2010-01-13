<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('gene_pattern_help')" class="help">
   &nbsp;</a>
    </div>

    <!--/Page Help-->        
    
    <h1><s:property value="#subTitleText" /></h1>
    <div class="box2">   
    <div class="whitebg">   
        <s:actionerror/>
            <s:div name="commentdiv" cssClass="inlinehelp_form_top" cssStyle="margin-left: 0px;height:110px">
                <div class="wwlbl">&nbsp;</div >
                <div class="wwctrl" style="width: 300px; white-space:normal; text-align: left; padding-top: 1em; padding-bottom: 1em;">
                    This form submits a job which analyzes samples using the GenePattern GISTIC module.<br>
                    <br>
                    <s:div cssStyle="text-align:left; width: 550px; margin-left: auto; margin-right: auto;">
                    <span style="font-weight:bold;">Job Name</span> - Please enter a job name.<br>
                    <span style="font-weight:bold;">GISTIC Service Type</span> -  Select whether to use the GISTIC web service or grid service and provide or select the service address. If the web service is selected, authentication information is also required<br>
                    <span style="font-weight:bold;">Clinical Query or List</span> - (Optional) Select a saved Clinical query or list to specify which samples will be processed.<br>
                    <span style="font-weight:bold;">Exclude Sample Control Set</span> - (Optional) Select a Control Sample Set to be excluded from the Clinical Query.<br>
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
                    <s:radio theme="css_xhtml" name="useWebService" list="#{true:'Use GenePattern GISTIC Web Service'}" onclick="document.getElementById('gridServiceInputParams').style.display = 'none'; document.getElementById('webServiceInputParams').style.display = 'block'; webServiceUrl.disabled = false; username.disabled = false; password.disabled = false; gridServiceUrl.disabled = true; " />
                    <s:radio theme="css_xhtml" name="useWebService" list="#{false:'Use GISTIC Grid Service'}" onclick="document.getElementById('webServiceInputParams').style.display = 'none'; document.getElementById('gridServiceInputParams').style.display = 'block'; webServiceUrl.disabled = true; webServiceUrl.value = ''; username.disabled = true; username.value = ''; password.disabled = true; password.value = ''; gridServiceUrl.disabled = false; " />
                </s:div>
            </s:div>
            <br />
            <s:div id="webServiceInputParams" cssStyle="%{useWebServiceOn}">
                <s:textfield id="webServiceUrl" name="webServiceUrl" label="GenePattern Web Service URL" size="50" required="true" /> <br />
                <s:textfield id="username" name="gisticParameters.server.username" label="GenePattern Username" size="50" required="true" /> <br />
                <s:password id="password" name="gisticParameters.server.password" label="GenePattern Password" size="50" showPassword="true" />
            </s:div>
            <br />
            <s:div id="gridServiceInputParams" cssStyle="display: none;">
            <s:select id="gridServiceUrl" name="gridServiceUrl"
                list="gisticServices" label="GISTIC Grid Server" required="true" disabled="true" />
            </s:div>    
            <br />
            <s:div name="commentdiv" cssClass="inlinehelp_form_top" cssStyle="margin: 1em 0 0 0px; height:110px; padding-bottom: 0;">
                <div class="wwlbl">&nbsp;</div >
                <div class="wwctrl" style="width: 300px; white-space:normal; text-align: left; padding-top: 1em; padding-bottom: 1em;">For the
                    Clinical Query / List parameter below, choose either "All Samples" or a clinical query or list.  If "All Samples" is selected, then all samples will be used.  If a clinical query or list is selected, only those samples which map to the subjects in the clinical query/list results will be used.  The clinical queries and lists in this list have been previously saved by the user.  Control samples can be excluded from this processing by selecting a control set name in the Exclude Sample Control Set dropdown.
                </div>
            </s:div>
            <br />
            <s:select name="gisticAnalysisForm.selectedQuery"
                headerKey="" headerValue="All Samples"
                list="gisticAnalysisForm.clinicalQueries" listValue="value.displayName" label="Clinical Queries and Lists" />
            <br />
            <s:select name="gisticAnalysisForm.excludeControlSampleSetName"
                headerKey="" headerValue="None"
                list="controlSampleSets" label="Exclude Sample Control Set"
                required="true" theme="css_xhtml"
                title="Samples in this set will be excluded."/>
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
            
            <s:div cssClass="wwgrp" cssStyle="padding: 1em 0 1em 0;">
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
