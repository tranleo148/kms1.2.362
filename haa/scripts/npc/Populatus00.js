var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    setting = [
        ["Easy_Populatus", 2, 220080100, 115],
        ["Normal_Populatus", 2, 220080200, 155],
        ["Chaos_Populatus", 2, 220080300, 190],
        ["Chaos_Populatus", 2, 220080300, 190]
    ]
    name = ["이지","노멀", "카오스"]
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        if (status == 0 && cm.itemQuantity(4031179) != 0) {
            st = selection;
            status++;
        }
        status++;
    }
    if (status == 0) {
        if (cm.getParty() == null) {
            cm.sendOk("1인 이상 파티를 맺어야만 입장할 수 있습니다.");
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
        말 = "#e<보스: 파풀라투스>#n\r\n"
        말 += "사고뭉치 파풀라투스가 차원을 계속 부수는 것을 막아야 합니다. 도와주시겠어요?\r\n\r\n\r\n"
        말 += "#L0# 이지 모드 ( 레벨 115 이상 )\r\n"
        말 += "#L1# 노멀 모드 ( 레벨 155 이상 )#l\r\n";
        말 += "#L2# 카오스 모드 ( 레벨 190 이상 )#l\r\n";
        말 += "#L3# 카오스 연습 모드 ( 레벨 190 이상 )#l";
        cm.sendSimpleS(말, 4, 2041021);
    } else if (status == 1) {
        st = selection;
        if (cm.itemQuantity(4031179) == 0) {
            cm.gainItem(4031179, 1);
        }
        if (!cm.isBossAvailable(setting[st][0], setting[st][1]) && st != 3) {
            cm.sendOkS(cm.isBossString(setting[st][0]), 0x04, 9010061);
            cm.dispose();
            return;
        }
        cm.sendNextS("#r#e파티원 모두#k #b차원 균열의 조각#n#k이 없으시군요. 파풀라투스를 만나기 위해서 꼭 필요합니다. 제가 마침 갖고 있는 것을 드리겠습니다.", 4, 2041021);
    } else if (status == 2) {
        if (!cm.isBossAvailable(setting[st][0], setting[st][1]) && st != 3) {
            cm.sendOkS(cm.isBossString(setting[st][0]), 0x04, 9010061);
            cm.dispose();
            return;
        }
        cm.sendNextPrevS("#b#e차원 균열의 조각#n#k을 드렸으니, 파풀라투스가 차원을 부수는 것을 꼭 막아 주세요!", 4, 2041021);
    } else if (status == 3) {
        if (cm.getPlayerCount(setting[st][2]) != 0) {
            cm.sendOk("이미 누군가가 파풀라투스에 도전하고 있습니다.\r\n다른채널을 이용 해 주세요.");
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
            talk += "#k#n님의 레벨이 부족합니다. 시계탑의 근원에 입장하실 수 없습니다.\r\n\r\n"
            talk += "(" + name[st] + "모드는 #e" + setting[st][3] + " 레벨 이상#n만 입장 가능합니다.)";
            cm.sendOk(talk);
            cm.dispose();
            return;
        } else {
            if (st == 3) {
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