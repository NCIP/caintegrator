<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

    <tiles:importAttribute name="subTitle" scope="request" />
    <s:set name="subTitleText" value="#request['subTitle']" />
    
    <head>
		<title>
		caIntegrator2 
		<s:if test="#subTitleText != null && #subTitleText != ''">
		    - <s:property value="#subTitleText" />
		</s:if>
		</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<link rel="address bar icon" href="favicon.ico" />
		<link rel="icon" href="favicon.ico" type="image/x-icon" />
		<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
		<link rel="stylesheet" type="text/css" href="/caintegrator2/common/css/caintegrator2.css" />
        <s:head theme="ajax"/>
        <script type="text/javascript" src="/caintegrator2/common/js/pde.js"></script>
        <script type="text/javascript" src="/caintegrator2/common/js/prototype.js"></script>
        <script type="text/javascript" src="/caintegrator2/common/js/scriptaculous.js"></script>
        <script type="text/javascript" src="/caintegrator2/common/js/effects.js"></script>
        <script type="text/javascript" src="/caintegrator2/common/js/prototype.js"></script>
        <script type="text/javascript" src="/caintegrator2/common/js/caintegrator2.js"></script>
        <script type="text/javascript" src="/caintegrator2/common/js/onlinehelp.js"></script>
        <script type="text/javascript" src="/caintegrator2/struts/optiontransferselect.js"></script>      
	</head>
	
	
	<tiles:insertAttribute name="bodyTile" />
		<a href="#content" id="navskip">Skip to Page Content</a>
		
		<div id="wrapper" class="curate">

			<!-- Global Header -->	
            
            <tiles:insertAttribute name="globalHeader" />
            
			<!-- /Global Header -->	

			<!--Work Area-->
				
			<div id="main">				
			    <!-- Begin hidden caBioGeneSearchForm -->
			    <s:div id="TB_overlay" cssClass="TB_overlayBG"/>
			    <s:form name="caBioGeneSearchForm" id="caBioGeneSearchForm" onsubmit="runCaBioSearch(); return false;">
			        <!-- For caBio to know which form element to publish gene symbols to. -->
			        <s:hidden name="geneSymbolElementId" />
			        <s:hidden name="caBioGeneSearchTopicPublished" value="false" />
			        <s:div theme="ajax" 
			            id="caBioGeneSearchInputDiv"
			            href="caBioGeneSearchInput.action"
			            loadingText="<img src='images/ajax-loader.gif'/>"
			            listenTopics="caBioGeneSearchTopic"
			            refreshOnShow="false" 
			            cssStyle="display:none;visibility:hidden;margin-left:-140px;margin-top:-62px;width:508px;max-height: 300px; overflow:auto;"/>
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
