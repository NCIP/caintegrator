<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->
    
    <div class="pagehelp"><a href="#" class="help"></a></div>
    
    <!--/Page Help-->           
    
    <h1>GenePattern Analysis</h1>
    
    <s:actionerror />
    
    <div id="analysisParameters" class="box2">   
        <s:form action="genePatternAnalysis" >
        
            <s:textfield name="url" label="GenePattern Server URL" value="http://localhost:28080/gp/services/Analysis" />
            <s:if test="analysisMethods != null">
                <s:select label="Analysis Method" 
                    name="analysisMethodName" 
                    value="analysisMethodName"
                    list="analysisMethods" />
            </s:if>
        </s:form>
    </div>                                                                                                      
            
</div>

<div class="clear"><br /></div>
