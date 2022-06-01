var status;

function start() {
    status = -1;
    action(1, 1, 0);
}

function action(mode, type, selection) {
    if (mode < 0) {
        cm.dispose();
    return;
    } else {
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
	if(cm.getPlayer().getMapId() == 992002000) {
	cm.sendYesNo("다시 위로 올라가겠어?");
	} else {
	cm.sendOk("별것도 아닌 일로 부르지 말라구!!");
	cm.dispose();
	}
} else if (status == 1) {
cm.warp(992002000);
cm.dispose();
}
}
}