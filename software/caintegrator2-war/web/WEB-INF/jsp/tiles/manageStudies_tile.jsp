<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script type='text/javascript' src='dwr/interface/StudyDeploymentAjaxUpdater.js'></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>
<script type="text/javascript" src="/caintegrator/common/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="/caintegrator/common/js/jTruncate.js"></script>
<script type="text/javascript">
    jQuery.noConflict();
</script>

<div id="content">

<script type="text/javascript">

    function initializeJsp() {
        dwr.engine.setActiveReverseAjax(true);
        StudyDeploymentAjaxUpdater.initializeJsp();
    }
    
    function truncateDescriptionDiv(divId) {
        jQuery("#" + divId).jTruncate();
    }
    
</script>

    <!--Page Help-->
    
    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('manage_study_help')" class="help">
    &nbsp;</a>
    </div>
    
    <!--/Page Help--> 
    
    <h1><s:property value="#subTitleText" /></h1>
    
    <p>View studies and click <strong>Edit</strong> to modify or click <strong>Delete</strong>. </p>
    <div class="form_wrapper_outer">
 
    <table class="form_wrapper_table">

            <tr>
                <td colspan="2" style="padding: 5px;">    
    
                <s:actionerror/>
            
                <div id="errorMessages" style="color: red;"> </div>
            
                <table class="data">
                    <tr>
                        <th>Name</th>
                        <th>Description</th>
                        <th>Last Modified By</th>
                        <th>Status</th>
                        <th>Deployment Start Date</th>
                        <th>Deployment Finish Date</th>
                        <th>caArray Data Updated</th>
                        <th>Action</th>
                    </tr>
                    <tbody id="studyDeploymentJobStatusTable" />
                </table>
    
                </td>
            </tr>
    </table>
    </div>    
</div>
<div class="clear"><br />
</div>
