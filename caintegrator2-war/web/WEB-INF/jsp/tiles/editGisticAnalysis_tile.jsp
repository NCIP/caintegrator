<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="content" >


<!--Page Help-->

<div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('edit_gistic_analysis_help')" class="help">
&nbsp;</a>
</div>

<!--/Page Help-->

<script language="javascript">

    function deleteGistic () {
        if (confirm('This gistic analysis will be permanently deleted. (Not yet implemented)')) {
            submitForm("delete");
        }
    }
    
    function submitForm(selectAction) {
        document.editGisticAnalysisForm.selectedAction.value = selectAction;
        document.editGisticAnalysisForm.submit();
    }
    
</script>

    <h1><s:property value="#subTitleText" /></h1>
    
    <p>Click <strong>Rename or Delete</strong> to perform editing on this GISTIC Analysis.</p>
    <div class="form_wrapper_outer">
    <table class="form_wrapper_table">
            <tr>
                <td colspan="2" style="padding: 5px;">    
                    <font color="green"> <s:actionmessage /> </font>
                    <s:actionerror/>
                        <s:form id="editGisticAnalysisForm" name="editGisticAnalysisForm" action="editGisticAnalysis"
                            method="post" enctype="multipart/form-data" theme="css_xhtml">
                            <s:hidden name="selectedAction" />
                            <s:hidden name="gisticAnalysis.id" />
                            <s:textfield name="gisticAnalysis.name" label="Gistic Analysis Name"
                                theme="css_xhtml" size="50" /><br>
                            <div class="wwgrp" >
                                <div class="wwlbl" >
                                    <label class="label" >GISTIC URL used:</label>
                                </div> 
                                <div class="wwctrl">
                                    <s:property value="gisticAnalysis.url" /><br>
                                </div> 
                            </div> <br/>
                            <div class="wwgrp" >
                                <div class="wwlbl" >
                                    <label class="label" >Amplifications Threshold:</label>
                                </div> 
                                <div class="wwctrl">
                                    <s:property value="gisticAnalysis.amplificationsThreshold" /><br>
                                </div> 
                            </div> <br/>
                            <div class="wwgrp" >
                                <div class="wwlbl" >
                                    <label class="label" >Deletions Threshold:</label>
                                </div> 
                                <div class="wwctrl">
                                    <s:property value="gisticAnalysis.deletionsThreshold" /><br>
                                </div> 
                            </div> <br/>
                            <div class="wwgrp" >
                                <div class="wwlbl" >
                                    <label class="label" >Genome Build:</label>
                                </div> 
                                <div class="wwctrl">
                                    <s:property value="gisticAnalysis.genomeBuildInformation" /><br>
                                </div> 
                            </div> <br/>
                            <div class="wwgrp" >
                                <div class="wwlbl" >
                                    <label class="label" >Join Segment Size:</label>
                                </div> 
                                <div class="wwctrl">
                                    <s:property value="gisticAnalysis.joinSegmentSize" /><br>
                                </div> 
                            </div> <br/>
                            <div class="wwgrp" >
                                <div class="wwlbl" >
                                    <label class="label" >Query or List Name:</label>
                                </div> 
                                <div class="wwctrl">
                                    <s:property value="gisticAnalysis.queryOrListName" /><br>
                                </div> 
                            </div> <br/>
                            <div class="wwgrp" >
                                <div class="wwlbl" >
                                    <label class="label" >QV Threshold:</label>
                                </div> 
                                <div class="wwctrl">
                                    <s:property value="gisticAnalysis.qvThreshold" /><br>
                                </div> 
                            </div> <br/>
                            <s:select label="Samples used in analysis calculation" list="gisticAnalysis.samplesUsedForCalculation" 
                            name=""
                            listValue="name"
                            disabled="true"    theme="css_xhtml" multiple="true" size="5"/><br>
                            
                            <s:select label="Amplified Genes" list="amplifiedGenes" 
                            name=""
                            listValue="symbol"
                            disabled="true"    theme="css_xhtml" multiple="true" size="5"/><br>

                            <s:select label="Deleted Genes" list="deletedGenes" 
                            name=""
                            listValue="symbol"
                            disabled="true"    theme="css_xhtml" multiple="true" size="5"/><br>
                            <!--Buttons-->

                            <div class="actionsrow"><del class="btnwrapper"><ul class="btnrow">
                                <li><s:a href="#" cssClass="btn" onclick="submitForm('save');">
                                    <span class="btn_img"><span class="save">Rename</span></span>
                                </s:a></li>
                                <li><s:a href="#" cssClass="btn" onclick="deleteGistic();">
                                    <span class="btn_img"><span class="delete">Delete</span></span>
                                </s:a></li>
                                <li><s:a href="#" cssClass="btn" onclick="submitForm('cancel');">
                                    <span class="btn_img"><span class="cancel">Cancel</span></span>
                                </s:a></li>
                            </ul></del></div>

                            <!--Buttons-->
                        </s:form>
                </td>
            </tr>
    </table>    
    </div>
</div>

<div class="clear"><br />
</div>