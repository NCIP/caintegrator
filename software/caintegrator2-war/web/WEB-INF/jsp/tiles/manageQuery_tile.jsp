<%@ page language="java"
    import="java.util.*, 
    com.opensymphony.xwork2.ActionContext"
    pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>

<script type="text/javascript" src="/caintegrator/common/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="/caintegrator/common/js/jTruncate.js"></script>
<script language="javascript">

    jQuery.noConflict();

    function setGroupAnnotations(onOff, size, checkboxListNum){
        for(i=1;i<=size;i++){
            var item = 'queryForm.resultConfiguration.columnSelectionLists[' + checkboxListNum +'].values-' + i;
            document.manageQueryForm[item].checked = onOff;
        }
    }

    function selectAll () {
        document.manageQueryForm.selectedAction.value='selectAll';
        document.manageQueryForm.submit();
    }

    function selectNone () {
        document.manageQueryForm.selectedAction.value='selectNone';
        document.manageQueryForm.submit();
    }

    function selectAllSubject () {
        document.manageQueryForm.selectedAction.value='selectAllSubject';
        document.manageQueryForm.submit();
    }

    function selectNoneSubject () {
        document.manageQueryForm.selectedAction.value='selectNoneSubject';
        document.manageQueryForm.submit();
    }
    
    function filterParam (field) {
        return field.name == "selectedAction";
    }
    
    function deleteQuery () {
        if (confirm('This query will be permanently deleted.')) {
            submitForm("deleteQuery");
        }
    }

    function runSearch() {
        var isPotentiallyLargeQuery = '<s:property value="queryForm.isPotentiallyLargeQuery()"/>';
        if (isPotentiallyLargeQuery == "true" || jQuery(':text[value=""][id^=FoldChangeGeneSymbol]').length != 0) {
            if (confirm("This query includes all genes and will potentially take many minutes to complete.\n"
                    + "Please confirm that you want to continue?")) {
            	showBusyDialog();
                submitForm("executeQuery");
            }
        }
        else {
        	showBusyDialog();
            submitForm("executeQuery");
        }
    }
    
    function saveQuery(saveType) {
        var orgName = '<s:property value="queryForm.getOrgQueryName()"/>';
        var newName = document.getElementById("saveName").value;
        if (saveType == "save") {
            if (orgName != "" && orgName != newName) {
                if (confirm("You are about to save and rename '" + orgName + "' to '" + newName + "'.\n"
                            + "If you want to save this query to a new query, please use the 'Save As' button.")) {
                    submitForm("saveQuery");
                }
            }
            else {
                submitForm("saveQuery");
            }
        }
        else if (saveType == "saveAs") {
            if (orgName == newName) {
                alert ("You need to enter a new name to save as.");
                return false;
            }
            submitForm("saveAsQuery");
        }
    }
    
    function submitForm(selectAction) {
        document.manageQueryForm.selectedAction.value = selectAction;
        document.manageQueryForm.submit();
    }
    
    function openExportLink() {
        var exportLink = jQuery("div.exportlinks");
        if (exportLink.length == 1) {
            window.location.href = exportLink[0].getElementsByTagName("a")[0].href;
        }
        else {
            alert ("Please report the error.\n" +
                "Export button is broken, error code: " + allLinks.length);
        }
    }

    function resetSorting(numberColumns) {
    	for (i=1;i<=numberColumns;i++) {
    		document.getElementById("sortType_" + i + "Ascending").checked = false;
    		document.getElementById("sortType_" + i + "Descending").checked = false;
    		document.getElementById("sortType_" + i + "No Sort").checked = true;
    		document.getElementById("columnIndex_" + i).selectedIndex = i - 1;
    	}
    }
    
    function sortGenomicResult(type, index) {
        document.manageQueryForm.genomicSortingType.value = type;
        document.manageQueryForm.genomicSortingIndex.value = index;
        submitForm("sortGenomicResult");
    }
    
</script>

<div id="content">
        <h1>Search <s:property value="study.shortTitleText"/></h1>
        <s:actionerror/>
        
        <!--Tab Box-->
        
        <s:url id="criteriaUrl" action="selectQueryTab">
            <s:param name="selectedAction">selectedTabCriteria</s:param>
        </s:url>
        <s:url id="columnsUrl" action="selectQueryTab">
            <s:param name="selectedAction">selectedTabColumns</s:param>
        </s:url>
        <s:url id="sortingUrl" action="selectQueryTab">
            <s:param name="selectedAction">selectedTabSorting</s:param>
        </s:url>
        <s:url id="searchResultsUrl" action="selectQueryTab">
            <s:param name="selectedAction">selectedTabSearchResults</s:param>
        </s:url>
        <s:url id="saveAsUrl" action="selectQueryTab">
            <s:param name="selectedAction">selectedTabSaveAs</s:param>
        </s:url>
        
        <s:form action="manageQuery" name="manageQueryForm" id="manageQueryForm" theme="simple">
            <link rel="stylesheet" type="text/css" href="/caintegrator/common/css/TabContainer.css" />

            <sx:tabbedpanel id="mainTabPanel" selectedTab="%{displayTab}"
                templateCssPath="/common/css/TabContainer.css">

                <sx:div href="%{criteriaUrl}" id="criteria" label="Criteria" formId="manageQueryForm" formFilter="filterParam" showLoadingText="true"/>
                <sx:div href="%{columnsUrl}" id="columns" label="Results Type" formId="manageQueryForm" formFilter="filterParam" showLoadingText="true"/>
                <sx:div href="%{sortingUrl}" id="sorting" label="Sorting" formId="manageQueryForm" refreshOnShow="true" showLoadingText="true"/>
                <sx:div href="%{searchResultsUrl}" id="searchResults" label="Query Results" formId="manageQueryForm" formFilter="filterParam" showLoadingText="true" preload="false" executeScripts="true"/>
                <s:if test="%{!anonymousUser}">
                    <sx:div href="%{saveAsUrl}" id="saveAs" label="Save query as..." formId="manageQueryForm" formFilter="filterParam" showLoadingText="true"/>
                </s:if>
            </sx:tabbedpanel>
	    </s:form>
        
        <!--/Tab Box-->
           
</div>

<s:div id="subjectlistdiv" cssStyle="display:none;visibility:hidden;margin-left:-140px;margin-top:-62px;width:465px;max-height: 150px; overflow:auto;">
    <s:div cssClass="TB_ajaxWindowTitle"/>
    <s:div cssClass="TB_closeAjaxWindow">
        <s:a href="" title="Click to close." onclick="hideSubjectListForm()" cssStyle="cursor: pointer;"><img src="/caintegrator/images/close.gif" border="none" align="center" alt="Click to close."/>&nbsp;</s:a>
    </s:div>
    <h2>Save Subject List</h2>
    <s:form id="myform">
        <tr><td colspan="2">
            <s:if test="%{anonymousUser}">
                <font color="red"><strong>Must be registered to use this feature.</strong></font>
            </s:if>
            <s:else>
                <strong>Please enter the name and description</strong>
            </s:else>
        </td></tr>
        <s:textfield id="slName" label="Name" name="T1" size="30" disabled="%{anonymousUser}"/>
        <s:textfield id="slDescription" label="Description" name="T2" size="50" disabled="%{anonymousUser}"/>
        <s:if test="%{studyManager}">
            <s:checkbox id="slVisibleToOthers" name="T3" label="Make Visible to Others" labelposition="left" disabled="%{anonymousUser}"/>
        </s:if>
        <tr>
            <td></td>
            <td><br>
                <s:if test="%{anonymousUser}">
                    <button type="button" onClick="submitSubjectListForm()" disabled="true">Save</button>
                </s:if>
                <s:else>
                    <button type="button" onClick="submitSubjectListForm()">Save</button>
                </s:else>
                <button type="button" onClick="hideSubjectListForm()">Cancel</button>
            </td>
        </tr>
    </s:form>
</s:div>

<div class="clear"><br /></div>