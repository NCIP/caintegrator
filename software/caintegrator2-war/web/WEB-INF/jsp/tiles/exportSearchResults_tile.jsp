<%@ page language="java"
    import="java.util.*, 
    com.opensymphony.xwork2.ActionContext,
     gov.nih.nci.caintegrator2.web.action.query.AnnotationSelection,
    gov.nih.nci.caintegrator2.web.action.query.QueryAnnotationCriteria"
    pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

            
<div id="content">                      

<!--Search Results-->
<s:form action="manageQuery" name="manageQueryForm" id="manageQueryForm" theme="simple">
    <jsp:include page="/WEB-INF/jsp/tabs/editQuerySearchResults.jsp" />
</s:form>
<!--/Search Results-->

</div>

<div class="clear"><br /></div>
