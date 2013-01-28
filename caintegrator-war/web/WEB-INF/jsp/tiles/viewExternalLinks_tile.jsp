<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

            
<div id="content">                      
    <!--ADD CONTENT HERE-->
    
    <h1><s:property value="externalLinkList.name" /></h1>
        <s:if test="externalLinkList.description != null && externalLinkList.description != ''" >
            <strong>Description:  </strong>
            <s:property value="externalLinkList.description" />
        </s:if>
    <br/> <br/>
    <div class="form_wrapper_outer">
        <s:iterator value="externalLinkList.linksByCategory" >
        <table class="form_wrapper_table">
        <tr>
            <th class="title"><s:property value="key"/></th>
        </tr>
        <tr>
        <s:iterator value="value">
            <tr height="30px">
            <td style="padding: 5px;">
            <a href='<s:property value="%{url}" escape="false"/>' target="_"><s:property value="name" /></a>
            </td>
            </tr>
        </s:iterator>
        </tr>
        </table>
        </s:iterator>
    </div>
</div>

<div class="clear"><br /></div>
