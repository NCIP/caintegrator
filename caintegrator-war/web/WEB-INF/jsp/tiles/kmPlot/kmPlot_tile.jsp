<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript">
    function selectGeneExpressionType() {
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
    $(document).ready(function() {
    	$.ajaxSetup({ cache: false });
        $("#tab-container").easytabs({
            animate: false,
            cache: true,
            updateHash: false
         });
        $('#tab-container').bind('easytabs:ajax:beforeSend', function(e, clicked, panel) {
            var tab = $(panel);
            tab.html("<img src='images/ajax-loader.gif' alt='ajax icon indicating loading process'/>");
         });
        $('#tab-container').easytabs('select', '#' + $('#displayTab').val());
    });
</script>

<div id="content">
        <!--Page Help-->

        <div class="pagehelp"><a href="javascript:openWikiHelp('DYDnAg', 'id-6-AnalyzingStudies-CreatingKaplan-MeierPlots')" class="help">
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

        <s:hidden id="displayTab" value="%{displayTab}"/>
        
        <s:div id="tab-container" cssClass="tab-container">
            <ul class="etabs">
                <li class="tab"><s:a href="%{annotationBasedUrl}" data-target="#annotationTab">For Annotation</s:a></li>
                <s:if test="%{showGeneExpressionTab}">
                    <li class="tab"><s:a href="%{geneExpressionBasedUrl}" data-target="#geneExpressionTab">For Gene Expression</s:a></li>
                </s:if>
                <s:if test="!anonymousUser || !displayableWorkspace.globalSubjectLists.isEmpty()">
                    <li class="tab"><s:a href="%{queryBasedUrl}" data-target="#queryTab">For Queries and Saved Lists</s:a></li>
                </s:if>
            </ul>
            <s:div id="annotationTab"></s:div>
            <s:if test="%{showGeneExpressionTab}">
                <s:div id="geneExpressionTab"></s:div>
            </s:if>
            <s:if test="!anonymousUser || !displayableWorkspace.globalSubjectLists.isEmpty()">
                <s:div id="queryTab"></s:div>
            </s:if>
        </s:div>
</div>
<div class="clear"><br /></div>