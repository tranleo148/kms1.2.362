var enter = "\r\n";
var seld = -1;

var hair = [30070, 30080, 32570, 32590, 32600, 32610, 30090, 32580, 32610, 32620, 32630, 32640, 32650];

var price = 1; // 개수

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
		var color = cm.getPlayer().getHair() % 10;
		selStr = "";
		if (color > 0 || cm.getPlayer().getBaseColor() != -1 || cm.getPlayer().getAddColor() != 0) {
			cm.getPlayer().setBaseColor(-1);
			cm.getPlayer().setAddColor(0);
			cm.getPlayer().setBaseProb(0);
			cm.setAvatar(4000000, cm.getPlayer().getHair() - color);
		}
		cm.sendStyle(selStr, hair);
	} else if (status == 1) {
		cm.gainItem(2430018, -price);
		cm.setAvatar(4000000, hair[sel]);
		cm.dispose();

	}
}