<%@ taglib prefix="s" uri="/struts-tags"%>

<script type="text/javascript" src="/caintegrator/common/js/jquery.editable-select.js"></script>
<script type="text/javascript">
    function toggleAll(idPrefix) {
        var inputs = $('input[id^=' + idPrefix +']');
        inputs.each(function(index, element) {
            element.checked = $('#selectAll_' + idPrefix)[0].checked;
        });
    }
    
    function initToggleAll(idPrefix) {
        var allSelected = true;
        var inputs = $('input[id^=' + idPrefix +']');
        inputs.each(function(index, element) {
            if (!element.checked) {
                allSelected = false;
                return false;
            }
        });
        $('#selectAll_' + idPrefix)[0].checked = allSelected;
    }
    $(document).ready(function() {
        $('#selectAll_shownInBrowseCheckBox').click(function() {
            toggleAll('shownInBrowseCheckBox');
        });
        initToggleAll('shownInBrowseCheckBox');
        $('#selectAll_showInAuthorizationCheckBox').click(function() {
            toggleAll('showInAuthorizationCheckBox');
        });
        initToggleAll('showInAuthorizationCheckBox');
    });
</script>

<div id="content">

    <h1 style="color: #FFFFFF; background: #263D6B; padding: 5px;">editing: <strong><s:property value="studyConfiguration.study.shortTitleText" /></strong></h1>

    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openWikiHelp('CIDnAg', 'id-2-CreatingaNewStudy-DefineFieldsPageforEditingAnnotations')" class="help">
   &nbsp;</a>
    </div>

    <!--/Page Help-->

    <h1><s:property value="#subTitleText" /></h1>
    <p>Assign annotation definitions to data fields.</p>
    <s:if test = "'Error'.equals(clinicalSource.status.value)" >
    <p><font color="red">Error: <s:property value="clinicalSource.statusDescription"/></font></p>
    </s:if>
    <div class="form_wrapper_outer">

    <table class="form_wrapper_table">

            <tr>
                <td colspan="2" style="padding: 5px;">
                <s:form id="subjectAnnotationsForm" name="subjectAnnotationsForm" action="saveClinicalSource" method="post" enctype="multipart/form-data">
                    <s:token />
                    <s:hidden name="studyConfiguration.id" />
                    <s:hidden name="clinicalSource.id" />
                <table class="data">
                    <tr>
                        <th>Annotation Group</th>
                        <th>Visible <input type="checkbox" id="selectAll_shownInBrowseCheckBox"/></th>
                        <th>Authorized <input type="checkbox" id="selectAll_showInAuthorizationCheckBox"/></th>
                        <th>Annotation Definition</th>
                        <th>Annotation Header from File</th>
                        <th colspan="3" />Data from File</th>
                    </tr>
                    <s:iterator value="displayableFields" status="columnIterator">
                        <s:if test="#columnIterator.odd == true">
                          <tr class="odd">
                        </s:if>
                        <s:else>
                          <tr class="even">
                        </s:else>
                            <td>
                                <s:if test="%{fieldDescriptor != null}">
                                    <s:select name="displayableFields[%{#columnIterator.count - 1}].annotationGroupName"
                                              list="selectableAnnotationGroups"
                                              listKey="name"
                                              listValue="name"
                                              theme="simple" />
                                </s:if>
                            </td>
                            <td>
                                <s:if test="%{fieldDescriptor != null}">
                                    <s:checkbox name="displayableFields[%{#columnIterator.count - 1}].fieldDescriptor.shownInBrowse"
                                        id="shownInBrowseCheckBox%{#columnIterator.count - 1}" theme="simple" disabled="false"/>
                                </s:if>
                            </td>
                            <td>
                                <s:if test="%{fieldDescriptor != null && !identifierType}">
                                    <s:checkbox name="displayableFields[%{#columnIterator.count - 1}].fieldDescriptor.showInAuthorization"
                                        id="showInAuthorizationCheckBox%{#columnIterator.count - 1}" theme="simple" disabled="false"/>
                                </s:if>
                            </td>
                            <td>
                                <s:if test="%{fieldDescriptor.hasValidationErrors}">
                                    <font class="formErrorMsg">
                                </s:if>
                                <s:else>
                                    <font color="black">
                                </s:else>
                                <s:if test="%{identifierType}">
                                    Identifier
                                </s:if>
                                <s:elseif test="%{timepointType}">
                                    Timepoint
                                </s:elseif>
                                <s:elseif test="%{fieldDescriptor != null && fieldDescriptor.definition != null}">
                                    <s:property value="fieldDescriptor.definition.displayName"/>
                                </s:elseif>
                                <s:url id="editClinicalFieldDescriptor" action="editClinicalFieldDescriptor" includeParams="none">
                                    <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                                    <s:param name="sourceId" value="clinicalSource.id" />
                                    <s:param name="fieldDescriptor.id" value="fieldDescriptor.id" />
                                </s:url>
                                </font>
                                <br>
                                <s:a href="%{editClinicalFieldDescriptor}">
                                    <s:if test="%{identifierType || timepointType || (fieldDescriptor != null && fieldDescriptor.definition != null) }">
                                        Change Assignment
                                    </s:if>
                                    <s:else>
                                        Assign Annotation Definition
                                    </s:else>
                                </s:a>
                            </td>
                            <td><s:property value="fieldDescriptor.name" /></td>
                            <td><s:if test="%{dataValues.size > 0}"><s:property value="dataValues[0]" /></s:if></td>
                            <td><s:if test="%{dataValues.size > 1}"><s:property value="dataValues[1]" /></s:if></td>
                            <td><s:if test="%{dataValues.size > 2}"><s:property value="dataValues[2]" /></s:if></td>
                        </tr>
                    </s:iterator>
                </table>
                <tr>
                    <td colspan="2"><div align="center">
                    <button type="button" onclick="document.subjectAnnotationsForm.action = 'cancelClinicalSource.action';
                        document.subjectAnnotationsForm.submit();">Cancel</button>

                    <button type="button" onclick="document.subjectAnnotationsForm.submit();">Save</button>
                    </div></td>
                </tr>
                </s:form>
                </td>
            </tr>
        </table>
    </div>
</div>

<div class="clear"><br /></div>