<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<s:hidden name="runSearchSelected" value="false" />

<!-- caBio Inputs -->
<s:div id="TB_ajaxWindowTitle"/>
<s:div id="TB_closeAjaxWindow">
        <s:a href="" id="TB_closeWindowButton" title="Click to close." onclick="hideCaBioInputForm()">Close</s:a>
</s:div>
<h2>caBio Gene Search</h2>

    <tr>

        <s:textfield label="Keywords" name="geneKeywords" id="geneKeywords" />

        <td style="border:0px"> 
        <button type="button" 
            onclick="document.caBioGeneSearchForm.runSearchSelected.value = 'true';dojo.event.topic.publish('searchCaBio')"> Search 
            </button>
        </td>
        <td style="border:0px">
            <em>Search caBio for genes based on keyword string.</em>
        </td>
    </tr>

<!-- /caBio Inputs -->
<s:url id="caBioGeneSearch" action="caBioGeneSearch"/>

<br><br>
<s:div id="caBioGeneSearchResultsDiv" 
        theme="ajax" 
        href="%{caBioGeneSearch}" 
        formId="caBioGeneSearchForm" 
        loadingText="<img src='images/ajax-loader.gif'/>"
        listenTopics="searchCaBio" 
        refreshOnShow="false" />
