<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<h1>Query Criteria Definition</h1>
<s:form>

         This query result id is: <s:property value="queryResult.getId()"/><br>
         query name: <s:property value="queryResult.getQuery().getName()"/><br>
         query description: <s:property value="queryResult.getQuery().getDescription()"/><br>  
         <br>
</s:form>