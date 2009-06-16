<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<s:hidden name="runSearchSelected" value="false" />

<s:if test="showCaBioSearch" >

<!-- caBio Inputs -->
<h2>caBio Gene Search</h2>
    <tr>
        <td>
            <s:textfield label="Keywords" name="geneKeywords" id="geneKeywords"  />
        </td>
        <td> 
        
        <button type="button" 
            onclick="<s:property value='searchOnclick' />"> Search 
            </button>
        </td>
        <td>
            <em>Search caBio for genes based on keyword string.</em>
        </td>
    </tr>

<!-- /caBio Inputs -->
<s:url id="caBioGeneSearch" action="caBioGeneSearch"/>

<br><br>
<s:div id="caBioGeneSearchResultsDiv" 
        theme="ajax" 
        href="%{caBioGeneSearch}" 
        formId="%{formName}" 
        loadingText="<img src='images/ajax-loader.gif'/>"
        listenTopics="searchCaBio" />
</s:if>
        