<%@ taglib prefix="s" uri="/struts-tags"%>
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
        <link rel="stylesheet" type="text/css" href="/caintegrator/common/css/ui-lightness/jquery-ui-1.10.1.custom.css" />
		<link rel="stylesheet" type="text/css" href="/caintegrator/common/css/caintegrator2.css" />
		<link rel="stylesheet" type="text/css" href="/caintegrator/common/css/cai2modal.css" />
        <link rel="stylesheet" type="text/css" href="/caintegrator/common/css/easytabs.css" />
        <link rel="stylesheet" type="text/css" href="/caintegrator/common/js/navgoco/jquery.navgoco.css"/>
        
        <script type="text/javascript" src="./struts/optiontransferselect.js"></script>
        <script type="text/javascript" src="/caintegrator/common/js/caintegrator2.js"></script>
        <script type="text/javascript" src="/caintegrator/common/js/cai2modal.js"></script>
        <script type="text/javascript" src="/caintegrator/common/js/onlinehelp.js"></script>
        
        <script type="text/javascript" src="/caintegrator/common/js/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="/caintegrator/common/js/jquery.editable-select.js"></script>
        <script type="text/javascript" src="/caintegrator/common/js/jquery-ui-1.10.1.custom.min.js"></script>
        <script type="text/javascript" src="/caintegrator/common/js/jquery.idletimer.js"></script>
        <script type="text/javascript" src="/caintegrator/common/js/jquery.idletimeout.js"></script>
        <script type="text/javascript" src="/caintegrator/common/js/jquery.easytabs.min.js"></script>
        
        <script type="text/javascript" src="/caintegrator/common/js/navgoco/jquery.navgoco.min.js"></script>
        
        
        <s:if test="%{!anonymousUser}"> 
            <script type="text/javascript">
            $(document).ready(function() {
                $("#dialog").dialog({
                    autoOpen: false,
                    modal: true,
                    width: 400,
                    height: 200,
                    closeOnEscape: false,
                    draggable: false,
                    resizable: false,
                    buttons: {
                        'Yes, Keep Working': function(){
                            $(this).dialog('close');
                            },
                        'No, Logoff': function(){
                            window.location = "login.jsp";
                            }
                    },
                    dialogClass: 'no-close'
                });
                // cache a reference to the countdown element so we don't have to query the DOM for it on each ping.
                var $countdown = $("#dialog-countdown");

                // start the idle timer plugin
                $.idleTimeout('#dialog', 'div.ui-dialog-buttonpane button:first', {
                    //Idle after 29.5 minutes
                    idleAfter: 1770,
                    //Poll every 30 minutes
                    pollingInterval: 1800,
                    keepAliveURL: 'keepAlive.jsp',
                    serverResponseEquals: 'OK',
                    onTimeout: function(){
                        window.location = "sessionTimeout.jsp";
                    },
                    onIdle: function(){
                        $(this).dialog("open");
                    },
                    onCountdown: function(counter){
                        $countdown.html(counter);
                    }
                });
                
                $('.editable-select').editableSelect({
                    bg_iframe: true,
                });
                
                $('.nav').navgoco({
                    caret: '<span class="caret"></span>',
                    accordion: true,
                    openClass: 'open',
                    slide: {
                        duration: 200,
                        easing: 'linear'
                    } 
                });
            });
            </script>
        </s:if>
        <s:head />
	</head>
	
	
	<tiles:insertAttribute name="bodyTile" />
		<a href="#content" id="navskip">Skip to Page Content</a>
		
		<div id="wrapper" class="curate">

			<!-- Global Header -->	
            
            <tiles:insertAttribute name="globalHeader" />
            
			<!-- /Global Header -->	

			<!--Work Area-->
				
			<div id="main">	
                <s:if test="%{!anonymousUser}"> 
                    <div id="dialog" title="Your session is about to expire!" style="display: none;">
                        <p>
                            <span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 50px 0;"></span>
                            You will be logged off in <span id="dialog-countdown" style="font-weight:bold"></span> seconds.
                        </p>
                        <p>Do you want to continue your session?</p>
                    </div>
                </s:if>
        
                <s:div id="TB_overlay" cssClass="TB_overlayBG"/>
                <!-- Begin hidden busyDialogDiv -->
                <s:div id="busyDialogDiv">
                <img id="busyDialogProcessingImage" src="/caintegrator/images/ajax-loader-snake.gif" alt="ajax snake icon indicating loading process"/><br></br> Please wait for action to complete ...
                </s:div>
                <!-- End busyDialogDiv -->
                
                <!-- Begin hidden geneListSearchForm -->
                <s:form name="geneListSearchForm" id="geneListSearchForm" onsubmit="runGeneListSearch(); return false;" theme="css_xhtml">
                    <!-- For gene list to know which form element to publish gene symbols to. -->
                    <s:hidden name="geneSymbolElementId"/>
                    <s:hidden name="geneListSearchTopicPublished" value="false" />
                    <s:div id="geneListSearchInputDiv" cssStyle="margin-left:-140px;margin-top:-62px;width:458px;max-height: 300px; overflow:auto;"></s:div>
                </s:form>
                <!-- End geneListSearchForm -->
                
                <s:form name="bioDbNetSearchForm" id="bioDbNetSearchForm" onsubmit="runBioDbNetSearch(); return false;" theme="css_xhtml">
                    <!-- For bioDbNet to know which form element to publish gene symbols to. -->
                    <s:hidden name="geneSymbolElementId" />
                    <s:div id="bioDbNetSearchInputDiv" cssStyle="margin-top:-62px;width:830px;max-height: 500px; overflow:auto;"></s:div>
                </s:form>
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
