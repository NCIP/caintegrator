<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      
    
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('gene_pattern_help')" class="help">
   (draft)</a>
    </div>

    <!--/Page Help-->        
    
    <h1>GISTIC Analysis</h1>
    
    <div class="box2">   
    <div class="whitebg">   
        <s:actionerror/>
        <s:form id="gisticAnalysisForm" action="gisticAnalysis">
        
            <s:hidden name="selectedAction" />
            
            <s:textfield name="currentGisticAnalysisJob.name" label="Job Name" size="50" required="true" />
            <s:select name="currentGisticAnalysisJob.gisticUrl"
                list="gisticServices" label="GISTIC Server" required="true" />
            
            <s:select name="gisticParameters.refgeneFile"
                list="gisticParameters.refgeneFileOptions" label="Refgene File" required="true" />
            <s:select name="gisticAnalysisForm.selectedQuery"
                headerKey="" headerValue="-- None --"
                list="gisticAnalysisForm.clinicalQueries" label="Clinical query" />
            
            <s:textfield name="gisticParameters.amplificationsThreshold" label="Amplifications Threshold" size="50" required="true" />
            <s:textfield name="gisticParameters.deletionsThreshold" label="Deletions Threshold" size="50" required="true" />
            <s:textfield name="gisticParameters.joinSegmentSize" label="Join Segment Size" size="50" required="true" />
            <s:textfield name="gisticParameters.qvThresh" label="QV Thresh" size="50" required="true" />
            <s:select name="gisticParameters.removeX" label="Remove X"
                list="gisticParameters.removeXOptions" required="true" />
            <br>
            <s:file name="gisticParameters.cnvSegmentsToIgnoreFile" label="cnv File" />
            <s:submit value="Perform Analysis" align="center"
                onclick="this.form.selectedAction.value = 'execute'; return true;" />

        </s:form>
    </div>                                                                                                      
    </div>                                                                                                      
            
</div>

<div class="clear"><br /></div>
