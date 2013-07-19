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

    /**
     * Replace a URL parameter (or change it if it already exists)
     * @param {theurl} string  this is the url string as input
     * @param {paramName}    string  the key to set
     * @param {newValue}    string  value 
     */    
    function replace_param(theurl, paramName, newValue) {
    	var uri_array = theurl.split('?'); // break up url/query
    	var params_array = uri_array[1].split('&'); // break up the query
    	var items_array = new Array;
    	for (i=0; i<params_array.length; i++) {
	    	items_array = params_array[i].split('='); // split name/value pairs
	    	if (items_array[0] == paramName) {
	    	params_array[i] = items_array[0] + '=' + newValue;
	    	} // end if
    	} // end for i
    	return uri_array[0] + '?' + params_array.join('&');
	} // end function     

    /**
     * Takes an Object and updates its href string by appending
     * the struts token parameters found elsewhere in this page.
     * @param {obj} string the object to operate on
     */    
    function updateUrlTokenParameters(obj){
    	// retrieve the struts tokens from elsewhere in this page.
    	var tokenName = document.getElementsByName('struts.token.name')[0].value;
    	var token = document.getElementsByName('token')[0].value;
    	// modify the href of the object
    	obj.href = replace_param(obj.href, 'struts.token.name', tokenName);
    	obj.href = replace_param(obj.href, 'token', token);
    }