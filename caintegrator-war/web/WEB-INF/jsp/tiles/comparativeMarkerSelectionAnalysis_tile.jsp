<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="content">                      
    
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openWikiHelp('DYDnAg', 'id-6-AnalyzingStudies-AnalyzingDatawithGenePattern')" class="help">
   &nbsp;</a>
    </div>

    <!--/Page Help-->        
    <h1><s:property value="#subTitleText" /></h1>
    
    <div class="box2">   
    <div class="whitebg">   
        <s:actionerror/>
        <s:form id="comparativeMarkerSelectionAnalysisForm" action="comparativeMarkerSelectionAnalysis">
            <s:token />
            <s:hidden name="selectedAction" />
            
            <s:textfield name="currentComparativeMarkerSelectionAnalysisJob.name" label="Job Name" size="50" required="true" />
            <s:select name="currentComparativeMarkerSelectionAnalysisJob.preprocessDataSetUrl"
                list="preprocessDatasetServices" label="Preprocess Server" required="true" cssClass="editable-select"/>
            <s:select name="currentComparativeMarkerSelectionAnalysisJob.comparativeMarkerSelectionUrl"
                list="comparativeMarkerSelectionServices" label="Comparative Server" required="true" cssClass="editable-select"/>
            
            <s:optiontransferselect
                id="allQueries"
                label="Annotation Queries and Lists"
                doubleId="querySelections"
                name="comparativeMarkerSelectionAnalysisForm.unselectedQueryNames"
                list="comparativeMarkerSelectionAnalysisForm.unselectedQueries"
                listValue="value.displayName"
                doubleName="comparativeMarkerSelectionAnalysisForm.selectedQueryNames"
                doubleList="comparativeMarkerSelectionAnalysisForm.selectedQueries"
                doubleListValue="value.displayName"
                allowAddAllToLeft="false"
                allowAddAllToRight="false"
                allowUpDownOnLeft="false"
                allowUpDownOnRight="false"
                leftTitle="Available Queries and Lists"
                rightTitle="Selected Queries and Lists"
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
                title="Must select two annotation queries and/or subject lists, which are used to group the samples into two separate classifications to run against ComparativeMarkerSelection. The queries and lists selected here have been previously saved by the user.  Selected queries and lists will result in the processing of only those samples which are mapped to patients in the saved query or list result."/>
            <s:if test="%{studyHasMultiplePlatforms}">
                <s:select name="comparativeMarkerSelectionAnalysisForm.platformName" 
                            label="Select Platform" 
                            list="platformsInStudy"
                            headerKey="" headerValue="Select Platform" required="true"/>
                <br/>
            </s:if>
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

            <s:select name="comparativeMarkerSelectionParameters.testDirection" label="Test Direction"
                list="comparativeMarkerSelectionParameters.testDirectionOptions" required="true" />
            <s:select name="comparativeMarkerSelectionParameters.testStatistic" label="Test Statistic"
                list="comparativeMarkerSelectionParameters.testStatisticOptions" required="true" />
            <s:textfield name="comparativeMarkerSelectionParameterSet.minStd" label="Min Std" size="50" required="true" />
            <s:textfield name="comparativeMarkerSelectionParameterSet.numberOfPermutations" label="Number Of Permutations" size="50" required="true" />
            <s:checkbox name="comparativeMarkerSelectionParameterSet.complete" label="Complete" labelposition="left" />
            <s:checkbox name="comparativeMarkerSelectionParameterSet.balanced" label="Balanced" labelposition="left" />
            <s:textfield name="comparativeMarkerSelectionParameterSet.randomSeed" label="Random Seed" size="50" required="true" />
            <s:checkbox name="comparativeMarkerSelectionParameterSet.smoothPvalues" label="Smooth Pvalues" labelposition="left" />
            <s:select name="comparativeMarkerSelectionParameters.phenotypeTest" label="Phenotype Test"
                list="comparativeMarkerSelectionParameters.phenotypeTestOptions" required="true" />
            <br>
            
            <tr>
                <td></td>
                <td> 
                <br>
                    <s:submit value="Perform Analysis" align="center"
                        onclick="this.form.selectedAction.value = 'execute'; return true;" theme="simple" />
                    <s:submit value="Cancel" action="cancelComparativeMarkerSelectionAnalysis" theme="simple"/>
                </td>
            </tr>

        </s:form>
    </div>                                                                                                      
    </div>                                                                                                      
            
</div>

<div class="clear"><br /></div>
