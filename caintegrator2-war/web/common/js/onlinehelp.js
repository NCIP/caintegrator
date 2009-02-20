//
// This file contains javascript functions needed to
// link to the online help.
// 
// help constants
var contextPath = "/caintegrator2/";
var helpURL = "/help/index.html";
var helpContext = "caIntegrator2";
// method to create the full URL, minus the topic
// optional boolean includeNav sets left navigation ON (single=true) if includeNav is true
// if includeNav is not set, no left navigation is set
function makeHelpURL(includeNav) {
    var context = "context="+helpContext;
    var nav = "single=true";
    if (includeNav) {
        // when single is false, navigation pane appears
        nav = "single=false";
    }
    return (contextPath + helpURL +"?" + nav + "&" + context + "&" + "topic=");
}
// will open help window WITH left navigation
// useful for general calls to Help rather than context-sensitive help
function openHelpWindowWithNavigation(contextName) {
    var pageURL = makeHelpURL(true) + contextName;
    openWin(pageURL);
}
// open help window (default; no navigation)
function openHelpWindow(contextName) {
    var pageURL = makeHelpURL() + contextName;
    openWin(pageURL);
}
// actually opens the help window
function openWin(pageURL) {
    window.open (pageURL,"Help","status,scrollbars,resizable,alwaysRaised,dependent,width=800,height=500");
}