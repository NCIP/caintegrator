<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('gene_pattern_help')" class="help">
   (draft)</a>
    </div>

    <!--/Page Help-->        
    
    <h1>GenePattern Analysis</h1>
    
    <s:actionerror />
    
    <div class="box2">   
    <div class="whitebg">   
        <s:form id="genePatternAnalysisForm" action="genePatternAnalysis">
        
            <s:hidden name="selectedAction" />
        
            <s:textfield name="analysisForm.url" label="GenePattern Server URL" size="50" required="true" />
            <s:textfield name="analysisForm.username" label="GenePattern Username" size="50" required="true" />
            <s:password name="analysisForm.password" label="GenePattern Password" size="50" showPassword="true" />
            <s:submit onclick="this.form.selectedAction.value = 'connect'; return true;" value="Connect" />
            <s:if test="%{!analysisForm.analysisMethodNames.empty}">
                <s:select label="Analysis Method" 
                    name="analysisMethodName" 
                    list="analysisForm.analysisMethodNames"
                    onchange="this.form.selectedAction.value = 'change'; this.form.submit();" />
                <s:url id="infoUrl" value="%{analysisForm.analysisMethodInformationUrl}" />
                <tr>
                <td></td>
                <td><a href="${infoUrl}" target="_">Analysis Method Documentation</a></td>
                </tr>
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
                <s:submit value="Perform Analysis" onclick="this.form.selectedAction.value = 'execute'; return true;" />
            </s:if>
        </s:form>
    </div>                                                                                                      
    </div>                                                                                                      
            
</div>

<div class="clear"><br /></div>
