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
        cm.sendNextS("밖으로 나갈건가?", 4, 2143004);
    } else if (status == 1) {
        cm.warp(271041000, 0);
        cm.dispose();
    }
}