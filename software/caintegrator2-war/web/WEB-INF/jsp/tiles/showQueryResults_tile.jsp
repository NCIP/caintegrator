<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<!--Search Results-->

    
    <div id="searchresults" class="box2" style="display:none;">                                             
        
        <h2>Search Results for: <s:property value="query.name"/></h2>

        <div class="tableheader">
            <label>Results per Page:</label>
            <s:select name="queryResult.pageSize" list="{'10', '20', '50', '100'}" />
            <s:a href="#"
                onclick="document.manageQueryForm.selectedAction.value = 'updateResultsPerPage';document.manageQueryForm.submit();">
                <span class="btn_img">Apply</span>
            </s:a>
        </div>

        <br>
        <table class="data">

		<s:if test='%{query.resultType.equals("genomic")}'>
		    <tr>
		        <td/><td/>
		        <th>
		        Patient ID
		        </th>
                <s:iterator value="genomicDataQueryResult.columnCollection">
                    <td><b><s:property value="sampleAcquisition.assignment.identifier"/></b></td>
                </s:iterator>
            </tr>
            <tr>
                <td/><td/>
                <th>
                Sample ID
                </th>
                <s:iterator value="genomicDataQueryResult.columnCollection">
                    <td><b><s:property value="sampleAcquisition.sample.name"/></b></td>
                </s:iterator>
            </tr>
            <tr/>
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
				<td><b><s:property value="reporter.gene.symbol"/></b></td>
				<td><b><s:property value="reporter.name"/></b></td>
				<td/>
				<s:iterator value="valueCollection">
					<td>
						<s:property value="value" />
					</td>
				</s:iterator>
			</s:iterator>
		</s:if>
		<s:else>
            <s:set name="pageSizeVar" id="pageSizeVar" value="%{queryResult.pageSize}"/>
            <display:table name="queryResult.rows" uid="queryResultRows" id="queryResultRows" 
                           pagesize="${pageSizeVar}" sort="list" class="data" requestURI="" export="true" >
                <display:setProperty name="paging.banner.placement" value="both"/> 
            	<s:if test="queryResult.hasSubjects" >
                    <display:column title="Subject Identifier" sortable="true">
                        <s:property value="%{queryResult.rows.get(#attr.queryResultRows_rowNum - 1).subjectAssignment.identifier}" />
                    </display:column>
                </s:if>
                <s:if test="queryResult.hasImageSeries" >
                    <display:column title="Image Series Identifier" sortable="true">
                        <s:if test="%{queryResult.rows.get(#attr.queryResultRows_rowNum - 1).imageSeries != null}">
                            <s:property value="%{queryResult.rows.get(#attr.queryResultRows_rowNum - 1).imageSeries.identifier}" />
                            <a href='<s:property value="%{queryResult.rows.get(#attr.queryResultRows_rowNum - 1).nciaLink}" escape="false"/>' target="_">View in NCIA</a>
                        </s:if>
                    </display:column>
                </s:if>
                
                <s:iterator value="queryResult.headers" status="status" id="column">
                    <display:column title="${column}" sortable="true">
                       <s:property value="%{queryResult.rows.get(#attr.queryResultRows_rowNum - 1).values.get(#status.count - 1)}"/>   
                    </display:column>
                </s:iterator>
            </display:table>
            
            </s:else>
        </table>

       <!--Buttons-->

        <div class="actionsrow">
            <del class="btnwrapper">
                <ul class="btnrow">
                   <li><s:a href="#" cssClass="btn" onclick="document.manageQueryForm.selectedAction.value = 'executeQuery';document.manageQueryForm.submit();"><span class="btn_img"><span class="search">Run Search</span></span></s:a></li><li><span class="btn_img"><span class="search"><br></span></span></li>
                </ul>   
            </del>
        </div>
        
        <!--/Buttons-->
                                                                                                  
    </div>

    
<!--/Search Results-->


<div class="clear"><br />
</div>
