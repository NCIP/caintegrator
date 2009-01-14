<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
                 
    <!--Page Help-->
    
    <div class="pagehelp"><a href="#" class="help"></a></div>
    
    <!--/Page Help-->           
    
    <!-- Kaplan-Meier Inputs -->
    <h1>Annotation Based Kaplan-Meier Survival Plots</h1>
    
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
                              list="#{'subject':'Subject', 'sample':'Sample', 'imageSeries':'Image Series'}"
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
                              onchange="document.kaplanMeierAnnotationInputForm.action = 'kmPlotUpdatePermissibleValues.action';document.kaplanMeierAnnotationInputForm.submit();"
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
        <center>
        <s:a href="#" cssClass="btn" cssStyle="margin:0 5px;" onclick="document.kaplanMeierAnnotationInputForm.action = 'resetAnnotationBasedKMPlot.action';document.kaplanMeierAnnotationInputForm.submit();"><span class="btn_img">Reset</span></s:a>
        <s:if test="creatable">
	        <s:a href="#" cssClass="btn" cssStyle="margin:0 5px;" onclick="document.kaplanMeierAnnotationInputForm.action = 'createAnnotationBasedKMPlot.action';document.kaplanMeierAnnotationInputForm.submit();"><span class="btn_img">Create Plot</span></s:a>
        </s:if>
        </center>
        
    <!-- /Kaplan-Meier Inputs -->
    
     <!-- Kaplan-Meier Graph -->
    <s:set name="kmPlot" value="#session['kmPlot']" />
    <s:if test="#kmPlot.annotationBasedKmPlot != null">
        <br>
        <center>
            <img src="retrieveAnnotationKMPlot.action"/>
            <br>
            
            <strong>Log-rank P-value for significance of difference in survival between groups</strong>
            <br>
            <table cellspacing="10">
            
            <!-- Outter Map -->
            <s:iterator value="allStringPValues">
                <s:set name="group1Name" value="key"/>
                <!-- Innter Map -->
                <s:iterator value="value">
                    <s:set name="group2Name" value="key"/>
                    <tr>
                        <td align="right"><s:property value="#group1Name"/></td>
                        <td> vs. </td>
                        <td align="left"> <s:property value="#group2Name"/> </td>
                        <td> = </td> 
                        <td> <s:property value="value"/> </td>
                    </tr>
                </s:iterator> <!-- End Innter Map -->
            </s:iterator> <!-- End Outter Map -->
            </table>
        </center>
    </s:if>
    <!-- /Kaplan-Meier Graph -->


