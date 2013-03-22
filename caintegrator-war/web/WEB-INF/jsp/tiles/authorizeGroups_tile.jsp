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
                    <s:url var="addAuthorizedGroup" includeParams="none" action="addAuthorizedGroup">
                        <s:param name="studyConfiguration.id" value="studyConfiguration.id" />
                        <s:param name="selectedGroupId" value="groupId"/>
                        <s:param name="struts.token.name">struts.token</s:param>
                        <s:param name="struts.token" value="%{struts.token}" />     
                    </s:url>
                    <s:a href="%{addAuthorizedGroup}" cssClass="btn" cssStyle="margin:0 5px;">
                        <span class="btn_img"><span class="add">Authorize This Group</span></span>
                    </s:a>
                </td>
                </tr>
            </s:iterator>
        </table>
        <div class="bottombtns">
            <ul class="btnrow">
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