<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<head/>

<body onload="document.viewIGVForm.submit()">
    View IGV URL = <s:property value="viewIGVUrl"/>
    <s:url id="url" value="%{viewIGVUrl}" />
    <form name="viewIGVForm" method="POST" action="${url}">
    </form>

</body>
