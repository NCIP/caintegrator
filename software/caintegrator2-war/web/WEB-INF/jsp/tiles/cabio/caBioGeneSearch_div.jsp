<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<s:hidden name="runSearchSelected" value="false" />
<s:hidden name="runCaBioGeneSearchFromPathways" value="false" />
<s:hidden name="checkedPathwayBoxes" value="" />

<!-- caBio Inputs -->
<s:div id="TB_ajaxWindowTitle"/>
<s:div id="TB_closeAjaxWindow">
        <s:a href="" id="TB_closeWindowButton" title="Click to close." onclick="hideCaBioInputForm()" cssStyle="cursor: pointer;"><img src="/caintegrator2/images/close.gif" border="none" align="center"/>&nbsp;</s:a>
</s:div>

<h2><img style="vertical-align: middle" src="/caintegrator2/images/cabiologo.gif"> caBio Gene Search</h2>

<s:div cssStyle="padding-left: 5px;">
    <tr>
        <s:textfield label="Search Terms" name="searchParams.keywords" /> in 
        <s:select name="searchParams.searchTypeForDisplay" 
        list="@gov.nih.nci.caintegrator2.external.cabio.CaBioSearchTypeEnum@getDisplayableValues()"/>
        
    </tr>
    <tr>
        <br/>
        <label class="label" for="caBioSearchPreference">Match Terms:</label>   
	    
		<s:radio
		    id="caBioSearchPreference"
		    name="searchParams.searchPreferenceForDisplay"
		    list="@gov.nih.nci.caintegrator2.external.cabio.KeywordSearchPreferenceEnum@getDisplayableValues()"
		    theme="simple"/>
	    
    </tr>
    <tr><br>
        <s:select name="searchParams.taxon" list="taxonList" label="Choose Taxon"/>
    </tr>
    <tr><br>
        <s:checkbox name="searchParams.filterGenesOnStudy" label="Show only genes that are part of this study" />
    </tr>
    <tr><br/>
    <td style="border:0px"> 
        <button type="button" 
            onclick="runCaBioSearch();"> Search 
            </button>
    </td>
    </tr>
</s:div>
<!-- /caBio Inputs -->
<s:url id="caBioSearch" action="caBioSearch"/>

<br>

<s:div id="caBioGeneSearchResultsDiv" 
        theme="ajax" 
        href="%{caBioSearch}" 
        formId="caBioGeneSearchForm" 
        loadingText="<img src='images/ajax-loader.gif'/>"
        listenTopics="searchCaBio" 
        refreshOnShow="false" />
