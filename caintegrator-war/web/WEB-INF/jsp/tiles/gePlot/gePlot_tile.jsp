<%@ taglib prefix="s" uri="/struts-tags"%>

<script type="text/javascript">
  function setDynamicPlot(imageId, imageSrc, linkIdName, linkIdNumber) {
      document.getElementById(imageId).src = imageSrc;
      for (i = 1; i <= 4; i++) {
          document.getElementById(linkIdName + i).style.backgroundColor = "white";
      }
      document.getElementById(linkIdName + linkIdNumber).style.backgroundColor="yellow";
  }

  $(document).ready(function() {
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

        <h1><s:property value="#subTitleText" /></h1>
        <s:actionerror/>
        
        <s:url id="annotationBasedUrl" action="gePlotAnnotationBasedInput">
            <s:param name="displayTab">annotationTab</s:param>
        </s:url>
        
        <s:url id="genomicQueryBasedUrl" action="gePlotGenomicQueryBasedInput">
            <s:param name="displayTab">genomicQueryTab</s:param>
        </s:url>
        
        <s:url id="clinicalQueryBasedUrl" action="gePlotClinicalQueryBasedInput">
            <s:param name="displayTab">clinicalQueryTab</s:param>
        </s:url>

        <s:hidden id="displayTab" value="%{displayTab}"/>
        
        <s:div id="tab-container" cssClass="tab-container">
            <ul class="etabs">
                <li class="tab"><s:a href="%{annotationBasedUrl}" data-target="#annotationTab">For Annotation</s:a></li>
                <s:if test="!anonymousUser">
                    <li class="tab"><s:a href="%{genomicQueryBasedUrl}" data-target="#genomicQueryTab">For Genomic Queries</s:a></li>
                </s:if>
                <s:if test="!anonymousUser || !displayableWorkspace.globalSubjectLists.isEmpty()">
                    <li class="tab"><s:a href="%{clinicalQueryBasedUrl}" data-target="#clinicalQueryTab">For Annotation Queries and Saved Lists</s:a></li>
                </s:if>
            </ul>
            <s:div id="annotationTab"></s:div>
            <s:if test="!anonymousUser">
                <s:div id="genomicQueryTab"></s:div>
            </s:if>
            <s:if test="!anonymousUser || !displayableWorkspace.globalSubjectLists.isEmpty()">
                <s:div id="clinicalQueryTab"></s:div>
            </s:if>
        </s:div>
</div>
<div class="clear"><br /></div>
