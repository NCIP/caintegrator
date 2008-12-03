<%@ taglib prefix="s" uri="/struts-tags"%>

<script language="javascript">

    function setClinicalAnnotations(onOff){
        var size='<s:property value="getClinicalDefinitionsSize()"/>';
        for(i=1;i<=size;i++){
            var item = 'selectedClinicalAnnotations-' + i;
            document.manageQueryForm[item].checked = onOff;
        }
    }
    function setImageAnnotations(onOff){
        var size='<s:property value="getImageDefinitionsSize()"/>';
        for(i=1;i<=size;i++){
            var item = 'selectedImageAnnotations-' + i;
            document.manageQueryForm[item].checked = onOff;
        }
    }
    
</script>

<!--Columns-->

<s:div id="columns" label="Columns" theme="ajax">
    <s:if test="!manageQueryHelper.clinicalAnnotationDefinitions.isEmpty()">
        <h2>Select Results Display</h2>
        <div><b>Select Result Type: </b> <s:radio name="manageQueryHelper.resultType"
            list="@gov.nih.nci.caintegrator2.application.query.ResultTypeEnum@getValueToDisplayableMap()" listKey="key"
            listValue="value"></s:radio> <br>
        <br>
        <b>Select Reporter Type: </b> <s:radio name="manageQueryHelper.reporterType"
            list="@gov.nih.nci.caintegrator2.application.arraydata.ReporterTypeEnum@getValueToDisplayableMap()"
            listKey="key" listValue="value"></s:radio></div>

        <h2>Select Columns for Results</h2>

        <div class="checklistwrapper">

        <h3>Subject Annotations</h3>
        <ul class="checklist">
            <s:checkboxlist cssClass="checklist" name="selectedClinicalAnnotations"
                list="manageQueryHelper.clinicalAnnotationDefinitions" listKey="id" listValue="displayName"
                theme="cai2simple" value="getSelectedClinicalAnnotations()"></s:checkboxlist>
        </ul>
        <ul>
            <s:if test="!manageQueryHelper.clinicalAnnotationDefinitions.isEmpty()">
                <input type="button" value="Select All" onclick="setClinicalAnnotations(1)" />
                <input type="button" value="Unselect All" onclick="setClinicalAnnotations()" />
            </s:if>
        </ul>
        </div>

        <div class="checklistwrapper">
        <h3>Image Annotations</h3>
        <ul class="checklist">
            <s:checkboxlist cssClass="checklist" name="selectedImageAnnotations"
                list="manageQueryHelper.imageAnnotationDefinitions" listKey="id" listValue="displayName"
                theme="cai2simple" value="getSelectedImageAnnotations()"></s:checkboxlist>
        </ul>

        <ul>
            <s:if test="!manageQueryHelper.imageAnnotationDefinitions.isEmpty()">
                <input type="button" value="Select All" onclick="setImageAnnotations(1)" />
                <input type="button" value="Unselect All" onclick="setImageAnnotations()" />
            </s:if>
        </ul>
        </div>
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
    </s:if>
    <s:else>
        <p>These options are fixed when viewing a saved query. Click the Criteria tab to edit a new query.</p>
    </s:else>
</s:div>

<!--/Columns-->
