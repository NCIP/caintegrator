<%@ page language="java"
    import="java.util.*, 
    com.opensymphony.xwork2.ActionContext,
     gov.nih.nci.caintegrator2.web.action.query.AnnotationSelection,
    gov.nih.nci.caintegrator2.web.action.query.QueryAnnotationCriteria"
    pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="content"><!--Page Help-->

        <div class="pagehelp"><a href="#" class="help"></a></div>
        <h1>Search <s:property value="study.shortTitleText"/></h1>
        
        <!--Tab Box-->
              
        <s:form action="manageQuery" name="manageQueryForm" theme="simple">
            <s:tabbedPanel id="mainTabPanel" selectedTab="%{displayTab}">
                <jsp:include page="/WEB-INF/jsp/tiles/editQueryCriteria_tile.jsp" />
                <jsp:include page="/WEB-INF/jsp/tiles/editQueryColumns_tile.jsp" />
                <jsp:include page="/WEB-INF/jsp/tiles/editQuerySorting_tile.jsp" />
	            <jsp:include page="/WEB-INF/jsp/tiles/showQueryResults_tile.jsp" />
	            <jsp:include page="/WEB-INF/jsp/tiles/saveQuery_tile.jsp" />
            </s:tabbedPanel>
	    </s:form>
        
        <!--/Tab Box-->
           
</div>

<div class="clear"><br /></div>