<%@ taglib prefix="s" uri="/struts-tags"%>

<!--Columns-->
    
    <h2 class="resultsColumnsTabHeader">Select Results Type:</h2>
    <div class="tableheader" style="white-space: nowrap; position: relative; width: 25em;">
        <div class="tabhelp" style="white-space:nowrap; position: relative; margin-top: 0.4em; margin-right: 0.5em;">
            <a href="javascript:openHelpWindowWithNavigation('search_columns_help')" class="help">
            &nbsp;</a>
        </div>
    </div>
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
        <div class="selectorNote">Reporter Id - will display all reporter ids.<br>
                Gene - will display a median value for each gene.</div>    
        <br>
        <div class="reporterTypeSelector">
        <b>Select Results Orientation: </b> 
            <s:radio name="queryForm.resultConfiguration.orientation"
                list="@gov.nih.nci.caintegrator2.domain.application.ResultsOrientationEnum@getValueToDisplayableMap()"
                listKey="key" 
                listValue="value" />
        </div>  
        <div class="selectorNote">Genes in Rows / Subjects in Columns - will display the subjects and samples along the top and genes and reporters along the side.<br>
                Genes in Columns / Subjects in Rows - will display the genes and reporters along the top and subjects and samples along the side. Useful when there are many samples and few reporters.</div>    
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
