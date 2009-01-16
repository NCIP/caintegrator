<%@ taglib prefix="s" uri="/struts-tags"%>

<!--Search Criteria-->

    <h2>Define Search Criteria</h2>

    <s:hidden name="selectedAction" value="" />
    <s:hidden name="rowNumber" />

    <!-- Add query criterion row selection -->
    <table class="data">
        <tr>
            <s:actionerror />
        </tr>
        <tr>
            <td colspan="4" class="tableheader">
                <select name="queryForm.criteriaGroup.criterionTypeName" id="searchcriteriaadd1"
                    style="margin-top: 10px; margin-left: 5px; width: 200px">
                    <option>Clinical</option>
                    <option>Gene Expression</option>
                    <option>Image Series</option>
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

        <s:if test="!queryForm.criteriaGroup.rows.isEmpty()">
            <s:iterator value="queryForm.criteriaGroup.rows" status="rowStatus" id="row">
                <s:set name="listRow" id="listRow" value="#rowStatus.count - 1" />
                <tr>
                    <td class="label_inline"><s:property value="rowType" /></td>
                    <td class="value_inline">
                        <s:select name="queryForm.criteriaGroup.rows[%{#rowStatus.index}].fieldName" 
                            headerKey=""
                            headerValue="-- Please Select --" 
                            list="availableFieldNames"
                            value="fieldName"
                            label="Criterion" 
                            onchange="this.form.selectedAction.value = 'updateCriteria'; this.form.submit();"
                            theme="simple" />
                    </td>
                    <td class="value_inline">
                        <table class="criterion">
                            <s:iterator value="parameters">
                                <tr>
                                    <td class="criterion">
                                        <s:property value="label" />
                                        <s:if test="!availableOperators.isEmpty()">
                                            <s:select name="%{formFieldName}.operator" 
                                                list="availableOperators"
                                                value="operator" 
                                                onchange="this.form.selectedAction.value = 'updateCriteria'; this.form.submit();"
                                                theme="simple" />
                                        </s:if>
                                    </td>
                                    <td class="criterion">
                                        <s:if test="fieldType == 'text'">
                                            <s:textfield name="%{formFieldName}.value"
                                                size="30"
                                                cssClass="keyword"
                                                theme="simple" />
                                        </s:if>
                                        <s:elseif test="fieldType == 'select'">
                                            <s:select name="%{formFieldName}.value"
                                                list="options"
                                                listKey="key"
                                                listValue="displayValue"
                                                value="value"
                                                onchange="if (%{updateFormOnChange}) {this.form.selectedAction.value = 'updateCriteria'; this.form.submit();}"
                                                theme="simple" />
                                        </s:elseif>
                                        <s:elseif test="fieldType == 'multiselect'">
                                            <s:select name="%{formFieldName}.values"
                                                multiple="true"
                                                size="5"
                                                list="options"
                                                listKey="key"
                                                listValue="displayValue"
                                                value="values"
                                                theme="simple" />
                                        </s:elseif>
                                    </td>
                                </tr>
                            </s:iterator>
                        </table>
                    </td>
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

    <div class="tablefooter"><s:radio name="queryForm.criteriaGroup.booleanOperator" list="{'or','and'}"></s:radio></div>

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
