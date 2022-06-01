importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang); 
importPackage(java.util);

var status = -1;

    var Mmobid = [9300900, 9300901];
    var Mmobx = [-902, -759, -553, -416, -185, -42, 155, 329, 544, 700];
    var Mmoby = [213, 273];

var EtcTimer = Packages.server.Timer.EtcTimer;


function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    setting = [
        ["RP_PQ", 4, 922010100, 275],
    ]
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        if(status == 0 && selection == 2){
            cm.sendOk("#e<파티퀘스트 : 차원의 균열>#n\r\n"
                    +"#b루디브리엄#k에 차원의 균열이 생겨났습니다! 이곳으로부터 침입한 몬스터들을 막아내려면 용감한 모험가들의 자발적인 도움이 절실해요."
                    +"믿을 수 있는 동료들과 힘을 합쳐서 루디브리엄을 구해주세요! 몬스터를 퇴치하거나 퀴즈를 풀어나가는 각종 난관을 해결하고 #r알리샤르#k에게 승리해야 한답니다.\r\n\r\n"
                    +"- #e레벨#n : 275레벨 이상 #r(추천레벨 : 275)#k\r\n"
                    +"- #e제한시간#n : 20분\r\n"
                    +"- #e참가인원#n : 3 ~ 6명\r\n"
                    +"- #e획득 아이템#n : #i1022073#  #z1022073#\r\n");
            cm.dispose();
            return;
        } else {
            status++;
        }
    }

    if (status == 0) {
        talk = "#e<파티퀘스트 : 차원의 균열>#n\r\n\r\n"
        talk += "이 위부터는 엄청나게 위험한 존재들로 가득 차 있어 더이상 올라가실 수 없어요. 파티원들과 함께 힘을 모아 퀘스트를 해결해 보시지 않겠습니까?"
        talk += "도전해 보고 싶다면 #b당신이 속한 파티의 파티장#k을 통해 저에게 말을 걸어주세요.\r\n\r\n"
        talk += "#L0##b파티퀘스트에 참가하고 싶습니다.\r\n"
        talk += "#L1#금이 간 안경을 받고 싶어요.\r\n"
        talk += "#L2#설명을 듣고 싶어요.\r\n"
        talk += "#L3#오늘 남은 도전 횟수를 알고 싶어요.#k\r\n"
        cm.sendSimple(talk);
    } else if (status == 1) {
        st = selection;
        if(st == 3){
           var talk = "#h #님이 남은 입장 횟수는 "+(3-cm.getPlayer().getV(setting[0][0]))+"번 입니다.";
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
            talk += "#k#n님이 오늘 입장했습니다. 차원의 균열 파티퀘스트는 하루에 " + setting[0][1] + "번만 도전하실 수 있습니다.";
            cm.sendOk(talk);
            cm.dispose();
            return;
        } else {
            //GameConstants.setPartylevel(cm.getPlayer());
            cm.resetMap(setting[0][2]);
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

function spawnmob () {
var tick = 0;
    schedule = EtcTimer.getInstance().register(function () {
	if(tick == 8){
        schedule.cancel(true);
	} else {
        if(tick == 7){
            spawnMonster();
        }
        cm.getPlayer().dropMessage(5, tick);
	}
        tick++;
    }, 1000);
}

function spawnMonster () {
    for(var i = 0; i < Mmobx.length; i++){
        for(var j = 0; j < Mmoby.length-1; j++){
            cm.getPlayer().getMap().spawnMonsterOnGroundBelow(Packages.server.life.MapleLifeFactory.getMonster(Mmobid[0]), new java.awt.Point(Mmobx[i], Mmoby[j]));
            cm.getPlayer().getMap().spawnMonsterOnGroundBelow(Packages.server.life.MapleLifeFactory.getMonster(Mmobid[1]), new java.awt.Point(Mmobx[i], Mmoby[j]));
        }
    }
}