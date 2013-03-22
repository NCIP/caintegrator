<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script type='text/javascript' src='dwr/interface/PersistedAnalysisJobAjaxUpdater.js'></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>

<div id="content">                      
    <script type="text/javascript">

    function initializeJsp() {
        dwr.engine.setActiveReverseAjax(true);
        PersistedAnalysisJobAjaxUpdater.initializeJsp();
    }
    
    </script>
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openWikiHelp('DYDnAg', 'id-6-AnalyzingStudies-AnalyzingDatawithGenePattern')" class="help">
   &nbsp;</a>
    </div>

    <!--/Page Help-->        
    <h1><s:property value="#subTitleText" /></h1>
    <s:if test="%{anonymousUser}">
    <font color="red"><strong>Must be registered to use this feature.</strong></font>
    </s:if>
    <s:else>
    <s:set name="displayableWorkspace" value="#session['displayableWorkspace']" />
    <div id="errors" style="color: red;"> </div>
    <br><br>
        <s:form action="selectAnalysis" theme="simple">
            <s:token />
        <table><tr>
        <td>
            <s:select id="analysisType" name="analysisType" label="Analysis Type"
                list="analysisTypes" />
        </td>
        <td>
            <s:submit value="New Analysis Job" />
        </td>
        </tr></table></s:form>
    <br><br>
    <table class="data">
        <tr>
            <th> Job Name </th>
            <th> Job Type </th>
            <th> Analysis Method </th>
            <th> Status </th>
            <th> Status Description </th>
            <th> Creation Date </th>
            <th> Status Update Date </th>
            <th> Action </th>
        </tr>
        <tbody id="analysisJobStatusTable" />
    </table>
    </s:else>
</div>

<div class="clear"><br /></div>

