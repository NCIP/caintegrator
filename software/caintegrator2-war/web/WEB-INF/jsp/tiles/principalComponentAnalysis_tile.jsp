<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('gene_pattern_help')" class="help">
   (draft)</a>
    </div>

    <!--/Page Help-->        
    
    <h1>Principal Component Analysis</h1>
    
    <div class="box2">   
    <div class="whitebg">   
        <s:actionerror/>
        
        <s:div name="commentdiv" cssClass="inlinehelp_form_top">

                This form submits a job which analyzes samples using the GenePattern Principal Component Analysis module.<br>
                <br>
                <s:div cssStyle="text-align:left; width: 500px; margin-left: auto; margin-right: auto;">
                <span style="font-weight:bold;">Job Name</span> - Please enter a job name.<br>
                <span style="font-weight:bold;">Principal Component Analysis Server</span> -  Select a PCA grid service from the dropdown.<br>
                <span style="font-weight:bold;">Clinical Queries</span> - Select one of your saved Clinical queries to specify which samples will be processed.<br>
                <span style="font-weight:bold;">Enable Preprocess Dataset</span> - (Optional)  Check this to display and configure preprocessing parameters.
                </s:div>

        </s:div>
        
        <s:form id="principalComponentAnalysisForm" action="principalComponentAnalysis" theme="css_xhtml">
        
            <s:hidden name="selectedAction" />
            
            <s:textfield name="currentPrincipalComponentAnalysisJob.name" label="Job Name" size="50" required="true" theme="css_xhtml" title="Please enter a name for this analysis job."/> <br>
            <s:select name="currentPrincipalComponentAnalysisJob.pcaUrl"
                list="pcaServices" label="Principal Component Analysis Server" required="true" theme="css_xhtml" title="Principle Component Analysis Server is a server which hosts the grid-enabled Gene Pattern Principle Component Analysis module.  Select one from the list and caIntegrator2 will use the selected server for this portion of the processing."/> <br>
            
            <s:optiontransferselect
                id="allQueries"
                label="Clincal Queries"
                doubleId="querySelections"
                name="principalComponentAnalysisForm.unselectedQueryIDs"
                list="principalComponentAnalysisForm.unselectedQueries"
                listValue="value.name"
                doubleName="principalComponentAnalysisForm.selectedQueryIDs"
                doubleList="principalComponentAnalysisForm.selectedQueries"
                doubleListValue="value.name"
                allowAddAllToLeft="false"
                allowAddAllToRight="false"
                allowUpDownOnLeft="false"
                allowUpDownOnRight="false"
                leftTitle="All Available Queries"
                rightTitle="Selected Queries"
                addToRightLabel=" Add >"
                addToLeftLabel=" < Remove "
                allowSelectAll="false"
                size="8"
                doubleSize="8"
                multiple="true"
                doubleMultiple="true"
                cssStyle="min-width:200px; vertical-align=middle; font-weight:bold; color: #475B82; background-color: #E9E9E9;"
                doubleCssStyle="min-width:200px; vertical-align=middle; font-weight:bold; color: #475B82; background-color: #E9E9E9;"
                buttonCssStyle="min-width:100px; vertical-align=middle;"
                required="true"
                title="Clinical Queries enable the user to specify which samples will be processed using PCA.  The queries selected here have been previously saved by the user.  Selected queries will result in the processing of only those samples which are mapped to subjects in the saved query result.  If multiple queries are selected, all of the sample from each saved query are processed PLUS the results set will be classified according to those queries.  (One class per selected query.)"
                theme="css_xhtml" />
                
             <br>    
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
    
            
            <s:submit value="Perform Analysis" align="center"
                onclick="this.form.selectedAction.value = 'execute'; return true;" theme="css_xhtml" />

        </s:form>
            
    </div>                                                                                                      
    </div>                                                                                                   
            
</div>

<div class="clear"><br /></div>
