var status;

function start() {
    status = -1;
    action(1, 1, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }
        if (status == 0) {
		cm.gainItem(2048716, 10);
		cm.sendOkS("#i2048716##z2048716# 10개 획득!", 2);
		cm.gainItem(2630755, -1);
		cm.dispose();
	}
}
