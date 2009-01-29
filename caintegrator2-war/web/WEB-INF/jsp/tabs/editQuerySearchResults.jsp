<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<!--Search Results-->
<s:div theme="xhtml">
    <h2>Search Results for: <s:property value="query.name" /></h2>

    <div class="tableheader">
        <label>Results per Page:</label>
        <s:select name="pageSize" list="{'10', '20', '50', '100'}" />
        <s:a href="#" onclick="document.manageQueryForm.selectedAction.value='updateResultsPerPage';document.manageQueryForm.submit();">
            <span class="btn_img">Apply</span>
        </s:a>
    </div>

    <br>
    <table class="data">

        <s:if test='%{query.resultType.equals("genomic")}'>
            <tr>
                <td />
                <td />
                <th>Patient ID</th>
                <s:iterator value="genomicDataQueryResult.columnCollection">
                    <td><b><s:property value="sampleAcquisition.assignment.identifier" /></b></td>
                </s:iterator>
            </tr>
            <tr>
                <td />
                <td />
                <th>Sample ID</th>
                <s:iterator value="genomicDataQueryResult.columnCollection">
                    <td><b><s:property value="sampleAcquisition.sample.name" /></b></td>
                </s:iterator>
            </tr>
            <tr />
            <tr>
                <th>Gene Name</th>
                <th>Probe Set</th>
            </tr>
            <s:iterator value="genomicDataQueryResult.rowCollection" status="status">
                <s:if test="#status.odd == true">
                    <tr class="odd">
                </s:if>
                <s:else>
                    <tr class="even">
                </s:else>
                <td><b><s:property value="reporter.gene.symbol" /></b></td>
                <td><b><s:property value="reporter.name" /></b></td>
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
                    <display:column title="${column}" sortable="true">
                        <s:property
                            value="%{queryResult.rows.get(#attr.queryResultRows_rowNum - 1).values.get(#status.count - 1)}" />
                    </display:column>
                </s:iterator>
            </display:table>

        </s:else>
    </table>

    <!--Buttons-->

    <div class="actionsrow">
    <del class="btnwrapper">
    <ul class="btnrow">
        <s:if test="!queryForm.resultConfiguration.imageSeriesColumns.isEmpty()">
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
