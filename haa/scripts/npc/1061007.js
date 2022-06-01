var status = -1;

function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status --;
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
            cm.sendYesNo("이곳에서 나가시겠습니까?");
    } else if (status == 1) {
        	cm.warp(100000000,0);
        	cm.dispose();
    }
}