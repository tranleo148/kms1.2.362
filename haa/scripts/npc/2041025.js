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
        cm.sendYesNo("삐리 삐리~ 저를 통해 안전한 곳으로 나가실 수 있습니다.\r\n"
        +"삐리 삐리~ 이대로 밖으로 나가시겠습니까?");
    } else if (status == 1) {
        cm.warp(220080000);
        cm.dispose();
    }
}