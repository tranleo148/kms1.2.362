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
        cm.sendNext("다음번에도 꼭 같이 게임하자!");
    } else if (status == 1) {
        cm.getPlayer().AddStarDustCoin(100);
        cm.warp(680000715, 0);
        cm.dispose();
    }
}