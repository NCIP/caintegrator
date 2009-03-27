<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script type='text/javascript' src='dwr/interface/GenePatternAjaxUpdater.js'></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>

<div id="content">                      
    <script type="text/javascript">
    
    initializeJsp();

    function initializeJsp() {
        dwr.engine.setActiveReverseAjax(true);
        GenePatternAjaxUpdater.initializeJsp();
    }
    </script>
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('gene_pattern_help')" class="help">
   (draft)</a>
    </div>

    <!--/Page Help-->        
    
    <h1>GenePattern Analysis Status</h1>
    <s:set name="displayableWorkspace" value="#session['displayableWorkspace']" />
    <div id="errors" > </div>
    <table class="data">
        <tr>
	        <th> Job Name </th>
	        <th> Job Status </th>
	        <th> Creation Date </th>
        </tr>
        <tbody id="genePatternStatusTable">
        </tbody>

    </table>
    <br><br>
        <s:url id="genePatternAnalysisUrl" includeParams="none" action="genePatternAnalysis" />
        <s:a href="%{genePatternAnalysisUrl}" cssClass="btn" cssStyle="margin:0 5px;"><span class="btn_img"><span class="add">New Analysis Job</span></span></s:a>
            
</div>

<div class="clear"><br /></div>
