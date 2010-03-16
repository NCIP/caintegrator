<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<style type="text/css">
    div#queryResultsDiv{height:400px; overflow:scroll;}
</style>

<!--Search Results-->

<s:hidden name="genomicSortingType" />
<s:hidden name="genomicSortingIndex" />
            
<s:div theme="xhtml">
    <h2>Query Results for: 
        <s:if test="query != null && query.name != null && query.name.length() > 0">
            <s:property value="query.name" />
        </s:if>
        <s:else>
            Unsaved Query
        </s:else>        
    </h2>

    <s:hidden name="subjectListName" value="" />
    <s:hidden name="subjectListDescription" value="" />
    
    <div class="tableheader" style="white-space: nowrap; position: relative; width: 25em;">
        <div style="float: left; position: relative; width: 100px; margin-top: 0.3em;">
            <s:if test='%{query.resultType.value.equals("genomic")}'></s:if>
            <s:else>
                <label>Results per Page:</label>
                <s:select name="pageSize" list="{'10', '20', '50', '100'}" />
                <s:a href="#" onclick="submitForm('updateResultsPerPage')">
                    <span class="btn_img">Apply</span>
                </s:a>
            </s:else>
        </div>
        <div class="tabhelp" style="white-space:nowrap; position: relative; margin-top: 0.4em; margin-right: 0.5em;"><a href="javascript:openHelpWindowWithNavigation('query_results_help')" class="help">
   &nbsp;</a>
        </div>
    </div>
    <s:if test="%{!query.geneSymbolsNotFound.isEmpty()}" >
        <div style="color: red;"> Warning: The following gene(s) were in the criterion but not found: 
        <s:iterator value="query.geneSymbolsNotFound" status="geneSymbolStatus">
            <b>
            <s:property /><s:if test="!#geneSymbolStatus.last">,</s:if>
            </b>
        </s:iterator>
        </div>
    </s:if>
    <div id="queryResultsDiv" >
        <s:if test='%{query.resultType.value.equals("genomic")}'>
        <s:set name="genomicDataNeedsHighlighting" value="genomicDataQueryResult.hasCriterionSpecifiedReporterValues" />
        <table class="data">
            <s:if test='%{query.orientation.value.equals("subjectsAsColumns")}'>
                <tr>
                    <td />
                    <s:if test='%{queryForm.resultConfiguration.reporterType.equals("geneExpressionProbeSet")}'>
                        <td />
                    </s:if>
                    <th>Subject ID</th>
                    <s:iterator value="genomicDataQueryResult.columnCollection" status="columnIterator">
                        <td>
                            <s:set name="columnIteratorIndex" value="%{#columnIterator.index}"/>
                            <a href="javascript:sortGenomicResult('row', ${columnIteratorIndex})"
                                 style="border-bottom: 1px ridge #000000; color: #333333">
                                <b><s:property value="sampleAcquisition.assignment.identifier" /></b>
                            </a>
                        </td>
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
                    <th>Gene</th>
                    <s:if test='%{queryForm.resultConfiguration.reporterType.equals("geneExpressionProbeSet")}'>
                        <th>Reporter ID</th>
                    </s:if>
                </tr>
                <s:if test="genomicDataQueryResult.filteredRowCollection.isEmpty()">
                    Nothing found to display.
                </s:if>
                <s:elseif test="genomicDataQueryResult.filteredRowCollection.size() > 2000">
                    Found more than 2000 results, please restrict your search or use the export option.
                </s:elseif>
                <s:else>
                    <s:iterator value="genomicDataQueryResult.filteredRowCollection" status="status">
                        <s:if test="#status.odd == true">
                            <tr class="odd">
                        </s:if>
                        <s:else>
                            <tr class="even">
                        </s:else>
                        <td><a href="${reporter.geneSymbolsCgapUrl}" target="cai2_CGAP" title="Click to find this Gene Symbol in the Cancer Genome Anatomy Project (CGAP)">
                            <b><s:property value="reporter.geneSymbols" /></b>
                        </a></td>
                        <s:if test='%{queryForm.resultConfiguration.reporterType.equals("geneExpressionProbeSet")}'>
                            <td><b><s:property value="reporter.name" /></b></td>
                        </s:if>
                        <td /><s:iterator value="values">
                            <s:if test="#genomicDataNeedsHighlighting && meetsCriterion">
                                <td nowrap="nowrap" bgcolor="<s:property value='highlightColor'/>"><b><font color="white"><s:property value="value" /></font></b></td>
                            </s:if>
                            <s:else>
                                <td nowrap="nowrap"><s:property value="value" /></td>
                            </s:else>
                        </s:iterator>
                    </s:iterator>
                </s:else>
            </s:if>
            <s:elseif test="genomicDataQueryResult.filteredRowCollection.size() > 2000">
                    Found more than 2000 results, please restrict your search or use the export option.
            </s:elseif>
            <s:else>
                <tr>
                    <td/><td/>
                    <th>Gene</th>
                    <s:iterator value="genomicDataQueryResult.filteredRowCollection" status="status">
                        <td>
                            <s:if test='%{queryForm.resultConfiguration.reporterType.equals("geneExpressionProbeSet")}'>
                                <b><s:property value="reporter.geneSymbols" /></b>&nbsp;
                                <a href="${reporter.geneSymbolsCgapUrl}" target="cai2_CGAP" title="Click to find this Gene Symbol in the Cancer Genome Anatomy Project (CGAP)">
                                    <img src="/caintegrator2/images/ico_info.gif" border="none"/>
                                </a>
                            </s:if>
                            <s:else>
                                <s:set name="nonFilterIndex" value="%{nonFilterIndex}"/>
                                <a href="javascript:sortGenomicResult('column', ${nonFilterIndex})"
                                    style="border-bottom: 1px ridge #000000; color: #333333">
                                    <b><s:property value="reporter.geneSymbols" /></b>&nbsp;
                                </a>
                                <a href="${reporter.geneSymbolsCgapUrl}" target="cai2_CGAP" title="Click to find this Gene Symbol in the Cancer Genome Anatomy Project (CGAP)">
                                    <b>Info</b>
                                </a>
                            </s:else>
                        </td>
                    </s:iterator>
                </tr>
                <s:if test='%{queryForm.resultConfiguration.reporterType.equals("geneExpressionProbeSet")}'>
                    <tr>
                        <td/><td/>
                        <th>Reporter ID</th>
                        <s:iterator value="genomicDataQueryResult.filteredRowCollection" status="status">
                            <th>
                                <s:set name="nonFilterIndex" value="%{nonFilterIndex}"/>
                                <a href="javascript:sortGenomicResult('column', ${nonFilterIndex})">
                                    <b><s:property value="reporter.name" /></b>
                                </a>
                            </th>
                        </s:iterator>
                    </tr>
                </s:if>
                <tr>
                    <th>Subject ID</th>
                    <th>Sample ID</th>
                </tr>
                <s:if test="genomicDataQueryResult.columnCollection.isEmpty()">
                    Nothing found to display.
                </s:if>
                <s:elseif test="genomicDataQueryResult.columnCollection.size() > 2000">
                    Found more than 2000 results, please restrict your search or use the export option.
                </s:elseif>
                <s:else>
                    <s:iterator value="genomicDataQueryResult.columnCollection" status="status">
                        <s:if test="#status.odd == true">
                            <tr class="odd">
                        </s:if>
                        <s:else>
                            <tr class="even">
                        </s:else>
                        <td><b><s:property value="sampleAcquisition.assignment.identifier" /></b></td>
                        <td><b><s:property value="sampleAcquisition.sample.name" /></b></td>
                        <td/>
                        <s:iterator value="values">
                            <s:if test="#genomicDataNeedsHighlighting && meetsCriterion">
                                <td nowrap="nowrap" bgcolor="<s:property value='highlightColor'/>"><b><font color="white">
                                    <s:property value="value" /></font></b>
                                </td>
                            </s:if>
                            <s:else>
                                <td nowrap="nowrap">
                                    <s:property value="value" />
                                </td>
                            </s:else>
                        </s:iterator>
                    </s:iterator>
                </s:else>
            </s:else>
        </table>    
        <br>
        <s:if test="!genomicDataQueryResult.columnCollection.isEmpty() &&
                    !genomicDataQueryResult.filteredRowCollection.isEmpty()">
            <div class="exportlinks">
                Export options:
                <s:a href="#" onclick="submitForm('exportGenomicResults')">
                    <span class="export csv">CSV</span>
                </s:a>
            </div>
        </s:if>
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
                    title="Select Image <br> <font style='font-size:9px;text-decoration:underline;cursor:pointer;'>
                    <s:a href='#' cssStyle='text-decoration: underline;' onclick='selectAll()'>
                    All</s:a>&nbsp;|&nbsp;<s:a href='#' onclick='selectNone()'>None</s:a> </font>" 
                    media="html" sortable="false">
                    <s:if test="%{queryResult.rows.get(#attr.queryResultRows_rowNum - 1).imagingRow}">
                        <s:checkbox theme="simple" name="queryResult.rows[%{#attr.queryResultRows_rowNum - 1}].checkedRow"/>
                    </s:if>
                </display:column>
                <s:if test="queryResult.hasSubjects">
                    <display:column 
                        title="Select Subject<br> <font style='font-size:9px;text-decoration:underline;cursor:pointer;'>
                        <s:a href='#' cssStyle='text-decoration: underline;' onclick='selectAllSubject()'>
                        All</s:a>&nbsp;|&nbsp;<s:a href='#' onclick='selectNoneSubject()'>None</s:a> </font>" 
                        media="html" sortable="false">
                        <s:checkbox theme="simple" name="queryResult.rows[%{#attr.queryResultRows_rowNum - 1}].selectedSubject"/>
                    </display:column>
                    <display:column title="Subject Identifier" sortable="true" comparator="gov.nih.nci.caintegrator2.web.action.query.NumericColumnDisplayTagComparator">
                        <s:property
                            value="%{queryResult.rows.get(#attr.queryResultRows_rowNum - 1).subjectAssignment.identifier}" />
                    </display:column>
                </s:if>
                <s:if test="queryResult.hasImageSeries">
                    <display:column title="Image Series Identifier" sortable="true">
                        <s:if test="%{queryResult.rows.get(#attr.queryResultRows_rowNum - 1).imageSeries != null}">
                            <s:property
                                value="%{queryResult.rows.get(#attr.queryResultRows_rowNum - 1).imageSeries.identifier}" />
                        </s:if>
                    </display:column>
                    <display:column title="View in NBIA" sortable="false" media="html">
                        <s:if test="%{queryResult.rows.get(#attr.queryResultRows_rowNum - 1).imageSeries != null}">
                            <a href='<s:property value="%{queryResult.rows.get(#attr.queryResultRows_rowNum - 1).nciaLink}" escape="false"/>'
                                target="_"><img src="/caintegrator2/images/ico_nbia.gif" border="none" title="View Image in NBIA"/></a>
                        </s:if>
                    </display:column>
                </s:if>

                <s:iterator value="queryResult.headers" status="status" id="column">
                    <s:set id="curValue" name="curValue" value="%{queryResult.rows.get(#attr.queryResultRows_rowNum - 1).values.get(#status.count - 1)}" />
                        <s:if test="#curValue.dateType">
                            <s:set id="dateValue" name="dateValue" value="#curValue.dateValue" />
                            <display:column title="${column}" sortable="true" decorator="gov.nih.nci.caintegrator2.web.action.query.DateFormatColumnDecorator" >
                                ${dateValue}
                            </display:column>
                        </s:if>
                        <s:elseif test="#curValue.numericType">
                            <display:column title="${column}" sortable="true" comparator="gov.nih.nci.caintegrator2.web.action.query.NumericColumnDisplayTagComparator" >
                                ${curValue}
                            </display:column>
                        </s:elseif>
                        <s:else>
                            <display:column title="${column}" sortable="true" >
                                ${curValue}
                            </display:column>
                        </s:else>
                </s:iterator>
            </display:table>

        </s:else>
    </div>
    <!--Buttons-->
    <s:if test='%{query.resultType.value.equals("genomic")}'>
        <s:if test="!genomicDataQueryResult.columnCollection.isEmpty() &&
                !genomicDataQueryResult.filteredRowCollection.isEmpty()">
            <div class="actionsrow">
                <del class="btnwrapper">
                    <ul class="btnrow">
                        <li><s:a href="#" cssClass="btn" onclick="submitForm('exportGenomicResults')">
                            <span class="btn_img"><span class="add">Export To CSV</span></span>
                        </s:a></li>
                    </ul>
                </del>
            </div>
        </s:if>
    </s:if>
    <s:else>
        <s:if test="!queryResult.rows.isEmpty()">
            <div class="actionsrow">
                <del class="btnwrapper">
                    <ul class="btnrow">
                        <li><s:a href="#" cssClass="btn" onclick="openExportLink(); return false;">
                            <span class="btn_img"><span class="export">Export To CSV</span></span>
                        </s:a></li>
                        <s:if test="queryResult.hasSubjects">
                            <li><s:a href="#" cssClass="btn" onclick="showSubjectListForm(); return false;">
                                <span class="btn_img"><span class="save">Save Subject List</span></span>
                            </s:a></li>
                        </s:if>
                        <s:if test="queryForm.hasImageDataSources()">
                            <li><s:a href="#" cssClass="btn" 
                                onclick="document.manageQueryForm.target='_blank';document.manageQueryForm.selectedAction.value='forwardToNcia';document.manageQueryForm.submit();document.manageQueryForm.target='_self'">
                                <span class="btn_img"><span class="externalsite">Forward To NBIA</span></span>
                            </s:a></li>
                            <li><s:a href="#" cssClass="btn" 
                                onclick="document.manageQueryForm.target='_blank';document.manageQueryForm.selectedAction.value='retrieveDicomImages';document.manageQueryForm.submit();document.manageQueryForm.target='_self'">
                                <span class="btn_img"><span class="download">Retrieve Dicom Images</span></span>
                            </s:a></li>
                        </s:if>
                    </ul>
                </del>
           </div>
        </s:if>
    </s:else>
    <!--/Buttons-->
</s:div>
<!--/Search Results-->


<div class="clear"><br />
</div>
