<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="content">
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
                <td class="title">Group Members:</td>
                <td>
                    <s:select list="groupMembers" listKey="loginName" listValue="%{lastName + ', ' + firstName}" size="10" disabled="true"/>
                </td>
            </tr>
        </table>
    </div>
</div>