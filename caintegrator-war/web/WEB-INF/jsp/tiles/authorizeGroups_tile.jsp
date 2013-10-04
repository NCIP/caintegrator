<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="content">
    <h1 style="color: #FFFFFF; background: #263D6B; padding: 5px;"> editing: <strong><s:property value="studyConfiguration.study.shortTitleText" /></strong>
    </h1>
    
    <div class="pagehelp">
        <a href="javascript:openWikiHelp('3xeSB', 'id-3-ManagingaStudyorPlatform-ManagingStudyAccess')" class="help">
            &nbsp;
        </a>
    </div>
    
    <h1>
        <s:property value="#subTitleText" />
    </h1>
    
    <div class="form_wrapper_outer">

        <table class="data">
            <tr>
                <th>User Group Name</th>
                <th>User Group Description</th>
                <th>&nbsp;</th>
            </tr>
            <s:iterator value="unauthorizedGroups" status="status">
                <s:if test="#status.odd == true">
                    <tr class="odd">
                </s:if>
                <s:else>
                    <tr class="even">
                </s:else>
                <td><s:property value="groupName"/></td>
                <td><s:property value="groupDesc"/></td>
                <td style="float: right;">
                    <s:form id="addAuthorizationForm_%{groupId}" action="addAuthorizedGroup" theme="simple">
                        <s:token/>
                        <s:hidden name="studyConfiguration.id" value="%{studyConfiguration.id}" />
                        <s:hidden name="selectedGroupId" value="%{groupId}"/>
                        <s:a href="javascript:void(0);"cssClass="btn" cssStyle="margin:0 5px;" onclick="$('#addAuthorizationForm_%{groupId}').submit();">
                            <span class="btn_img"><span class="add">Authorize This Group</span></span>
                        </s:a>
                    </s:form>
                </td>
                </tr>
            </s:iterator>
        </table>
        <div class="bottombtns">
            <ul class="btnrow">
                <li>
                    <s:form id="cancelAuthorizedGroupForm" action="cancelAuthorizedGroup" theme="simple">
                        <s:token/>
                        <s:hidden name="studyConfiguration.id" value="%{studyConfiguration.id}" />
                    </s:form>
                    <s:a href="javascript:void(0);"cssClass="btn" cssStyle="margin:0 5px;" onclick="$('#cancelAuthorizedGroupForm').submit();">
                        <span class="btn_img"><span class="cancel">Cancel</span></span>
                    </s:a>
                </li>
            </ul>   
        </div>
    </div>
</div>