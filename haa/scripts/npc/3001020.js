var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    setting = [
        ["Hard_Magnus", 2, 401060100, 175],
        ["Normal_Magnus", 2, 401060200, 155],
        ["Easy_Magnus", 2, 401060300, 115]
    ]
    name = ["하드", "노멀", "이지"]
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        talk = "매그너스 퇴치를 위해 폭군의 왕좌로 이동 하시겠습니까??\r\n"
        talk += "#L0##b폭군의 왕좌(하드)로 이동 한다. (레벨 175이상)#l\r\n"
        talk += "#L1#폭군의 왕좌(노멀)로 이동 한다. (레벨 155이상)#l\r\n"
        talk += "#L2#폭군의 왕좌(이지)로 이동 한다. (레벨 115이상)#l\r\n"
        talk += "#L3#이동하지 않는다.#l"
        cm.sendSimple(talk);
    } else if (status == 1) {
        st = selection;
        if (cm.getPlayer().getParty() == null) {
            cm.sendOk("1인 이상의 파티에 속해야만 입장할 수 있습니다.");
            cm.dispose();
            return;
        } else if (!cm.isLeader()) {
            cm.sendOk("파티장만 입장을 신청할 수 있습니다.");
            cm.dispose();
            return;
        } else if (cm.getPlayerCount(setting[st][2]) >= 1) {
            cm.sendOk("이미 누군가가 매그너스에 도전하고 있습니다.");
            cm.dispose();
            return;
        } else if (!cm.allMembersHere()) {
            cm.sendOk("모든 멤버가 같은 장소에 있어야 합니다.");
            cm.dispose();
            return;
        }
        if (!cm.isBossAvailable(setting[st][0], setting[st][1])) {
            c = 1;
            cm.sendOkS(cm.isBossString(setting[st][0]), 0x04, 9010061);
            cm.dispose();
            return;
        } else if (!cm.isLevelAvailable(setting[st][3])) {
            c = 2;
            cm.sendNext("폭군의 왕좌(" + name[st] + " 모드)는 레벨 " + setting[st][3] + "이상만 입장이 가능합니다.");
            cm.dispose();
            return;
        } else {
            cm.addBoss(setting[st][0]);
            em = cm.getEventManager(setting[st][0]);
            if (em != null) {
                cm.getEventManager(setting[st][0]).startInstance_Party(setting[st][2] + "", cm.getPlayer());
            }
            cm.dispose();
        }
    } else if (status == 2) {
        talk = "파티원 중 #b#e"
        if (c == 1) {
            for (i = 0; i < cm.BossNotAvailableChrList(setting[st][0], setting[st][1]).length; i++) {
                if (i != 0) {
                    talk += ", "
                }
                talk += "#b#e" + cm.BossNotAvailableChrList(setting[st][0], setting[st][1])[i] + ""
            }
        } else {
            for (i = 0; i < cm.LevelNotAvailableChrList(setting[st][3]).length; i++) {
                if (i != 0) {
                    talk += ", "
                }
                talk += "#b#e" + cm.LevelNotAvailableChrList(setting[st][3])[i] + ""
            }
        }
        talk += "#k#n 님이 들어갈 수 있는 자격이 없습니다.";
        cm.sendNext(talk);
        cm.dispose();
    }
}