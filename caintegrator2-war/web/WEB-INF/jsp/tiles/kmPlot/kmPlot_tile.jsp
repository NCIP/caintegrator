<%@ page language="java"
    import="java.util.*, 
    com.opensymphony.xwork2.ActionContext"
    pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<link rel="stylesheet" type="text/css" href="/caintegrator2/common/css/TabContainer.css" />
<div id="content">
        <!--Page Help-->

        <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('KM_annotation_help')" class="help">
       &nbsp;</a>
        </div>

        <!--/Page Help-->
        
        <h1><s:property value="#subTitleText" /></h1>
        <s:actionerror/>
        
        <!--Tab Box-->
        
        <s:url id="annotationBasedUrl" action="kmPlotAnnotationBasedInput">
            <s:param name="displayTab">annotationTab</s:param>
        </s:url>
        
        <s:url id="geneExpressionBasedUrl" action="kmPlotGeneExpressionBasedInput">
            <s:param name="displayTab">geneExpressionTab</s:param>
        </s:url>
        
         <s:url id="queryBasedUrl" action="kmPlotQueryBasedInput">
            <s:param name="displayTab">queryTab</s:param>
        </s:url>

      <s:set name="displayTab" id="displayTab" value="%{displayTab}"/>

    <sx:tabbedpanel id="mainTabPanel" selectedTab="%{displayTab}" templateCssPath="/common/css/TabContainer.css">
		<sx:div href="%{annotationBasedUrl}" id="annotationTab" label="For Annotation" showLoadingText="true" />
		<sx:div href="%{geneExpressionBasedUrl}" id="geneExpressionTab" label="For Gene Expression" showLoadingText="true"/>
		<sx:div href="%{queryBasedUrl}" id="queryTab" label="For Queries and Saved Lists" showLoadingText="true"/>
    </sx:tabbedpanel>

	<!--/Tab Box-->

</div>

<div class="clear"><br /></div>
