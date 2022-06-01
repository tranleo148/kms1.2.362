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
	cm.dispose();
	cm.sendOk("몬스터파크는 잘 즐겼나? 보상을 챙겨 주었네, 크크...", 9071000);
    }
}