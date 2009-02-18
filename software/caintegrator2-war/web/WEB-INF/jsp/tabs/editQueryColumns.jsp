<%@ taglib prefix="s" uri="/struts-tags"%>

<!--Columns-->
    
    <h2 class="resultsColumnsTabHeader">Select Results Type:</h2>
    <div class="resultTypeSelector">
        <s:radio name="queryForm.resultConfiguration.resultType"
            onclick="this.form.selectedAction.value = 'updateColumns'; this.form.submit();"
            list="@gov.nih.nci.caintegrator2.domain.application.ResultTypeEnum@getValueToDisplayableMap()" 
            listKey="key"
            listValue="value"/><br>
        
        <div class="selectorNote">Genomic result type - will display a gene expression data matrix.<br>
                Clinical result type - will display tabular data, including column selection.</div>
    <s:if test="queryForm.resultConfiguration.resultType == 'genomic'">
        <br>
        <div class="reporterTypeSelector">
        <b>Select Reporter Type: </b> 
            <s:radio name="queryForm.resultConfiguration.reporterType"
                list="@gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum@getValueToDisplayableMap()"
                listKey="key" 
                listValue="value" />
        </div>
        <div class="selectorNote">Probe Set - will display all probe sets.<br>
                Gene - will display an average value for each gene.</div>    
    </s:if>
    </div>
    
    <s:if test="queryForm.resultConfiguration.resultType == 'clinical'">
        <div style="margin-top: 10px;">
        <h2>Select Columns for Results</h2>
        
            <div class="checklistwrapper">
            
            <h3>Subject Annotations</h3>
            <ul class="checklist">
                <s:checkboxlist 
                    id="subjectColumnsId"
                    cssClass="checklist" 
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
        </div>    
        
        <div class="checklistwrapper">
        <h3>Image Annotations</h3>
        <ul class="checklist">
            <s:checkboxlist 
                id="imageSeriesColumnsId"
                cssClass="checklist" 
                name="queryForm.resultConfiguration.imageSeriesColumns.values"
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
            onclick="runSearch()">
            <span class="btn_img"><span class="search">Run Query</span></span>
        </s:a></li>
    </ul>
    </del>
    </div>
    
    <div class="clear"></div>

<!--/Columns-->
