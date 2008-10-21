<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->
    
    <div class="pagehelp"><a href="#" class="help"></a></div>
    
    <!--/Page Help-->           
    
    <h1>GenePattern Analysis</h1>
    
    <s:actionerror />
    
    <div class="box2">   
    <div class="whitebg">   
        
        <p>Your job has been submitted to GenePattern. You may view the results at <a href='<s:property value="resultUrl"/>' target="_"><s:property value="resultUrl"/></a>.
        
        <p>The job parameters are below</p>
        
        <s:push value="analysisForm">
        <table>
            <tr>
                <th>Method</th>
                <td><s:property value="analysisMethodName"/></td>
            </tr>
            <s:iterator status="status" value="parameters">
                <tr>
                    <th><s:property value="name" /></th>
                    <td><s:property value="value" /></td>
                </tr>
            </s:iterator>
        </table>
        </s:push>
    </div>                                                                                                      
    </div>                                                                                                      
            
</div>

<div class="clear"><br /></div>
