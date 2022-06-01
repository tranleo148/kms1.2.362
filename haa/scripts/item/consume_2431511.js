var status;
importPackage(Packages.server);

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
	cm.gainItem(2431511, -1);
	cm.gainItem(2049360, 1);
	cm.sendOkS("#b놀라운 장비강화 주문서를 얻었다!#k", 2)
	cm.dispose();
	}
}
