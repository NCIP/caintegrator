<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script type='text/javascript' src='dwr/interface/IGVAjaxUpdater.js'></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>

<div id="content">
    <h1><s:property value="#subTitleText" /></h1>
    
    <div id="errorMessages" style="color: red;"> </div>
	<script type="text/javascript">
		dwr.engine.setActiveReverseAjax(true);
		IGVAjaxUpdater.runViewer();	
	</script>
	
    <h2>Current Status:</h2> 
	<div id="overallStatusDiv">
	
	<br><img src="images/ajax-loader.gif" id="loading" alt="ajax icon indicating loading process"/> <span id="currentStatus"> </span> 
	</div>
	
	<div id="finalizedText"> </div>
	
	<div> 
	<br>
    Clicking on the "Launch Integrative Genomics Viewer" link will launch an application that is hosted outside of NIH.<br>
<br>
    The U.S. Government, NIH and their employees and contractors do not make any warranty, express or implied, including the warranties of merchantability and fitness for a particular purpose with respect to documents available from this server. In addition, the U.S. Government, NIH, and their employees and contractors assume no legal liability for the accuracy, completeness, or usefulness of any information, apparatus, product, or process disclosed herein and do not represent that use of such information, apparatus, product or process would not infringe on privately owned rights.
Links to other Internet sites are provided for the convenience of World Wide Web users. NIH is not responsible for the availability or content of these external sites, nor does NIH endorse, warrant or guarantee the products, services or information described or offered at these other Internet sites.
    </div>
	
</div>
