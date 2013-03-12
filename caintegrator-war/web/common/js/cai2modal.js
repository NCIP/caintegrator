//***********************//
// GeneList Modal Dialog //
//***********************//
	
	function showGeneListInputForm(geneSymbolElementIdValue) {
        document.getElementById('TB_overlay').style.display = 'block';
        document.getElementById('geneListSearchInputDiv').style.display = 'block';
        document.getElementById('geneListSearchInputDiv').style.visibility = 'visible';
        if (document.geneListSearchForm.geneListSearchTopicPublished.value == 'false') {
            document.geneListSearchForm.geneListSearchTopicPublished.value = true;
            dojo.event.topic.publish('geneListSearchTopic');
            dojo.event.topic.publish('searchGeneList');
        } else {
            document.getElementById('geneListSearchResultsDiv').style.display = 'block';
            document.getElementById('geneListSearchResultsDiv').style.visibility = 'visible';
        }
        document.geneListSearchForm.geneSymbolElementId.value = geneSymbolElementIdValue;
    }
    
    function runGeneListSearch() {
        document.geneListSearchForm.runSearchSelected.value = 'true';
        dojo.event.topic.publish('searchGeneList');
        document.getElementById('geneListSearchResultsDiv').style.display = 'block';
        document.getElementById('geneListSearchResultsDiv').style.visibility = 'visible';
    }
    
    function hideGeneListInputForm() {
        document.getElementById('TB_overlay').style.display = 'none';
        document.getElementById('geneListSearchInputDiv').style.visibility = 'hidden';
        hideGeneListSearchResults();
    }
    
    function hideGeneListSearchResults() {
        document.getElementById('geneListSearchResultsDiv').style.display = 'none';
        document.getElementById('geneListSearchResultsDiv').style.visibility = 'hidden';
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
        document.getElementById('TB_overlay').style.display = 'block';
        document.getElementById('bioDbNetSearchInputDiv').style.display = 'block';
        document.getElementById('bioDbNetSearchInputDiv').style.visibility = 'visible';
        dojo.event.topic.publish('bioDbNetSearchTopic');    
        document.bioDbNetSearchForm.geneSymbolElementId.value = geneSymbolElementIdValue;        
    }
    
    function hideBioDbNetInputForm() {
        document.getElementById('TB_overlay').style.display = 'none';
        document.getElementById('bioDbNetSearchInputDiv').style.visibility = 'hidden';
        hideBioDbNetSearchResults();
    }
    
    function hideBioDbNetSearchResults() {
        document.getElementById('bioDbNetSearchResultsDiv').style.display = 'none';
        document.getElementById('bioDbNetSearchResultsDiv').style.visibility = 'hidden';
    }
    
    function runBioDbNetSearch() {
        dojo.event.topic.publish('searchBioDbNet'); 
        document.getElementById('bioDbNetSearchResultsDiv').style.display = 'block';
        document.getElementById('bioDbNetSearchResultsDiv').style.visibility = 'visible';
    }
    
    function captureBioDbNetCheckBoxes(geneSymbolsTextbox) {
        var inputForm = document.bioDbNetSearchForm;
        captureCheckBoxes(inputForm, geneSymbolsTextbox);
        hideBioDbNetInputForm();
    }
    
    function captureCheckBoxes(inputForm, geneSymbolsTextbox) {
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
        document.getElementById(geneSymbolsTextbox).value = cbResults;
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
