<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->
    
    <div class="pagehelp"><a href="#" class="help"></a></div>
    
    <!--/Page Help-->           
    
    <h1>GenePattern Analysis</h1>
    
    <s:actionerror />
    
    <div id="analysisParameters" class="box2">   
        <s:form id="genePatternAnalysisForm" action="genePatternAnalysis">
        
            <s:textfield name="url" label="GenePattern Server URL" value="http://localhost:28080/gp/services/Analysis" />
            <s:if test="%{!analysisForm.analysisMethodNames.empty}">
                <s:select label="Analysis Method" 
                    name="analysisForm.analysisMethodName" 
                    list="analysisForm.analysisMethodNames"
                    onchange="genePatternAnalysisForm.submit();" />
            </s:if>
            <s:iterator status="status" value="analysisForm.parameters">
                <s:if test='%{displayType == "textfield"}'>
                    <s:textfield 
                        label="%{name}" 
                        name="analysisForm.parameters[%{#status.index}].value" 
                        required="required" 
                        value="%{value}" />
                </s:if>
                <s:elseif test='%{displayType == "select"}'>
                    <s:select 
                        label="%{name}" 
                        name="analysisForm.parameters[%{#status.index}].value" 
                        list="choices" 
                        value="%{value}" />
                </s:elseif>
            </s:iterator>
            <s:if test='%{analysisForm.executable}'>
                <s:submit value="Perform Analysis" action="executeGenePatternAnalysis" />
            </s:if>
        </s:form>
    </div>                                                                                                      
            
</div>

<div class="clear"><br /></div>
