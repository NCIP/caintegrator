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
        <s:form id="principalComponentAnalysisForm" action="principalComponentAnalysis">
        
            <s:hidden name="selectedAction" />
            
            <s:textfield name="currentPrincipalComponentAnalysisJob.name" label="Job Name" size="50" required="true" />
            <s:select name="currentPrincipalComponentAnalysisJob.preprocessDataSetUrl"
                list="preprocessDatasetServices" label="Preprocess Server" required="true" />
            <s:select name="currentPrincipalComponentAnalysisJob.pcaUrl"
                list="pcaServices" label="Principal Component Analysis Server" required="true" />
            
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
                required="true" />
                
            <s:checkbox name="principalComponentAnalysisForm.usePreprocessDataset" label="Enable Preprocess Dataset" labelposition="left" />
            <s:checkbox name="preprocessDatasetParameterSet.filterFlag" label="Filter flag" labelposition="left" />
            <s:select name="preprocessDatasetParameters.preprocessingFlag" label="Preprocessing Flag"
                list="preprocessDatasetParameters.preprocessingFlagOptions" required="true" />
            <s:textfield name="preprocessDatasetParameterSet.minChange" label="Min Change" size="50" required="true" />
            <s:textfield name="preprocessDatasetParameterSet.minDelta" label="Min Delta" size="50" required="true" />
            <s:textfield name="preprocessDatasetParameterSet.threshold" label="Threshold" size="50" required="true" />
            <s:textfield name="preprocessDatasetParameterSet.ceiling" label="Ceiling" size="50" required="true" />
            <s:textfield name="preprocessDatasetParameterSet.maxSigmaBinning" label="Max Sigma Binning" size="50" required="true" />
            <s:textfield name="preprocessDatasetParameterSet.probabilityThreshold" label="Probability Threshold" size="50" required="true" />
            <s:textfield name="preprocessDatasetParameterSet.numExclude" label="Num Exclude" size="50" required="true" />
            <s:checkbox name="preprocessDatasetParameterSet.logBaseTwo" label="Log Base Two" labelposition="left" />
            <s:textfield name="preprocessDatasetParameterSet.numberOfColumnsAboveThreshold" label="Number Of Columns Above Threshold" size="50" required="true" />
            <s:select name="pcaParameters.clusterBy" label="Cluster By"
                list="pcaParameters.clusterByOptions" required="true" />
            <br>
            <s:submit value="Perform Analysis" align="center"
                onclick="this.form.selectedAction.value = 'execute'; return true;" />

        </s:form>
    </div>                                                                                                      
    </div>                                                                                                      
            
</div>

<div class="clear"><br /></div>
