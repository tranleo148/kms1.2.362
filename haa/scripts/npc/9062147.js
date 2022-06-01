var status = -1;

var year, month, date2, date, day
var hour, minute;
//어드벤처 드릴
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    talk = ""
    for (i = 1; i <= 10; i++) {
        if (i != 1)
            talk += "\r\n";
        talk += "#e- " + i + " ~ " + (i * 4) + "마리 : #i4009005##z4009005# " + (i * 10) + "개#n"
    }
    dialogue = [
        ["nextprev", 0, "#b#e<어드벤처 드릴>#k#n은 1분 안에 최대한 많은 #e허수아비 몬스터#n를 처치하는 미션이다."],
        ["nextprev", 0, "총 10개의 발판마다 각각 종류가 다른 허수아비 몬스터 4종이 배치되어 있지."],
        ["nextprev", 0, "#b#e<일반 허수아비>#k#n\r\n\r\n#fMob/9300799.img/stand/0#\r\n첫 번째로 #e일반 허수아비.#n 별 다른 특징은 없다."],
        ["nextprev", 0, "#b#e<웡키 허수아비>#k#n\r\n\r\n#fMob/9305681.img/stand/0#\r\n두 번째는 #e웡키 허수아비#n다.\r\n일반 허수아비와 다르게 #e방어율#n을 갖추고 있지."],
        ["nextprev", 0, "#b#e<껨디 허수아비>#k#n\r\n\r\n#fMob/9305678.img/stand/0#\r\n세 번째로 #e껨디 허수아비#n다.\r\n일반 허수아비와 다르게 #e높은 체력과 보스 몬스터 속성#n을 지녔다고 알려져 있다."],
        ["nextprev", 0, "#b#e<소공 허수아비>#k#n\r\n\r\n#fMob/9305679.img/stand/0#\r\n마지막으로 #e소공 허수아비.#n\r\n위쪽 발판으로 갈수록 점점 #e레벨#n이 올라가지."],
        ["nextprev", 0, "당연히 각각의 허수아비들은 올라갈수록 더 강한 체력과 능력을 갖추고 있다."],
        ["nextprev", 0, "1분 동안 얼마나 많은 허수아비를 처치했느냐에 따라서 #b#e#i4009005##z4009005##k#n을 지급해 주도록 하지."],
        ["nextprev", 0, talk],
        ["nextprev", 0, "#b#e<어드벤처 드릴>#k#n은 #r#e캐릭터 당 하루 한 번#k#n만 받아갈 수 있다는 점 명심하도록."],
        ["nextprev", 0, "참고로 처치 결과가 마음에 들지 않는다면 #e보상 획득을 포기#n하고 언제든 #e재도전#n이 가능하다."],
        ["nextprev", 0, "오직 강인한 전사만이 새로운 모험을 즐길 자격이 있는 법!"],
        ["nextprev", 0, "가라! #b" + cm.getPlayer().getName() + "#k!\r\n#b#e<어드벤처 드릴>#k#n을 정복하고 돌아와라!"]
    ]
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        talk = "#e<어드벤처 드릴>#n을 시작할 준비가 됐나?\r\n\r\n"
        talk += "#L0##e<어드벤처 드릴>#n 실전모드 입장하기#l\r\n"
        //talk += "#L1##e<어드벤처 드릴>#n 연습모드 입장하기#l\r\n"
        talk += "#L2##e<어드벤처 드릴>#n 설명 다시 듣기"
        cm.sendSimple(talk);
    } else {
        if (status == 1) {
            st = selection;
        }
        if (st == 2) {
            if (status <= dialogue.length) {
                sendByType(dialogue[status - 1][0], dialogue[status - 1][1], dialogue[status][2]);
            } else {
                cm.dispose();
            }
        } else {
            if (status == 1) {
                if (selection == 0) {
		    getData();
                    if (cm.getPlayer().getParty() != null) {
                        cm.sendOk("#e<어드벤처 드릴>#n은 파티 상태로 입장할 수 없다.");
                        cm.dispose();
                        return;
                    }
                    if (cm.getKeyValue(date, "AdventureDrill") == 1) {
                        cm.sendNext("오늘은 이미 #b#e" + cm.getPlayer().getName() + "#k#n 캐릭터로 <어드벤처 드릴>을 완료했군!")
                    } else {
                        if (cm.getEventManager("AdventureDrill").getInstance("AdventureDrill") != null) {
                            cm.sendOk("이미 누군가 #e<어드벤처 드릴>#n을 도전하고 있군. 다른 채널을 이용해라.");
                            cm.dispose();
                            return;
                        }
                        var event = cm.getEventManager("AdventureDrill").getInstance("AdventureDrill");
                        if (event == null) {
                            cm.getEventManager("AdventureDrill").startInstance_Solo("" + 993080200, cm.getPlayer());
                            cm.dispose();
                        } else {
                            cm.sendOk("이미 누군가 #e<어드벤처 드릴>#n을 도전하고 있군. 다른 채널을 이용해라.");
                            cm.dispose();
                            return;
                        }
                    }
                } else {
                    if (cm.getPlayerCount(180000000) != 0) {
                        cm.sendOk("이미 누군가가 #e<어드벤처 드릴>#n을 연습하고 있군. 다른 채널을 이용해라.");
                        cm.dispose();
                        return;
                    }
                    cm.resetMap(180000000);
                    cm.warp(180000000, 0);
                    var mobx = [45, 240, 440, 635];
                    var moby = [-357, -657, -957, -1257, -1557, -1857, -2157, -2457, -2757, -3057]
                    for (i = 0; i < mobx.length; i++) {
                        for (j = 0; j < moby.length; j++) {
                            var mobid = 9833338 + (i * 10) + j;
                            cm.spawnMob(mobid, mobx[i], moby[j]);
                        }
                    }
                    cm.dispose();
                }
            } else if (status == 2) {
                cm.sendOk("내일 다시 찾아오도록!");
                cm.dispose();
            }
        }
    }
}

function sendByType(type, type2, text) {
    switch (type) {
        case "next":
            cm.sendNextS(text, type2);
            break;
        case "nextprev":
            cm.sendNextPrevS(text, type2);
            break;
    }
}

function getData() {
/*
	year = CurrentTime.년() + 1900;
	month = CurrentTime.월() + 1;
	date2 = CurrentTime.일();
	date = year * 10000 + month * 100 + date2;
	day = CurrentTime.요일();
	hour = CurrentTime.시();
	minute = CurrentTime.분();
*/
   time = new Date();
   year = time.getFullYear();
   month = time.getMonth() + 1;
   if (month < 10) {
      month = "0"+month;
   }
   date2 = time.getDate() < 10 ? "0"+time.getDate() : time.getDate();
   date = year+""+month+""+date2;
   day = time.getDay();
   hour = time.getHours();
   minute = time.getMinutes();

}