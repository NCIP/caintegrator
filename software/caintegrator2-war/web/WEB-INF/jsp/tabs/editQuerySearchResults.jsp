<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<style type="text/css">
    div#queryResultsDiv{height:400px; overflow:scroll;}
</style>

<!--Search Results-->
<s:div theme="xhtml">
    <h2>Query Results for: 
        <s:if test="query != null && query.name != null && query.name.length() > 0">
            <s:property value="query.name" />
        </s:if>
        <s:else>
            Unsaved Query
        </s:else>        
    </h2>

    <div class="tableheader">
        <label>Results per Page:</label>
        <s:select name="pageSize" list="{'10', '20', '50', '100'}" />
        <s:a href="#" onclick="submitForm('updateResultsPerPage')">
            <span class="btn_img">Apply</span>
        </s:a>
    </div>

    <div id="queryResultsDiv" >
    <table class="data">

        <s:if test='%{query.resultType.value.equals("genomic")}'>
            <tr>
                <td />
                <s:if test='%{queryForm.resultConfiguration.reporterType.equals("geneExpressionProbeSet")}'>
                    <td />
                </s:if>
                <th>Patient ID</th>
                <s:iterator value="genomicDataQueryResult.columnCollection">
                    <td><b><s:property value="sampleAcquisition.assignment.identifier" /></b></td>
                </s:iterator>
            </tr>
            <tr>
                <td />
                <s:if test='%{queryForm.resultConfiguration.reporterType.equals("geneExpressionProbeSet")}'>
                    <td />
                </s:if>
                <th>Sample ID</th>
                <s:iterator value="genomicDataQueryResult.columnCollection">
                    <td><b><s:property value="sampleAcquisition.sample.name" /></b></td>
                </s:iterator>
            </tr>
            <tr />
            <tr>
                <th>Gene Name</th>
                <s:if test='%{queryForm.resultConfiguration.reporterType.equals("geneExpressionProbeSet")}'>
                    <th>Probe Set</th>
                </s:if>
            </tr>
            <s:iterator value="genomicDataQueryResult.rowCollection" status="status">
                <s:if test="#status.odd == true">
                    <tr class="odd">
                </s:if>
                <s:else>
                    <tr class="even">
                </s:else>
                <td><b><s:property value="reporter.gene.symbol" /></b></td>
                <s:if test='%{queryForm.resultConfiguration.reporterType.equals("geneExpressionProbeSet")}'>
                    <td><b><s:property value="reporter.name" /></b></td>
                </s:if>
                <td /><s:iterator value="valueCollection">
                    <td><s:property value="value" /></td>
                </s:iterator>
            </s:iterator>
        </s:if>
        <s:else>
            <s:set name="pageSizeVar" id="pageSizeVar" value="%{queryResult.pageSize}" />
            <display:table name="queryResult.rows" uid="queryResultRows" id="queryResultRows" pagesize="${pageSizeVar}"
                sort="list" class="data" requestURI="manageQuery.action#" export="true">
                <display:setProperty name="paging.banner.placement" value="both" />
                <display:setProperty name="export.excel" value="false" />
                <display:setProperty name="export.xml" value="false" />
                <display:setProperty name="export.csv.filename" value="StudySearchResults.csv" />
                <display:setProperty name="export.csv.include_header" value="true" />
                <display:column 
                    title="Select <br> <font style='font-size:9px;text-decoration:underline;cursor:pointer;'><s:a href='#' cssStyle='text-decoration: underline;' onclick='selectAll()'> All</s:a> | <s:a href='#' onclick='selectNone()'>None</s:a> </font>" 
                    media="html"
                    sortable="false">
                    <s:checkbox theme="simple" name="queryResult.rows[%{#attr.queryResultRows_rowNum - 1}].checkedRow"/>
                </display:column>
                <s:if test="queryResult.hasSubjects">
                    <display:column title="Subject Identifier" sortable="true">
                        <s:property
                            value="%{queryResult.rows.get(#attr.queryResultRows_rowNum - 1).subjectAssignment.identifier}" />
                    </display:column>
                </s:if>
                <s:if test="queryResult.hasImageSeries">
                    <display:column title="Image Series Identifier" sortable="true">
                        <s:if test="%{queryResult.rows.get(#attr.queryResultRows_rowNum - 1).imageSeries != null}">
                            <s:property
                                value="%{queryResult.rows.get(#attr.queryResultRows_rowNum - 1).imageSeries.identifier}" />
                            <a
                                href='<s:property value="%{queryResult.rows.get(#attr.queryResultRows_rowNum - 1).nciaLink}" escape="false"/>'
                                target="_">View in NCIA</a>
                        </s:if>
                    </display:column>
                </s:if>

                <s:iterator value="queryResult.headers" status="status" id="column">
                    <s:set id="curValue" name="curValue" value="%{queryResult.rows.get(#attr.queryResultRows_rowNum - 1).values.get(#status.count - 1)}" />
                        <s:if test="#curValue.dateType">
                            <s:set id="dateValue" name="dateValue" value="#curValue.dateValue" />
                            <display:column title="${column}" sortable="true" decorator="gov.nih.nci.caintegrator2.application.query.DateFormatColumnDecorator" >
                                ${dateValue}
                            </display:column>
                        </s:if>
                        <s:else>
                            <display:column title="${column}" sortable="true" >
                                ${curValue}
                            </display:column>
                        </s:else>
                </s:iterator>
            </display:table>

        </s:else>
    </table>    
    </div>
    <!--Buttons-->

    <div class="actionsrow">
    <del class="btnwrapper">
    <ul class="btnrow">
        <s:if test="queryForm.hasImageDataSources()">
            <li><s:a href="#" cssClass="btn" 
                onclick="document.manageQueryForm.target='_blank';document.manageQueryForm.selectedAction.value='forwardToNcia';document.manageQueryForm.submit();document.manageQueryForm.target='_self'">
                <span class="btn_img"><span class="add">Forward To NCIA</span></span>
            </s:a></li>
            <li><s:a href="#" cssClass="btn" 
                onclick="document.manageQueryForm.target='_blank';document.manageQueryForm.selectedAction.value='retrieveDicomImages';document.manageQueryForm.submit();document.manageQueryForm.target='_self'">
                <span class="btn_img"><span class="add">Retrieve Dicom Images</span></span>
            </s:a></li>
        </s:if>
    </ul>
    </del>
    </div>

    <!--/Buttons-->
</s:div>
<!--/Search Results-->


<div class="clear"><br />
</div>
