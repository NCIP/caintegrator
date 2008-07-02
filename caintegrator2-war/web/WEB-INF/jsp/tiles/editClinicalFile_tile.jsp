<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->
    
    <div class="pagehelp"><a href="#" class="help"></a></div>
    
    <!--/Page Help-->           
    
    <h1>Define Fields for Clinical Data</h1>
    <h2>Study Name: <s:property value="study.shortTitleText" /></h2>
    <s:form>
    <table class="data">
        <tr>
            <th>Field Definition</th>
            <th>File Header</th>
            <th colspan="3" />Data from File</th>
        </tr>
        <s:iterator value="clinicalSourceConfiguration.annotationFile.columns" status="status">
            <s:if test="#status.odd == true">
              <tr class="odd">
            </s:if>
            <s:else>
              <tr class="even">
            </s:else>            
                <td></td>
                <td><s:property value="name" /></td>
                <td><s:if test="%{dataValues.size > 0}"><s:property value="dataValues[0]" /></s:if></td>
                <td><s:if test="%{dataValues.size > 1}"><s:property value="dataValues[1]" /></s:if></td>
                <td><s:if test="%{dataValues.size > 2}"><s:property value="dataValues[2]" /></s:if></td>
            </tr>
        </s:iterator>
    </table>
    </s:form>
            
</div>

<div class="clear"><br /></div>
