<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
     
    <s:actionerror />
    <!-- CaBio Gene Results -->
    <s:if test="%{!caBioGenes.empty}">
        <tr>
        <th> <input type="checkbox" name="checkall" onclick="checkUncheckAll(this);" checked="checked"/> </th>
        <th> Symbol </th>
        <th> Taxon </th>
        <th> Full Name </th>
        </tr><br>
	    <s:iterator value="caBioGenes">
	        <tr>
	           <td><input type="checkbox" checked="checked" name="cb_symbols" value="<s:property value='symbol'/>"/> </td>
	           <td><s:property value="symbol" /></td>
	           <td><s:property value="taxonCommonName" /> </td>
	           <td><s:property value="fullName" /></td>
	        </tr><br>
	    </s:iterator>
    <button type="button" onclick="captureCaBioCheckBoxes('<s:property value='geneSymbolElementId' />')">Use Genes</button>
    </s:if>
    <!-- /CaBio Gene Results -->


