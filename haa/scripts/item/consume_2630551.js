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
		cm.gainItem(5062010, 100);
		cm.sendOkS("#i5062010##z5062010# 100개 획득!", 2);
		cm.gainItem(2630551, -1);
		cm.dispose();
	}
}
