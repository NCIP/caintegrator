<%@ page language="java"
    import="java.util.*, 
    com.opensymphony.xwork2.ActionContext"
    pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="s" uri="/struts-tags"%>

<script language="javascript">

     function setDynamicPlot(imageId, imageSrc, linkIdName, linkIdNumber) {
        document.getElementById(imageId).src = imageSrc;
        for (i = 1; i <= 4; i++) {
            document.getElementById(linkIdName + i).style.backgroundColor = "white";
        }
        document.getElementById(linkIdName + linkIdNumber).style.backgroundColor="yellow";

     }
</script>

<div id="content">

        <h1><s:property value="#subTitleText" /></h1>
        <s:actionerror/>
        
        <!--Tab Box-->
        
        <s:url id="annotationBasedUrl" action="gePlotAnnotationBasedInput">
            <s:param name="displayTab">annotationTab</s:param>
        </s:url>
        
        
        <s:url id="genomicQueryBasedUrl" action="gePlotGenomicQueryBasedInput">
            <s:param name="displayTab">genomicQueryTab</s:param>
        </s:url>
        
        <s:url id="clinicalQueryBasedUrl" action="gePlotClinicalQueryBasedInput">
            <s:param name="displayTab">clinicalQueryTab</s:param>
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
		<div dojoType="ContentPane" label="For Genomic Queries" id="genomicQueryTab">
			<div dojoType="struts:BindDiv" href="${genomicQueryBasedUrl}" theme="ajax"
				refreshOnShow="true" dojoType="ContentPane"></div>
		</div>
		<div dojoType="ContentPane" label="For Clinical Queries" id="clinicalQueryTab">
            <div dojoType="struts:BindDiv" href="${clinicalQueryBasedUrl}" theme="ajax"
                refreshOnShow="true" dojoType="ContentPane"></div>
        </div>
	</div>


	<!--/Tab Box-->

</div>

<div class="clear"><br /></div>
