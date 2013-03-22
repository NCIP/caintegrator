<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>

<s:form name="geneExpressionAnnotationInputForm" id="geneExpressionAnnotationInputForm" theme="simple">
    <s:token />            
    <s:hidden name="createPlotSelected" value="false" />
    <s:hidden name="permissibleValuesNeedUpdate" value="false" />
    
    <!-- Gene Expression Inputs -->
    <h2>Annotation Based Gene Expression Plots</h2>
    
    <div class="tableheader" style="white-space: nowrap; position: relative; width: 25em;">
        <div class="tabhelp" style="white-space:nowrap; position: relative; margin-top: 0.4em; margin-right: 0.5em;">
	    <a href="javascript:openWikiHelp('DYDnAg', 'id-6-AnalyzingStudies-GeneExpressionValuePlotforAnnotation')" class="help">
	   &nbsp;</a>
	    </div>
	</div>
    
    <table class="data">
            <tr>
                <td class="value_inline" style="min-width: 5px; width: 10px; padding-left: 10px;">
                1.)
                </td>
                <td class="value_inline" style="min-width: 5px; width: 190px; white-space: nowrap;">
                    Gene Symbol(s) (comma separated list):<br>
                    <div style="font-size: 75%; color: #666666;"></div>
                </td>
                <td class="value_inline" colspan="3">
                
                    <s:component template="genetextfield.ftl" theme="cai2simple">
                        <s:param name="createTextField" value="true" />
                        <s:param name="textFieldId" value="%{'annotationGeneSymbolsId'}"/>
                        <s:param name="textFieldName" value="%{'gePlotForm.annotationBasedForm.geneSymbol'}"/>
                    </s:component>
                </td>
            </tr>

            <tr>
                <td class="value_inline" style="min-width: 5px; width: 10px; padding-left: 10px;">
                2.)
                </td>
                <td class="value_inline" style="min-width: 5px; width: 120px;">
                    Select Reporter Type:
                </td>
                <td class="value_inline">
	                <s:radio name="gePlotForm.annotationBasedForm.reporterType"
	                list="@gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum@getValueToDisplayableMap()"
	                listKey="key" 
	                listValue="value" />                
                </td>
                <td class="value_inline"></td>
                <td class="value_inline"></td>
            </tr>
            <s:if test="%{studyHasMultiplePlatforms}">
            <tr>
                <td class="value_inline" style="min-width: 5px; width: 10px; padding-left: 10px;">
                2a.)
                </td>
                <td class="value_inline" colspan="4">
                    Select Platform:  
                    <s:select name="gePlotForm.annotationBasedForm.platformName" 
                        list="platformsInStudy"
                        headerKey="" headerValue="Select Platform"
                         onchange="showBusyDialog();document.geneExpressionAnnotationInputForm.action = 'updateControlSamplesAnnotationBasedGEPlot.action';document.geneExpressionAnnotationInputForm.submit();"/>
                </td>
            </tr>
            </s:if>

            <tr>
                <td class="value_inline" style="min-width: 5px; width: 10px; padding-left: 10px;" />
                <td class="value_inline" style="min-width: 5px; width: 190px; white-space: nowrap;" />
                <th> Annotation Group</th>
                <th> Annotation </th>
                <th> Values </th>
            </tr>
            <tr>
                <td class="value_inline" style="min-width: 5px; padding-left: 10px; border-top: 0px;">
                    3.)
                </td>    
                <td class="value_inline" style="min-width: 5px; width: 120px; border-top: 0px;">    
                    Sample Groups:
                </td>
                <td class="value_inline">
                    <s:select name="gePlotForm.annotationBasedForm.annotationGroupSelection" 
                              list="currentStudy.annotationGroups"
                              listValue="name" 
                              listKey="name"
                              headerKey="invalidSelection"
                              headerValue="Select Annotation Group"
                              onchange="showBusyDialog();document.geneExpressionAnnotationInputForm.action = 'gePlotUpdateAnnotationDefinitions.action';document.geneExpressionAnnotationInputForm.submit();"
                              theme="simple"/>
                </td>
                <td class="value_inline">
                <s:select name="gePlotForm.annotationBasedForm.selectedAnnotationId" 
                              list="gePlotForm.annotationBasedForm.annotationFieldDescriptors"
                              listValue="value.definition.displayName"
                              headerKey="-1"
                              headerValue="Select Annotation"
                              onchange="showBusyDialog();document.geneExpressionAnnotationInputForm.action = 'gePlotUpdatePermissibleValues.action';document.geneExpressionAnnotationInputForm.permissibleValuesNeedUpdate.value = 'true';document.geneExpressionAnnotationInputForm.submit();"
                              theme="simple"/>
                </td>
                <td class="value_inline">
                <s:select name="gePlotForm.annotationBasedForm.selectedValuesIds" 
                              list="gePlotForm.annotationBasedForm.permissibleValues"
                              multiple="true"
                              theme="simple"/>
                </td>
            </tr>

            <tr>
                <td class="value_inline" style="min-width: 5px; width: 10px; padding-left: 10px;">
                4.)
                </td>
                <td class="value_inline" colspan="4">
                <s:checkbox name="gePlotForm.annotationBasedForm.addPatientsNotInQueriesGroup" theme="simple"/>Add additional group containing all other subjects not found in selected queries.
                </td>
            </tr>
            
            <tr>
                <td class="value_inline" style="min-width: 5px; width: 10px; padding-left: 10px;">
                5.)
                </td>
                <td class="value_inline" colspan="4">
                <s:checkbox name="gePlotForm.annotationBasedForm.addControlSamplesGroup" disabled="%{!hasControlSamples()}"
                    theme="simple"/>Add additional group containing all control samples for this study.
                    <s:select name="gePlotForm.annotationBasedForm.controlSampleSetName" list="controlSampleSets"
                        disabled="%{!hasControlSamples()}"/>
                </td>
            </tr>
            
        </table>
        

        <br>
        <div>
        <center>
        
        <button type="button" 
                onclick="document.geneExpressionAnnotationInputForm.action = 'resetAnnotationBasedGEPlot.action';
                document.geneExpressionAnnotationInputForm.submit();"> Reset 
        </button>
        
        <button type="button" 
                onclick="document.geneExpressionAnnotationInputForm.createPlotSelected.value = 'true';
                dojo.event.topic.publish('createAnnotationPlot');"> Create Plot 
        </button>
        </center>
        </div>

    <!-- /Gene Expression Inputs -->
    <s:url id="createAnnotationBasedGEPlot" action="createAnnotationBasedGEPlot"/>
    
    <br>
    <center>
    <sx:div id="annotationGePlotDiv" 
            href="%{createAnnotationBasedGEPlot}" 
            formId="geneExpressionAnnotationInputForm" 
            showLoadingText="true"
            loadingText="<img src='images/ajax-loader-processing.gif' alt='ajax icon indicating loading process'/>"
            listenTopics="createAnnotationPlot" refreshOnShow="true" />
        
    </center>
    
</s:form>