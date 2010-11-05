<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<head/>

<s:if test="hasActionErrors()">
    <s:actionerror/>
</s:if>
<s:else>
    <body onload="document.viewIGVForm.submit()">
        View Integrative Genomics Viewer URL = <s:property value="viewIGVUrl"/>
        <s:url id="url" value="%{viewIGVUrl}" />
        <form name="viewIGVForm" method="POST" action="${url}">
        </form>
    </body>
</s:else>