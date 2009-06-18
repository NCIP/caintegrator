<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
     
    <s:actionerror />
    <!-- CaBio Gene Results -->
    <s:if test="%{!caBioGenes.empty}">
    <table class="data">
        <tr>
        <th> </th>
        <th> Symbol </th>
        <th> Taxon </th>
        <th> Full Name </th>
        </tr>
	    <s:iterator value="caBioGenes">
	        <tr>
	           <td><input type="checkbox" checked="checked" name="cb_symbols" value="<s:property value='symbol'/>"/> </td>
	           <td><s:property value="symbol" /></td>
	           <td><s:property value="taxonCommonName" /> </td>
	           <td><s:property value="fullName" /></td>
	        </tr>
	    </s:iterator>
    </table>
    <button type="button" onclick="captureCaBioCheckBoxes(<s:property value='formName' />, '<s:property value='geneSymbolElementId' />')">Use Genes</button>
    </s:if>
    <!-- /CaBio Gene Results -->


