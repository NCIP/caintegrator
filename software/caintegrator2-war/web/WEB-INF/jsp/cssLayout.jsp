<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

    <tiles:importAttribute name="subTitle" scope="request" />
    <s:set name="subTitleText" value="#request['subTitle']" />
    
    <head>
		<title>
		caIntegrator 
		<s:if test="#subTitleText != null && #subTitleText != ''">
		    - <s:property value="#subTitleText" />
		</s:if>
		</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<link rel="address bar icon" href="/caintegrator/images/favicon.ico" />
		<link rel="icon" href="/caintegrator/images/favicon.ico" type="image/x-icon" />
		<link rel="shortcut icon" href="/caintegrator/images/favicon.ico" type="image/x-icon" />
		<link rel="stylesheet" type="text/css" href="/caintegrator/common/css/caintegrator2.css" />
		<link rel="stylesheet" type="text/css" href="/caintegrator/common/css/cai2modal.css" />
        <sx:head parseContent="true"/>
        <script type="text/javascript" src="/caintegrator/common/js/pde.js"></script>
        <script type="text/javascript" src="/caintegrator/common/js/prototype.js"></script>
        <script type="text/javascript" src="/caintegrator/common/js/scriptaculous.js"></script>
        <script type="text/javascript" src="/caintegrator/common/js/effects.js"></script>
        <script type="text/javascript" src="/caintegrator/common/js/caintegrator2.js"></script>
        <script type="text/javascript" src="/caintegrator/common/js/cai2modal.js"></script>
        <script type="text/javascript" src="/caintegrator/common/js/onlinehelp.js"></script>
        <script type="text/javascript" src="/caintegrator/struts/optiontransferselect.js"></script>
	</head>
	
	
	<tiles:insertAttribute name="bodyTile" />
		<a href="#content" id="navskip">Skip to Page Content</a>
		
		<div id="wrapper" class="curate">

			<!-- Global Header -->	
            
            <tiles:insertAttribute name="globalHeader" />
            
			<!-- /Global Header -->	

			<!--Work Area-->
				
			<div id="main">	
                <sx:div id="TB_overlay" cssClass="TB_overlayBG"/>
                <!-- Begin hidden busyDialogDiv -->
                <s:div id="busyDialogDiv">
                <img id="busyDialogProcessingImage" src="/caintegrator/images/ajax-loader-snake.gif" alt="ajax snake icon indicating loading process"/><br></br> Please wait for action to complete ...
                </s:div>
                <!-- End busyDialogDiv -->
                
                <!-- Begin hidden geneListSearchForm -->
                <s:form name="geneListSearchForm" id="geneListSearchForm" onsubmit="runGeneListSearch(); return false;" theme="css_xhtml">
                    <!-- For gene list to know which form element to publish gene symbols to. -->
                    <s:hidden name="geneSymbolElementId" />
                    <s:hidden name="geneListSearchTopicPublished" value="false" />
                    <sx:div 
                        id="geneListSearchInputDiv"
                        href="geneListSearchInput.action"
                        showLoadingText="true"
                        loadingText="<img src='images/ajax-loader.gif' alt='ajax icon indicating loading process'/>"
                        listenTopics="geneListSearchTopic"
                        refreshOnShow="false" 
                        cssStyle="display:none;visibility:hidden;margin-left:-140px;margin-top:-62px;width:458px;max-height: 300px; overflow:auto;"/>
                </s:form>
                <!-- End geneListSearchForm -->
                
                <!-- Begin hidden caBioGeneSearchForm -->
                <s:form name="caBioGeneSearchForm" id="caBioGeneSearchForm" onsubmit="runCaBioSearch(); return false;" theme="css_xhtml">
                    <!-- For caBio to know which form element to publish gene symbols to. -->
                    <s:hidden name="geneSymbolElementId" />
                    <s:hidden name="caBioGeneSearchTopicPublished" value="false" />
                    <sx:div id="caBioGeneSearchInputDiv"
                        href="caBioSearchInput.action"
                        showLoadingText="true"
                        loadingText="<img src='images/ajax-loader.gif' alt='ajax icon indicating loading process'/>"
                        listenTopics="caBioGeneSearchTopic"
                        refreshOnShow="false" 
                        cssStyle="display:none;visibility:hidden;margin-top:-62px;width:830px;max-height: 300px; overflow:auto;"/>
                </s:form>
                <!-- End caBioGeneSearchForm -->
				<div id="contentwrapper">
				    
				    <!--Content-->
						
	                <tiles:insertAttribute name="content" />
					
					<!--/Content-->
    
                </div>
                
				<!--Left Navigation Column-->
				
				<tiles:insertAttribute name="leftNavMenu" />
								
				<!--/Left Navigation Column-->
				
			</div>			
			
			<!--/Work Area-->		
		
			<!--Footer-->
			
			<tiles:insertAttribute name="globalFooter" />
			
			<!--/Footer-->
		
		</div>
        
			
	</body>
	
</html>
