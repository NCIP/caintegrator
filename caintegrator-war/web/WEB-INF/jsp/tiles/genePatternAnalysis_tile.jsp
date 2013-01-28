<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openWikiHelp('DYDnAg', '6-AnalyzingStudies-AnalyzingDatawithGenePattern')" class="help">
   &nbsp;</a>
    </div>

    <!--/Page Help-->        
    
    <h1><s:property value="#subTitleText" /></h1>
    
    <s:actionerror />
    
    <div class="box2">   
    <div class="whitebg">   
        <s:form id="genePatternAnalysisForm" action="genePatternAnalysis">
        
            <s:hidden name="selectedAction" />
        
            <s:textfield name="genePatternAnalysisForm.url" label="GenePattern Server URL" size="50" required="true" />
            <s:textfield name="genePatternAnalysisForm.username" label="GenePattern Username" size="50" required="true" />
            <s:password name="genePatternAnalysisForm.password" label="GenePattern Password" size="50" showPassword="true" />
            <tr>
	            <td> </td>
	            <td>
	            <br>
	            <s:submit onclick="this.form.selectedAction.value = 'connect'; return true;" value="Connect" theme="simple"/>
	            <s:submit value="Cancel" action="cancelGenePatternAnalysis" theme="simple"/>
	            </td>
            </tr>
            <s:if test="%{!genePatternAnalysisForm.analysisMethodNames.empty}">
                <s:textfield name="currentGenePatternAnalysisJob.name" label="Job Name" required="true" />
                <s:select label="Analysis Method" 
                    name="analysisMethodName" 
                    list="genePatternAnalysisForm.analysisMethodNames"
                    onchange="this.form.selectedAction.value = 'change'; this.form.submit();" />
                <s:url id="infoUrl" value="%{genePatternAnalysisForm.analysisMethodInformationUrl}" />
                <tr>
                <td></td>
                <td><a href="${infoUrl}" target="_">Analysis Method Documentation</a></td>
                </tr>
            </s:if>
            <s:iterator status="status" value="genePatternAnalysisForm.parameters">
                <s:if test='%{displayType == "textfield"}'>
                    <s:textfield 
                        label="%{name}" 
                        name="genePatternAnalysisForm.parameters[%{#status.index}].value" 
                        required="required" 
                        value="%{value}" />
                </s:if>
                <s:elseif test='%{displayType == "select"}'>
                    <s:select 
                        label="%{name}" 
                        name="genePatternAnalysisForm.parameters[%{#status.index}].value" 
                        list="choices" 
                        required="required"
                        value="%{value}" />
                </s:elseif>
            </s:iterator>
            <s:if test='%{genePatternAnalysisForm.executable}'>
                <s:submit value="Perform Analysis" onclick="this.form.selectedAction.value = 'execute'; return true;" />
            </s:if>
        </s:form>
    </div>                                                                                                      
    </div>                                                                                                      
            
</div>

<div class="clear"><br /></div>
