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
        <s:form id="gisticAnalysisForm" action="gisticAnalysis" method="post" enctype="multipart/form-data" theme="css_xhtml">
        
            <s:hidden name="selectedAction" />
            <s:textfield name="currentGisticAnalysisJob.name" label="Job Name" size="50" required="true" />
            <br />
            <s:select name="currentGisticAnalysisJob.gisticUrl"
                list="gisticServices" label="GISTIC Server" required="true" />
            <br />    
            <s:div name="commentdiv" cssClass="inlinehelp_form_top">
                <div class="wwlbl">
                &nbsp;  
                </div >
                <div class="wwctrl" >Choose below either "All non-control Samples" or a Clinical query.  If "All non-control Samples" is selected, then all samples which are not included in a Control Set will be used.  If a clinical query is selected, only those samples which map to the subjects in the clinical query results will be used.  The queries selected here have been previously saved by the user. 
                </div>
            </s:div>

            
            <s:select name="gisticAnalysisForm.selectedQuery"
                headerKey="" headerValue="All non-control Samples"
                list="gisticAnalysisForm.clinicalQueries" label="Clinical query" />
            <br />
            <s:textfield name="gisticParameters.amplificationsThreshold" label="Amplifications Threshold" size="50" required="true" />
            <br />
            <s:textfield name="gisticParameters.deletionsThreshold" label="Deletions Threshold" size="50" required="true" />
            <br />
            <s:textfield name="gisticParameters.joinSegmentSize" label="Join Segment Size" size="50" required="true" />
            <br />
            <s:textfield name="gisticParameters.qvThresh" label="QV Thresh" size="50" required="true" />
            <br />
            <s:select name="gisticParameters.removeX" label="Remove X"
                list="gisticParameters.removeXOptions" required="true" />
            <br />
            <s:file name="gisticParameters.cnvSegmentsToIgnoreFile" label="cnv File" />
            <br>
            <s:submit value="Perform Analysis" align="center"
                onclick="this.form.selectedAction.value = 'execute'; return true;" />

        </s:form>
    </div>                                                                                                      
    </div>                                                                                                      
            
</div>

<div class="clear"><br /></div>
