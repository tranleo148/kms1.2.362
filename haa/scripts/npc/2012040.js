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
		if (cm.getPlayer().getEventInstance() != null) {
		    cm.sendYesNo("지하 수로 조사를 그만하고 이곳을 나가시려는 겁니까?");
		} else
		cm.dispose();
	} else if (status == 1) {
		if (cm.getPlayer().getEventInstance() != null) {
		    cm.warp(680000715, 0);
		}
		cm.dispose();
	}
}