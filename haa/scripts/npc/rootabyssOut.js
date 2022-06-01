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
        cm.sendOkS("기왕 열쇠를 사용하고 들어왔는데 보스까지 클리어하도록 하자.", 2);
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        cm.sendYesNoS("이미 열쇠를 써버렸다. 소모된 열쇠가 아까우니 이대로 나가는 것보다 보스를 처치하는 것이 더 좋을 것 같은데.. 그래도 그냥 나갈까?", 2);
    } else if (status == 1) {
        cm.dispose();
        if (mode == 1) {
            cm.warp(105200000);
            cm.setDeathcount(0);
        }
    }
}