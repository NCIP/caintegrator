
var PageName = 'Kaplan Meier Plot By Gene Expression';
var PageId = 'p4a8acf0c1fc04b3c936ae79099c4f91b'
var PageUrl = 'Kaplan_Meier_Plot_By_Gene_Expression.html'
document.title = 'Kaplan Meier Plot By Gene Expression';

if (top.location != self.location)
{
	if (parent.HandleMainFrameChanged) {
		parent.HandleMainFrameChanged();
	}
}

var $OnLoadVariable = '';

var $CSUM;

var hasQuery = false;
var query = window.location.hash.substring(1);
if (query.length > 0) hasQuery = true;
var vars = query.split("&");
for (var i = 0; i < vars.length; i++) {
    var pair = vars[i].split("=");
    if (pair[0].length > 0) eval("$" + pair[0] + " = decodeURI(pair[1]);");
} 

if (hasQuery && $CSUM != 1) {
alert('Prototype Warning: Variable values were truncated.');
}

function GetQuerystring() {
    return encodeURI('#OnLoadVariable=' + $OnLoadVariable + '&CSUM=1');
}

function PopulateVariables(value) {
  value = value.replace(/\[\[OnLoadVariable\]\]/g, $OnLoadVariable);
  value = value.replace(/\[\[PageName\]\]/g, PageName);
  return value;
}

function OnLoad() {

}

var u10 = document.getElementById('u10');

var u5 = document.getElementById('u5');
gv_vAlignTable['u5'] = 'top';
var u0 = document.getElementById('u0');

var u13 = document.getElementById('u13');

var u3 = document.getElementById('u3');
gv_vAlignTable['u3'] = 'top';
var u9 = document.getElementById('u9');

var u6 = document.getElementById('u6');

var u1 = document.getElementById('u1');
gv_vAlignTable['u1'] = 'center';
var u12 = document.getElementById('u12');

var u4 = document.getElementById('u4');
gv_vAlignTable['u4'] = 'top';
var u11 = document.getElementById('u11');
gv_vAlignTable['u11'] = 'top';
var u7 = document.getElementById('u7');

var u2 = document.getElementById('u2');

var u8 = document.getElementById('u8');
gv_vAlignTable['u8'] = 'top';
if (window.OnLoad) OnLoad();
