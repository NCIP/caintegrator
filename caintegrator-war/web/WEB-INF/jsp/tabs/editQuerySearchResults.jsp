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
    <s:hidden name="subjectListVisibleToOthers" />
    
    <div class="tableheader" style="white-space: nowrap; position: relative; width: 25em;">
        <div style="float: left; position: relative; width: 100px; margin-top: 0.3em;">
            <s:if test='%{query.resultType.value.equals("geneExpression")}'></s:if>
            <s:else>
                <label>Results per Page:</label>
                <s:select name="pageSize" list="{'10', '20', '50', '100', '200', '500', '1000'}" />
                <s:a href="#" onclick="submitForm('updateResultsPerPage')">
                    <span class="btn_img">Apply</span>
                </s:a>
            </s:else>
        </div>
        <div class="tabhelp" style="white-space:nowrap; position: relative; margin-top: 0.4em; margin-right: 0.5em;">
            <a href="javascript:openWikiHelp('FoDnAg', '5-ViewingQueryResults-QueryResultsOverview')" class="help">&nbsp;</a>
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
    <s:if test="%{!query.subjectIdsNotFound.isEmpty()}" >
        <div style="color: red;"> Warning: The following subject(s) were in the criterion but not found: 
        <s:iterator value="query.subjectIdsNotFound" status="subjectIdStatus">
            <b>
            <s:property /><s:if test="!#subjectIdStatus.last">,</s:if>
            </b>
        </s:iterator>
        </div>
    </s:if>
    <s:set name="subjectIdHeaderValue" value="'Subject ID'" />
    <s:if test='%{query.hasMaskedValues}' >
        <s:url id="dataDictionaryUrl" includeParams="none" action="viewDataDictionary" />
        <div style="color: red;">
            *Search results might include values that have been restricted to a range by the study manager.
              (<a href="${dataDictionaryUrl}">View restrictions</a>)
        </div>
        <s:set name="subjectIdHeaderValue" value="'Subject ID*'" />
    </s:if>
        
    <div id="queryResultsDiv">
        <s:if test='%{query.resultType.value.equals("geneExpression")}'>
            <s:set name="genomicDataNeedsHighlighting" value="genomicDataQueryResult.hasCriterionSpecifiedValues" />
            <s:if test="genomicDataQueryResult.hasHighVarianceValues" >
                <div style="color: red;">
                    **Derived from technical replicates that exceed the correlation threshold as set by the study manager.  See the study summary page for the correlation threshold.  
                </div>
            </s:if>
           <s:if test="genomicDataQueryResult.filteredRowCollection.size() > 1000">
           <div style="color: red;">
               Found more than 1000 results, please restrict your search or use the export option.
           </div>
           </s:if> 
           <s:else> 
            <table class="data">
                <s:if test='%{query.orientation.value.equals("subjectsAsColumns")}'>
                    <tr>
                        <td />
                        <s:if test='%{queryForm.resultConfiguration.reporterType.equals("geneExpressionProbeSet")}'>
                            <td />
                        </s:if>
                        <th><s:property value="%{subjectIdHeaderValue}"/></th>
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
                    <s:else>
                        <s:iterator value="genomicDataQueryResult.filteredRowCollection" status="status">
                            <s:if test="#status.odd == true">
                                <tr class="odd">
                            </s:if>
                            <s:else>
                                <tr class="even">
                            </s:else>
                            <td style="white-space:nowrap;"><b><s:property value="reporter.geneSymbols" /></b>
                                <a href="${reporter.geneSymbolsCgapUrl}" target="cai2_CGAP"
                                    title="Click to find this Gene Symbol in the Cancer Genome Anatomy Project (CGAP)">
                                    <img src="/caintegrator/images/ico_info.gif" border="none"/>
                                </a>
                            </td>
                            <s:if test='%{queryForm.resultConfiguration.reporterType.equals("geneExpressionProbeSet")}'>
                                <td><b><s:property value="reporter.name" /></b></td>
                            </s:if>
                            <td />
                            <s:iterator value="values">
                                <s:if test="#genomicDataNeedsHighlighting && meetsCriterion">
                                    <td nowrap="nowrap" bgcolor="<s:property value='highlightColor'/>">
                                        <b><font color="white"><s:property value="displayableValue" /></font></b>
                                    </td>
                                </s:if>
                                <s:else>
                                    <td nowrap="nowrap"><s:property value="displayableValue" /></td>
                                </s:else>
                            </s:iterator>
                        </s:iterator>
                    </s:else>
                </s:if>
                <s:else>
                    <tr>
                        <td/><td/>
                        <th>Gene</th>
                        <s:iterator value="genomicDataQueryResult.filteredRowCollection" status="status">
                            <td style="white-space:nowrap;">
                            <s:if test='%{queryForm.resultConfiguration.reporterType.equals("geneExpressionProbeSet")}'>
                                <b><s:property value="reporter.geneSymbols" /></b>&nbsp;
                                <a href="${reporter.geneSymbolsCgapUrl}" target="cai2_CGAP" title="Click to find this Gene Symbol in the Cancer Genome Anatomy Project (CGAP)">
                                    <img src="/caintegrator/images/ico_info.gif" border="none"/>
                                </a>
                            </s:if>
                            <s:else>
                                <s:set name="rowIteratorIndex" value="%{#status.index}"/>
                                <a href="javascript:sortGenomicResult('column', ${rowIteratorIndex})"
                                    style="border-bottom: 1px ridge #000000; color: #333333">
                                    <b><s:property value="reporter.geneSymbols" /></b>&nbsp;
                                </a>
                                <a href="${reporter.geneSymbolsCgapUrl}" target="cai2_CGAP" title="Click to find this Gene Symbol in the Cancer Genome Anatomy Project (CGAP)">
                                    <img src="/caintegrator/images/ico_info.gif" border="none"/>
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
                                    <s:set name="rowIteratorIndex" value="%{#status.index}"/>
                                    <a href="javascript:sortGenomicResult('column', ${rowIteratorIndex})">
                                        <b><s:property value="reporter.name" /></b>
                                    </a>
                                </th>
                            </s:iterator>
                        </tr>
                    </s:if>
                    <tr>
                        <th><s:property value="%{subjectIdHeaderValue}"/></th>
                        <th>Sample ID</th>
                    </tr>
                    <s:if test="genomicDataQueryResult.columnCollection.isEmpty()">
                        Nothing found to display.
                    </s:if>
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
                                        <s:property value="displayableValue" /></font></b>
                                    </td>
                                </s:if>
                                <s:else>
                                    <td nowrap="nowrap">
                                        <s:property value="displayableValue" />
                                    </td>
                                </s:else>
                            </s:iterator>
                        </s:iterator>
                    </s:else>
                </s:else>
            </table>
            </s:else>    
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
        <s:elseif test='%{query.resultType.value.equals("copyNumber")}'>
            <s:set name="pageSizeVar" id="pageSizeVar" value="%{copyNumberQueryResult.pageSize}" />
            <s:if test='%{query.orientation.value.equals("subjectsAsColumns")}'>
                <display:table name="copyNumberQueryResult.rows" uid="copyNumberQueryResultRows" id="copyNumberQueryResultRows" pagesize="${pageSizeVar}"
                    sort="list" class="data" requestURI="manageQuery.action#" export="true">
                    <display:setProperty name="paging.banner.placement" value="both" />
                    <display:setProperty name="export.excel" value="false" />
                    <display:setProperty name="export.xml" value="false" />
                    <display:setProperty name="export.csv.filename" value="StudySearchResults.csv" />
                    <display:setProperty name="export.csv.include_header" value="true" />
                    <display:setProperty name="export.decorated" value="false" />
                
                    <display:column title="Chromosome" sortable="true" comparator="gov.nih.nci.caintegrator.web.action.query.NumericColumnDisplayTagComparator">
                        <s:property  value="%{copyNumberQueryResult.rows.get(#attr.copyNumberQueryResultRows_rowNum - 1).chromosome}" />
                    </display:column>
                    <display:column title="Start Position" sortable="true" comparator="gov.nih.nci.caintegrator.web.action.query.NumericColumnDisplayTagComparator">
                        <s:property  value="%{copyNumberQueryResult.rows.get(#attr.copyNumberQueryResultRows_rowNum - 1).startPosition}" />
                    </display:column>
                    <display:column title="End Position" sortable="true" comparator="gov.nih.nci.caintegrator.web.action.query.NumericColumnDisplayTagComparator">
                        <s:property  value="%{copyNumberQueryResult.rows.get(#attr.copyNumberQueryResultRows_rowNum - 1).endPosition}" />
                    </display:column>
                    <display:column title="Genes" sortable="true" decorator="gov.nih.nci.caintegrator.web.action.query.DivFormatColumnDecorator">
                        <s:property  value="%{copyNumberQueryResult.rows.get(#attr.copyNumberQueryResultRows_rowNum - 1).genes}" />
                    </display:column>
                
                    <s:iterator value="copyNumberQueryResult.sampleHeaders" status="status" id="column">
                        <s:if test="%{query.subscription.study.hasCghCalls()}">
	                        <s:set id="curCalls" name="curCalls" value="%{copyNumberQueryResult.rows.get(#attr.copyNumberQueryResultRows_rowNum - 1).values.get(#status.count - 1).callsValue}" />
	                        <s:set id="colorCalls" name="colorCalls" value="%{copyNumberQueryResult.rows.get(#attr.copyNumberQueryResultRows_rowNum - 1).values.get(#status.count - 1).colorCalls}" />
	                        <s:if test="#colorCalls">
	                            <s:set id="highlightColorCalls" name="highlightColorCalls" value="%{copyNumberQueryResult.rows.get(#attr.copyNumberQueryResultRows_rowNum - 1).values.get(#status.count - 1).highlightColorCalls}" />
	                            <display:column title="${column}" sortable="true" comparator="gov.nih.nci.caintegrator.web.action.query.NumericColumnDisplayTagComparator" style="background-color:${highlightColorCalls};color:white">${curCalls}</display:column>
	                        </s:if>
	                        <s:else>
	                            <display:column title="${column}" sortable="true" comparator="gov.nih.nci.caintegrator.web.action.query.NumericColumnDisplayTagComparator" style="">${curCalls}</display:column>
	                        </s:else>
                        </s:if>
                        <s:else>
	                        <s:set id="curValue" name="curValue" value="%{copyNumberQueryResult.rows.get(#attr.copyNumberQueryResultRows_rowNum - 1).values.get(#status.count - 1).displayableValue}" />
	                        <s:set id="meetsCriterion" name="meetsCriterion" value="%{copyNumberQueryResult.rows.get(#attr.copyNumberQueryResultRows_rowNum - 1).values.get(#status.count - 1).meetsCriterion}" />
	                        <s:if test="#meetsCriterion">
	                            <s:set id="highlightColor" name="highlightColor" value="%{copyNumberQueryResult.rows.get(#attr.copyNumberQueryResultRows_rowNum - 1).values.get(#status.count - 1).highlightColor}" />
	                            <display:column title="${column}" sortable="true" style="background-color:${highlightColor};color:white" comparator="gov.nih.nci.caintegrator.web.action.query.NumericColumnDisplayTagComparator">${curValue}</display:column>
	                        </s:if>
	                        <s:else>
	                            <display:column title="${column}" sortable="true" style="" comparator="gov.nih.nci.caintegrator.web.action.query.NumericColumnDisplayTagComparator">${curValue}</display:column>
	                        </s:else>
                        </s:else>
                    </s:iterator>
                </display:table>
            </s:if>
            <s:else>
                <display:table name="copyNumberQueryResult.sampleRows" uid="copyNumberQueryResultRows" id="copyNumberQueryResultRows" pagesize="${pageSizeVar}"
                    sort="list" class="data" requestURI="manageQuery.action#" export="true">
                    <display:setProperty name="paging.banner.placement" value="both" />
                    <display:setProperty name="export.excel" value="false" />
                    <display:setProperty name="export.xml" value="false" />
                    <display:setProperty name="export.csv.filename" value="StudySearchResults.csv" />
                    <display:setProperty name="export.csv.include_header" value="true" />
                    <display:setProperty name="export.decorated" value="false" />
                
                    <display:column title="Subject" sortable="true">
                        <s:property value="%{copyNumberQueryResult.sampleRows.get(#attr.copyNumberQueryResultRows_rowNum - 1).subject}" />
                    </display:column>
                    <display:column title="Sample" sortable="true">
                        <s:property value="%{copyNumberQueryResult.sampleRows.get(#attr.copyNumberQueryResultRows_rowNum - 1).sample}" />
                    </display:column>
                    <display:column title="Genes" sortable="true" decorator="gov.nih.nci.caintegrator.web.action.query.DivFormatColumnDecorator">
                        <s:property value="%{copyNumberQueryResult.sampleRows.get(#attr.copyNumberQueryResultRows_rowNum - 1).genes}" />
                    </display:column>
                    <display:column title="Chromosome" sortable="true" comparator="gov.nih.nci.caintegrator.web.action.query.NumericColumnDisplayTagComparator">
                        <s:property value="%{copyNumberQueryResult.sampleRows.get(#attr.copyNumberQueryResultRows_rowNum - 1).chromosome}" />
                    </display:column>
                    <display:column title="Start Position" sortable="true" comparator="gov.nih.nci.caintegrator.web.action.query.NumericColumnDisplayTagComparator">
                        <s:property value="%{copyNumberQueryResult.sampleRows.get(#attr.copyNumberQueryResultRows_rowNum - 1).startPosition}" />
                    </display:column>
                    <display:column title="End Position" sortable="true" comparator="gov.nih.nci.caintegrator.web.action.query.NumericColumnDisplayTagComparator">
                        <s:property value="%{copyNumberQueryResult.sampleRows.get(#attr.copyNumberQueryResultRows_rowNum - 1).endPosition}" />
                    </display:column>
                    <s:if test="%{query.subscription.study.hasCghCalls()}">
                        <s:set id="curCalls" name="curCalls" value="%{copyNumberQueryResult.sampleRows.get(#attr.copyNumberQueryResultRows_rowNum - 1).value.callsValue}" />
                        <s:set id="colorCalls" name="colorCalls" value="%{copyNumberQueryResult.sampleRows.get(#attr.copyNumberQueryResultRows_rowNum - 1).value.colorCalls}" />
                        <s:if test="#colorCalls">
                            <s:set id="highlightColorCalls" name="highlightColorCalls" value="%{copyNumberQueryResult.sampleRows.get(#attr.copyNumberQueryResultRows_rowNum - 1).value.highlightColorCalls}" />
                            <display:column title="Calls" sortable="true" comparator="gov.nih.nci.caintegrator.web.action.query.NumericColumnDisplayTagComparator" style="background-color:${highlightColorCalls};color:white">${curCalls}</display:column>
                        </s:if>
                        <s:else>
                            <display:column title="Calls" sortable="true" comparator="gov.nih.nci.caintegrator.web.action.query.NumericColumnDisplayTagComparator" style="">${curCalls}</display:column>
                        </s:else>
                        <display:column title="Probability Loss" sortable="true" comparator="gov.nih.nci.caintegrator.web.action.query.NumericColumnDisplayTagComparator">
                            <s:property value="%{copyNumberQueryResult.sampleRows.get(#attr.copyNumberQueryResultRows_rowNum - 1).value.probabilityLoss}" />
                        </display:column>
                        <display:column title="Probability Normal" sortable="true" comparator="gov.nih.nci.caintegrator.web.action.query.NumericColumnDisplayTagComparator">
                            <s:property value="%{copyNumberQueryResult.sampleRows.get(#attr.copyNumberQueryResultRows_rowNum - 1).value.probabilityNormal}" />
                        </display:column>
                        <display:column title="Probability Gain" sortable="true" comparator="gov.nih.nci.caintegrator.web.action.query.NumericColumnDisplayTagComparator">
                            <s:property value="%{copyNumberQueryResult.sampleRows.get(#attr.copyNumberQueryResultRows_rowNum - 1).value.probabilityGain}" />
                        </display:column>
                        <display:column title="Probability Amplification" sortable="true" comparator="gov.nih.nci.caintegrator.web.action.query.NumericColumnDisplayTagComparator">
                            <s:property value="%{copyNumberQueryResult.sampleRows.get(#attr.copyNumberQueryResultRows_rowNum - 1).value.probabilityAmplification}" />
                        </display:column>
                    </s:if>
                    <s:set id="curValue" name="curValue" value="%{copyNumberQueryResult.sampleRows.get(#attr.copyNumberQueryResultRows_rowNum - 1).value.displayableValue}" />
                    <s:set id="meetsCriterion" name="meetsCriterion" value="%{copyNumberQueryResult.sampleRows.get(#attr.copyNumberQueryResultRows_rowNum - 1).value.meetsCriterion}" />
                        <s:if test="#meetsCriterion">
                            <s:set id="highlightColor" name="highlightColor" value="%{copyNumberQueryResult.sampleRows.get(#attr.copyNumberQueryResultRows_rowNum - 1).value.highlightColor}" />
                            <display:column title="Value" sortable="true" comparator="gov.nih.nci.caintegrator.web.action.query.NumericColumnDisplayTagComparator" style="background-color:${highlightColor};color:white">${curValue}</display:column>
                        </s:if>
                        <s:else>
                            <display:column title="Value" sortable="true" comparator="gov.nih.nci.caintegrator.web.action.query.NumericColumnDisplayTagComparator" style="">${curValue}</display:column>
                        </s:else>
                </display:table>
            </s:else>
            <SCRIPT LANGUAGE="JavaScript" TYPE="TEXT/JAVASCRIPT">
                jQuery("div[name*='truncateDiv']").jTruncate({length: 8, minTrail: 3});
            </SCRIPT>
        </s:elseif>
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
                        <s:checkbox theme="simple" name="queryResult.rows[%{#attr.queryResultRows_rowNum - 1}].selectedSubject" disabled="%{anonymousUser}"/>
                    </display:column>
                    <display:column title="${subjectIdHeaderValue}" sortable="true" comparator="gov.nih.nci.caintegrator.web.action.query.NumericColumnDisplayTagComparator">
                        <s:property value="%{queryResult.rows.get(#attr.queryResultRows_rowNum - 1).subjectAssignment.identifier}" />
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
                                target="_"><img src="/caintegrator/images/ico_nbia.gif" border="none" title="View Image in NBIA"/></a>
                        </s:if>
                    </display:column>
                </s:if>

                <s:iterator value="queryResult.headers" status="status" id="column">
                    <s:set id="curValue" name="curValue" value="%{queryResult.rows.get(#attr.queryResultRows_rowNum - 1).values.get(#status.count - 1)}" />
                        <s:if test="#curValue.dateType">
                            <s:set id="dateValue" name="dateValue" value="#curValue.dateValue" />
                            <display:column title="${column}" sortable="true" decorator="gov.nih.nci.caintegrator.web.action.query.DateFormatColumnDecorator" >
                                ${dateValue}
                            </display:column>
                        </s:if>
                        <s:elseif test="#curValue.numericType">
                            <display:column title="${column}" sortable="true" comparator="gov.nih.nci.caintegrator.web.action.query.NumericColumnDisplayTagComparator" >
                                ${curValue}
                            </display:column>
                        </s:elseif>
                        <s:elseif test="#curValue.urlType">
                            <display:column title="${column}" sortable="true" >
                                <a href="${curValue}" target="_">
                                    View                                
                                </a>
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
    <s:if test='%{query.resultType.value.equals("geneExpression")}'>
        <s:if test="!genomicDataQueryResult.columnCollection.isEmpty() &&
                !genomicDataQueryResult.filteredRowCollection.isEmpty()">
            <div class="actionsrow">
                <del class="btnwrapper">
                    <ul class="btnrow">
                        <li><s:a href="#" cssClass="btn" onclick="submitForm('exportGenomicResults')">
                            <span class="btn_img"><span class="add">Export To CSV</span></span>
                        </s:a></li>
                        <li><s:a href="#" cssClass="btn"
                            onclick="document.manageQueryForm.target='_blank';
                            document.manageQueryForm.selectedAction.value='viewIGV';
                            document.manageQueryForm.submit();
                            document.manageQueryForm.target='_self'">
                            <span class="btn_img"><span class="igv">View in Integrative Genomics Viewer</span></span>
                        </s:a></li>
                    </ul>
                </del>
            </div>
           <s:if test="query.twoChannelType">
                <i>Expression values are log2 ratios of sample to common reference.</i>
           </s:if>
        </s:if>
    </s:if>
    <s:elseif test='%{query.resultType.value.equals("copyNumber")}'>
        <s:if test="!copyNumberQueryResult.rows.isEmpty() or !copyNumberQueryResult.sampleRows.isEmpty()">
            <div class="actionsrow">
                <del class="btnwrapper">
                    <ul class="btnrow">
                        <li><s:a href="#" cssClass="btn" onclick="openExportLink(); return false;">
                            <span class="btn_img"><span class="export">Export To CSV</span></span>
                        </s:a></li>
                        <li><s:a href="#" cssClass="btn"
                            onclick="document.manageQueryForm.target='_blank';
                                document.manageQueryForm.selectedAction.value='viewIGV';
                                document.manageQueryForm.submit();
                                document.manageQueryForm.target='_self'">
                            <span class="btn_img"><span class="igv">View in Integrative Genomics Viewer</span></span>
                        </s:a></li>
                        <s:if test="!study.hasCghCalls()">
                        <li><s:a href="#" cssClass="btn"
                            onclick="document.manageQueryForm.target='_blank';document.manageQueryForm.selectedAction.value='viewHeatmap';document.manageQueryForm.submit();document.manageQueryForm.target='_self'">
                            <span class="btn_img"><span class="externalsite">View in Heat Map Viewer</span></span>
                        </s:a></li>
                        </s:if>
                    </ul>
                </del>
           </div>
        </s:if>
    </s:elseif>
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
                        <s:if test="queryForm.hasGenomicDataSources()">
                            <li><s:a href="#" cssClass="btn"
                                onclick="document.manageQueryForm.target='_blank';document.manageQueryForm.selectedAction.value='viewIGV';document.manageQueryForm.submit();document.manageQueryForm.target='_self'">
                                <span class="btn_img"><span class="igv">View in Integrative Genomics Viewer</span></span>
                            </s:a></li>
                        </s:if>
                        <s:if test="queryForm.hasCopyNumberData() && !study.hasCghCalls()">
                            <li><s:a href="#" cssClass="btn"
                                onclick="document.manageQueryForm.target='_blank';document.manageQueryForm.selectedAction.value='viewHeatmap';document.manageQueryForm.submit();document.manageQueryForm.target='_self'">
                                <span class="btn_img"><span class="externalsite">View in Heat Map Viewer</span></span>
                            </s:a></li>
                        </s:if>
                        <s:if test="queryForm.hasImageDataSources()">
                            <li><s:a href="#" cssClass="btn" 
                                onclick="document.manageQueryForm.target='_blank';document.manageQueryForm.selectedAction.value='forwardToNcia';document.manageQueryForm.submit();document.manageQueryForm.target='_self'">
                                <span class="btn_img"><span class="externalsite">Forward To NBIA</span></span>
                            </s:a></li>
                            <s:if test="%{!anonymousUser}">
                            <li><s:a href="#" cssClass="btn" 
                                onclick="document.manageQueryForm.target='_blank';document.manageQueryForm.selectedAction.value='retrieveDicomImages';document.manageQueryForm.submit();document.manageQueryForm.target='_self'">
                                <span class="btn_img"><span class="download">Retrieve DICOM Images</span></span>
                            </s:a></li>
                            </s:if>
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
