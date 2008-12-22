<%@ taglib prefix="s" uri="/struts-tags"%>

<!--Columns-->
    
    <h2>Select Results Display</h2>
    <div><b>Select Result Type: </b> 
        <s:radio name="queryForm.resultConfiguration.resultType"
            onchange="this.form.selectedAction.value = 'updateColumns'; this.form.submit();"
            list="@gov.nih.nci.caintegrator2.application.query.ResultTypeEnum@getValueToDisplayableMap()" 
            listKey="key"
            listValue="value"/><br>
    <s:if test="queryForm.resultConfiguration.resultType == 'genomic'">
        <br>
        <b>Select Reporter Type: </b> 
            <s:radio name="queryForm.resultConfiguration.reporterType"
                list="@gov.nih.nci.caintegrator2.application.arraydata.ReporterTypeEnum@getValueToDisplayableMap()"
                listKey="key" 
                listValue="value" />
    </s:if>
    </div>
    
    <s:if test="queryForm.resultConfiguration.resultType == 'clinical'">
        <h2>Select Columns for Results</h2>
        
        <div class="checklistwrapper">
        
        <h3>Subject Annotations</h3>
        <ul class="checklist">
            <s:checkboxlist cssClass="checklist" 
                name="queryForm.resultConfiguration.subjectColumns.values"
                list="queryForm.resultConfiguration.subjectColumns.options" 
                listKey="key" 
                listValue="displayValue"
                theme="cai2simple" 
                value="queryForm.resultConfiguration.subjectColumns.values" />
        </ul>
        <ul>
            <s:if test="!queryForm.resultConfiguration.subjectColumns.isEmpty()">
                <input type="button" value="Select All" onclick="setClinicalAnnotations(1)" />
                <input type="button" value="Unselect All" onclick="setClinicalAnnotations()" />
            </s:if>
        </ul>
        </div>
        
        <div class="checklistwrapper">
        <h3>Image Annotations</h3>
        <ul class="checklist">
            <s:checkboxlist cssClass="checklist" name="queryForm.resultConfiguration.imageSeriesColumns.values"
                list="queryForm.resultConfiguration.imageSeriesColumns.options" 
                listKey="key" 
                listValue="displayValue"
                theme="cai2simple" 
                value="queryForm.resultConfiguration.imageSeriesColumns.values" />
        </ul>
        
        <ul>
            <s:if test="!queryForm.resultConfiguration.imageSeriesColumns.isEmpty()">
                <input type="button" value="Select All" onclick="setImageAnnotations(1)" />
                <input type="button" value="Unselect All" onclick="setImageAnnotations()" />
            </s:if>
        </ul>
        </div>
    
    </s:if>
    
    <div class="actionsrow">
    <del class="btnwrapper">
    <ul class="btnrow">
        <li><s:a href="#" cssClass="btn"
            onclick="document.manageQueryForm.selectedAction.value='executeQuery';document.manageQueryForm.submit();">
            <span class="btn_img"><span class="search">Run Search</span></span>
        </s:a></li>
    </ul>
    </del>
    </div>
    
    <div class="clear"></div>

<!--/Columns-->
