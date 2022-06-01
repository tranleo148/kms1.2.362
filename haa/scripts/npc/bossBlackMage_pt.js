var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    setting = [
        ["Black_Mage", 2, 450013000, 255]
    ]
    name = ["노멀", "하드"];
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.showSpineScreen(false, false, true, "Effect/Direction20.img/bossBlackMage/start2_spine/black_space", "animation", 0, false, ""));
        talk = "검은마법사와 대적하기 위해 #b어둠의 신전#k으로 이동할까?\r\n\r\n"
        talk += "#L0##b어둠의 신전으로 이동한다.#k#l\r\n";
        talk += "#L1#이동하지 않는다.#l\r\n";
        cm.sendSimpleS(talk, 0x26);
    } else if (status == 1) {
        st = selection;
        if (st == 1) {
            cm.dispose();
            return;
        }
        if (!cm.partyhaveItem(4001895, 1)) {
            talk = "파티원 중 #i4001895# #b#z4001895##k를 소지하고 있지 않은 파티원이 있습니다.";
            cm.sendOkS(talk, 0x04, 9010061);
            cm.dispose();
            return;
        }
        if (cm.getParty() == null) {
            cm.sendOkS("1인 이상 파티를 맺어야만 입장할 수 있는것 같다..", 0x26);
            cm.dispose();
            return;
        } else if (cm.getPlayerCount(450010100) >= 1) {
            cm.sendOkS("이미 누군가가 검은 마법사에 도전하고 있는것 같다..\r\n다른채널을 이용 해 주세요.", 0x26);
            cm.dispose();
            return;
        } else if (!cm.isLeader()) {
            cm.sendOkS("파티장만이 입장을 신청할 수 있는것 같다..", 0x26);
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
            talk += "#k#n님의 레벨이 부족하다.. 검은 마법사 " + name[st] + "모드는 " + setting[st][3] + " 레벨 이상만 입장 가능한 것 같다.";
            cm.sendOkS(talk, 0x26);
            cm.dispose();
            return;
        } else {
            cm.gainItem(4001895, -1);
            cm.addBoss(setting[st][0]);
            em = cm.getEventManager(setting[st][0]);
            cm.getPlayer().dropMessage(6, em == null);
            if (em != null) {
                cm.getEventManager(setting[st][0]).startInstance_Party(setting[st][2] + "", cm.getPlayer());
            }
            cm.dispose();
        }
    }
}