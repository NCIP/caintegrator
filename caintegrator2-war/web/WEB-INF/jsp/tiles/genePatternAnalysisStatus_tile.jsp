<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script type='text/javascript' src='dwr/interface/GenePatternAjaxUpdater.js'></script>
<script type='text/javascript' src='dwr/interface/ComparativeMarkerSelectionAjaxUpdater.js'></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>

<div id="content">                      
    <script type="text/javascript">
    
    initializeJsp();

    function initializeJsp() {
        dwr.engine.setActiveReverseAjax(true);
        GenePatternAjaxUpdater.initializeJsp();
        ComparativeMarkerSelectionAjaxUpdater.initializeJsp();
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
            <th> GenePattern Job Name </th>
            <th> Status </th>
            <th> Creation Date </th>
        </tr>
        <tbody id="genePatternStatusTable" />
    </table>
    <br><br>
    <table class="data">
        <tr>
            <th> Comparative Marker Selection Job Name </th>
            <th> Status </th>
            <th> Creation Date </th>
        </tr>
        <tbody id="comparativeMarkerSelectionStatusTable" />
    </table>
    <br><br>
        <s:form ><table><tr>
        <td>
            <s:select id="analysisType" name="analysisType" label="Analysis Type" value="GPM" 
                list="#{'genePatternModules':'Gene Pattern Modules', 'comparativeMarkerSelection':'Comparative Marker Selection'}" />
        </td>
        <td>
            <s:submit value="New Analysis Job" action="selectAnalysis" />
        </td>
        </tr></table></s:form>
            
</div>

<div class="clear"><br /></div>
