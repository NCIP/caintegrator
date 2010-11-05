<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script type='text/javascript' src='dwr/interface/IGVAjaxUpdater.js'></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>

<div id="content">

    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('IGV_help')" class="help">
   &nbsp;</a>
    </div>

    <!--/Page Help-->

    <h1><s:property value="#subTitleText" /></h1>
    
    <div id="errorMessages" style="color: red;"> </div>
	<script type="text/javascript">
		dwr.engine.setActiveReverseAjax(true);
		IGVAjaxUpdater.runIgvFromQuery();	
	</script>
	
    <h2>Current Status:</h2> 
	<div id="overallStatusDiv">
	
	<br><img src="images/ajax-loader.gif" id="loading"/> <span id="currentStatus"> </span> 
	</div>
	
	<div id="finalizedText"> </div>
	
</div>
