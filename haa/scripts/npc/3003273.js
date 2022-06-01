var seld = -1;
var enter = "\r\n";

var itemid = -1;
var allstat = -1;
var atk = -1;

function start() {
	status = -1;
	action(1, 0, 0);
}
function action(mode, type, sel) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
    	}
	if (status == 0) {
	    cm.dispose();
	    cm.sendUI(1269);
	}
}