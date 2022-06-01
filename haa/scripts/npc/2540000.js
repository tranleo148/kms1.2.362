var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    setting = [
        ["Dorothy", 4, 992050000, 300]
    ]
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        if (cm.getPlayer().getMapId() == 992049000) {
            talk = "#fs20##fn나눔고딕 ExtraBold#B - 50F#fn##fs#\r\n\r\n"
            talk+= "이제 마지막, 지하 50층이야..\r\n"
            talk+= "그녀를 제압할 수 있을까... 솔직히 이 세계의 누구라도 자신 있게 이야기하진 못 할거야. 심지어 창조자인 나조차도 말야.\r\n"
            talk+= "조심해야 할거야. 그녀의 마법, 분신, 애완견까지도 말야.\r\n\r\n";
            talk+= "#L0# 도로시 입장을 신청한다."
            cm.sendSimple(talk);
        } else {
            cm.sendYesNo("퇴장멘트");
        }
    } else if (status == 1) {
        if (cm.getPlayer().getMapId() == 105200000) {
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
                cm.sendNext("이미 다른 파티가 안으로 들어가 도로시에게 도전하고 있는 중입니다.");
                cm.dispose();
                return;
	} else if (!cm.allMembersHere()) {
	    cm.sendOk("모든 멤버가 같은 장소에 있어야 합니다.");
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
                talk += "#k#n님이 오늘 입장했습니다. 블러디 퀸은 하루에 " + setting[st][1] + "번만 도전하실 수 있습니다.";
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
                talk += "#k#n님의 레벨이 부족합니다. 블러디 퀸은 레벨 " + setting[st][3] + "이상만 도전하실 수 있습니다.";
            } else {
                cm.addBoss(setting[st][0]);
                em = cm.getEventManager(setting[st][0]);
                if (em != null) {
                    cm.getEventManager(setting[st][0]).startInstance_Party(setting[st][2] + "", cm.getPlayer());
                }
                cm.dispose();
            }
        } else {
            cm.warp(105200000);
            cm.dispose();
        }
    }
}