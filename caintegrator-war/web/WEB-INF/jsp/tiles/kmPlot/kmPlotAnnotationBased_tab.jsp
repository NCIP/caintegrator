<%@ taglib prefix="s" uri="/struts-tags"%>

<script type="text/javascript">
  function resetAnnotationForm() {
      var form =  $('#kaplanMeierAnnotationInputForm');
      form.attr('action', 'resetAnnotationBasedKMPlot.action');
      form.submit();
  }
  
  function createAnnotationPlot() {
      var form =  $('#kaplanMeierAnnotationInputForm');
      form.find('#createPlotSelected').val(true);
      $('#annotationKmPlotDiv').html("<img src='images/ajax-loader-processing.gif' alt='ajax icon indicating loading process'/>");
      $.post('createAnnotationBasedKMPlot.action', form.serialize(), function(data) {
          $('#annotationKmPlotDiv').html(data);
      }, 'html');
  }
</script>

<s:form name="kaplanMeierAnnotationInputForm" id="kaplanMeierAnnotationInputForm" theme="simple">
    <s:token />                
    <s:hidden name="createPlotSelected" id="createPlotSelected" value="false" />
    <s:hidden name="permissibleValuesNeedUpdate" value="false" />
       
    <!-- Kaplan-Meier Inputs -->
    <h2>Annotation Based Kaplan-Meier Survival Plots</h2>
    
        <table class="data">
            <tr>
                <th/>
                <th> Annotation Group</th>
                <th> Annotation </th>
                <th> Values </th>
            </tr>
            <tr>
                <td class="value_inline">
                    1.) Patient Groups:
                </td>
                <td class="value_inline">
                    <s:select name="kmPlotForm.annotationBasedForm.annotationGroupSelection" 
                              list="currentStudy.annotationGroups"
                              listValue="name" 
                              listKey="name"
                              headerKey="invalidSelection"
                              headerValue="Select Annotation Group"
                              onchange="showBusyDialog();document.kaplanMeierAnnotationInputForm.action = 'kmPlotUpdateAnnotationDefinitions.action';document.kaplanMeierAnnotationInputForm.submit();"
                              theme="simple"/>
                </td>
                <td class="value_inline">
                <s:select name="kmPlotForm.annotationBasedForm.selectedAnnotationId" 
                              list="kmPlotForm.annotationBasedForm.annotationFieldDescriptors"
                              listValue="value.definition.displayName"
                              headerKey="-1"
                              headerValue="Select Annotation"
                              onchange="showBusyDialog();document.kaplanMeierAnnotationInputForm.action = 'kmPlotUpdatePermissibleValues.action';document.kaplanMeierAnnotationInputForm.permissibleValuesNeedUpdate.value = 'true';document.kaplanMeierAnnotationInputForm.submit();"
                              theme="simple"/>
                </td>
                <td>
                <s:select name="kmPlotForm.annotationBasedForm.selectedValuesIds" 
                              list="kmPlotForm.annotationBasedForm.permissibleValues"
                              multiple="true"
                              theme="simple"/>
                </td>
            </tr>
        
        </table>
        <table class="data">
            <tr>
                <th/>
                <th> Survival Value </th>
            </tr>
            <tr>
                <td class="value_inline">
                    2.) Select Survival Measure:
                </td>
                <td class="value_inline">
                <s:select name="kmPlotForm.survivalValueDefinitionId" 
                      list="kmPlotForm.survivalValueDefinitions" 
                      listValue="value.name"
                      theme="simple"/>
                </td>
            </tr>
        </table>

        <br>
        <div>
        <center>
            <button type="button" onclick="resetAnnotationForm();">Reset</button>
            <button type="button" onclick="createAnnotationPlot();">Create Plot</button>
        </center>
        </div>
    <!-- /Kaplan-Meier Inputs -->
    <br><br>
    <center>
        <s:div id="annotationKmPlotDiv"></s:div>
    </center>
</s:form>