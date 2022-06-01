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
        cm.sendNext("이동하시려면 다시 말을 걸어 주세요.");
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        if (cm.getPlayer().getParty() == null || !cm.isLeader()) {
            cm.sendOk("파티장을 통해 진행해 주십시오.");
            cm.dispose();
            return;
        }
        cm.sendYesNo("석판에 쓰여진 글씨가 빛나더니 석판 뒤로 작은 문이 열렸다. 비밀통로를 이용하시겠습니까?");
    } else if (status == 1) {
        cm.warpParty(240050400);
        cm.dispose();
    }
}