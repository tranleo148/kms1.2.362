importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.util);

var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    setting = [
        ["PoisonForest", 3, 930000000, 275],
    ]
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        if(status == 0 && selection == 1){
            cm.sendOk("하지만 알테어 캠프에 있는 용사들은 캐프 유지와 새로운 지역 발굴 때문에 너무 바빠서 도와줄 수 없다고 해. 그러니, 네가 도와주지 않을래? 용사들을 대신해. 네가 그들이 되어서..."
                    +"\r\n"
                    +"- #e레벨#n : 275레벨 이상 #r(추천레벨 : 275)#k\r\n"
                    +"- #e제한시간#n : 20분\r\n"
                    +"- #e참가인원#n : 3 ~ 5명\r\n"
                    +"- #e획득 아이템#n : #i2432408#");
            cm.dispose();
            return;
        } else {
            status++;
        }
    }

    if (status == 0) {
        talk = "#e<파티퀘스트 : 독안개의 숲>#n\r\n\r\n"
        talk += "괴인에 의해 오염되어 버린 숲을 구해야 해! 하지만 알테어 캠프의 용사들은 모두 생업에 바빠서 움직일 수 없어. 네가 도와주지 않을래? #b275레벨 이상의 모험가#k라면, 도와줄 수 있을 거야!\r\n"
        talk += "#L0##b독안개의 숲으로 들어간다\r\n"
        talk += "#L1#엘린의 이야기를 듣는다\r\n"
        talk += "#L2#오늘 남은 도전 횟수를 알고 싶어요.#k\r\n"
        cm.sendSimple(talk);
    } else if (status == 1) {
        st = selection;
        if(st == 2){
           var talk = "#h #님이 남은 독안개의 숲 입장 횟수는 "+(3-cm.getPlayer().getV(setting[0][0]))+"번 입니다.";
            cm.sendOk(talk);
            cm.dispose();
            return;
        }
        if (cm.getParty() == null) {
            cm.sendOk("파티를 맺어야만 입장할 수 있습니다.");
            cm.dispose();
            return;
        } else if (cm.getParty().getMembers().size() < 4 && !cm.getPlayer().isGM()) {
            cm.sendOk("4인 이상 파티를 맺어야만 입장할 수 있습니다.");
            cm.dispose();
            return;
        } else if (cm.getPlayerCount(setting[st][2]) >= 1) {
            cm.sendOk("이미 누군가가 도전하고 있습니다.\r\n다른채널을 이용 해 주세요.");
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
        if (!cm.isBossAvailable(setting[0][0], setting[0][1])) {
            talk = "파티원 중 "
            for (i = 0; i < cm.BossNotAvailableChrList(setting[0][0], setting[0][1]).length; i++) {
                if (i != 0) {
                    talk += ", "
                }
                talk += "#b#e" + cm.BossNotAvailableChrList(setting[0][0], setting[0][1])[i] + ""
            }
            talk += "#k#n님이 오늘 입장했습니다. 독안개의 숲 파티퀘스트는 하루에 " + setting[0][1] + "번만 도전하실 수 있습니다.";
            cm.sendOk(talk);
            cm.dispose();
            return;
        } else {
            for (i = 0; i < cm.getPlayer().getParty().getMembers().size(); ++i) {
                mem = cm.getPlayer().getParty().getMembers().get(i).getName();
                conn = cm.getClient().getChannelServer().getPlayerStorage().getCharacterByName(mem);
                conn.removeItem(4001161, -conn.itemQuantity(4001161));
                conn.removeItem(4001162, -conn.itemQuantity(4001162));
                conn.removeItem(4001163, -conn.itemQuantity(4001163));
            }
            //GameConstants.setPartylevel(cm.getPlayer());
            cm.resetMap(930000000);
            cm.addBoss(setting[0][0]);
            if (st <= 1) {
                em = cm.getEventManager(setting[0][0]);
                if (em != null) {
                    cm.getEventManager(setting[0][0]).startInstance_Party(setting[0][2] + "", cm.getPlayer());
                }
                cm.dispose();
            }
        }
    }
}