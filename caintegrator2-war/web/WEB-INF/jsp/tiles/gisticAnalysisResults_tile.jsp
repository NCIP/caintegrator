<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<style type="text/css">
    div#gisticResultsDiv {height: 400px; overflow: scroll;}
</style>
            
<div id="content">

<!--Search Results-->
<s:div theme="xhtml">
    <h2>GISTIC Analysis Results for:
        <s:property value="gisticJobResult.jobName"/>
    </h2>
    <div class="tableheader"><label>Results per Page:</label>
        <s:form id="gisticForm" name="gisticForm" action="gisticAnalysisResults" theme="simple">
            <s:select name="pageSize" list="{'10', '20', '50', '100'}" />
            <s:a href="#" onclick="document.gisticForm.submit();"><span class="btn_img">Apply</span></s:a>
        </s:form>
    </div>

    <div id="gisticResultsDiv">
        <table class="data">
            <s:set name="pageSizeVar" id="pageSizeVar" value="%{gisticJobResult.pageSize}" />
            <display:table name="gisticJobResult.rows" uid="jobResultRow" id="jobResultRow" pagesize="${pageSizeVar}"
                sort="list" class="data" requestURI="gisticAnalysisResults.action#" export="true">
                <display:setProperty name="paging.banner.placement" value="both" />
                <display:setProperty name="export.excel" value="false" />
                <display:setProperty name="export.xml" value="false" />
                <display:setProperty name="export.pdf" value="false" />
                <display:setProperty name="export.csv.filename" value="gisticAnalysisResults.csv" />
                <display:setProperty name="export.csv.include_header" value="true" />

                <display:column title="Amplification;" sortable="true" >
                    <s:property value="%{gisticJobResult.rows.get(#attr.jobResultRow_rowNum - 1).amplification}" />
                </display:column>
                <display:column title="Amplitude" sortable="true"
                    decorator="gov.nih.nci.caintegrator2.application.analysis.DoubleFormatColumnDecorator">
                    <s:property value="%{gisticJobResult.rows.get(#attr.jobResultRow_rowNum - 1).amplitude}" />
                </display:column>
                <display:column title="Score" sortable="true"
                    decorator="gov.nih.nci.caintegrator2.application.analysis.DoubleFormatColumnDecorator">
                    <s:property value="%{gisticJobResult.rows.get(#attr.jobResultRow_rowNum - 1).score}" />
                </display:column>
                <display:column title="qValue" sortable="true"
                    decorator="gov.nih.nci.caintegrator2.application.analysis.DoubleFormatColumnDecorator">
                    <s:property value="%{gisticJobResult.rows.get(#attr.jobResultRow_rowNum - 1).qValue}" />
                </display:column>
                <display:column title="Frequency" sortable="true"
                    decorator="gov.nih.nci.caintegrator2.application.analysis.DoubleFormatColumnDecorator">
                    <s:property value="%{gisticJobResult.rows.get(#attr.jobResultRow_rowNum - 1).frequency}" />
                </display:column>
            </display:table>
    </table>
    </div>
</s:div> <!--/Search Results-->
</div>

<div class="clear"><br />
</div>
