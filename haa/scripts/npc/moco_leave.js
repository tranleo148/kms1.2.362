function start() {
    status = -1;
    action(1, 0, 0);
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
	cm.sendYesNo("정말로 퇴장하시겠습니까?");
    } else if (status == 1) {
	cm.setDeathcount(0);
	cm.warp(1000000);
	cm.dispose();
    }
}