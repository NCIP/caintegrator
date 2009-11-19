<%@ taglib prefix="s" uri="/struts-tags"%>

<!--Sort Order-->

<s:if test="!queryForm.resultConfiguration.selectedColumns.isEmpty()">
    <h2>Set Sort Order for Selected Columns</h2>
    
    
    <div class="tableheader" style="white-space: nowrap; position: relative; width: 25em;">
        <div class="tabhelp" style="white-space:nowrap; position: relative; margin-top: 0.4em; margin-right: 0.5em;">
	       <a href="javascript:openHelpWindowWithNavigation('search_columns_help')" class="help">
	       &nbsp;</a>
        </div>
    </div>

    <table class="data">
        <tr>
            <th>Column<br></th>
            <th>Column Order (L-R)<br></th>
            <th>Row Order</th>
        </tr>
        <s:set name="numberColumns" value="queryForm.resultConfiguration.selectedColumns.size" />
        <s:iterator value="queryForm.resultConfiguration.selectedColumns" id="column" status="columnIterator">
            <tr>
                <td><s:property value="annotationDefinition.displayName" /></td>
                <td><s:select 
                    id="columnIndex_%{#columnIterator.count}"
                    name="queryForm.resultConfiguration.columnIndex['%{annotationDefinition.displayName}']"
                    list="queryForm.resultConfiguration.columnIndexOptions" theme="simple" />
                </td>
                <td>
                <s:radio id="sortType_%{#columnIterator.count}"
                name="queryForm.resultConfiguration.sortType['%{annotationDefinition.displayName}']"
                list="@gov.nih.nci.caintegrator2.domain.application.SortTypeEnum@getDisplayableValues()"
                theme="simple"/>
                </td>
            </tr>
        </s:iterator>

    </table>
</s:if>
<s:else>
    <h2>Sorting feature is not applicable to this result type.</h2>
</s:else>
    <!--Buttons-->

    <div class="actionsrow">
    <del class="btnwrapper">
    <ul class="btnrow">
        <li><s:a href="#" cssClass="btn" onclick="resetSorting(%{numberColumns})">
            <span class="btn_img"><span class="cancel">Reset</span></span>
        </s:a></li>
        <li><s:a href="#" cssClass="btn" onclick="runSearch()">
            <span class="btn_img"><span class="search">Run Query</span></span>
        </s:a></li>
    </ul>
    </del>
    </div>

    <!--Buttons-->

<!--/Sort Order-->
