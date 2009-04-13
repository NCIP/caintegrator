<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<style type="text/css">
    div#cmsResultsDiv {height: 400px; overflow: scroll;}
</style>
            
<div id="content">

<!--Search Results-->
<s:div theme="xhtml">
    <h2>Comparative Marker Selection Analysis Results for:
        <s:property value="cmsJobResult.jobName"/>
    </h2>
    <div class="tableheader"><label>Results per Page:</label>
        <s:form id="cmsForm" name="cmsForm" action="comparativeMarkerSelectionAnalysisResults" theme="simple">
            <s:select name="pageSize" list="{'10', '20', '50', '100'}" />
            <s:a href="#" onclick="document.cmsForm.submit();"><span class="btn_img">Apply</span></s:a>
        </s:form>
    </div>

    <div id="cmsResultsDiv">
        <table class="data">
            <s:set name="pageSizeVar" id="pageSizeVar" value="%{cmsJobResult.pageSize}" />
            <display:table name="cmsJobResult.rows" uid="jobResultRow" id="jobResultRow" pagesize="${pageSizeVar}"
                sort="list" class="data" requestURI="comparativeMarkerSelectionAnalysisResults.action#" export="true">
                <display:setProperty name="paging.banner.placement" value="both" />
                <display:setProperty name="export.excel" value="false" />
                <display:setProperty name="export.xml" value="false" />
                <display:setProperty name="export.pdf" value="false" />
                <display:setProperty name="export.csv.filename" value="ComparativeMarkerSelectionAnalysisResults.csv" />
                <display:setProperty name="export.csv.include_header" value="true" />

                <display:column title="Rank" sortable="true" >
                    <s:property value="%{cmsJobResult.rows.get(#attr.jobResultRow_rowNum - 1).rank}" />
                </display:column>
                <display:column title="Feature" sortable="true" >
                    <s:property value="%{cmsJobResult.rows.get(#attr.jobResultRow_rowNum - 1).feature}" />
                </display:column>
                <display:column title="Description" sortable="true" >
                    <s:property value="%{job.results.get(#attr.jobResultRow_rowNum - 1).description}" />
                </display:column>
                <display:column title="Score" sortable="true" >
                    <s:property value="%{job.results.get(#attr.jobResultRow_rowNum - 1).score}" />
                </display:column>
                <display:column title="Feature P" sortable="true" >
                    <s:property value="%{cmsJobResult.rows.get(#attr.jobResultRow_rowNum - 1).featureP}" />
                </display:column>
                <display:column title="Feature P Low" sortable="true" >
                    <s:property value="%{cmsJobResult.rows.get(#attr.jobResultRow_rowNum - 1).featurePLow}" />
                </display:column>
                <display:column title="Feature P High" sortable="true" >
                    <s:property value="%{cmsJobResult.rows.get(#attr.jobResultRow_rowNum - 1).featurePHigh}" />
                </display:column>
                <display:column title="FDR" sortable="true" >
                    <s:property value="%{cmsJobResult.rows.get(#attr.jobResultRow_rowNum - 1).fdr}" />
                </display:column>
                <display:column title="Q Value" sortable="true" >
                    <s:property value="%{cmsJobResult.rows.get(#attr.jobResultRow_rowNum - 1).qvalue}" />
                </display:column>
                <display:column title="Bonferroni" sortable="true" >
                    <s:property value="%{cmsJobResult.rows.get(#attr.jobResultRow_rowNum - 1).bonferroni}" />
                </display:column>
                <display:column title="Max T" sortable="true" >
                    <s:property value="%{cmsJobResult.rows.get(#attr.jobResultRow_rowNum - 1).maxT}" />
                </display:column>
                <display:column title="FWER" sortable="true" >
                    <s:property value="%{cmsJobResult.rows.get(#attr.jobResultRow_rowNum - 1).fwer}" />
                </display:column>
                <display:column title="Fold Change" sortable="true" >
                    <s:property value="%{cmsJobResult.rows.get(#attr.jobResultRow_rowNum - 1).foldChange}" />
                </display:column>
                <display:column title="class0Mean" sortable="true" >
                    <s:property value="%{cmsJobResult.rows.get(#attr.jobResultRow_rowNum - 1).class0Mean}" />
                </display:column>
                <display:column title="Class0 Std" sortable="true" >
                    <s:property value="%{cmsJobResult.rows.get(#attr.jobResultRow_rowNum - 1).class0Std}" />
                </display:column>
                <display:column title="Class1 Mean" sortable="true" >
                    <s:property value="%{cmsJobResult.rows.get(#attr.jobResultRow_rowNum - 1).class1Mean}" />
                </display:column>
                <display:column title="Class1 Std" sortable="true" >
                    <s:property value="%{cmsJobResult.rows.get(#attr.jobResultRow_rowNum - 1).class1Std}" />
                </display:column>
                <display:column title="K" sortable="true" >
                    <s:property value="%{cmsJobResult.rows.get(#attr.jobResultRow_rowNum - 1).k}" />
                </display:column>
            </display:table>
    </table>
    </div>
</s:div> <!--/Search Results-->
</div>

<div class="clear"><br />
</div>
