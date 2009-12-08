<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

            
<div id="content">                      
    
    <!--Page Help-->

    <div class="pagehelp"><a href="javascript:openHelpWindowWithNavigation('external_links_help')" class="help">
   &nbsp;</a>
    </div>

    <!--/Page Help-->         
    
    <!--ADD CONTENT HERE-->

    <h1><s:property value="externalLinkList.name" /></h1>
    <strong>Description:  </strong>
        <s:if test="externalLinkList.description != null && externalLinkList.description != ''" >
            <s:property value="externalLinkList.description" />
        </s:if>
        <s:else>
            N/A
        </s:else>
    <br/> <br/>
    <ul>
    <s:iterator value="externalLinkList.externalLinks">
        <li>
        <a href='<s:property value="%{url}" escape="false"/>' target="_"><s:property value="name" /></a>
        </li>
    </s:iterator>
    </ul>
</div>

<div class="clear"><br /></div>
