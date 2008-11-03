// prepareFormForSubmit
//
// This function modifies the html form prior
// to submission. 
function prepareFormForSubmit(form, actionParameter, selectedTab){

    var holdFormAction;
    
    holdFormAction = form.action;
    
    // set the value for the selectedAction parameter
    form.selectedAction.value = actionParameter;
    
    // add the tab name selection to the form action
    form.action = holdFormAction + "#" + selectedTab;
}