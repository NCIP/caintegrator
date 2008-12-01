<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->
    
    <div class="pagehelp"><a href="#" class="help"></a></div>
    
    <!--/Page Help-->           
    
    <h1>Kaplan-Meier Surival Plots</h1>
    
    <s:actionerror/>

    <s:form name="kaplanMeierInputForm" theme="simple">
        <table class="data">
            <tr>
                <th/>
                <th> Annotation Type</th>
                <th> Annotation </th>
                <th> Values </th>
            </tr>
            <tr>
                <td class="value_inline">
                    Patient Groups:
                </td>
		        <td class="value_inline">
                    <s:select name="kaplanMeierFormValues.annotationTypeSelection" 
                              list="#{'subject':'Subject', 'sample':'Sample', 'imageSeries':'Image Series'}"
                              headerKey="invalidSelection"
                              headerValue="Select Annotation Type"
                              onchange="document.kaplanMeierInputForm.action = 'kmPlotUpdateAnnotationDefinitions.action';document.kaplanMeierInputForm.submit();"/>
		        </td>
		                      <td class="value_inline">
                <s:select name="kaplanMeierFormValues.selectedAnnotationId" 
                              list="annotationDefinitions"
                              listValue="value.displayName"
                              headerKey="-1"
                              headerValue="Select Annotation"
                              onchange="document.kaplanMeierInputForm.action = 'kmPlotUpdatePermissibleValues.action';document.kaplanMeierInputForm.submit();"/>
                </td>
                <td>
                <s:select name="kaplanMeierFormValues.selectedValuesIds" 
                              list="permissibleValues"
                              multiple="true"/>
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
                    Select Survival Measure:
                </td>
                <td class="value_inline">
	            <s:select name="kaplanMeierFormValues.survivalValueDefinitionId" 
	                  list="survivalValueDefinitions" 
	                  listValue="value.name"/>
	            </td>
            </tr>
        </table>
        <br>
        <center>
        <s:a href="#" cssClass="btn" cssStyle="margin:0 5px;" onclick="document.kaplanMeierInputForm.action = 'createKMPlot.action';document.kaplanMeierInputForm.submit();"><span class="btn_img">Create Plot</span></s:a>
        </center>
    </s:form>
</div>

<div class="clear"><br /></div>
