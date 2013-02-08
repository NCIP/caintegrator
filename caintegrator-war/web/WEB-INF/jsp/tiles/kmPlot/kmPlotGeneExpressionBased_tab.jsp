<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>

<s:form name="kaplanMeierGeneExpressionInputForm" id="kaplanMeierGeneExpressionInputForm" theme="simple">
    <s:token />
    <s:hidden name="createPlotSelected" value="false" />
    <s:hidden name="resetSelected" value="false" />
    
    <!-- Kaplan-Meier Inputs -->
    <h2>Gene Expression Based Kaplan-Meier Survival Plots</h2>

        <table class="data">
            <tr>
                <td class="value_inline" style="min-width: 5px; width: 10px; padding-left: 10px;">
                1.)
                </td>
                <td class="value_inline" style="min-width: 5px; width: 150px;">
                    Gene Symbol:  <br>
                    <div style="font-size: 75%; color: #666666;"></div>
                </td>
                <td class="value_inline">
                    <s:component template="genetextfield.ftl" theme="cai2simple">
                        <s:param name="createTextField" value="true" />
                        <s:param name="textFieldId" value="%{'kmGeneSymbolsId'}"/>
                        <s:param name="textFieldName" value="%{'kmPlotForm.geneExpressionBasedForm.geneSymbol'}"/>
                    </s:component>
		        </td>
	        </tr>
	        
	        <tr>
                <td class="value_inline" style="min-width: 5px; width: 10px; padding-left: 10px;">
                2.)
                </td>
                <td class="value_inline" style="min-width: 5px; width: 120px;">
                    Select Expression Type:
                </td>
                <td class="value_inline">
                    <s:radio name="kmPlotForm.geneExpressionBasedForm.expressionType"
                    list="@gov.nih.nci.caintegrator.application.analysis.KMGeneExpressionBasedParameters@getExpressionTypeValuesToDisplay()"
                    onclick="selectGeneExpressionType()" disabled="kmPlotForm.geneExpressionBasedForm.disableExpressionTypeSelector"/>                
                </td>
                <td class="value_inline"></td>
                <td class="value_inline"></td>
            </tr>
	        <tr>
                <td class="value_inline" style="min-width: 5px; width: 10px; padding-left: 10px;">
                3.)
                </td>
		        <td class="value_inline">
		          <span id="overValueTextPrefix"><s:property value="%{overValueTextPrefix}" /></span><br>
                </td>
                <td class="value_inline">
                    <s:textfield id="overexpressedNumberTextField" name="kmPlotForm.geneExpressionBasedForm.overexpressedNumber" size="4" theme="simple"/> <span id="overValueTextSuffix"><s:property value="%{valueSuffix}" /></span>
                </td> 
            </tr>
            <tr>
                <td class="value_inline" style="min-width: 5px; width: 10px; padding-left: 10px;">
                4.)
                </td>
                <td class="value_inline">
                    <span id="underValueTextPrefix"><s:property value="%{underValueTextPrefix}" /></span><br>
                </td>
                <td class="value_inline">
                    <s:textfield id="underexpressedNumberTextField" name="kmPlotForm.geneExpressionBasedForm.underexpressedNumber" size="4" theme="simple"/> <span id="underValueTextSuffix"><s:property value="%{valueSuffix}" /></span>
                </td>
            </tr>
            <tr>
                <td class="value_inline" style="min-width: 5px; width: 10px; padding-left: 10px;">
                    5.)
                </td>
                <td class="value_inline" style="min-width: 5px; width: 150px;">
                    Select Survival Value:<br>
                </td>
                <td class="value_inline">
                <s:select name="kmPlotForm.survivalValueDefinitionId" 
                      list="kmPlotForm.survivalValueDefinitions" 
                      listValue="value.name"
                      theme="simple"/>
                </td>
            </tr>
            <s:if test="%{studyHasMultiplePlatforms}">
	            <tr>
	                <td class="value_inline" style="min-width: 5px; width: 10px; padding-left: 10px;">
	                6a.)
	                </td>
	                <td class="value_inline" colspan="4">
	                    Select Platform:  
	                    <s:select name="kmPlotForm.geneExpressionBasedForm.platformName" 
	                        list="platformsInStudy"
	                        headerKey="" headerValue="Select Platform"
	                         onchange="document.kaplanMeierGeneExpressionInputForm.action = 'updateControlSamplesGeneExpressionBasedKMPlot.action';document.kaplanMeierGeneExpressionInputForm.submit();"/>
	                </td>
	            </tr>
            </s:if>
            <tbody id="controlSampleTbody" style="<s:property value='%{controlsDisplayStyle}' />" />
            <tr>
                <td class="value_inline" style="min-width: 5px; width: 10px; padding-left: 10px;">
                6.)
                </td>
                <td class="value_inline" style="min-width: 5px; width: 150px;">
                    Select Control Sample Set:<br>
                </td>
                <td class="value_inline">
                    <s:select name="kmPlotForm.geneExpressionBasedForm.controlSampleSetName"
                        list="controlSampleSets" theme="simple"/>
                </td>
            </tr>
            </tbody>
        </table>
        <br>
        <div>
        <center>
        <button type="button" 
                onclick="document.kaplanMeierGeneExpressionInputForm.resetSelected.value = 'true';
                document.kaplanMeierGeneExpressionInputForm.action = 'resetGeneExpressionBasedKMPlot.action';
                document.kaplanMeierGeneExpressionInputForm.submit();"> Reset 
        </button>
        <s:if test="creatable">
            <button type="button" 
                    onclick="document.kaplanMeierGeneExpressionInputForm.createPlotSelected.value = 'true';
                    dojo.event.topic.publish('createGeneExpressionPlot');"> Create Plot 
            </button>
        </s:if>
        </center>
        </div>
    <!-- /Kaplan-Meier Inputs -->
    
    <s:url id="createGeneExpressionBasedKMPlot" action="createGeneExpressionBasedKMPlot"/>
    
    <br><br>
    <center>
    <sx:div id="geneExpressionKmPlotDiv" 
            href="%{createGeneExpressionBasedKMPlot}" 
            formId="kaplanMeierGeneExpressionInputForm" 
            showLoadingText="true"
            loadingText="<img src='images/ajax-loader-processing.gif'/>"
            listenTopics="createGeneExpressionPlot" refreshOnShow="true" />
        
    </center>
</s:form>