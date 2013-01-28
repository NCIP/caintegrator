<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<head/>

<body onload="document.nciaBasketForm.submit()">

    <s:url id="url" value="%{nciaBasketUrl}" />
    <form name="nciaBasketForm" method="POST" action="${url}">
        <s:iterator value="nciaBasket.imageSeriesIDs" id="seriesId">
            <s:hidden name="serieses" value="%{seriesId}" />
        </s:iterator>
        <s:iterator value="nciaBasket.imageStudyIDs" id="studyId">
            <s:hidden name="studies" value="%{studyId}" />
        </s:iterator>
        <s:iterator value="nciaBasket.imagePatientIDs" id="patientId">
            <s:hidden name="patients" value="%{patientId}" />
        </s:iterator>
    </form>

</body>
