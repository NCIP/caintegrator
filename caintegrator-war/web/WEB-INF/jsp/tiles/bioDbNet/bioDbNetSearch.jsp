<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>

<s:div cssClass="TB_ajaxWindowTitle"/>
<s:div cssClass="TB_closeAjaxWindow">
        <s:a href="" title="Click to close." onclick="hideBioDbNetInputForm()" cssStyle="cursor: pointer;">
            <img src="/caintegrator/images/close.gif" border="none" align="center" alt="Click to close."/>&nbsp;
        </s:a>
</s:div>

<h2><img style="vertical-align: middle" src="/caintegrator/images/cabiologo.gif" alt="caBIO logo">Biological Database Network Search</h2>

<s:div cssStyle="padding: 1px 0px 0px 5px;">
    <tr><s:actionerror /></tr>
    <tr>
        <s:textfield label="Search Terms (comma separated)" name="searchParameters.inputValues" cssStyle="padding:0px;"/> in 
        <s:select name="searchParameters.searchType" 
            list="@gov.nih.nci.caintegrator.external.biodbnet.enums.SearchType@values()"
            listValue="name"/>
    </tr>
    <tr><br>
        <s:select name="searchParameters.taxon" list="@gov.nih.nci.caintegrator.external.biodbnet.enums.Taxon@values()"
            listValue="name" label="Select Taxon"/>
    </tr>
    <tr><br>
        <s:checkbox name="searchParameters.filterGenesOnStudy" label="Show only genes that are part of this study" />
    </tr>
    <tr><br/>
    <td style="border:0px"> 
        <button type="button" onclick="runBioDbNetSearch();">Search</button>
    </td>
    </tr>
</s:div>

<s:url id="bioDbNetSearch" action="bioDbNetSearch"/>
<sx:div id="bioDbNetSearchResultsDiv" 
        href="%{bioDbNetSearch}" 
        formId="bioDbNetSearchForm" 
        showLoadingText="true"
        loadingText="<img src='images/ajax-loader.gif' alt='ajax icon indicating loading process'/>"
        listenTopics="searchBioDbNet" 
        refreshOnShow="false"
        preload="false"
        cssStyle="padding: 0 0 0 5px;" />