// checkanddisplay
//
// This function used by the pca submission form to display
// and hide a section of the form.
// 
function checkanddisplay(chk){
  if (chk.checked == 1)
  {
    document.getElementById('principalComponentAnalysisForm_collapsiblediv').style.display = 'block';
    // alert("It is checked.  Thank You");
  }
  else
  {
    // alert("You didn't check it!");
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

