<%@ taglib prefix="s" uri="/struts-tags"%>

<s:div cssClass="TB_ajaxWindowTitle"/>
<s:div cssClass="TB_closeAjaxWindow">
        <s:a href="" title="Click to close." onclick="hideBioDbNetInputForm()" cssStyle="cursor: pointer;">
            <img src="/caintegrator/images/close.gif" border="none" align="center" alt="Click to close."/>&nbsp;
        </s:a>
        <div class="pagehelp">
            <a href="javascript:openWikiHelp('B4DnAg', 'id-4-SearchingacaIntegratorStudy-bioDbNetSearch')" class="help"></a>
        </div>
</s:div>

<h2><img style="vertical-align: middle" src="/caintegrator/images/bioDbNet.gif" alt="bioDbNet logo"> Biological Database Network Search</h2>

<s:div cssStyle="padding: 1px 0px 0px 5px;">
    <tr><s:actionerror /></tr>
    <tr>
        <s:textfield label="Search Terms (comma separated)" name="searchParameters.inputValues" id="inputValues" cssStyle="padding:0px;"/> in 
        <s:select name="searchParameters.searchType" id="searchType"
            list="@gov.nih.nci.caintegrator.external.biodbnet.enums.SearchType@values()"
            listValue="name"/>
    </tr>
    </tr>
    <tr><br>
        <s:select name="searchParameters.taxon" list="@gov.nih.nci.caintegrator.external.biodbnet.enums.Taxon@values()"
            listValue="name" label="Select Taxon"/>
    </tr>
    <tr><br>
        <s:checkbox name="searchParameters.filterGenesOnStudy" label="Show only genes that are part of this study" />
    </tr>
    <tr>
        <br>
        <s:checkbox name="searchParameters.caseSensitiveSearch" label="Case Sensitive Search" />
    </tr>
    <tr><br/>
    <td style="border:0px"> 
        <button type="button" onclick="runBioDbNetSearch();">Search</button>
    </td>
    </tr>
</s:div>
<div id="bioDbNetSearchResultsDiv"></div>