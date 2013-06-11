//***********************//
// GeneList Modal Dialog //
//***********************//
	
	function showGeneListInputForm(geneSymbolElementIdValue) {
	    $('#TB_overlay').show();
	    $('#geneListSearchInputDiv').html("<img src='images/ajax-loader.gif' alt='ajax icon indicating loading process'/>");
	    $('#geneListSearchInputDiv').show();
        $.when($.get('geneListSearchInput.action', function(data) {
                $('#geneListSearchInputDiv').html(data);
            }
        )).done(function() {
            runGeneListSearch();
        });
        $('#geneListSearchForm').find('[name=geneSymbolElementId]').val(geneSymbolElementIdValue);
    }
    
    function runGeneListSearch() {
        var form = $('#geneListSearchForm');
        $('#geneListSearchResultsDiv').html("<img src='images/ajax-loader.gif' alt='ajax icon indicating loading process'/>");
        $('#geneListSearchResultsDiv').show();
        $.post('geneListSearch.action', form.serialize(), function(data) {
            $('#geneListSearchResultsDiv').html(data);
        }, 'html');
    }
    
    function hideGeneListInputForm() {
        $('#TB_overlay').hide();
        $('#geneListSearchInputDiv').hide();
        $('#geneListSearchResultsDiv').hide();
    }
    
    function captureGeneListCheckBoxes(geneSymbolsTextbox) {
        var inputForm = document.geneListSearchForm;
        captureCheckBoxes(inputForm, geneSymbolsTextbox);
        hideGeneListInputForm(inputForm);
    }
    
    function captureGeneListStringValue(geneSymbolsTextBox, geneSymbolsString) {
    	document.getElementById(geneSymbolsTextBox).value = geneSymbolsString;
    	hideGeneListInputForm(document.geneListSearchForm);
    }
    
    function showBioDbNetInputForm(geneSymbolElementIdValue) {
        $('#TB_overlay').show();
        $('#bioDbNetSearchInputDiv').html("<img src='images/ajax-loader.gif' alt='ajax icon indicating loading process'/>");
        $('#bioDbNetSearchInputDiv').show();
        $.get('bioDbNetSearchInput.action', function(data) {
                $('#bioDbNetSearchInputDiv').html(data);
            }
        );
        $('#bioDbNetSearchForm').find('[name=geneSymbolElementId]').val(geneSymbolElementIdValue);
    }
    
    function hideBioDbNetInputForm() {
        $('#TB_overlay').hide();
        $('#bioDbNetSearchInputDiv').hide();
        $('#bioDbNetSearchResultsDiv').hide();
    }
    
    function runBioDbNetSearch() {
        $('#bioDbNetSearchResultsDiv').html("<img src='images/ajax-loader.gif' alt='ajax icon indicating loading process'/>");
        $.post('bioDbNetSearch.action', $('#bioDbNetSearchForm').serialize(), function(data) {
            $('#bioDbNetSearchResultsDiv').html(data);
        }, 'html');
        $('#bioDbNetSearchResultsDiv').show();
    }
    
    function runGenesFromPathwaySearch(geneSymbolsTextbox) {
        var inputForm = document.bioDbNetSearchForm;
        var pathways = retrieveCheckedValues(inputForm, geneSymbolsTextbox);
        $('#inputValues')[0].value = pathways;
        $('#searchType')[0].value = 'PATHWAY';
        runBioDbNetSearch();
    }
    
    function captureBioDbNetCheckBoxes(geneSymbolsTextbox) {
        var inputForm = document.bioDbNetSearchForm;
        captureCheckBoxes(inputForm, geneSymbolsTextbox);
        hideBioDbNetInputForm();
    }
    
    function captureCheckBoxes(inputForm, geneSymbolsTextbox) {
        document.getElementById(geneSymbolsTextbox).value = retrieveCheckedValues(inputForm, geneSymbolsTextbox);
    }
    
    function retrieveCheckedValues(inputForm, geneSymbolsTextbox) {
        var cbResults = '';
        var cb_symbols_length = inputForm.cb_symbols.length;
        if (cb_symbols_length == 0 || cb_symbols_length == null) { // Only 1 checkbox
            cbResults = inputForm.cb_symbols.value;
        } else { // Multiple checkboxes.
            for (var i = 0; i < inputForm.cb_symbols.length; i++ ) {
                if (inputForm.cb_symbols[i].checked == true) {
                    if (cbResults != '') {
                        cbResults += ',';
                    }
                    cbResults += inputForm.cb_symbols[i].value;
                }
            }
        }
        return cbResults;
    }
    
//**************************//
// subjectList Modal Dialog //
//**************************//
     function showSubjectListForm() {
         document.getElementById('TB_overlay').style.display = 'block';
         document.getElementById('subjectlistdiv').style.display = 'block';
         document.getElementById('subjectlistdiv').style.visibility = 'visible';
     }
     
     function submitSubjectListForm() {
    	 document.manageQueryForm.subjectListName.value = document.getElementById("slName").value;
         document.manageQueryForm.subjectListDescription.value = document.getElementById("slDescription").value;
         if (document.getElementById("slVisibleToOthers") != null) {
             document.manageQueryForm.subjectListVisibleToOthers.value = document.getElementById("slVisibleToOthers").checked;
         }
         submitForm("saveSubjectList");
     }
     
     function hideSubjectListForm() {
         document.getElementById('TB_overlay').style.display = 'none';
         document.getElementById('subjectlistdiv').style.visibility = 'hidden';
     }
     
 //**************************//
 // busyDialog Modal Dialog //
 //**************************//
       function showBusyDialog() {
           document.getElementById('TB_overlay').style.display = 'block';
           ProgressImg = document.getElementById('busyDialogProcessingImage');
           document.getElementById('busyDialogDiv').style.display = 'block';
           document.getElementById('busyDialogDiv').style.visibility = 'visible';
           setTimeout("ProgressImg.src = ProgressImg.src",100);
       }
       
       function hideBusyDialog() {
           document.getElementById('TB_overlay').style.display = 'none';
           document.getElementById('busyDialogDiv').style.display = 'none';
           document.getElementById('busyDialogDiv').style.visibility = 'hidden';
       }
