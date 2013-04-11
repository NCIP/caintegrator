<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript" src="/caintegrator/common/js/dynatree/jquery.cookie.js"></script>
<link href="/caintegrator/common/js/dynatree/skin/ui.dynatree.css" rel="stylesheet" type="text/css" id="skinSheet">
<link href="/caintegrator/common/js/dynatree/custom/custom.css" rel="stylesheet" type="text/css" >
<script type="text/javascript" src="/caintegrator/common/js/dynatree/jquery.dynatree-1.2.4.js"></script>
<script type="text/javascript">
    $(document).ready(function() {
        $("#left_tree").dynatree({
            checkbox: true,
            generateIds: true,
            clickFolderMode: 2,
            activeVisible: true,
            debugLevel: 1,
            onSelect: function(select, dtnode) {
                dtnode.visit(function(dtnode) {
                    $("#descriptor-" + dtnode.data.key).attr("checked", select);
                  }, true);
            },
            onExpand: function(select, dtnode) {
                dtnode.visit(function(dtnode) {
                    $("#descriptor-" + dtnode.data.key).attr("checked", dtnode.isSelected()).hide();
                  }, true);
            }
        });
        
        $("#middle_tree").dynatree({
            checkbox: true,
            generateIds: true,
            clickFolderMode: 2,
            activeVisible: true,
            debugLevel: 1,
            onSelect: function(select, dtnode) {
                dtnode.visit(function(dtnode) {
                    $("#datasource-" + dtnode.data.key).attr("checked", select);
                  }, true);
            },
            onExpand: function(select, dtnode) {
                dtnode.visit(function(dtnode) {
                    $("#datasource-" + dtnode.data.key).attr("checked", dtnode.isSelected()).hide();
                  }, true);
            }
        });
        
        
        $('#right_tree').dynatree({
            checkbox: true,
            generateIds: true,
            clickFolderMode: 2,
            activeVisible: true,
            debugLevel: 1,
            onSelect: function(select, dtnode) {
                dtnode.visit(function(dtnode) {
                    $("#definition-id-" + dtnode.data.key).attr("checked", select);
                    $("#definition-value-" + dtnode.data.key).attr("checked", select);
                  }, true);
            },
            onExpand: function(select, dtnode) {
                dtnode.visit(function(dtnode) {
                    $("#definition-id-" + dtnode.data.key).attr("checked", dtnode.isSelected()).hide();
                    $("#definition-value-" + dtnode.data.key).attr("checked", dtnode.isSelected()).hide();
                  }, true);
            }
            
        });
        
        $("#left_tree :checkbox").each(function() {
            $(this).hide();
        });
        $("#middle_tree :checkbox").each(function() {
            $(this).hide();
        });
        $("#right_tree :checkbox").each(function() {
            $(this).hide();
        });
        
        $.map($("#left_tree").dynatree("getTree").getSelectedNodes(), function (dtnode) {
            $("#descriptor-" + dtnode.data.key).attr("checked", true);
            dtnode.activate();
        });
        $.map($("#middle_tree").dynatree("getTree").getSelectedNodes(), function (dtnode) {
            $("#datasource-" + dtnode.data.key).attr("checked", true);
            dtnode.activate();
        });
        $.map($("#right_tree").dynatree("getTree").getSelectedNodes(), function (dtnode) {
            $("#definition-id-" + dtnode.data.key).attr("checked", true);
            $("#definition-value-" + dtnode.data.key).attr("checked", true);
            dtnode.activate();
        });
});
</script>


<div id="content">
    <div class="pagehelp">
        <a href="javascript:openWikiHelp('3xeSB', 'id-3-ManagingaStudyorPlatform-EditingUserGroupAuthorization')" class="help">
            &nbsp;
        </a>
    </div>
    
    <h1><s:property value="#subTitleText" /></h1>
    <div class="form_wrapper_outer">
    
        <table class="form_wrapper_table">
            <tr>
                <td class="title">Group Name:</td>
                <td><s:property value="authorizedGroup.authorizedGroup.groupName"/></td>
            </tr>
            <tr>
                <td class="title">Group Description:</td>
                <td><s:property value="authorizedGroup.authorizedGroup.groupDesc"/></td>
            </tr>
            <tr>
                <td class="title"><label for="groupMembers">Group Members:</label></td>
                <td>
                    <s:select list="groupMembers" id="groupMembers" listKey="loginName" listValue="%{lastName + ', ' + firstName}" size="10" disabled="true"/>
                </td>
            </tr>
        </table>
        <s:form id="editAuthorizedGroupForm" name="editAuthorizedGroupForm" cssClass="form" action="saveAuthorizedGroup">
            <s:hidden name="studyConfiguration.id"/>
            <s:hidden name="authorizedGroup.id" />
            <s:token />
            <table class="form_wrapper_table">
                <tr>
                    <th colspan="2">Can see these data:</th>
                    <th>Restrict Subject by:</th>
                </tr>
                <tr>
                    <td class="tree_container">
                        <div id="left_tree">
                            <ul>
                                <s:iterator value="trees.annotationGroups">
                                    <li class="folder" data="hideCheckbox: true"><s:property value="label"/>
                                        <ul>
                                            <s:iterator value="children">
                                                <li id="<s:property value='nodeId'/>" data="select: <s:property value='selected'/>">
                                                    <input type="checkbox" id="descriptor-<s:property value='nodeId'/>" name="selectedDescriptorIds" value="<s:property value='nodeId'/>"/>
                                                    <s:property value="label"/>
                                                </li>
                                            </s:iterator>    
                                        </ul>
                                    </li>
                                </s:iterator>
                            </ul>
                        </div>
                    </td>
                    <td class="tree_container">
                        <div id="middle_tree">
                            <ul>
                                <li class="folder" data="hideCheckbox: true">Molecular Data
                                    <ul>
                                        <li class="folder" data="hideCheckbox: true">Expression Experiments
                                            <ul>
                                                <s:iterator value="trees.expressionDataSources">
                                                    <li id="<s:property value='nodeId'/>" data="select: <s:property value='selected'/>">
                                                        <input type="checkbox" id="datasource-<s:property value='nodeId'/>" name="selectedDataSourceIds" value="<s:property value='nodeId'/>"/>
                                                        <s:property value="label"/>
                                                    </li>
                                                </s:iterator>
                                            </ul>
                                        </li>
                                        <li class="folder" data="hideCheckbox: true">Copy Number Experiments
                                            <ul>
                                                <s:iterator value="trees.copyNumberDataSources">
                                                    <li id="<s:property value='nodeId'/>" data="select: <s:property value='selected'/>">
                                                        <input type="checkbox" id="datasource-<s:property value='nodeId'/>" name="selectedDataSourceIds" value="<s:property value='nodeId'/>"/>
                                                        <s:property value="label"/>
                                                    </li>
                                                </s:iterator>
                                            </ul>
                                        </li>
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </td>
                    <td class="tree_container">
                        <div id="right_tree">
                            <ul>
                                <s:set var="count" value="0"/>
                                <s:iterator value="trees.fieldDescriptors" var="fd">
                                    <li class="folder" data="hideCheckbox: true"><s:property value="label"/>
                                        <ul>
                                            <s:iterator value="children">
                                                <li id="<s:property value='#count'/>" data="select: <s:property value='selected'/>">
                                                    <input type="checkbox" id="definition-id-<s:property value='#count'/>" 
                                                        name="selectedQueryParameters[<s:property value='#count'/>].annotationDefinitionId" value="<s:property value='#fd.nodeId'/>"/>
                                                    <input type="checkbox" id="definition-value-<s:property value='#count'/>" 
                                                        name="selectedQueryParameters[<s:property value='#count'/>].value" value="<s:property value='label'/>"/>
                                                    <s:property value="label"/>
                                                    <s:set var="count" value="#count + 1"/>
                                                </li>
                                            </s:iterator>                                        
                                        </ul>
                                    </li>
                                </s:iterator>
                            </ul>
                        </div>
                    </td>
                </tr>
            </table>
        </s:form>
        
        <br>
        <div class="bottombtns">
            <ul class="btnrow">
                <li><input type="image" src="images/btn_save.gif" alt="Save" onClick="$('#editAuthorizedGroupForm').submit()"/></li>
                <li>
                    <s:url id="cancelAuthorizedGroup" includeParams="none" action="cancelAuthorizedGroup">
                        <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                        <s:param name="struts.token.name">struts.token</s:param>
                        <s:param name="struts.token" value="%{struts.token}" />
                    </s:url> 
                    <s:a href="%{cancelAuthorizedGroup}" cssClass="btn" cssStyle="margin:0 5px;">
                        <span class="btn_img"><span class="cancel">Cancel</span></span>
                    </s:a>
                </li>
            </ul>   
        </div>
    </div>
</div>