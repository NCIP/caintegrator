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
            if (excludeThisElementName == null) {
                if(theForm[z].type == 'checkbox' && theForm[z].name != 'checkall'){
                    theForm[z].checked = theElement.checked;
                }
            } else {
                if(theForm[z].type == 'checkbox' && theForm[z].name != 'checkall' && theForm[z].name != excludeThisElementName){
                    theForm[z].checked = theElement.checked;
                }
            }
        }
    }
    
    function ignoreReturnKey(e) {
        if (e.keyCode == 13) {
            return false;
        }
        return true;
    }