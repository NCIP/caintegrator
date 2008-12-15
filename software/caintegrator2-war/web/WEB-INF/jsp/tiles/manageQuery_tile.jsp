<%@ page language="java"
    import="java.util.*, 
    com.opensymphony.xwork2.ActionContext,
     gov.nih.nci.caintegrator2.web.action.query.AnnotationSelection,
    gov.nih.nci.caintegrator2.web.action.query.QueryAnnotationCriteria"
    pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="s" uri="/struts-tags"%>

<script language="javascript">

    function setClinicalAnnotations(onOff){
        var size='<s:property value="getClinicalDefinitionsSize()"/>';
        for(i=1;i<=size;i++){
            var item = 'selectedClinicalAnnotations-' + i;
            document.manageQueryForm[item].checked = onOff;
        }
    }
    function setImageAnnotations(onOff){
        var size='<s:property value="getImageDefinitionsSize()"/>';
        for(i=1;i<=size;i++){
            var item = 'selectedImageAnnotations-' + i;
            document.manageQueryForm[item].checked = onOff;
        }
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

                <s:div href="%{criteriaUrl}" id="criteria" label="Criteria" theme="ajax" formId="manageQueryForm"/>
                <s:div href="%{columnsUrl}" id="columns" label="Columns" theme="ajax" formId="manageQueryForm"/>
                <s:div href="%{sortingUrl}" id="sorting" label="Sorting" theme="ajax" formId="manageQueryForm"/>
                <s:div href="%{searchResultsUrl}" id="searchResults" label="Search Results" theme="ajax" formId="manageQueryForm"/>
                <s:div href="%{saveAsUrl}" id="saveAs" label="Save as..." theme="ajax" formId="manageQueryForm"/>

            </s:tabbedPanel>
	    </s:form>
        
        <!--/Tab Box-->
           
</div>

<div class="clear"><br /></div>