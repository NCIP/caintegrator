<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
            
<div id="content">

    <!--Page Help-->

    <div class="pagehelp"><a href="#" class="help"></a></div>

    <!--/Page Help-->           
    
    <!--ADD CONTENT HERE-->
    
    <h1>DICOM Retrieval</h1>
    <s:actionerror />
    <s:url id="downloadDicomFile" action="downloadDicomFile"/>
    <s:a href="%{downloadDicomFile}" cssClass="btn" cssStyle="margin:0 5px;"><span class="btn_img"><span class="add">Download DICOM Files</span></span></s:a>
        
            
</div>

<div class="clear"><br /></div>


