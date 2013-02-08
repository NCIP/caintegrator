<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script type="text/javascript" src="/caintegrator/common/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="/caintegrator/common/js/jquery.editable-select.js"></script>
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
    });
    
    function saveDatasource() {
        if (document.getElementById("loadStatus").value == "Loaded") {
            if (confirm("You are adding a new sample mapping file."
                        + " All previous sample mappings will be deleted and re-mapped based on the new file."
                        + " All GISTIC jobs will be deleted and the study will be marked not-deployed."
                        + " Please click OK or Cancel.")) {
                document.dnaAnalysisDataConfigurationForm.submit();
            }
        } else {
            document.dnaAnalysisDataConfigurationForm.submit();
        }
    }

    // This function is called at body onload.
    function initializeJsp() {
        val1 = document.getElementsByName('dnaAnalysisDataConfiguration.useCghCall')[0].checked;//first radio button
        val2 = document.getElementsByName('dnaAnalysisDataConfiguration.useCghCall')[1].checked;

        if (val1 == true) {
            selectService('DNAcopy');
        } else if (val2 == true) {
            selectService('CGHcall');
        } else {
        	selectService('DNAcopy');
        } 
    }

    
    function selectService(service) {
        if (service == "CGHcall") {
            document.getElementById('dnaCopyInputParams').style.display = 'none';
            document.getElementById('cghCallInputParams').style.display = 'block';
        } else {
            document.getElementById('cghCallInputParams').style.display = 'none';
            document.getElementById('dnaCopyInputParams').style.display = 'block';
        } 
    }    

</script>   
            
<div id="content">                      

    <h1 style="color: #FFFFFF; background: #263D6B; padding: 5px;">editing: <strong><s:property value="studyConfiguration.study.shortTitleText" /></strong></h1>
    
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openWikiHelp('CIDnAg', '2-CreatingaNewStudy-ConfiguringCopyNumberData')" class="help">
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
                    action="saveDnaAnalysisDataConfiguration" method="post" enctype="multipart/form-data" theme="css_xhtml">
                    <s:token />
                    <s:hidden name="studyConfiguration.id" />
                    <s:hidden name="genomicSource.id" />
                    <s:hidden name="useGlad" value="false"/>
                    <s:hidden name="formAction" />
                    <s:hidden name="dnaAnalysisDataConfiguration.segmentationService.url" id="serverProfileUrl" />
                    <s:hidden name="genomicSource.status.value" id="loadStatus"/>
                    
                <s:if test="%{studyConfiguration.hasLoadedClinicalDataSource()}">
                    <s:textfield label="caArray Server Hostname" name="genomicSource.serverProfile.hostname" readonly="true" cssClass="readonly" size="35" />
                    <s:textfield label="caArray Experiment Id" name="genomicSource.experimentIdentifier" readonly="true" cssClass="readonly" size="35" />
                    <s:textfield label="Loading Type" name="genomicSource.loadingTypeString" readonly="true" cssClass="readonly" size="35" />
                    <s:file name="mappingFile" label="Subject and Sample Mapping File" size="40"/><br>
                        <s:div id="commentCsvDiv" cssClass="inlinehelp_form_element" cssStyle="display: block;">
                            <span class="wwlbl">(csv file with 2 columns for using parsed data or 6 columns otherwise)</span>
                            <span class="wwctrl"></span>
                        </s:div>
                    <br/>
                    
                    <s:div cssStyle="padding: 1em 0 0 0;">
                        <s:div cssClass="wwlbl"><label class="label">Bioconductor Service Type:&nbsp;</label></s:div>
                        <s:div id="bioconductorSelector">
                            <s:radio theme="css_xhtml" name="dnaAnalysisDataConfiguration.useCghCall" list="#{false:'Use caDNACopy Service'}" onclick="selectService('DNAcopy');" />
                            <s:radio theme="css_xhtml" name="dnaAnalysisDataConfiguration.useCghCall" list="#{true:'Use caCGHCall Service'}" onclick="selectService('CGHcall');" />
                        </s:div>
                    </s:div>
                    <br/>
                    <s:div id="cghCallInputParams" cssStyle="%{defaultBioconductorSelectCss};">
                    <s:select id="caCghCallUrl" name="caCghCallUrl" cssStyle="width=531px;"
                        list="caCghCallServices" label="CaCGHCall Service URL" required="true" cssClass="editable-select"/><br>
                    <s:div cssClass="wwlbl"><label class="label">Call Level:&nbsp;</label></s:div>
                    <s:div>
                        <s:radio theme="css_xhtml" name="numberLevelCall" list="#{3:'Use 3 level calls'}"  />
                        <s:radio theme="css_xhtml" name="numberLevelCall" list="#{4:'Use 4 level calls'}"  />
                    </s:div>
                    </s:div>
                    <s:div id="dnaCopyInputParams">
                    <s:select id="caDnaCopyUrl" name="caDnaCopyUrl" cssStyle="width=531px"
                        list="caDnaCopyServices" label="CaDNACopy Service URL" required="true" cssClass="editable-select"/><br>
                    </s:div>  
                    <s:textfield name="dnaAnalysisDataConfiguration.changePointSignificanceLevel" label="Change Point Significance Level" /><br>
                    <s:textfield name="dnaAnalysisDataConfiguration.earlyStoppingCriterion" label="Early Stopping Criterion" /><br>
                    <s:textfield name="dnaAnalysisDataConfiguration.permutationReplicates" label="Permutation Replicates" /><br>
                    <s:textfield name="dnaAnalysisDataConfiguration.randomNumberSeed" label="Random Number Seed" required="true" /><br>
                </s:if>
                    <tr>
                        <td></td>
                        <td><br>
                            <button type="button" onclick="saveDatasource();">Save Segmentation Data Calculation Configuration</button>
                            <s:submit type="button" value="Cancel"
                                onclick="document.dnaAnalysisDataConfigurationForm.action = 'cancelDnaAnalysisDataConfiguration.action';
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
