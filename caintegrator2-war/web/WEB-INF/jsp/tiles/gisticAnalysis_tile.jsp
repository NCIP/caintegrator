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
            <s:div name="commentdiv" cssClass="inlinehelp_form_top">

                This form submits a job which analyzes samples using the GenePattern GISTIC module.<br>
                <br>
                <s:div cssStyle="text-align:left; width: 550px; margin-left: auto; margin-right: auto;">
                <span style="font-weight:bold;">Job Name</span> - Please enter a job name.<br>
                <span style="font-weight:bold;">GenePattern Server URL / GISTIC Server</span> -  Select whether to use the GISTIC web service or grid service and provide or select the service address. If the web service is selected, authentication information is also required<br>
                <span style="font-weight:bold;">Clinical Queries</span> - (Optional) Select a saved Clinical query to specify which samples will be processed.<br>
                <span style="font-weight:bold;">Exclude Sample Control Set</span> - (Optional) Select a Control Sample Set to be excluded from the Clinical Query.<br>
            </s:div>

        </s:div>
        
        <s:form id="gisticAnalysisForm" action="gisticAnalysis" method="post" enctype="multipart/form-data" theme="css_xhtml">
        
            <s:hidden name="selectedAction" />
            <s:textfield name="currentGisticAnalysisJob.name" label="Job Name" size="50" required="true" />
            <br />
            <s:radio theme="simple" name="useWebService" list="#{true:'Use GenePattern GISTIC Web Service'}" onclick="webServiceUrl.disabled = false; username.disabled = false; password.disabled = false; gridServiceUrl.disabled = true" />
            <s:textfield id="webServiceUrl" name="webServiceUrl" label="GenePattern Server URL" size="50" required="true" />
            <s:textfield id="username" name="gisticParameters.server.username" label="GenePattern Username" size="50" required="true" />
            <s:password id="password" name="gisticParameters.server.password" label="GenePattern Password" size="50" showPassword="true" />
            <br />
            <s:radio theme="simple" name="useWebService" list="#{false:'Use GISTIC Grid Service'}" onclick="webServiceUrl.disabled = true; webServiceUrl.value = ''; username.disabled = true; username.value = ''; password.disabled = true; password.value = ''; gridServiceUrl.disabled = false" />
            <s:select id="gridServiceUrl" name="gridServiceUrl"
                list="gisticServices" label="GISTIC Server" required="true" disabled="true" />
            <br />
            <s:div name="commentdiv" cssClass="inlinehelp_form_top" cssStyle="margin-left: 0px;height:110px">
                <div class="wwlbl">&nbsp;</div >
                <div class="wwctrl" style="width: 300px; white-space:normal; text-align: left; padding-top: 1em; padding-bottom: 1em;">For the
                    Clinical query parameter below, choose either "All Samples" or a clinical query.  If "All Samples" is selected, then all samples will be used.  If a clinical query is selected, only those samples which map to the subjects in the clinical query results will be used.  The clinical queries in this list have been previously saved by the user.  Control samples can be excluded from this processing by selecting a control set name in the Exclude Sample Control Set dropdown.
                </div>
            </s:div>
            <s:select name="gisticAnalysisForm.selectedQuery"
                headerKey="" headerValue="All Samples"
                list="gisticAnalysisForm.clinicalQueries" label="Clinical query" />
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
            <s:div cssClass="wwgrp">
            <s:submit value="Perform Analysis" align="center"
                onclick="this.form.selectedAction.value = 'execute'; return true;" cssClass="wwgrp"/>
            </s:div>
        </s:form>
    </div>                                                                                                      
    </div>                                                                                                      
            
</div>

<div class="clear"><br /></div>
