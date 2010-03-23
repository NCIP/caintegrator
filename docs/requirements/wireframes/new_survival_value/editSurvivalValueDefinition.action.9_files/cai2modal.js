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
    
//********************//
// CaBio Modal Dialog //
//********************//
    
    function showCaBioInputForm(geneSymbolElementIdValue) {
        document.getElementById('TB_overlay').style.display = 'block';
        document.getElementById('caBioGeneSearchInputDiv').style.display = 'block';
        document.getElementById('caBioGeneSearchInputDiv').style.visibility = 'visible';
        if (document.caBioGeneSearchForm.caBioGeneSearchTopicPublished.value == 'false') {
            document.caBioGeneSearchForm.caBioGeneSearchTopicPublished.value = true;
            dojo.event.topic.publish('caBioGeneSearchTopic');    
        } else {
            hideCaBioSearchResults();
        }
        document.caBioGeneSearchForm.geneSymbolElementId.value = geneSymbolElementIdValue;        
    }
    
    function runCaBioSearch() {
        document.caBioGeneSearchForm.runSearchSelected.value = 'true';
        document.caBioGeneSearchForm.runCaBioGeneSearchFromPathways.value = 'false';
        dojo.event.topic.publish('searchCaBio'); 
        document.getElementById('caBioGeneSearchResultsDiv').style.display = 'block';
        document.getElementById('caBioGeneSearchResultsDiv').style.visibility = 'visible';
    }
    
    function runCaBioPathwayGeneSearch(numberPathways) {
        var checkedBoxesString = "";
        for(i=0; i<numberPathways; i++) {
            if (document.getElementById("caBioPathwayCkBox_" + i).checked) {
                checkedBoxesString += i + " ";
            }
        }
        document.caBioGeneSearchForm.checkedPathwayBoxes.value = checkedBoxesString;
        document.caBioGeneSearchForm.runSearchSelected.value = 'false';
        document.caBioGeneSearchForm.runCaBioGeneSearchFromPathways.value = 'true';
        dojo.event.topic.publish('searchCaBio'); 
        document.getElementById('caBioGeneSearchResultsDiv').style.display = 'block';
        document.getElementById('caBioGeneSearchResultsDiv').style.visibility = 'visible';
    }
    
    
    function hideCaBioInputForm() {
        document.getElementById('TB_overlay').style.display = 'none';
        document.getElementById('caBioGeneSearchInputDiv').style.visibility = 'hidden';
        hideCaBioSearchResults();
    }
    
    function hideCaBioSearchResults() {
        document.getElementById('caBioGeneSearchResultsDiv').style.display = 'none';
        document.getElementById('caBioGeneSearchResultsDiv').style.visibility = 'hidden';
    }
    
    function captureCaBioCheckBoxes(geneSymbolsTextbox) {
        var inputForm = document.caBioGeneSearchForm;
        captureCheckBoxes(inputForm, geneSymbolsTextbox);
        hideCaBioInputForm(inputForm);
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
