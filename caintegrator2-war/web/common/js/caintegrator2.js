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
    window.open(url, 'cai2_CGAP');
}