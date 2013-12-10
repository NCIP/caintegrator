<%@ taglib prefix="s" uri="/struts-tags"%>

<script type="text/javascript">
  function resetGenomicQueryForm() {
      var form =  $('#geneExpressionGenomicQueryInputForm');
      form.find('#resetSelected').val(true);
      form.attr('action', 'resetGenomicQueryBasedGEPlot.action');
      form.submit();
  }
  
  function createGenomicQueryPlot() {
      var form =  $('#geneExpressionGenomicQueryInputForm');
      form.find('#createPlotSelected').val(true);
      $('#genomicQueryGePlotDiv').html("<img src='images/ajax-loader-processing.gif' alt='ajax icon indicating loading process'/>");
      $.post('createGenomicQueryBasedGEPlot.action', form.serialize(), function(data) {
          $('#genomicQueryGePlotDiv').html(data);
      }, 'html');
  }
</script>


<s:form name="geneExpressionGenomicQueryInputForm" id="geneExpressionGenomicQueryInputForm" theme="simple">
    <s:token />                
    <s:hidden name="createPlotSelected" id="createPlotSelected" value="false" />
    <s:hidden name="resetSelected" id="resetSelected" value="false" />
    
       
    <!-- Gene Expression Inputs -->
    <h2>Genomic Query Based Gene Expression Plots</h2>
    
    <div class="tableheader" style="white-space: nowrap; position: relative; width: 25em;">
        <div class="tabhelp" style="white-space:nowrap; position: relative; margin-top: 0.4em; margin-right: 0.5em;">
           <a href="javascript:openWikiHelp('DYDnAg', 'id-6-AnalyzingStudies-GeneExpressionValuePlotforGenomicQueries')" class="help">
            &nbsp;</a>
        </div>
    </div>
    
    <table class="data">
            <tr>
                <td class="value_inline" style="min-width: 5px; width: 10px; padding-left: 10px;">
                1.)
                </td>
                <td class="value_inline" style="min-width: 5px; width: 190px;">
                    Select a Genomic Query:<br>
                    <div style="font-size: 75%; color: #666666;"></div>
                </td>
                <td class="value_inline">
                    <s:select
	                    list="gePlotForm.geneExpressionQueryBasedForm.queries"
	                    listValue="value.name"
	                    listKey="key"
	                    name="gePlotForm.geneExpressionQueryBasedForm.selectedQueryId" 
	                    size="5"
	                    multiple="false"
	                    theme="simple" />                
                </td>
            </tr>
        </table>
        <table class="data">
            <tr>
                <td class="value_inline" style="min-width: 5px; width: 10px; padding-left: 10px;">
                2.)
                </td>
                <td class="value_inline" style="min-width: 5px; width: 120px;">
                    Select Reporter Type:
                </td>
                <td class="value_inline">
	                <s:radio name="gePlotForm.geneExpressionQueryBasedForm.reporterType"
	                list="@gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum@getValueToDisplayableMap()"
	                listKey="key" 
	                listValue="value" />                
                </td>
            </tr>
        </table>
        <br>
        <div>
            <center>
                <button type="button" onclick="resetGenomicQueryForm();">Reset</button>
                <s:if test="creatable">
                    <button type="button" onclick="createGenomicQueryPlot();">Create Plot</button>
                </s:if>
            </center>
        </div>
    <br>
    <center>
        <s:div id="genomicQueryGePlotDiv"></s:div>
    </center>
</s:form>