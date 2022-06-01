var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    setting = [
        ["Normal_Pierre", 2, 105200200, 125],
        ["Chaos_Pierre", 1, 105200600, 180],
        ["Chaos_Pierre", 1, 105200600, 180]
    ]
    name = ["노멀", "하드"]
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        talk = "#r#e<루타비스 서쪽 정원 입구>#k#n\r\n"
        talk += "루타비스 서쪽 봉인의 수호자인 #r피에르#k가 지키고 있는 정원으로 가는 문이다. #r클리어 기록은 노멀의 경우 당일 자정, 카오스의 경우 매주 목요일 자정을 기준으로 초기화 됩니다.#k#n\r\n\r\n"
        talk += "#L0##i4033611##b#z4033611#를 사용하여 노말 모드로 이동한다.(125레벨 이상)#l\r\n"
        talk += "#L1##i4033611##b#z4033611#를 사용하여 카오스 모드로 이동한다.(180레벨 이상)#l\r\n"
        talk += "#L2#카오스 연습 모드로 이동한다.(180레벨 이상)#l\r\n"
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
        } else if (cm.getPlayerCount(setting[st][2] + 10) >= 1 || cm.getPlayerCount(setting[st][2]) >= 1) {
            cm.sendNext("이미 다른 파티가 안으로 들어가 피에르에게 도전하고 있는 중입니다.");
            cm.dispose();
            return;
        } else if (!cm.allMembersHere()) {
            cm.sendOk("모든 멤버가 같은 장소에 있어야 합니다.");
            cm.dispose();
            return;
        }
        if (!cm.partyhaveItem(4033611, 1)) {
            talk = "파티원 중 #i4033611# #b#z4033611##k를 소지하고 있지 않은 파티원이 있습니다.";
            cm.sendOk(talk);
            cm.dispose();
            return;
        }
        if (!cm.isBossAvailable(setting[st][0], setting[st][1]) && st != 2) {
            cm.sendOkS(cm.isBossString(setting[st][0]), 0x04, 9010061);
            cm.dispose();
            return;
        } else if (!cm.isLevelAvailable(setting[st][3])) {
            talk = "파티원 중 #b#e"
            for (i = 0; i < cm.LevelNotAvailableChrList(setting[st][3]).length; i++) {
                if (i != 0) {
                    talk += ", "
                }
                talk += "#b#e" + cm.LevelNotAvailableChrList(setting[st][3])[i] + ""
            }
            talk += "#k#n님의 레벨이 부족합니다. 피에르는 레벨 " + setting[st][3] + "이상만 도전하실 수 있습니다.";
        } else {
            cm.givePartyItems(4033611, -1);
            if (st == 2) {
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
}