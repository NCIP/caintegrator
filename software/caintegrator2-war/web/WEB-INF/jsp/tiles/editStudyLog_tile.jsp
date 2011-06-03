<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
            
<div id="content">

    <h1 style="color: #FFFFFF; background: #263D6B; padding: 5px;">editing: <strong><s:property value="studyConfiguration.study.shortTitleText" /></strong></h1>    

    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('view_log_help')" class="help">
   &nbsp;</a>
    </div>

    <!--/Page Help-->          
    
    <h1><s:property value="#subTitleText" /></h1>
    <s:if test="%{readOnly}">
        <p>Viewing the study log.</p>
    </s:if>
    <s:else>
        <p>Update the descriptions and click <strong>Save</strong>.  Log descriptions can be viewed by the Study Investigator.</p>
    </s:else>
    <div class="form_wrapper_outer">

    <table class="form_wrapper_table">

            <tr>
                <td colspan="2" style="padding: 5px;">

                <s:form id="editStudyLogForm" name="editStudyLogForm" 
                    action="saveStudyLog" method="post" enctype="multipart/form-data">
                <s:hidden name="studyConfiguration.id" />
                <s:hidden name="readOnly" value="%{isReadOnly()}" />
                <s:set name="readOnly" id="readOnly" value="%{isReadOnly()}" />
                <display:table name="displayableLogEntries" uid="logEntryRows" id="logEntryRows"
                sort="list" class="data" requestURI="editStudyLog.action#" export="${readOnly}">
                    <display:setProperty name="paging.banner.placement" value="both" />
                    <display:setProperty name="export.excel" value="false" />
                    <display:setProperty name="export.xml" value="false" />
                    <display:setProperty name="export.csv.filename" value="StudyLog.csv" />
                    <display:setProperty name="export.csv.include_header" value="true" />
                    <display:column title="Date" property="logEntry.displayableLogDate" sortable="${readOnly}"/>
                    <display:column title="Username" property="logEntry.username" sortable="${readOnly}" />
                    <display:column title="System Log" property="logEntry.systemLogMessage" sortable="${readOnly}"/>
                    <s:if test="%{readOnly}">
                        <display:column title="Description" property="description" sortable="true"/>
                    </s:if>
                    <s:else>
                        <display:column title="Description" sortable="false">
                            <s:textarea name="displayableLogEntries[%{#attr.logEntryRows_rowNum - 1}].description" cols="40" rows="3" theme="simple"/>
                        </display:column>
                    </s:else>
                    <s:if test="%{!isReadOnly()}">
                    <display:column title="Update" sortable="false">
                        <s:checkbox theme="simple" name="displayableLogEntries[%{#attr.logEntryRows_rowNum - 1}].updateDescription"/>
                    </display:column>
                    </s:if>
                </display:table>
                
                <tr>
                    <td colspan="2">
                    <div align="center">
                    <button type="button" onclick="document.editStudyLogForm.action = 'cancelStudyLog.action';
                    	document.editStudyLogForm.submit();">Cancel</button>
                    <s:if test="%{!isReadOnly()}">
                        <button type="button" onclick="document.editStudyLogForm.submit();">Save</button>
                    </s:if>
				    </div>
				    </td>
				</tr>
                </s:form>
                
                </td>
            </tr>
        </table>
    </div>
</div>

<div class="clear"><br /></div>
