<%@ page language="java"
    import="java.util.*, 
    com.opensymphony.xwork2.ActionContext"
    pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="s" uri="/struts-tags"%>

<script language="javascript">
    function filterParam (field) {
        return field.name == "selectedAction";
    }

</script>


<div id="content"><!--Page Help-->

        <div class="pagehelp"><a href="#" class="help"></a></div>
        <h1>Kaplan-Meier Survival Plots</h1>
        <s:actionerror/>
        
        <!--Tab Box-->
        
        <s:url id="annotationBasedUrl" action="kmPlotAnnotationBasedInput">
            <s:param name="displayTab">annotationTab</s:param>
        </s:url>
        
        <s:url id="geneExpressionBasedUrl" action="kmPlotGeneExpressionBasedInput">
            <s:param name="displayTab">geneExpressionTab</s:param>
        </s:url>

            <s:tabbedPanel id="mainTabPanel" selectedTab="%{displayTab}"
                templateCssPath="/common/css/TabContainer.css">

                <s:form name="kaplanMeierAnnotationInputForm" id="kaplanMeierAnnotationInputForm" theme="simple">
                <s:div href="%{annotationBasedUrl}" id="annotationTab" label="For Annotation" theme="ajax" formId="kaplanMeierAnnotationInputForm" refreshOnShow="true" />
                </s:form>
                
                <s:form name="kaplanMeierGeneExpressionInputForm" id="kaplanMeierGeneExpressionInputForm" theme="simple">
                <s:div href="%{geneExpressionBasedUrl}" id="geneExpressionTab" label="For Gene Expression" theme="ajax" formId="kaplanMeierGeneExpressionInputForm" refreshOnShow="true" />
                </s:form>
            </s:tabbedPanel>
        <!--/Tab Box-->

</div>

<div class="clear"><br /></div>