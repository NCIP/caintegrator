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

                This form submits a job which analyzes samples using the GenePattern Principal Component Analysis module.<br>
                <br>
                <s:div cssStyle="text-align:left; width: 550px; margin-left: auto; margin-right: auto;">
                <span style="font-weight:bold;">Job Name</span> - Please enter a job name.<br>
                <span style="font-weight:bold;">Principal Component Analysis Server</span> -  Select a PCA grid service from the dropdown.<br>
                <span style="font-weight:bold;">Clinical Queries</span> - (Optional) Select a saved Clinical query to specify which samples will be processed.<br>
                <span style="font-weight:bold;">Exclude Sample Control Set</span> - (Optional) Select a Control Sample Set to exclude from the Clinical Query.<br>
                <span style="font-weight:bold;">Enable Preprocess Dataset</span> - (Optional) Check this to display and configure preprocessing parameters.
                </s:div>

        </s:div>
        
        <s:form id="principalComponentAnalysisForm" action="principalComponentAnalysis" theme="css_xhtml">
        
            <s:hidden name="selectedAction" />
            
            <s:textfield name="currentPrincipalComponentAnalysisJob.name" label="Job Name" size="50" required="true" theme="css_xhtml" title="Please enter a name for this analysis job."/> <br>
            <s:select name="currentPrincipalComponentAnalysisJob.pcaUrl"
                list="pcaServices" label="Principal Component Analysis Server"
                required="true" theme="css_xhtml"
                title="Principle Component Analysis Server is a server which hosts the grid-enabled Gene Pattern Principle Component Analysis module.  Select one from the list and caIntegrator2 will use the selected server for this portion of the processing."/>
            <br />
            <s:div name="commentdiv" cssClass="inlinehelp_form_top" cssStyle="margin-left: 0px;height:110px;width:100%">
                <div class="wwlbl">&nbsp;</div >
                <div class="wwctrl" style="width: 300px; white-space:normal; text-align: left; padding-top: 1em; padding-bottom: 1em;">For the
                    Clinical query parameter below, choose either "All Samples" or a clinical query.  If "All Samples" is selected, then all samples will be used.  If a clinical query is selected, only those samples which map to the subjects in the clinical query results will be used.  The clinical queries in this list have been previously saved by the user.  Control samples can be excluded from this processing by selecting a control set name in the Exclude Sample Control Set dropdown.
                </div>
            </s:div>
            <s:select name="principalComponentAnalysisForm.selectedQueryID"
                headerKey="" headerValue="All Samples"
                list="principalComponentAnalysisForm.queries" label="Clinical Queries"
                required="false" theme="css_xhtml"
                title="Clinical Queries enable the user to specify which samples will be processed using PCA.  The queries selected here have been previously saved by the user.  Selected queries will result in the processing of only those samples which are mapped to subjects in the saved query result."/>
            <s:select name="principalComponentAnalysisForm.excludeControlSampleSetName"
                headerKey="" headerValue="None"
                list="controlSampleSets" label="Exclude Sample Control Set"
                required="false" theme="css_xhtml"
                title="Samples in this set will be excluded."/>
            <s:checkbox name="principalComponentAnalysisForm.usePreprocessDataset"
                label="Enable Preprocess Dataset:"
                value="aBoolean"
                fieldValue="true"
                labelposition="left"
                theme="css_xhtml"
                onclick="checkanddisplay(this);"
                tooltip="stuff"
                title="Checking this box will display the Preprocess Dataset options and will enable Preprocessing."/>
            <s:div name="commentdiv" cssClass="inlinehelp_form_element">
                <span class="wwlbl">
                (check to display preprocess parameters)
                </span>
                <span class="wwctrl">
                </span>
            </s:div>
            <br>

            <s:div name="collapsiblediv" cssStyle="display: none;">          
                <s:select name="currentPrincipalComponentAnalysisJob.preprocessDataSetUrl"
                    list="preprocessDatasetServices" label="Preprocess Server" required="true" theme="css_xhtml"/> <br>
                <s:checkbox name="preprocessDatasetParameterSet.filterFlag" label="Filter flag" labelposition="left" theme="css_xhtml"/> <br>
                <s:select name="preprocessDatasetParameters.preprocessingFlag" label="Preprocessing Flag"
                    list="preprocessDatasetParameters.preprocessingFlagOptions" required="true" theme="css_xhtml"/> <br>
                <s:textfield name="preprocessDatasetParameterSet.minChange" label="Min Change" size="50" required="true" theme="css_xhtml"/> <br>
                <s:textfield name="preprocessDatasetParameterSet.minDelta" label="Min Delta" size="50" required="true" theme="css_xhtml"/> <br>
                <s:textfield name="preprocessDatasetParameterSet.threshold" label="Threshold" size="50" required="true" theme="css_xhtml"/> <br>
                <s:textfield name="preprocessDatasetParameterSet.ceiling" label="Ceiling" size="50" required="true" theme="css_xhtml"/> <br>
                <s:textfield name="preprocessDatasetParameterSet.maxSigmaBinning" label="Max Sigma Binning" size="50" required="true" theme="css_xhtml"/> <br>
                <s:textfield name="preprocessDatasetParameterSet.probabilityThreshold" label="Probability Threshold" size="50" required="true" theme="css_xhtml"/> <br>
                <s:textfield name="preprocessDatasetParameterSet.numExclude" label="Num Exclude" size="50" required="true" theme="css_xhtml"/> <br>
                <s:checkbox name="preprocessDatasetParameterSet.logBaseTwo" label="Log Base Two" labelposition="left" theme="css_xhtml"/> <br>
                <s:textfield name="preprocessDatasetParameterSet.numberOfColumnsAboveThreshold" label="Number Of Columns Above Threshold" size="50" required="true" theme="css_xhtml"/> <br>
                <!-- Currently we don't want the user to choose this options, use default rows.  If we do, uncomment this -->
                <!-- 
                <s:select name="pcaParameters.clusterBy" label="Cluster By"
                    list="pcaParameters.clusterByOptions" required="true" />
                -->
            </s:div>           
            <br>
            <br>
            <s:div cssClass="wwgrp">
                <s:submit value="Perform Analysis" align="center"
                    onclick="this.form.selectedAction.value = 'execute'; return true;" theme="simple" />
                    <s:submit value="Cancel" action="cancelGenePatternAnalysis" theme="simple"/>
            </s:div>
        </s:form>
            
    </div>                                                                                                      
    </div>                                                                                                   
            
</div>

<div class="clear"><br /></div>
