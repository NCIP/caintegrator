<%@ taglib prefix="s" uri="/struts-tags"%>

<!--Sort Order-->

    <h2>Set Sort Order for Selected Columns<div class="tabhelp"><a href="javascript:openHelpWindowWithNavigation('search_columns_help')" class="help">
   (draft)</a>
    </div></h2>

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
