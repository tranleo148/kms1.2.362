var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        cm.sendYesNo("이 곳에서 나가겠는가? 다음번에 들어올 때는 처음부터 다시 도전해야 한다네.");
    } else if (status == 1) {
        cm.warp(211042300, "ps00");
        cm.dispose();
    }
}