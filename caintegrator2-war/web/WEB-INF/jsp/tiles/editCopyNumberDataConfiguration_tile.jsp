<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      

    <h1 style="color: #FFFFFF; background: #263D6B; padding: 5px;">editing: <strong><s:property value="studyConfiguration.study.shortTitleText" /></strong></h1>
    
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('edit_genomic_data_help')" class="help">
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
                <s:form id="copyNumberDataConfigurationForm" name="copyNumberDataConfigurationForm" action="saveCopyNumberDataConfiguration" method="post" enctype="multipart/form-data" theme="css_xhtml">
                    <s:hidden name="studyConfiguration.id" />
                    <s:hidden name="genomicSource.id" />
                    <s:hidden name="useGlad" value="false"/>
                    <s:hidden name="formAction" />
                    <s:file name="copyNumberMappingFile" label="Subject and Sample to Copy Number Mapping File" size="40"/><br>
                    <s:select id="caDnaCopyUrl" name="caDnaCopyUrl"
                            list="caDnaCopyServices" label="CaDNACopy Service URL" required="true" /><br>
                    <s:textfield name="copyNumberDataConfiguration.changePointSignificanceLevel" label="Change Point Significance Level" /><br>
                    <s:textfield name="copyNumberDataConfiguration.earlyStoppingCriterion" label="Early Stopping Criterion" /><br>
                    <s:textfield name="copyNumberDataConfiguration.permutationReplicates" label="Permutation Replicates" /><br>
                    <s:textfield name="copyNumberDataConfiguration.randomNumberSeed" label="Random Number Seed" /><br>
                    
                    <s:div cssClass="wwgrp" cssStyle="margin: 1em 0 1em 0px;">
                        <s:div cssClass="wwlbl">&nbsp;</s:div>
                        <s:div cssClass="wwctrl" cssStyle="width: 350px;">
                            <s:submit type="button" value="Cancel" onclick="document.copyNumberDataConfigurationForm.action = 'cancelGenomicSource.action';
                                document.copyNumberDataConfigurationForm.submit();" cssStyle="float: right;"/>
                            <s:submit type="button" value="Save Segmentation Data Calculation Configuration" cssStyle="float: right;"/>
                        </s:div>
                    </s:div><br>
                </s:form>
                </td>
            </tr>
    </table>            
    </div>    
</div>

<div class="clear"><br /></div>
