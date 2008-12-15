<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->
    
    <div class="pagehelp"><a href="#" class="help"></a></div>
    
    <!--/Page Help-->           
    
    <h1>Annotation Based Kaplan-Meier Surival Plots</h1>
    
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
                    1.) Patient Groups:
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
                    2.) Select Survival Measure:
                </td>
                <td class="value_inline">
	            <s:select name="kaplanMeierFormValues.survivalValueDefinitionId" 
	                  list="survivalValueDefinitions" 
	                  listValue="value.name"/>
	            </td>
            </tr>
        </table>
        <s:if test="creatable">
	        <br>
	        <center>
	        <s:a href="#" cssClass="btn" cssStyle="margin:0 5px;" onclick="document.kaplanMeierInputForm.action = 'createAnnotationBasedKMPlot.action';document.kaplanMeierInputForm.submit();"><span class="btn_img">Create Plot</span></s:a>
	        </center>
        </s:if>
    </s:form>
    <s:set name="kmPlot" value="#session['kmPlot']" />
    <s:if test="#kmPlot != null">
        <br>
        <center>
            <img src="retrieveKMPlot.action"/>
            <br>
            
            <strong>Log-rank P-value for significance of difference in survival between groups</strong>
            <br>
            <s:if test="!#kmPlot.configuration.filteredGroups.empty">
                <s:iterator value="#kmPlot.configuration.filteredGroups">
                    <font color="red">
                    <s:property value="name"/> has 0 patients. 
                    </font>
                    <br> 
                </s:iterator>
            </s:if>
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
</div>

<div class="clear"><br /></div>
