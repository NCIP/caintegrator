<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<h1>Query Results</h1>
<s:form>

         This query result id is: <s:property value="queryResult.getId()"/><br>
         query name: <s:property value="queryResult.getQuery().getName()"/><br>
         query description: <s:property value="queryResult.getQuery().getDescription()"/><br>  
         <br>
    <table class="data">
        <tr>
            <th>struts iterator</th>
            <th>Row Index</th>
            <th>Row Id</th>
            <th>Subject Identifier</th>
            <th>Sample Name</th>
        </tr>
        
         <s:iterator value="queryResult.getRowCollection()" status="status">
            <s:if test="#status.odd == true">
              <tr class="odd">
            </s:if>
            <s:else>
              <tr class="even">
            </s:else>
                <td><s:property value="#status.count" /></td>       
                <td><s:property value="rowIndex" /></td>
                <td><s:property value="id" /></td>
                <td><s:property value="subjectAssignment.identifier" /></td>
                <td><s:property value="sampleAcquisition.sample.name" /></td>
            </tr>
        </s:iterator>       
    </table>
</s:form>

<div class="clear"><br />
</div>
