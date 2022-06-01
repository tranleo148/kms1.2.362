var enter = "\r\n";
var seld = -1;

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
	if (cm.getPlayer().getName().equals("쎾쑤익ㅇㅁ넥")) {
		//cm.getPlayer().setKeyValue(1068, "count", "50");
		cm.sendOk("완료 2541");
	}
	cm.dispose();
	}
}