<%@ page language="java"
    import="java.util.*, 
    com.opensymphony.xwork2.ActionContext"
    pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="s" uri="/struts-tags"%>

<script language="javascript">

    function setClinicalAnnotations(onOff){
        var size='<s:property value="queryForm.resultConfiguration.subjectColumns.options.size"/>';
        for(i=1;i<=size;i++){
            var item = 'queryForm.resultConfiguration.subjectColumns.values-' + i;
            document.manageQueryForm[item].checked = onOff;
        }
    }
    function setImageAnnotations(onOff){
        var size='<s:property value="queryForm.resultConfiguration.imageSeriesColumns.options.size"/>';
        for(i=1;i<=size;i++){
            var item = 'queryForm.resultConfiguration.imageSeriesColumns.values-' + i;
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
        if (isPotentiallyLargeQuery == "true") {
            if (confirm("This query includes all genes and will potentially take many minutes to complete.\n"
                    + "Please confirm that you want to continue?")) {
                submitForm("executeQuery");
            }
        }
        else {
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
    
    
</script>


<div id="content"><!--Page Help-->

        <div class="pagehelp"><a href="#" class="help"></a></div>
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
            <s:tabbedPanel id="mainTabPanel" selectedTab="%{displayTab}"
                templateCssPath="/common/css/TabContainer.css">

                <s:div href="%{criteriaUrl}" id="criteria" label="Criteria" theme="ajax" formId="manageQueryForm" formFilter="filterParam"/>
                <s:div href="%{columnsUrl}" id="columns" label="Columns" theme="ajax" formId="manageQueryForm" formFilter="filterParam"/>
                <s:div href="%{sortingUrl}" id="sorting" label="Sorting" theme="ajax" formId="manageQueryForm" refreshOnShow="true" />
                <s:div href="%{searchResultsUrl}" id="searchResults" label="Query Results" theme="ajax" formId="manageQueryForm" formFilter="filterParam"/>
                <s:div href="%{saveAsUrl}" id="saveAs" label="Save as..." theme="ajax" formId="manageQueryForm" formFilter="filterParam"/>

            </s:tabbedPanel>
	    </s:form>
        
        <!--/Tab Box-->
           
</div>

<div class="clear"><br /></div>