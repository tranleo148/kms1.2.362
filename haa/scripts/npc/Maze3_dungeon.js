var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
        var count = 1;
    if (cm.getPlayer().getBossTier() >= 3) {
        count = 2
    }
    if (cm.getPlayer().getBossTier() >= 7) {
        count = 3
    }
    setting = [
        ["Normal_JinHillah", 2, 450010400, 250],
        ["Normal_JinHillah", 2, 450010400, 250]
    ]
    name = ["노멀"];
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        talk = "이제 욕망의 제단으로 진입하여 진 힐라를 대적해야 한다.\r\n\r\n";
	talk += "#L0##b진 힐라 입장하기#k#r (레벨 250이상)#k#l\r\n";
              //talk += "#L1##b진 힐라 #Cgray#(연습 모드) #k#r(레벨 250이상)#k#l\r\n";
        cm.sendSimpleS(talk, 2);
    } else if (status == 1) {
        st = selection;
        if (cm.getParty() == null) {
            cm.sendOkS("1인 이상 파티를 맺어야만 입장할 수 있습니다.", 0x26);
            cm.dispose();
            return;
        } else if (cm.getPlayerCount(450010400) >= 1 || cm.getPlayerCount(450010500) >= 1 || cm.getPlayerCount(450010600) >= 1) {
            cm.sendOkS("이미 누군가가 진 힐라에 도전하고 있습니다.\r\n다른채널을 이용 해 주세요.", 0x26);
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
        if (!cm.isBossAvailable(setting[st][0], setting[st][1])) {
            talk = "파티원 중 "
            for (i = 0; i < cm.BossNotAvailableChrList(setting[st][0], setting[st][1]).length; i++) {
                if (i != 0) {
                    talk += ", "
                }
                talk += "#b#e" + cm.BossNotAvailableChrList(setting[st][0], setting[st][1])[i] + ""
            }
            talk += "#k#n님이 오늘 입장했습니다. 진 힐라는 하루에 " + setting[st][1] + "번만 도전하실 수 있습니다.";
            cm.sendOkS(talk, 0x26);
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
            talk += "#k#n님의 레벨이 부족합니다. 진 힐라는 " + setting[st][3] + " 레벨 이상만 입장 가능합니다.";
            cm.sendOkS(talk, 0x26);
            cm.dispose();
            return;
        } else {
	    if (st == 0) {
                cm.warpParty(setting[st][2]);
                cm.dispose();
	        cm.openNpc(3003771);
            } else if (st == 1) {
                cm.sendYesNoS("연습 모드에 입장을 선택하셨습니다. 연습 모드에서는 #b#e경험치와 보상을 받을 수 없으며#n#k 보스 몬스터의 종류와 상관없이 #b#e하루 5회#k#n만 이용할 수 있습니다. 입장하시겠습니까?", 4, 2007);
            } else {
                cm.sendYesNoS("익스트림 모드에 입장을 선택하셨습니다. 익스트림 모드에서는 데미지가 70% 감소하지만, #b#e보다 강력한 장비 보상이 추가옵션과 함께 드롭됩니다.#n#k 해당 장비의 추가옵션은 #b#e이노센트 주문서 이용 시 함께 사라지며, 이는 복구가 불가능합니다.#k#n 입장하시겠습니까?", 4, 2007);
            }
	    
        }
    } else if (status == 2) {
        cm.dispose();
	if (st == 1) {
            cm.warpParty(setting[st][2]);
            cm.dispose();
	    cm.openNpc(3003771);
	}
    }
}