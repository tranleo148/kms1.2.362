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
		cm.gainItem(5062009, 100);
		cm.sendOkS("#i5062009##z5062009# 100개 획득!", 2);
		cm.gainItem(2630756, -1);
		cm.dispose();
	}
}
