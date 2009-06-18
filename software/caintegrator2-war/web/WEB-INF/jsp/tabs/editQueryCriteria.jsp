<%@ taglib prefix="s" uri="/struts-tags"%>

<!--Search Criteria-->

    <h2>Define Query Criteria for:
        <s:if test="query != null && query.name != null && query.name.length() > 0">
            <s:property value="query.name" />
        </s:if>
        <s:else>
            Unsaved Query
        </s:else>
        <div class="tabhelp"><a href="javascript:openHelpWindowWithNavigation('search_criteria_help')" class="help">
   (draft)</a>
    </div>
    </h2>

    <s:hidden name="selectedAction" value="" />
    <s:hidden name="rowNumber" />
    <!-- For caBio to know which form element to publish gene symbols to. -->
    <s:hidden name="geneSymbolElementId" />

    <!-- Add query criterion row selection -->
    <table class="data">
        <tr>
            <s:actionerror />
        </tr>
        <tr>
            <td colspan="4" class="tableheader">
                <s:select name="queryForm.criteriaGroup.criterionTypeName" id="searchcriteriaadd1"
                    list="queryForm.criteriaTypeOptions" theme="simple"
                    cssStyle="margin-top: 10px; margin-left: 5px; width: 200px" />
                <ul class="btnrow" style="margin: -22px 0 0 200px; height: 32px">
                <li><s:a href="#" cssClass="btn" cssStyle="margin:0 5px;"
                    onclick="submitForm('addCriterionRow')">
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
                            onchange="submitForm('updateCriteria')"
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
                                                onchange="submitForm('updateCriteria')"
                                                theme="simple" />
                                        </s:if>
                                    </td>
                                    <td class="criterion" style="white-space:nowrap">
                                        <s:if test="fieldType == 'text'">
                                            <s:textfield id="%{formFieldName}.value" name="%{formFieldName}.value"
                                                size="30"
                                                cssClass="keyword"
                                                theme="simple"
                                                title="%{title}" />
                                            <s:if test="geneSymbol">
                                                <s:component template="genetextfield.ftl" theme="cai2simple">
							                        <s:param name="textFieldId" value="%{'%{formFieldName}.value'}"/>
							                        <s:param name="dojoEventTopic" value="%{'caBioSearchInput_%{#rowStatus.index}'}" />
							                        <s:param name="textFieldName" value="%{'%{formFieldName}.value'}"/>
							                        <s:param name="formId" value="%{'manageQueryForm'}"/>
							                        <s:param name="refreshOnShow" value="%{'true'}" />
							                    </s:component>
                                            </s:if>
                                        </s:if>
                                        <s:elseif test="fieldType == 'select'">
                                            <s:select name="%{formFieldName}.value"
                                                list="options"
                                                listKey="key"
                                                listValue="displayValue"
                                                value="value"
                                                onchange="if (%{updateFormOnChange}) {submitForm('updateCriteria')}"
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
                        onclick="document.manageQueryForm.rowNumber.value = ${listRow};submitForm('remove');"><span
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
        <li><s:a href="#" cssClass="btn" onclick="runSearch()">
            <span class="btn_img"><span class="search">Run Query</span></span>
        </s:a></li>
        <li><span class="btn_img"><span class="search"><br>
        </span></span></li>
    </ul>
    </del>
    </div>

    <!--/Buttons-->

<!--/Search Criteria-->
