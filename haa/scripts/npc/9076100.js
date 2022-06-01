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
        ["MoonPQ", 3, 933001000, 250],
    ]
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        if(status == 0 && selection == 1){
            cm.sendOk("#e<파티퀘스트 : 월묘의 떡>#n\r\n"
                    +"만월의 #b달맞이꽃 언덕#k에만 등장한다는 신비한 토끼 월묘. #b어흥이#k에게 #r월묘의 떡#k을 구해줄 모험가들을 #b헤네시스공원#k의 #b토리#k가 찾고 있어요."
                    +"월묘를 만나고 싶으면 달맞이꽃 씨앗을 심어 보름달을 불러내야 해요. #r떡 10개#k를 다 만들때까지 난폭한 동물들에게서 월묘를 안전하게 지켜주세요.\r\n"
                    +"- #e레벨#n : 250레벨 이상 #r(추천레벨 : 250 ~ 999)#k\r\n"
                    +"- #e제한시간#n : 10분\r\n"
                    +"- #e참가인원#n : 3 ~ 6명\r\n"
                    +"- #e획득 아이템#n : #i1002798# #z1002798#\r\n"
                    +"#fc0xFFFFFFFF#- #e획득 아이템#n : #k#b(떡 20개를 토리에게 주면 획득 가능)#k");
            cm.dispose();
            return;
        } else {
            status++;
        }
    }

    if (status == 0) {
        talk = "#e<파티퀘스트 : 월묘의 떡>#n\r\n\r\n"
        talk += "안녕하세요? 저는 토리라고 해요. 달맞이 꽃 언덕에 가보셧나요? 이 안은 달맞이꽃이 피어나는 아름다운 언덕이에요.\r\n"
        talk += "그런데 그 곳에 살고 있는 어흥이라는 호랑이가 몹시 배가 고파 먹을 것을 찾고 있다고 하네요. 여행자님께서 달맞이 꽃 언덕으로 가서 파티원들과 함께 힘을 모아 어흥이를 도와주지 않을시겠어여?\r\n\r\n"
        talk += "#L0##b달맞이꽃 언덕으로 간다.\r\n"
        talk += "#L1#달맞이꽃 언덕에 대해 설명을 듣는다\r\n"
        talk += "#L2#오늘 남은 도전 횟수를 알고 싶어요.#k\r\n"
        cm.sendSimple(talk);
    } else if (status == 1) {
        st = selection;
        if(st == 2){
           var talk = "#h #님이 남은 월묘의 떡 입장 횟수는 "+(3-cm.getPlayer().getV(setting[0][0]))+"번 입니다.";
            cm.sendOk(talk);
            cm.dispose();
            return;
        }
        if (cm.getParty() < 3 && !cm.getPlayer().isGM()) {
            cm.sendOk("3인 이상 파티를 맺어야만 입장할 수 있습니다.");
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
            talk += "#k#n님이 오늘 입장했습니다. 월묘의 떡 파티퀘스트는 하루에 " + setting[0][1] + "번만 도전하실 수 있습니다.";
            cm.sendOk(talk);
            cm.dispose();
            return;
        } else {
            cm.resetMap(933000000);
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