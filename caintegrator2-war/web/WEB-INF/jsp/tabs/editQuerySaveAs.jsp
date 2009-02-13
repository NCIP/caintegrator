<%@ taglib prefix="s" uri="/struts-tags"%>

<!--Save Search-->

    <h2>Save This Search</h2>

    <table class="form">

        <tr>
            <td class="label"><label for="searchname">Search Name:</label></td>
            <td class="value"><s:textfield id="saveName" label="Search Name" name="queryForm.query.name" theme="simple" /></td>
        </tr>
        <tr>
            <td class="label"><label for="searchdesc">Search Description:</label></td>
            <td class="value"><s:textarea label="Search Description" name="queryForm.query.description" cols="40" rows="2"
                theme="simple" /></td>

        </tr>
        <tr>
            <td class="label"><input type="checkbox" name="columpref" id="columnpref" /></td>
            <td class="value"><label for="columnpref">Make Column selections my defaults for future
            searches</label></td>
        </tr>
        <tr>
            <td class="label"><input type="checkbox" name="sortpref" id="sortpref" /></td>
            <td class="value"><label for="sortpref">Make Sort Order selections my defaults for future
            searches</label></td>

        </tr>

    </table>

    <!--Buttons-->

    <div class="actionsrow">
    <del class="btnwrapper">
    <ul class="btnrow">
        <li><s:a href="#" cssClass="btn"
            onclick="saveQuery('save')">
            <span class="btn_img"><span class="save">Save</span></span>
        </s:a></li>
        <s:if test="queryForm.isSavedQuery()">
            <li>
                <s:a href="#" cssClass="btn"
                    onclick="saveQuery('saveAs')">
                    <span class="btn_img"><span class="save">Save As</span></span>
            </s:a></li>
            <li>
                <s:a href="#" cssClass="btn"
                    onclick="deleteQuery()">
                    <span class="btn_img"><span class="delete">Delete</span></span>
            </s:a></li>
        </s:if>
    </ul>
    </del>
    </div>

    <!--Buttons-->
    
<!--/Save Search-->
