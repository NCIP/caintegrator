<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<script type="text/javascript">
      

    function selectGeneExpressionType()
    {
        var arrayOfElements=document.getElementsByName("kmPlotForm.geneExpressionBasedForm.expressionType");
        var tbody = document.getElementById('controlSampleTbody');
        for (var i = 0; i < arrayOfElements.length; i++){
            if (arrayOfElements[i].checked==true) {
                if (arrayOfElements[i].value == 'By Fold Change') {
	                document.getElementById("overValueTextPrefix").innerHTML = "Overexpressed >= ";  
	                document.getElementById("overValueTextSuffix").innerHTML = " fold";
	                document.getElementById("underValueTextPrefix").innerHTML = "Underexpressed >= ";  
	                document.getElementById("underValueTextSuffix").innerHTML = " fold";
	                document.getElementById("overexpressedNumberTextField").value = "2";
	                document.getElementById("underexpressedNumberTextField").value = "2";
	                tbody.style.display = "";
	                break;
                } else {
	            	document.getElementById("overValueTextPrefix").innerHTML = "Above Expression Level ";  
	                document.getElementById("overValueTextSuffix").innerHTML = "";
	                document.getElementById("underValueTextPrefix").innerHTML = "Below Expression Level ";  
	                document.getElementById("underValueTextSuffix").innerHTML = "";
	                document.getElementById("overexpressedNumberTextField").value = "100";
	                document.getElementById("underexpressedNumberTextField").value = "50";
	                tbody.style.display = "none";
	                break;
                }
            }
        } 
    }   
</script>

<link rel="stylesheet" type="text/css" href="/caintegrator/common/css/TabContainer.css" />
<div id="content">
        <!--Page Help-->

        <div class="pagehelp"><a href="javascript:openWikiHelp('DYDnAg', '6-AnalyzingStudies-CreatingKaplanMeierPlots')" class="help">
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
		<s:if test="%{showGeneExpressionTab}" >
		  <sx:div href="%{geneExpressionBasedUrl}" preload="false" executeScripts="true" id="geneExpressionTab" label="For Gene Expression" showLoadingText="true"/>
	    </s:if>
		<s:if test="!anonymousUser || !displayableWorkspace.globalSubjectLists.isEmpty()">
		  <sx:div href="%{queryBasedUrl}" preload="false" id="queryTab" label="For Queries and Saved Lists" showLoadingText="true"/>
	    </s:if>
    </sx:tabbedpanel>
	<!--/Tab Box-->

</div>

<div class="clear"><br /></div>
