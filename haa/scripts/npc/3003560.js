var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    setting = [
        ["Normal_Will", 2, 450008700, 235],
        ["Hard_Will", 2, 450008100, 235],
        ["Normal_Will", 2, 450008700, 235],
        ["Hard_Will", 2, 450008100, 235],
        ["Extreme_Will", 2, 450008100, 235]
    ]
    name = ["노멀", "하드", "노말 연습", "하드 연습"];
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        talk = "윌을 저지하기 위해 #b'회절의 회랑'#k으로 이동할까?\r\n\r\n"
        //        if (cm.getPlayer().isGM()) {
        // talk+= "#L4##b회절의 회랑(익스트림 모드)으로 이동 한다.#k#r(레벨 235이상)#k#l\r\n"
        //      }
        talk += "#L0#회절의 회랑(#b노멀 모드#k)으로 이동 한다.#k#r(레벨 235이상)#k#l\r\n";
        talk += "#L1#회절의 회랑(#b하드 모드#k)으로 이동 한다.#k#r(레벨 235이상)#k#l\r\n";
        talk += "#L2#회절의 회랑(#b노멀 연습 모드#k)으로 이동 한다.#k#r(레벨 235이상)#k#l\r\n";
        talk += "#L3#회절의 회랑(#b하드 연습 모드#k)으로 이동 한다.#k#r(레벨 235이상)#k#l\r\n";
        cm.sendSimpleS(talk, 0x86);
    } else if (status == 1) {
        st = selection;
        if (cm.getParty() == null) {
            cm.sendOkS("1인 이상 파티를 맺어야만 입장할 수 있습니다.", 0x26);
            cm.dispose();
            return;
        } else if (cm.getPlayerCount(setting[st][2]) >= 1 || cm.getPlayerCount(setting[st][2] + 50) >= 1 || cm.getPlayerCount(setting[st][2] + 100) >= 1 || cm.getPlayerCount(setting[st][2] + 150) >= 1 || cm.getPlayerCount(setting[st][2] + 200) >= 1 || cm.getPlayerCount(setting[st][2] + 250) >= 1) {
            cm.sendOkS("이미 누군가가 윌에 도전하고 있습니다.\r\n다른채널을 이용 해 주세요.", 0x26);
            cm.dispose();
            return;
        } else if (!cm.isLeader()) {
            cm.sendOkS("파티장만이 입장을 신청할 수 있습니다.", 0x26);
            cm.dispose();
            return;
        } else if (!cm.allMembersHere()) {
            cm.sendOk("모든 멤버가 같은 장소에 있어야 합니다.");
            cm.dispose();
            return;
        }
        a = 0;

        if (!cm.isBossAvailable(setting[st][0], setting[st][1] + a) && st != 2 && st != 3) {
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
            talk += "#k#n님의 레벨이 부족합니다. 윌 " + name[st] + "모드는 " + setting[st][3] + " 레벨 이상만 입장 가능합니다.";
            cm.sendOkS(talk, 0x26);
            cm.dispose();
            return;
        }

        if (st == 2 || st == 3) {
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