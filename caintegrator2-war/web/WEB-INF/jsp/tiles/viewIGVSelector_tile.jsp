<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">                      

    <h1 style="color: #FFFFFF; background: #263D6B; padding: 5px;">View IGV for: <strong><s:property value="studyConfiguration.study.shortTitleText" /></strong></h1>
    
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('edit_genomic_data_help')" class="help">
   &nbsp;</a>
    </div>

    <!--/Page Help-->
    
    <h1><s:property value="#subTitleText" /></h1>
    
    <p>Enter IGV viewer parameters and click <strong>View</strong>.</p>
    <div class="form_wrapper_outer">
 
    <table class="form_wrapper_table">
            <tr>
                <th class="title" style="height: 2.5em;">Platform</th>
                <th class="alignright">&nbsp;</th>
            </tr>
            <tr>
                <td colspan="2" style="padding: 5px;">    
                <s:actionerror/>
                <s:form id="igvViewerForm" name="igvViewerForm" method="post" enctype="multipart/form-data"
                    action="viewAllIGV" theme="css_xhtml">
                    <s:hidden name="selectedAction" />
                    <s:select id="expressionPlatformName" name="expressionPlatformName" label="Gene Expression Platform" list="expressionPlatformsInStudy"/>
                    <s:select id="copyNumberPlatformName" name="copyNumberPlatformName" label="Copy Number Platform" list="copyNumberPlatformsInStudy"/>
                    <br/>
                    <s:div cssClass="wwgrp">
                        <s:div cssClass="wwlbl"></s:div>
                        <s:div cssClass="wwctrl">
                        <center>
                            <button type="button" onclick="document.igvViewerForm.selectedAction.value = 'cancel';
                                document.igvViewerForm.submit();"> Cancel </button>
                            <button type="button" onclick="document.igvViewerForm.target = '_blank';
                                document.igvViewerForm.selectedAction.value = 'viewIGV';
                                document.igvViewerForm.submit();
                                document.igvViewerForm.target = '_self'"> View </button>
                        </center>
                        </s:div>
                    </s:div>
                </s:form>
                </td>
            </tr>
    </table>            
    </div>
</div>
<div class="clear"><br /></div>
