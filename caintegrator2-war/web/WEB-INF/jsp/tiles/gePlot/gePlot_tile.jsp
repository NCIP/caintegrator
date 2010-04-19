<%@ page language="java"
    import="java.util.*, 
    com.opensymphony.xwork2.ActionContext"
    pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>

<script language="javascript">

     function setDynamicPlot(imageId, imageSrc, linkIdName, linkIdNumber) {
        document.getElementById(imageId).src = imageSrc;
        for (i = 1; i <= 4; i++) {
            document.getElementById(linkIdName + i).style.backgroundColor = "white";
        }
        document.getElementById(linkIdName + linkIdNumber).style.backgroundColor="yellow";

     }
</script>
<link rel="stylesheet" type="text/css" href="/caintegrator2/common/css/TabContainer.css" />
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
    
    <sx:tabbedpanel id="mainTabPanel" selectedTab="%{displayTab}" templateCssPath="/common/css/TabContainer.css">
        <sx:div href="%{annotationBasedUrl}" id="annotationTab" label="For Annotation" showLoadingText="true" />
        <s:if test="!anonymousUser">
            <sx:div href="%{genomicQueryBasedUrl}" id="genomicQueryTab" label="For Genomic Queries" showLoadingText="true"/>
        </s:if>
        <s:if test="!anonymousUser || !displayableWorkspace.globalSubjectLists.isEmpty()">
            <sx:div href="%{clinicalQueryBasedUrl}" id="clinicalQueryTab" label="For Annotation Queries and Saved Lists" showLoadingText="true"/>
        </s:if>
    </sx:tabbedpanel>

	<!--/Tab Box-->

</div>

<div class="clear"><br /></div>
