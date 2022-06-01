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
        ["EscapePQ", 3, 921160100, 275],
    ]
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        if(status == 0 && selection == 1){
            cm.sendOk("이 성 안에는 숨겨진 탑이 있어. 탑에 있는 공중 감옥에는 많은 사람들이 갇혀있지. 이들을 구해줘야해...\r\n"
                    +"- #e레벨#n : 275레벨 이상 #r(추천레벨 : 275)#k\r\n"
                    +"- #e제한시간#n : 20분\r\n"
                    +"- #e참가인원#n : 3 ~ 6명\r\n"
                    +"- #e획득 아이템#n : \r\n"
                    +"#i3994745#");
            cm.dispose();
            return;
        } else if(status == 0 && selection == 2){
            cm.sendOk("#r교도관의 열쇠#k는 숨겨진 탑의 교도관이 가지고 있던 열쇠야."
                    +"#b5개#k 정도를 모아오면 내가 작은 선물을 줄께. 5개를 모았다는 것은 그만큼 억울하게 성에 갇혀 있는 사람들을 많이 도와 \r\n줬다는 뜻이기도 하니까.\r\n"
                    +"#b#i1132094# #z1132094#\r\n#i1132095# #z1132095#\r\n#i1132096# #z1132096#\r\n#i1132097# #z1132097#\r\n#i1132098# #z1132098##k");
            cm.dispose();
            return;
        } else {
            status++;
        }
    }

    if (status == 0) {
        talk = "#e<파티퀘스트 : 탈출>#n\r\n\r\n"
        talk += "솔직히 이대로 당장 도망가고 싶긴 하지만...그의 부탁을 외면할 수 없었어. 이 성 안, 공중 감옥에 갇혀있는 사람이 이곳을 탈출하게 도와줄 사람을 찾고 있어.\r\n\r\n"
        talk += "#L0##b성에 갇힌 모험가를 도와주러 가겠어요!\r\n"
        talk += "#L1#성의 감옥에 대해 알려주세요.\r\n"
        talk += "#L2#교도관의 열쇠에 대해 알려주세요.\r\n"
        talk += "#L3#오늘 남은 도전 횟수를 알고 싶어요.#k\r\n"
        cm.sendSimple(talk);
    } else if (status == 1) {
        st = selection;
        if(st == 3){
           var talk = "오늘 남은 도전 횟수는 "+(3-cm.getPlayer().getV(setting[0][0]))+"야";
            cm.sendOk(talk);
            cm.dispose();
            return;
        }
        if (cm.getParty() == null) {
            cm.sendOk("이곳은 매우 위험해. 무서운 녀석들 투성이야. 혼자서는 아무래도 힘들텐데... 안에 들어가고 싶다면, 당신이 속한 파티의 파티장이 나에게 말을 걸도록 해.");
            cm.dispose();
            return;
        } else if (cm.getParty().getMembers().size() < 4 && !cm.getPlayer().isGM()) {
            cm.sendOk("파티원이 3명 미만이로군. 이곳은 위험한 곳이야. 적어도 레벨 120이상의 3명이상 파티원이 있어야 입장하실 수 있으니 다시 확인해 봐.");
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
            cm.resetMap(921160100);
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