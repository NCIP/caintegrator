<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script type="text/javascript" src="/caintegrator2/common/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="/caintegrator2/common/js/jquery.editable-select.js"></script>
<script type="text/javascript">
    jQuery.noConflict();

    jQuery(function() {
      jQuery('.editable-select').editableSelect(
        {
          bg_iframe: true,
          onSelect: false,
          case_sensitive: false, // If set to true, the user has to type in an exact
                                 // match for the item to get highlighted
          items_then_scroll: 10 // If there are more than 10 items, display a scrollbar
        }
      );
      document.getElementById("caDnaCopyUrl").value = document.dnaAnalysisDataConfigurationForm.serverProfileUrl.value;
    });

    function saveDatasource() {
    	document.dnaAnalysisDataConfigurationForm.serverProfileUrl.value = document.getElementById("caDnaCopyUrl").value; // The value isn't set without doing this.
    	document.dnaAnalysisDataConfigurationForm.submit();
    }

</script>   
            
<div id="content">                      

    <h1 style="color: #FFFFFF; background: #263D6B; padding: 5px;">editing: <strong><s:property value="studyConfiguration.study.shortTitleText" /></strong></h1>
    
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('configure_copy_number_help')" class="help">
   &nbsp;</a>
    </div>

    <!--/Page Help-->
    
    <h1><s:property value="#subTitleText" /></h1>
    
    <p>Enter data source parameters and click <strong>Save</strong>.</p>
    <div class="form_wrapper_outer">
 
    <table class="form_wrapper_table">
            <tr>
                <th class="title" style="height: 2.5em;">Data Source</th>
                <th class="alignright">&nbsp;</th>
            </tr>
            <tr>
                <td colspan="2" style="padding: 5px;">    
    
                <s:actionerror/>
                <s:form id="dnaAnalysisDataConfigurationForm" name="dnaAnalysisDataConfigurationForm"
                    action="saveDnaAnalysisDataConfiguration"
                    method="post" enctype="multipart/form-data" theme="css_xhtml">
                    <s:hidden name="studyConfiguration.id" />
                    <s:hidden name="genomicSource.id" />
                    <s:hidden name="useGlad" value="false"/>
                    <s:hidden name="formAction" />
                    <s:hidden name="dnaAnalysisDataConfiguration.segmentationService.url" id="serverProfileUrl" />
                    
                    <s:file name="mappingFile" label="Subject and Sample Mapping File" size="40"/><br>
                        <s:div id="commentCsvDiv" cssClass="inlinehelp_form_element" cssStyle="display: block;">
                            <span class="wwlbl">(csv file with 6 column format)</span>
                            <span class="wwctrl"></span>
                        </s:div>
                    <br/>
                    <s:select id="caDnaCopyUrl" name="dnaAnalysisDataConfiguration.segmentationService.url"
                            list="caDnaCopyServices" label="CaDNACopy Service URL" required="true" cssClass="editable-select"/><br>
                    <s:textfield name="dnaAnalysisDataConfiguration.changePointSignificanceLevel" label="Change Point Significance Level" /><br>
                    <s:textfield name="dnaAnalysisDataConfiguration.earlyStoppingCriterion" label="Early Stopping Criterion" /><br>
                    <s:textfield name="dnaAnalysisDataConfiguration.permutationReplicates" label="Permutation Replicates" /><br>
                    <s:textfield name="dnaAnalysisDataConfiguration.randomNumberSeed" label="Random Number Seed" /><br>
                    <s:checkbox name="genomicSource.singleDataFile" label="Multiple Samples Per Data File"
                        labelposition="left" /><br>
                        <s:div id="commentCsvDiv" cssClass="inlinehelp_form_element" cssStyle="display: block;">
                            <span class="wwlbl">(Default is 1 sample per data file)</span>
                            <span class="wwctrl"></span>
                        </s:div>
                    <tr>
                        <td></td>
                        <td><br>
                            
                            <s:submit type="button" value="Save Segmentation Data Calculation Configuration" 
                                align="center" theme="simple" onclick="saveDatasource();" disabled="%{genomicSource.status.value == 'Loaded'}"/>
                            <s:submit type="button" value="Cancel"
                                onclick="document.dnaAnalysisDataConfigurationForm.action = 'cancelGenomicSource.action';
                                    document.dnaAnalysisDataConfigurationForm.submit();"  theme="simple"/>
                        </td>
                    </tr><br>
                </s:form>
                </td>
            </tr>
    </table>            
    </div>    
</div>

<div class="clear"><br /></div>
