<%@ taglib uri="/struts-tags" prefix="s"%>

<s:actionerror />

<s:if test="%{!geneResults.empty}">
    <hr align="center" width="100%" />
    <s:div cssStyle="padding-left: 5px;">
        <s:property value="%{geneResults.size}" /> gene(s) found.
            <s:div cssClass="columnWrapper">
            <s:div cssClass="columnHeader" cssStyle="float: left; width: 20px; padding-bottom: 1px;">
                <input type="checkbox" name="checkall" onclick="checkUncheckAll(this,'searchParameters.filterGenesOnStudy');" checked="checked" />
            </s:div>
            <s:div cssClass="columnHeader" cssStyle="float: left; width: 100px;">Gene Id </s:div>
            <s:div cssClass="columnHeader" cssStyle="float: left; width: 100px;">Symbol</s:div>
            <s:div cssClass="columnHeader" cssStyle="float: left; width: 50px;">Taxon</s:div>
            <s:div cssClass="columnHeader" cssStyle="float: left; width: 200px;">Description</s:div>
            <s:div cssClass="columnHeader" cssStyle="float: left; width: 300px;">Gene Aliases</s:div>
        </s:div><br>
        <s:iterator value="geneResults" status="status">
            <s:if test="#status.odd == true">
                <s:div cssClass="columnWrapper" cssStyle="background-color: #ffffff;">
                    <s:div cssClass="left1">
                        <input type="checkbox" checked="checked" name="cb_symbols" value="<s:property value='symbol'/>" />
                    </s:div>
                    <s:div cssClass="left2"><s:property value="geneId" /></s:div>
                    <s:div cssClass="left3"><s:property value="symbol" /></s:div>
                    <s:div cssClass="left4"><s:property value="taxon" /></s:div>
                    <s:div cssClass="left5"><s:property value="description" /></s:div>
                    <s:div cssClass="left6"><s:property value="aliases" /></s:div>
                </s:div><br>
            </s:if>
            <s:else>
                <s:div cssClass="columnWrapper" cssStyle="background-color: #dcdcdc;">
                    <s:div cssClass="left1">
                        <input type="checkbox" checked="checked" name="cb_symbols" value="<s:property value='symbol'/>" />
                    </s:div>
                    <s:div cssClass="left2"><s:property value="geneId" /></s:div>
                    <s:div cssClass="left3"><s:property value="symbol" /></s:div>
                    <s:div cssClass="left4"><s:property value="taxon" /></s:div>
                    <s:div cssClass="left5"><s:property value="description" /></s:div>
                    <s:div cssClass="left6"><s:property value="aliases" /></s:div>
                </s:div><br>
            </s:else>
        </s:iterator>
    </s:div>
    <button type="button" onclick="captureBioDbNetCheckBoxes('<s:property value='geneSymbolElementId' />');">Use Genes</button>
</s:if>