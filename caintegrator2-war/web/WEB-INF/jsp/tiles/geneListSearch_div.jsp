<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<s:hidden name="runSearchSelected" value="false" />

<!-- Gene List Inputs -->
<s:div id="TB_GL_ajaxWindowTitle"/>
<s:div id="TB_GL_closeAjaxWindow">
        <s:a href="" id="TB_GL_closeWindowButton" title="Click to close." onclick="hideGeneListInputForm()"
            cssStyle="cursor: pointer;"><img src="/caintegrator2/images/close.gif"
            border="none" align="center"/>&nbsp;</s:a>
</s:div>

<h2><img style="vertical-align: middle" src="/caintegrator2/images/ico_list.gif"> Gene List Picker</h2>

<s:div cssStyle="padding-left: 5px;">
    <tr>
        <s:if test="genes.isEmpty()">
            No Gene List available.
        </s:if>
        <s:else>
            <s:select name="geneList" list="studySubscription.geneListNames" label="Gene List"
                onchange="runGeneListSearch();"/>
        </s:else>
    </tr>
    <br>
    <s:div id="geneListSearchResultsDiv" 
        theme="ajax" 
        href="geneListSearch.action" 
        formId="geneListSearchForm" 
        loadingText="<img src='images/ajax-loader.gif'/>"
        listenTopics="searchGeneList" 
        refreshOnShow="false" />
</s:div>