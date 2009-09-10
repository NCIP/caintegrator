<%@ taglib prefix="s" uri="/struts-tags"%>

<!--Sort Order-->

<s:if test="queryForm.resultConfiguration.selectedColumns.size() > 1">
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
            <th>Order (L-R)<br></th>
        </tr>

        <s:iterator value="queryForm.resultConfiguration.selectedColumns" id="column">
            <tr>
                <td><s:property value="annotationDefinition.displayName" /></td>
                <td><s:select name="queryForm.resultConfiguration.columnIndex['%{annotationDefinition.displayName}']"
                    list="queryForm.resultConfiguration.columnIndexOptions" theme="simple" />
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
        <li><s:a href="#" cssClass="btn" onclick="runSearch()">
            <span class="btn_img"><span class="search">Run Query</span></span>
        </s:a></li>
    </ul>
    </del>
    </div>

    <!--Buttons-->

<!--/Sort Order-->
