<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<s:hidden name="runSearchSelected" value="false" />

<!-- caBio Inputs -->
<s:div id="TB_ajaxWindowTitle"/>
<s:div id="TB_closeAjaxWindow">
        <s:a href="" id="TB_closeWindowButton" title="Click to close." onclick="hideCaBioInputForm()">Close</s:a>
</s:div>

<h2><img style="vertical-align: middle" src="/caintegrator2/images/cabiologo.gif"> caBio Gene Search</h2>

    <tr>
        <s:textfield label="Keywords" name="geneSearchParams.keywords" id="geneKeywords" />

        <td style="border:0px"> 
        <button type="button" 
            onclick="runCaBioSearch();"> Search 
            </button>
        </td>
        <td style="border:0px">
            <em>Search caBio for genes</em>
        </td>
    </tr>
    
    <tr><br>
        <s:select name="geneSearchParams.taxon" list="taxonList" label="Choose Taxon"/>
    </tr>
    <tr><br>
        <s:checkbox name="geneSearchParams.filterGenesOnStudy" label="Show only genes that are part of this study" />
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
