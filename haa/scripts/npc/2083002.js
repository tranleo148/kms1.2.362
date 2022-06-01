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
    if (mode == 0) {
        cm.sendNext("다시 생각해 보시고 말을 걸어 주세요.");
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        mapid = cm.getPlayer().getMapId();
        if (mapid >= 240060000) {
            st = 240050400;
            cm.sendYesNo("전투를 그만두고 밖으로 나가시겠습니까?");
        } else {
            st = 240050000;
            cm.sendYesNo("동굴 입구로 돌아가시겠습니까?")
        }
    } else if (status == 1) {
        cm.warp(st);
        cm.dispose();
    }
}