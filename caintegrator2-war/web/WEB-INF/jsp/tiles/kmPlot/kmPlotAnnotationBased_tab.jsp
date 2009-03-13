<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<s:form name="kaplanMeierAnnotationInputForm" id="kaplanMeierAnnotationInputForm" theme="simple">
                
    <s:hidden name="createPlotSelected" value="false" />
    <s:hidden name="permissibleValuesNeedUpdate" value="false" />
       
    <!-- Kaplan-Meier Inputs -->
    <h2>Annotation Based Kaplan-Meier Survival Plots</h2>
    
        <table class="data">
            <tr>
                <th/>
                <th> Annotation Type</th>
                <th> Annotation </th>
                <th> Values </th>
            </tr>
            <tr>
                <td class="value_inline">
                    1.) Patient Groups:
                </td>
                <td class="value_inline">
                    <s:select name="kmPlotForm.annotationBasedForm.annotationTypeSelection" 
                              list="#{'subject':'Subject', 'imageSeries':'Image Series'}"
                              headerKey="invalidSelection"
                              headerValue="Select Annotation Type"
                              onchange="document.kaplanMeierAnnotationInputForm.action = 'kmPlotUpdateAnnotationDefinitions.action';document.kaplanMeierAnnotationInputForm.submit();"
                              theme="simple"/>
                </td>
                <td class="value_inline">
                <s:select name="kmPlotForm.annotationBasedForm.selectedAnnotationId" 
                              list="kmPlotForm.annotationBasedForm.annotationDefinitions"
                              listValue="value.displayName"
                              headerKey="-1"
                              headerValue="Select Annotation"
                              onchange="document.kaplanMeierAnnotationInputForm.action = 'kmPlotUpdatePermissibleValues.action';document.kaplanMeierAnnotationInputForm.permissibleValuesNeedUpdate.value = 'true';document.kaplanMeierAnnotationInputForm.submit();"
                              theme="simple"/>
                </td>
                <td>
                <s:select name="kmPlotForm.annotationBasedForm.selectedValuesIds" 
                              list="kmPlotForm.annotationBasedForm.permissibleValues"
                              multiple="true"
                              theme="simple"/>
                </td>
            </tr>
        
        </table>
        <table class="data">
            <tr>
                <th/>
                <th> Survival Value </th>
            </tr>
            <tr>
                <td class="value_inline">
                    2.) Select Survival Measure:
                </td>
                <td class="value_inline">
                <s:select name="kmPlotForm.survivalValueDefinitionId" 
                      list="kmPlotForm.survivalValueDefinitions" 
                      listValue="value.name"
                      theme="simple"/>
                </td>
            </tr>
        </table>

        <br>
        <div>
        <center>
        
        <button type="button" 
                onclick="document.kaplanMeierAnnotationInputForm.action = 'resetAnnotationBasedKMPlot.action';
                document.kaplanMeierAnnotationInputForm.submit();"> Reset 
        </button>
        
        <s:if test="creatable">
            <button type="button" 
                    onclick="document.kaplanMeierAnnotationInputForm.createPlotSelected.value = 'true';
                    dojo.event.topic.publish('createAnnotationPlot');"> Create Plot 
            </button>
        </s:if>
        </center>
        </div>
    <!-- /Kaplan-Meier Inputs -->
    <s:url id="createAnnotationBasedKMPlot" action="createAnnotationBasedKMPlot"/>
    
    <br><br>
    <center>
    <s:div id="annotationKmPlotDiv" 
            theme="ajax" 
            href="%{createAnnotationBasedKMPlot}" 
            formId="kaplanMeierAnnotationInputForm" 
            loadingText="<img src='images/ajax-loader-processing.gif'/>"
            listenTopics="createAnnotationPlot" refreshOnShow="true" />
        
    </center>
    
</s:form>