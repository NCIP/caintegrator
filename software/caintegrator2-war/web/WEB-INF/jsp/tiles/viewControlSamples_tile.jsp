<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="content">

    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('control_samples_help')" class="help">
   &nbsp;</a>
    </div>

    <!--/Page Help-->  
    
    <h1><s:property value="#subTitleText" /></h1>
    <p>View control samples sets as defined by the study manager. Shown are the sample names which comprise each set and the subject identifier to which each sample is mapped.</p>
    <div class="form_wrapper_outer">
    <h4>Experiment: <s:property value="genomicSource.experimentIdentifier" /></h4>
    <s:iterator value="genomicSource.controlSampleSetCollection" status="iterator">
        <table class="form_wrapper_table">
                <tr>
                <td style="padding: 5px 0 0 0;">
					<div class="wwgrp" style="padding: 5px 0 0 0;">
					<div class="wwlbl" style="width: 9em; text-align: left;padding: 0 5px 0 5px;"><label class="label" for="annotationGroupForm_groupName" style="font-weight: normal;">Control Set Name:</label></div>
					<div class="wwctrl" style="font-weight: bold;"><s:property value="name" /></div>
					</div>
				</td>
                </tr>
                
                <tr>
                <td style="padding: 0 0 5px 0;"></td>
                </tr>
                <tr>
                    <td colspan="2" style="padding: 5px;">                         
                        <table class="data">
                            <tr>
                                <th>Sample Name</th>
                                <th>Subject Identifier</th>
                            </tr>
                            <s:iterator value="samples">
                                <tr>
                                    <td><s:property value="name"/></td>
                                    <td><s:property value="sampleAcquisition.assignment.identifier"/></td>
                                </tr>
                            </s:iterator>
                        </table>
                    </td>
                </tr>
        </table>
    </s:iterator>
    </div>
</div>

<div class="clear"><br /></div>
