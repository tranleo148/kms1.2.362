var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
        var count = 3;
    if (cm.getPlayer().getBossTier() >= 1) {
        count += cm.getPlayer().getBossTier();
    }
    setting = [
        ["JINH", 2, 450010500, 130],
    ]
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        if (cm.getPlayer().getParty() == null) {
            cm.sendOk("파티를 맺어야만 입장할 수 있다.");
            cm.dispose();
            return;
        }
        if (!cm.isLeader()) {
            cm.sendOk("파티장만 입장을 신청할 수 있다.");
            cm.dispose();
            return;
        }
        talk = "#e<보스: 진 힐라>#n\r\n"
        talk += "#L0##b <보스: 진 힐라> 입장을 신청한다."
        cm.sendSimple(talk);
    } else if (status == 1) {
        if (cm.getPlayer().getParty() == null) {
            cm.sendOk("파티를 맺어야만 입장할 수 있다.");
            cm.dispose();
            return;
        }
        if (!cm.isLeader()) {
            cm.sendOk("파티장만 입장을 신청할 수 있다.");
            cm.dispose();
            return;
        }
        talk = "#e<보스: 진 힐라>#n\r\n"
        talk+= "#L0# 노멀 모드"
        cm.sendSimple(talk);
    } else if (status == 2) {
        st = selection;
        if (cm.getPlayer().getParty() == null) {
            cm.sendOk("파티를 맺어야만 입장할 수 있다.");
            cm.dispose();
            return;
        } else if (!cm.isLeader()) {
            cm.sendOk("파티장만 입장을 신청할 수 있다.");
            cm.dispose();
            return;
	} else if (!cm.allMembersHere()) {
	    cm.sendOk("모든 멤버가 같은 장소에 있어야 합니다.");
	    cm.dispose();
            return;
        } else if (cm.getPlayerCount(setting[st][2]) >= 1) {
            cm.sendOk("이미 누군가가 진 힐라에 도전하고 있다.");
            cm.dispose();
            return;
        }
        if (!cm.isBossAvailable(setting[st][0], setting[st][1])) {
            talk = "파티원 중 #b#e"
            for (i = 0; i < cm.BossNotAvailableChrList(setting[st][0], setting[st][1]).length; i++) {
                if (i != 0) {
                    talk += ", "
                }
                talk += "#b#e" + cm.BossNotAvailableChrList(setting[st][0], setting[st][1])[i] + ""
            }
            talk += "이#k#n 오늘 입장했군. 그렇다면 오늘은 더 이상 들어갈 수 없다.";
            cm.sendOk(talk);
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
            talk += "의#k#n 레벨이 부족하군. 그렇다면 들어갈 수 없다.";
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