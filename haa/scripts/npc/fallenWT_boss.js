var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    setting = [
        ["Normal_Demian", 2, 350160100, 190],
        ["Hard_Demian", 2, 350160200, 190],
        ["Normal_Demian", 2, 350160100, 190],
        ["Hard_Demian", 2, 350160200, 190],
        ["Extreme_Demian", 2, 350160200, 190]
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
        talk = "데미안을 쓰러뜨리기 위해 '타락한 세계수 정상'으로 이동할까?\r\n\r\n"
        //        if (cm.getPlayer().isGM()) {
        //talk+= "#L4#타락한 세계수 정상(#b익스트림 모드#k)으로 이동 한다.(레벨 190이상)#l\r\n"
        //      }
        talk += "#L0#타락한 세계수 정상#b(노멀 모드)#k으로 이동 한다.(레벨 190이상)#l\r\n";
        talk += "#L1#타락한 세계수 정상#b(하드 모드)#k으로 이동 한다.(레벨 190이상)#l\r\n";
        talk += "#L2#타락한 세계수 정상#b(노멀 연습 모드)#k으로 이동 한다.(레벨 190이상)#l\r\n";
        talk += "#L3#타락한 세계수 정상#b(하드 연습 모드)#k으로 이동 한다.(레벨 190이상)#l\r\n\r\n";
        talk += "#L4#이동하지 않는다.\r\n";
        cm.sendSimpleS(talk, 0x86);
    } else if (status == 1) {
        if (selection == 4) {
            cm.dispose();
            return;
        }
        st = selection;
        if (cm.getParty() == null) {
            cm.sendOkS("1인 이상 파티를 맺어야만 입장할 수 있습니다.", 0x24);
            cm.dispose();
            return;
        } else if (cm.getPlayerCount(setting[st][2]) >= 1 || cm.getPlayerCount(setting[st][2] + 20) >= 1 || cm.getPlayerCount(Number(setting[st][2]) + 40) >= 1 || cm.getPlayerCount(setting[st][2] + 60) >= 1) {
            cm.sendOkS("이미 누군가가 데미안에 도전하고 있습니다.\r\n다른채널을 이용 해 주세요.", 0x24);
            cm.dispose();
            return;
        } else if (!cm.isLeader()) {
            cm.sendOkS("파티장만이 입장을 신청할 수 있습니다.", 0x24);
            cm.dispose();
            return;
        } else if (!cm.allMembersHere()) {
            cm.sendOk("모든 멤버가 같은 장소에 있어야 합니다.");
            cm.dispose();
            return;
        }
        if (!cm.isBossAvailable(setting[st][0], setting[st][1]) && st != 2 && st != 3) {
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
            talk += "#k#n님의 레벨이 부족합니다. 데미안 " + name[st] + "모드는 " + setting[st][3] + " 레벨 이상만 입장 가능합니다.";
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