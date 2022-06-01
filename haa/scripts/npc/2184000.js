var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    setting = [
        ["Normal_Hillah", 2, 262030100, 120],
        ["Hard_Hillah", 2, 262031100, 170]
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
        말 = "#e<보스: 힐라>#n\r\n"
        말 += "힐라를 처치하고, 아스완의 진정한 해방을 이뤄낼 준비는 되셨습니까? 다른 지역에 있는 파티원이 있다면, 모두 모여 주세요.\r\n\r\n"
        말 += "#L0# #b<보스: 힐라> 입장을 신청한다."
        cm.sendSimple(말);
    } else if (status == 1) {
        말 = "#e<보스: 힐라>#n\r\n"
        말 += "원하시는 모드를 선택해 주세요.\r\n\r\n"
        말 += "#L0# 노말 모드 ( 레벨 120 이상 )\r\n"
        말 += "#L1# 하드 모드 ( 레벨 170 이상 )"
        cm.sendSimple(말);
    } else if (status == 2) {
        st = selection;
        if (cm.getParty() == null) {
            cm.sendOk("1인 이상 파티를 맺어야만 입장할 수 있습니다.");
            cm.dispose();
            return;
        } else if (cm.getPlayerCount(setting[st][2]) >= 1 || cm.getPlayerCount(Number(setting[st][2]) + 100) >= 1 || cm.getPlayerCount(Number(setting[st][2]) + 200) >= 1 || cm.getPlayerCount(Number(setting[st][2]) + 210) >= 1) {
            cm.sendOk("이미 누군가가 힐라에 도전하고 있습니다.\r\n다른채널을 이용 해 주세요.");
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
        if (!cm.isBossAvailable(setting[st][0], setting[st][1])) {
            cm.sendOkS(cm.isBossString(setting[st][0]), 0x04, 9010061);
            cm.dispose();
            return;
        } else if (!cm.isLevelAvailable(setting[st][3])) {
            talk = "파티원 중 "
            for (i = 0; i < cm.LevelNotAvailableChrList(setting[st][3]).length; i++) {
                if (i != 0) {
                    talk += ", "
                }
                talk += "#b#e" + cm.LevelNotAvailableChrList(setting[st][3])[i] + ""
            }
            talk += "#k#n님의 레벨이 부족합니다. <보스:힐라> " + name[st] + "모드는 " + setting[st][3] + " 레벨 이상만 입장 가능합니다.";
            cm.sendOk(talk);
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
    }
}