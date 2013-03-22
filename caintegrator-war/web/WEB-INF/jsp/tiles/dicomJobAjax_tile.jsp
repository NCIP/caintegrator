<%@ taglib prefix="s" uri="/struts-tags"%>

<script type='text/javascript' src='dwr/interface/DicomRetrievalAjaxUpdater.js'></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>

<div id="content">

    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openWikiHelp('FoDnAg', 'id-5-ViewingQueryResults-RetrievingDICOMImages')" class="help">
   &nbsp;</a>
    </div>

    <!--/Page Help-->

    <h1><s:property value="#subTitleText" /></h1>
    
    <div id="errorMessages" style="color: red;"> </div>
	<script type="text/javascript">
		dwr.engine.setActiveReverseAjax(true);
		DicomRetrievalAjaxUpdater.runDicomJob();	
	</script>
	
	<h2>Job Description:</h2>
    <div id="imageSeriesDiv">
        <span id="imageSeriesStatus"> </span>
        <ul id="imageSeriesList"> </ul>
    </div>
    <div id="imageStudyDiv">
        <span id="imageStudyStatus"> </span>
        <ul id="imageStudyList"> </ul>
    </div>
    
    <h2>Current Status:</h2> 
	<div id="overallStatusDiv">
	
	<br><img src="images/ajax-loader.gif" id="loading" alt="ajax icon indicating loading process"/> <span id="currentStatus"> </span> 
	</div>
	
	<div id="finalizedText"> </div>
	
</div>
