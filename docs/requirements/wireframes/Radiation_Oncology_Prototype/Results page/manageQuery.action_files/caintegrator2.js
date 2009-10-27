    // checkanddisplay
    //
    // This function used by the pca submission form to display
    // and hide a section of the form.
    // 
    function checkanddisplay(chk){
      if (chk.checked == 1)
      {
        document.getElementById('principalComponentAnalysisForm_collapsiblediv').style.display = 'block';
      }
      else
      {
        document.getElementById('principalComponentAnalysisForm_collapsiblediv').style.display = 'none';
        chk.checked = 0;
      }
    }
    
    function gotoCGAP(cgapUrl, geneSymbols) {
        var url = cgapUrl + document.getElementById(geneSymbols).value;
        
        var holdString = trimIt(document.getElementById(geneSymbols).value);
        
        if(holdString=="") {
            alert("No values entered.  Please enter a gene symbol.");
            return false;
        }
        
        window.open(url, 'cai2_CGAP');
    }
    
    function trimIt (str) {
        var str = str.replace(/^\s\s*/, ''),
            ws = /\s/,
            i = str.length;
        while (ws.test(str.charAt(--i)));
        return str.slice(0, i + 1);
    }
    
    function checkUncheckAll(theElement, excludeThisElementName) {
        var theForm = theElement.form, z = 0;
        for(z=0; z<theForm.length;z++){
            if(theForm[z].type == 'checkbox' && theForm[z].name != 'checkall' && theForm[z].name != excludeThisElementName){
                theForm[z].checked = theElement.checked;
            }
        }
    }
    
    
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
        var cbResults = '';
        var inputForm = document.caBioGeneSearchForm;
        var cb_symbols_length = inputForm.cb_symbols.length;
        if (cb_symbols_length == 0 || cb_symbols_length == null) { // Only 1 checkbox
            cbResults = inputForm.cb_symbols.value;
        } else { // Multiple checkboxes.
            for (var i = 0; i < inputForm.cb_symbols.length; i++ ) {
                if (inputForm.cb_symbols[i].checked == true) {
                    cbResults += inputForm.cb_symbols[i].value + ',';
                }
            }
        }
        document.getElementById(geneSymbolsTextbox).value = cbResults;
        hideCaBioInputForm(inputForm);
    }
    
    function ignoreReturnKey(e) {
        if (e.keyCode == 13) {
            return false;
        }
        return true;
    }