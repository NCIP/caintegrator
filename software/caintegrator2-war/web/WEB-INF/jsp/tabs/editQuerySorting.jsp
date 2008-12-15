<%@ taglib prefix="s" uri="/struts-tags"%>

<!--Sort Order-->

    <h2>Set Sort Order for Selected Columns</h2>

    <table class="data">
        <tr>
            <th>Column<br></th>
            <th>Order (L-R)<br></th>
            <th>Vertical Sorting (1st)<br></th>
            <th>Vertical Sorting (2nd)<br></th>
            <th>Action<br></th>
        </tr>
        <s:if test="!manageQueryHelper.columnList.isEmpty()">

            <s:iterator value="manageQueryHelper.columnList" status="columnStatus" id="currentColumn">
                <tr>
                    <td><s:property value="annotationDefinition.displayName" /></td>

                    <td><s:select name="manageQueryHelper.columnList[%{#columnStatus.index}].columnIndex"
                        list="manageQueryHelper.columnIndexOptions" value="%{columnIndex}" theme="simple"/></td>

                </tr>
            </s:iterator>
        </s:if>
        <s:else>
            <p>These options are fixed when viewing a saved query. Click the Criteria tab to edit a new query.</p>
        </s:else>

    </table>
    <!--Buttons-->

    <div class="actionsrow">
    <del class="btnwrapper">
    <ul class="btnrow">
        <li><s:a href="#" cssClass="btn"
            onclick="document.manageQueryForm.selectedAction.value='executeQuery';document.manageQueryForm.submit();">
            <span class="btn_img"><span class="search">Run Search</span></span>
        </s:a></li>
    </ul>
    </del>
    </div>

    <!--Buttons-->
    
<!--/Sort Order-->
