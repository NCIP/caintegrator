<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>

<s:hidden name="runSearchSelected" value="false" />

<!-- Gene List Inputs -->
<s:div cssClass="TB_ajaxWindowTitle"/>
<s:div cssClass="TB_closeAjaxWindow">
        <s:a href="" title="Click to close." onclick="hideGeneListInputForm()"
            cssStyle="cursor: pointer;"><img src="/caintegrator2/images/close.gif"
            border="none" align="center"/>&nbsp;</s:a>
</s:div>

<h2><img style="vertical-align: middle" src="/caintegrator2/images/ico_list.gif"> Gene List Picker</h2>

<s:div cssStyle="padding-left: 5px;">
    <tr>
        <s:if test="studySubscription.allGeneListNames.isEmpty()">
            No Gene List available.
        </s:if>
        <s:else>
            <s:select name="geneListName" list="studySubscription.allGeneListNames" label="Gene List"
                onchange="runGeneListSearch();"/>
        </s:else>
    </tr>
    <br>
    <sx:div id="geneListSearchResultsDiv" 
        href="geneListSearch.action" 
        formId="geneListSearchForm" 
        showLoadingText="true"
        loadingText="<img src='images/ajax-loader.gif'/>"
        listenTopics="searchGeneList" 
        refreshOnShow="false" />
</s:div>