<%@ page language="java"
    import="java.util.*, 
    com.opensymphony.xwork2.ActionContext"
    pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="content">

        <!--Page Help-->

        <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('GE_annotation_help')" class="help">
       (draft)</a>
        </div>

        <!--/Page Help-->
        
        <h1>Gene Expression Value Plots</h1>
        <s:actionerror/>
        
        <!--Tab Box-->
        
        <s:url id="annotationBasedUrl" action="gePlotAnnotationBasedInput">
            <s:param name="displayTab">annotationTab</s:param>
        </s:url>
        
        
         <s:url id="queryBasedUrl" action="gePlotQueryBasedInput">
            <s:param name="displayTab">queryTab</s:param>
        </s:url>

      <s:set name="displayTab" id="displayTab" value="%{displayTab}"/>

    <!-- See the following issue to see why I used a TabContainer instead of tabPanel, basically IE was erroring out
         when switching tabs and trying to do "Create Plot" (dynamic div inside a tab panel). 
https://issues.apache.org/struts/browse/WW-1906?page=com.atlassian.jira.plugin.system.issuetabpanels:comment-tabpanel 
    -->
	<div dojoType="TabContainer" id="mainTabPanel"
		selectedChild="${displayTab}" doLayout="false"
		templateCssPath="/caintegrator2/common/css/TabContainer.css">
		<div dojoType="ContentPane" label="For Annotation" id="annotationTab">
			<div dojoType="struts:BindDiv" href="${annotationBasedUrl}"
				theme="ajax" refreshOnShow="true">
			</div>
		</div>
		<!-- 
		<div dojoType="ContentPane" label="For Queries" id="queryTab">
			<div dojoType="struts:BindDiv" href="${queryBasedUrl}" theme="ajax"
				refreshOnShow="true" dojoType="ContentPane"></div>
		</div>
		 -->
	</div>


	<!--/Tab Box-->

</div>

<div class="clear"><br /></div>
