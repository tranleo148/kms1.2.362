var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    setting = [
        ["Easy_Lucid", 2, 450003800, 220],
        ["Normal_Lucid", 2, 450004100, 220],
        ["Hard_Lucid", 2, 450004400, 220],
        ["Easy_Lucid", 2, 450003800, 220],
        ["Normal_Lucid", 2, 450004100, 220],
        ["Hard_Lucid", 2, 450004400, 220]
    ]
    name = ["이지", "노멀", "하드", "이지 연습", "노멀 연습", "하드 연습"]
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        talk = "#e<보스: 루시드>#n\r\n"
        talk += "루시드를 막지 못한다면, 무서운 일이 일어날 것입니다.\r\n\r\n"
        talk += "#L0# #b<보스: 루시드(이지)> 입장을 신청한다.#l\r\n"
        talk += "#L1# #b<보스: 루시드(노멀)> 입장을 신청한다.#l\r\n"
        talk += "#L2# #b<보스: 루시드(하드)> 입장을 신청한다.#l\r\n"
        talk += "#L3# #b<보스: 루시드(이지 연습)> 입장을 신청한다.#l\r\n"
        talk += "#L4# #b<보스: 루시드(노멀 연습)> 입장을 신청한다.#l\r\n"
        talk += "#L5# #b<보스: 루시드(하드 연습)> 입장을 신청한다.#l\r\n"
        cm.sendSimple(talk);
    } else if (status == 1) {
        st = selection;
        if (cm.getParty() == null) {
            cm.sendOk("파티를 맺어야만 입장할 수 있습니다.")
            cm.dispose();
            return;
        } else if (!cm.isLeader()) {
            cm.sendOk("파티장만이 입장을 신청할 수 있습니다.");
            cm.dispose();
            return;
        } else if (!cm.allMembersHere()) {
            cm.sendOk("모든 멤버가 같은 장소에 있어야 합니다.");
            cm.dispose();
            return;
        }
        if (selection == 0 || selection == 3) {
            if (cm.getPlayerCount(setting[st][2]) >= 1 || cm.getPlayerCount(Number(setting[st][2]) + 40) || cm.getPlayerCount(Number(setting[st][2]) + 80) || cm.getPlayerCount(Number(setting[st][2]) + 120) || cm.getPlayerCount(Number(setting[st][2]) + 160) >= 1) {
                cm.sendOk("이미 누군가가 루시드에 도전하고 있습니다.\r\n다른채널을 이용 해 주세요.");
                cm.dispose();
                return;
            }
        } else {
            if (cm.getPlayerCount(setting[st][2]) >= 1 || cm.getPlayerCount(Number(setting[st][2]) + 50) || cm.getPlayerCount(Number(setting[st][2]) + 100) || cm.getPlayerCount(Number(setting[st][2]) + 150) || cm.getPlayerCount(Number(setting[st][2]) + 200) >= 1) {
                cm.sendOk("이미 누군가가 루시드에 도전하고 있습니다.\r\n다른채널을 이용 해 주세요.");
                cm.dispose();
                return;
            }
        }
        if (!cm.isBossAvailable(setting[st][0], setting[st][1]) && st != 3 && st != 4 && st != 5) {
            cm.sendOkS(cm.isBossString(setting[st][0]), 0x04, 9010061);
            cm.dispose();
            return;
        }
        if (!cm.isLevelAvailable(setting[st][3])) {
            talk = "파티원 중 "
            for (i = 0; i < cm.LevelNotAvailableChrList(setting[st][3]).length; i++) {
                if (i != 0) {
                    talk += ", "
                }
                talk += "#b#e" + cm.LevelNotAvailableChrList(setting[st][3])[i] + ""
            }
            talk += "#k#n님의 레벨이 부족합니다. 루시드 " + name[st] + "모드는 " + setting[st][3] + " 레벨 이상만 입장 가능합니다.";
            cm.sendOk(talk);
            cm.dispose();
            return;
        }

        if (st == 3 || st == 4 || st == 5) {
            cm.addBossPractice(setting[st][0]);
        } else {
            cm.addBoss(setting[st][0]);
        }
        em = cm.getEventManager(setting[st][0]);
        if (em != null) {
            cm.getEventManager(setting[st][0]).startInstance_Party(setting[st][2] + "", cm.getPlayer());
        }
        cm.dispose();


    }
}