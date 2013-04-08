<%@ taglib uri="/struts-tags" prefix="s"%>

<s:actionerror />

<s:if test="%{!pathwayResults.empty}">
    <hr align="center" width="100%" />
    <s:div cssStyle="padding-left: 5px;">
        <s:property value="%{pathwayResults.size}" /> pathway(s) found.
            <s:div cssClass="columnWrapper">
            <s:div cssClass="columnHeader" cssStyle="float: left; width: 20px; padding-bottom: 1px;">
                <input type="checkbox" name="checkall" onclick="checkUncheckAll(this,'searchParameters.filterGenesOnStudy');" checked="checked" />
            </s:div>
            <s:div cssClass="columnHeader" cssStyle="float: left; width: 15%;">Pathway Name </s:div>
            <s:div cssClass="columnHeader" cssStyle="float: left; width: 82.1%;">Pathway Title</s:div>
        </s:div><br>
        <s:iterator value="pathwayResults" status="status">
            <s:if test="#status.odd == true">
                <s:div cssClass="columnWrapper" cssStyle="background-color: #ffffff;">
                    <s:div cssClass="left1" cssStyle="width: 20px;">
                        <input type="checkbox" checked="checked" name="cb_symbols" value="<s:property value='name'/>" />
                    </s:div>
                    <s:div cssClass="left2" cssStyle="width: 15%;"><s:property value="name" /></s:div>
                    <s:div cssClass="left6" cssStyle="width: 82.1%;"><s:property value="title" /></s:div>
                </s:div><br>
            </s:if>
            <s:else>
                <s:div cssClass="columnWrapper" cssStyle="background-color: #dcdcdc;">
                    <s:div cssClass="left1" cssStyle="width: 20px;">
                        <input type="checkbox" checked="checked" name="cb_symbols" value="<s:property value='name'/>" />
                    </s:div>
                    <s:div cssClass="left2" cssStyle="width: 15%;"><s:property value="name" /></s:div>
                    <s:div cssClass="left6" cssStyle="width: 82.1%;"><s:property value="title" /></s:div>
                </s:div><br>
            </s:else>
        </s:iterator>
    </s:div>
    <button type="button" onclick="runGenesFromPathwaySearch('<s:property value='geneSymbolElementId' />');">Retrieve Genes for selected Pathways</button>
</s:if>