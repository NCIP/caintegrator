<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!--Search Criteria-->
    
<div id="criteria" class="box2">

    <h2>Define Search Criteria</h2>
    <ul id="sectabs" class="tabs">
        <li><a href="#basic" class="active">Basic Criteria Definition</a></li>
        <li><a href="#advanced">Advanced Criteria Definition</a></li>
    </ul>
    
    <div class="clear"></div>
    <div class="whitebg">
    
            <!--Basic Criteria Definition-->
            <div id="basic">
    
            <s:form>
                     query name: <s:property value="queryResult.getQuery().getName()"/><br>
                     query description: <s:property value="queryResult.getQuery().getDescription()"/><br>  
                     <br>
            </s:form>
            
            </div>
            <!--/Basic Criteria Definition-->
            
    </div>
    
</div>

<!--/Search Criteria-->
    