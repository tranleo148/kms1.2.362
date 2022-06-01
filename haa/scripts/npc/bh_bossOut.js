var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        var eim = cm.getPlayer().getEventInstance();
        if (eim.getProperty("stage") == "3") {
            cm.sendYesNoS("스우를 물리쳤다.코어 입구로 돌아갈까?", 0x26);
        } else {
            cm.sendYesNoS("이곳은 위험하다. 전투를 포기하고 밖으로 나갈까?", 0x26);
        }
    } else if (status == 1) {
        cm.warp(350060300, 1);
        cm.dispose();
    }
}