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
        if (eim.getProperty("stage") == "4") {
            cm.sendYesNoS("데미안을 물리쳤다. '세계수 정상으로 가는 길'로 돌아갈까?", 0x26);
        } else {
            cm.sendYesNoS("이곳은 위험하다. 전투를 포기하고 밖으로 나갈까?", 0x26);
        }
    } else if (status == 1) {
        cm.warp(105300303, 1);
        cm.dispose();
    }
}