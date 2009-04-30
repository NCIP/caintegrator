<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script type='text/javascript' src='dwr/interface/StudyDeploymentAjaxUpdater.js'></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>

<div id="content">

<script type="text/javascript">
    
    initializeJsp();

    function initializeJsp() {
        dwr.engine.setActiveReverseAjax(true);
        StudyDeploymentAjaxUpdater.initializeJsp();
    }
    
    </script>

    <!--Page Help-->
    
    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('manage_study_help')" class="help">
    &nbsp;</a>
    </div>
    
    <!--/Page Help--> 

<h1>Manage Studies</h1>
<s:actionerror/>

<div id="errorMessages" style="color: red;"> </div>

    <table class="data">
        <tr>
            <th>Name</th>
            <th>Description</th>
            <th>Status</th>
            <th>Deployment Start Date</th>
            <th>Deployment Finish Date</th>
            <th>Action</th>
        </tr>
        <tbody id="studyDeploymentJobStatusTable" />
    </table>
    
    </div> 
<div class="clear"><br />
</div>
