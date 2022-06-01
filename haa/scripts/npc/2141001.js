var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    setting = [
        ["Normal_Pinkbean", 2, 270050100, 160],
        ["Chaos_Pinkbean", 2, 270051100, 170],
        ["Chaos_Pinkbean", 2, 270051100, 170]
    ]
    name = ["노멀", "카오스"]
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        if (cm.getPlayer().getMapId() == 270050000) {
            if (cm.getPlayer().getParty() == null) {
                cm.sendOk("파티를 맺어야만 입장할 수 있습니다.");
                cm.dispose();
                return;
            }
            if (!cm.isLeader()) {
                cm.sendOk("파티장만 입장을 신청할 수 있습니다.");
                cm.dispose();
                return;
            }
            talk = "#e<보스:핑크빈>#n\r\n"
            talk += "침입자는 여신의 제단으로 향한 듯 합니다. 그를 어서 저지하지 못하면 무서운 일이 일어날 겁니다.\r\n\r\n"
            talk += "#L0# #b<보스: 핑크빈> 입장을 신청한다.";
            cm.sendSimple(talk);
        } else {
            cm.sendYesNo("전투를 마치고 핑크빈의 제단에서 퇴장하시겠습니까?");
        }
    } else if (status == 1) {
        if (cm.getPlayer().getMapId() == 270050000) {
            talk = "#e<보스:핑크빈> #n\r\n"
            talk += "원하시는 모드를 선택해주세요.\r\n\r\n"
            talk += "#L0#노멀 모드 ( 레벨 160 이상 )#l\r\n";
            talk += "#L1#카오스 모드 ( 레벨 170 이상 )#l\r\n";
            talk += "#L2#카오스 연습 모드 ( 레벨 170 이상 )#l\r\n";
            cm.sendSimple(talk);
        } else {
            cm.warp(270050000);
            cm.dispose();
        }
    } else if (status == 2) {
        st = selection;
        if (cm.getPlayer().getParty() == null) {
            cm.sendOk("파티를 맺어야만 입장할 수 있습니다.");
            cm.dispose();
            return;
        } else if (!cm.isLeader()) {
            cm.sendOk("파티장만 입장을 신청할 수 있습니다.");
            cm.dispose();
            return;
        } else if (cm.getPlayerCount(setting[st][2]) >= 1) {
            cm.sendNext("이미 다른 파티가 안으로 들어가 핑크빈에게 도전하고 있는 중입니다.");
            cm.dispose();
            return;
	} else if (!cm.allMembersHere()) {
	    cm.sendOk("모든 멤버가 같은 장소에 있어야 합니다.");
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
            talk += "#k#n님의 레벨이 부족합니다. <보스: 핑크빈>은 레벨 " + setting[st][3] + "이상만 도전하실 수 있습니다.";
        } else {
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