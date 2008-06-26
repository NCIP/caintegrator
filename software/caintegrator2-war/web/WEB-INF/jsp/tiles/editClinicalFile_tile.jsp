<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->
    
    <div class="pagehelp"><a href="#" class="help"></a></div>
    
    <!--/Page Help-->           
    
    <h1>Define Fields for Clinical Data</h1>
    <h2>Study Name: <s:property value="study.shortTitleText" /></h2>
    <s:form>
    <table>
        <tr>
            <th>Field Definition</th>
            <th>File Header</th>
            <th colspan="3" />Data from File</th>
        </tr>
        <s:iterator value="clinicalSourceConfiguration.annotationFile.columns" status="status">
        <tr>
            <td></td>
            <td><s:property value="name" /></td>
            <s:iterator value="" >
            </s:iterator>
        </tr>
        </s:iterator>
    </table>
    </s:form>
            
</div>

<div class="clear"><br /></div>
