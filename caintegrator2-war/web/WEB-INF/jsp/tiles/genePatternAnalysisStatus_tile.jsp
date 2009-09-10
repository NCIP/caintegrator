<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script type='text/javascript' src='dwr/interface/PersistedAnalysisJobAjaxUpdater.js'></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>

<div id="content">                      
    <script type="text/javascript">
    
    initializeJsp();

    function initializeJsp() {
        dwr.engine.setActiveReverseAjax(true);
        PersistedAnalysisJobAjaxUpdater.initializeJsp();
    }
    
    </script>
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('gene_pattern_help')" class="help">
   &nbsp;</a>
    </div>

    <!--/Page Help-->        
    <h1><s:property value="#subTitleText" /></h1>
    <s:set name="displayableWorkspace" value="#session['displayableWorkspace']" />
    <div id="errors" style="color: red;"> </div>
    <br><br>
        <s:form theme="simple"><table><tr>
        <td>
            <s:select id="analysisType" name="analysisType" label="Analysis Type" value="GPM" 
                list="#{'genePatternModules':'Gene Pattern Modules', 
                        'comparativeMarkerSelection':'Comparative Marker Selection (Grid Service)',
                        'principalComponentAnalysis':'Principal Component Analysis (Grid Service)',
                        'gistic':'GISTIC (Grid Service)'}" />
        </td>
        <td>
            <s:submit value="New Analysis Job" action="selectAnalysis" />
        </td>
        </tr></table></s:form>
    <br><br>
    <table class="data">
        <tr>
            <th> Job Name </th>
            <th> Job Type </th>
            <th> Status </th>
            <th> Creation Date </th>
            <th> Status Update Date </th>
            <th> Action </th>
        </tr>
        <tbody id="analysisJobStatusTable" />
    </table>
</div>

<div class="clear"><br /></div>

