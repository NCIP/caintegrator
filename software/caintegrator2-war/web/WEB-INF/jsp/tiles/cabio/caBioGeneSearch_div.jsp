<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>

<s:hidden name="runSearchSelected" value="false" />
<s:hidden name="runCaBioGeneSearchFromPathways" value="false" />
<s:hidden name="checkedPathwayBoxes" value="" />

<!-- caBio Inputs -->
<s:div cssClass="TB_ajaxWindowTitle"/>
<s:div cssClass="TB_closeAjaxWindow">
        <s:a href="" title="Click to close." onclick="hideCaBioInputForm()" cssStyle="cursor: pointer;"><img src="/caintegrator/images/close.gif" border="none" align="center"/>&nbsp;</s:a>
</s:div>

<h2><img style="vertical-align: middle" src="/caintegrator/images/cabiologo.gif"> caBIO Gene Search</h2>

<s:div cssStyle="padding: 1px 0px 0px 5px;">
    <tr><s:actionerror /></tr>
    <s:if test="%{ableToConnect}">
    <tr>
        <s:textfield label="Search Terms" name="searchParams.keywords" cssStyle="padding:0px;"/> in 
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
    </s:if>
</s:div>
<!-- /caBio Inputs -->
<s:url id="caBioSearch" action="caBioSearch"/>

<sx:div id="caBioGeneSearchResultsDiv" 
        href="%{caBioSearch}" 
        formId="caBioGeneSearchForm" 
        showLoadingText="true"
        loadingText="<img src='images/ajax-loader.gif'/>"
        listenTopics="searchCaBio" 
        refreshOnShow="false"
        cssStyle="padding: 0 0 0 5px;" />
