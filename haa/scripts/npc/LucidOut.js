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
        if (cm.getPlayer().getMapId() == 450004300 || cm.getPlayer().getMapId() == 450004600) {
            cm.sendYesNoS("루시드를 물리쳤다. '악몽의 시계탑'으로 돌아갈까?", 0x26);
        } else {
            cm.sendYesNoS("이곳은 위험하다. 전투를 포기하고 밖으로 나갈까?", 0x26);
        }
    } else if (status == 1) {
        cm.getPlayer().addKV("bossPractice", "0");
        cm.warp(450004000, 1);
        cm.dispose();
    }
}