function start() {
	status = -1;
	action(1, 1, 0);
}

function action(mode, type, selection) {
	itemid = 2431894;
	reward = 5062010;
	if (mode == 0) {
		cm.dispose();
		return;
	}
	if (mode == 1) {
		status++;
	}
	if (status == 0) {
		if (cm.itemQuantity(itemid) < 50) {
			cm.sendNext("#b#z"+itemid+"##r 50개#k를 모으면 #i"+reward+"##b#z"+reward+"# 10개#k를 받을 수 있습니다.");
			cm.dispose();
			return;
		} else {
			talk = "#b#z"+itemid+"# " + cm.itemQuantity(itemid) + "개#k를 가지고 있습니다. #z"+reward+"# 10개는 #r50개#k를 사용하여 교환 할 수 있습니다.\r\n\r\n"
			talk += "#L0##i"+reward+"# #b#z"+reward+"# 10개#k";
			cm.sendSimple(talk);
		}
	} else if (status == 1) {
		if (cm.itemQuantity(itemid) < 50) {
			cm.sendNext("#b#z"+itemid+"##r 50개#k를 모으면 #i"+reward+"##b#z"+reward+"# 10개#k를 받을 수 있습니다.");
		} else {
			cm.gainItem(itemid, -50);
			cm.gainItem(reward, 10);
		}
			cm.dispose();
			return;
	}
}