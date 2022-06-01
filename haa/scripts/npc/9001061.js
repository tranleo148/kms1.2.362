var status = -1;
var idx, skill, req, sp;

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
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
	cm.sendNextS("드래곤의 알을 발견했다!", 0x3);
    } else if (status == 1) {
	cm.dispose();
	cm.getPlayer().setKeyValue(15042, "Stage", "6");
	cm.warp(993000601);
    }
}