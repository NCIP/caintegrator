<%@ taglib prefix="s" uri="/struts-tags"%>

<!--Save Search-->

    <h2>Save This Query</h2>
    
    <div class="tableheader" style="white-space: nowrap; position: relative; width: 25em;">
        <div class="tabhelp" style="white-space:nowrap; position: relative; margin-top: 0.4em; margin-right: 0.5em;">
	        <a href="javascript:openWikiHelp('B4DnAg', '4-SearchingacaIntegratorStudy-SavingaQuery')" class="help">
	        &nbsp;</a>
        </div>
    </div>

    <table class="form">

        <tr>
            <td class="label"><label for="searchname">Query Name:</label></td>
            <td class="value"><s:textfield id="saveName" label="Search Name" name="queryForm.query.name" theme="simple" /></td>
        </tr>
        <tr>
            <td class="label"><label for="searchdesc">Query Description:</label></td>
            <td class="value"><s:textarea label="Search Description" name="queryForm.query.description" cols="40" rows="2"
                theme="simple" /></td>

        </tr>

    </table>

    <!--Buttons-->

    <div class="actionsrow">
    <del class="btnwrapper">
    <ul class="btnrow">
        <li><s:a href="#" cssClass="btn"
            onclick="saveQuery('save')">
            <span class="btn_img"><span class="save">Save Query</span></span>
        </s:a></li>
        <s:if test="queryForm.isSavedQuery()">
            <li>
                <s:a href="#" cssClass="btn"
                    onclick="saveQuery('saveAs')">
                    <span class="btn_img"><span class="save">Save Query As</span></span>
            </s:a></li>
            <li>
                <s:a href="#" cssClass="btn"
                    onclick="deleteQuery()">
                    <span class="btn_img"><span class="delete">Delete Query</span></span>
            </s:a></li>
        </s:if>
    </ul>
    </del>
    </div>

    <!--Buttons-->
    
<!--/Save Search-->
