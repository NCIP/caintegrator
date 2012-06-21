<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
            
<div id="content">

        <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openWikiHelp('ngPTAg', '1-GettingStartedwithcaIntegrator-StudyLog')" class="help">
   &nbsp;</a>
    </div>

    <!--/Page Help-->          
    
    <h1><s:property value="#subTitleText" /></h1>
    
    <div class="form_wrapper_outer">
    <s:if test="%{displayableLogEntries.empty}">
        There are no log entries for this study.
    </s:if>
    <s:else>
        <table class="form_wrapper_table">
            <tr>
                <td colspan="2" style="padding: 5px;">

                <display:table name="displayableLogEntries" uid="logEntries" id="logEntries"
                sort="list" class="data" requestURI="viewStudyLog.action#" export="true">
	                <display:setProperty name="paging.banner.placement" value="both" />
	                <display:setProperty name="export.excel" value="false" />
	                <display:setProperty name="export.xml" value="false" />
	                <display:setProperty name="export.csv.filename" value="StudyLog.csv" />
	                <display:setProperty name="export.csv.include_header" value="true" />
                    <display:column title="Date" property="logEntry.displayableLogDate" sortable="true"/>
                    <display:column title="Description" property="logEntry.description" sortable="true"/>
                </display:table>
                </td>
            </tr>
        </table>
    </s:else>
    </div>
</div>

<div class="clear"><br /></div>
