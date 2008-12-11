<%@ taglib prefix="s" uri="/struts-tags"%>

<!--Search Criteria-->

    <h2>Define Search Criteria</h2>

    <s:hidden name="manageQueryHelper.advancedView" value="false" />
    <s:hidden name="selectedAction" value="" />
    <s:hidden name="rowNumber" value="" />

    <!-- Add query criterion row selection -->
    <table class="data">
        <tr>
            <s:actionerror />
        </tr>
        <tr>
            <td colspan="5" class="tableheader"><select name="selectedRowCriterion" id="searchcriteriaadd1"
                style="margin-left: 5px; width: 200px">
                <option>Select Criteria Type</option>
                <option value="subject">Clinical</option>
                <option value="geneExpression">Gene Expression</option>
                <option value="imageSeries">Image Series</option>
            </select>
            <ul class="btnrow" style="margin: -22px 0 0 200px; height: 32px">
                <li><s:a href="#" cssClass="btn" cssStyle="margin:0 5px;"
                    onclick="document.manageQueryForm.selectedAction.value='addCriterionRow';document.manageQueryForm.submit();">
                    <span class="btn_img"><span class="add">Add</span></span>
                </s:a></li>
            </ul>
            </td>
        </tr>

        <!-- Reproduce existing query criteria rows if any -->

        <s:if test="!manageQueryHelper.queryCriteriaRowList.isEmpty()">
            <s:iterator value="manageQueryHelper.queryCriteriaRowList" status="itStatus" id="currentRow">
                <s:set name="listRow" id="listRow" value="#itStatus.count - 1" />
                <tr>
                    <td class="label_inline"><s:property value="#currentRow.rowLabel" /></td>
                    <s:if test="rowType.value.equals('geneExpression')">
                        <td class="value_inline"><s:select name="selectedAnnotations" headerKey="1"
                            headerValue="-- Please Select --" list="annotationSelection.genomicAnnotationDefinitions"
                            listValue="value" listKey="value" value="#currentRow.selectedAnnotationDefinition"
                            label="Criterion" theme="simple" /></td>
                        <td class="value_inline2"><s:select name="selectedOperators" headerKey="1"
                            headerValue="Select Operator" list="{'equals'}" value="#currentRow.selectedOperator"
                            theme="simple" /></td>
                        <td class="value_inline"><s:textfield name="selectedValues"
                            value="%{selectedAnnotationValue}" size="30" theme="simple" /></td>
                    </s:if>
                    <s:else>
                        <td class="value_inline"><s:select name="selectedAnnotations" headerKey="1"
                            headerValue="-- Please Select --" list="annotationSelection.annotationDefinitions"
                            listValue="displayName" listKey="displayName"
                            value="#currentRow.selectedAnnotationDefinition" label="Criterion"
                            onchange="document.manageQueryForm.selectedAction.value='updateAnnotationDefinition';document.manageQueryForm.rowNumber.value = %{listRow};document.manageQueryForm.submit();"
                            theme="simple" /></td>
                        <td class="value_inline2"><s:select name="selectedOperators" headerKey="1"
                            headerValue="Select Operator" list="annotationSelection.currentAnnotationOperatorSelections"
                            value="#currentRow.selectedOperator" theme="simple" /></td>
                        <s:if test="annotationSelection.currentAnnotationPermissibleSelections.isEmpty()">
                            <td class="value_inline"><s:textfield name="selectedValues"
                                value="%{selectedAnnotationValue}" size="30" theme="simple" /></td>
                        </s:if>
                        <s:else>
                            <td class="value_inline"><s:select name="selectedValues"
                                list="annotationSelection.currentAnnotationPermissibleSelections"
                                value="#currentRow.selectedAnnotationValue" theme="simple" /></td>
                        </s:else>
                    </s:else>
                    <td class="action_inline"><a href="#" class="remove"
                        onclick="document.manageQueryForm.selectedAction.value='remove'; document.manageQueryForm.rowNumber.value = ${listRow};document.manageQueryForm.submit();"><span
                        class="remove">Remove</span></a></td>
                </tr>
            </s:iterator>
        </s:if>
        <s:else>
            <tr class="odd">
                <td colspan="5" class="value_inline">
                <p style="margin: 0; padding: 2px 0 3px 0; text-align: center;"><strong>No criteria added</strong>.
                Please select criteria from the pulldown box.</p>
                </td>
            </tr>
        </s:else>
    </table>

    <div class="tablefooter"><s:radio name="manageQueryHelper.selectedBasicOperator" list="{'or','and'}"></s:radio></div>

    <!--Buttons-->

    <div class="actionsrow">
    <del class="btnwrapper">
    <ul class="btnrow">
        <li><s:a href="#" cssClass="btn"
            onclick="document.manageQueryForm.selectedAction.value='executeQuery';document.manageQueryForm.submit();">
            <span class="btn_img"><span class="search">Run Search</span></span>
        </s:a></li>
        <li><span class="btn_img"><span class="search"><br>
        </span></span></li>
    </ul>
    </del>
    </div>

    <!--/Buttons-->

<!--/Search Criteria-->
