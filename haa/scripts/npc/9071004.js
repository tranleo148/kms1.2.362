//importPackage(java.util);
//importPackage(java.lang);

var status = -1;

var data;
var day;

function start() {
    status = -1;
    action (1, 0, 0);
}

function String(date) {
	switch (date) {
		case 1:
		return "창조의 월요일";
		case 2:
		return "강화의 화요일";
		case 3:
		return "성향의 수요일";
		case 4:
		return "명예의 목요일";
		case 5:
		return "황금의 금요일";
		case 6:
		return "축제의 토요일";
		case 0:
		return "성장의 일요일";
	}
}

var map = new Array(953020000, 953030000, 953040000, 953050000, 953060000, 953070000, 953080000, 953090000, 954000000, 954010000, 954020000, 954030000, 954040000, 954050000, 954060000);
var mapname = new Array("자동 경비 구역(Lv.105~114)", "이끼나무 숲(Lv.115~124)", "하늘 숲 수련장(Lv.120~129)", "해적단의 비밀 기지(Lv.125~134)", "이계의 전장(Lv.135~144)", "외딴 숲 위험 지역(Lv.140~149)", "금지된 시간(Lv.145~154)", "숨겨진 유적(Lv.150~159)", "폐허가 된 도시(Lv.160~169)", "죽은 나무의 숲(Lv.170~179)", "감시의 탑(Lv.175~184)", "용의 둥지(Lv.180~189)", "망각의 신전(Lv.185~194)", "기사단의 요새(Lv.190~199)", "원혼의 협곡(Lv.200~209)");
var exp = new Array(3517411, 5989675, 7311630, 8129820, 11524015, 11953470, 13978390, 15311670, 19790735, 26950030, 27953565, 33576980, 35340485, 39775800, 40650435);
var select;

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
/*
	if (!cm.getPlayer().isGM()) {
		cm.dispose();
		return;
	}
*/
	getData();
	if (cm.getClient().getKeyValue(201820, "mc_"+data) == -1) {
		cm.getClient().setKeyValue(201820, "mc_"+data, "0");
	}
	var text = "#e<오늘은 #b" + String(Packages.tools.CurrentTime.요일()) + "#k입니다.>\r\n<오늘 클리어 횟수 #b" + cm.getClient().getKeyValue(201820, "mc_"+data) + " / 7회#k (월드 통합당 기준)>\r\n#e오늘의 무료 클리어 가능 횟수가 #b" + (7 - cm.getClient().getKeyValue(201820, "mc_"+data)) +"회#k 남았습니다.#n#b\r\n";
	for (i = 0; i < map.length; i++) {
		text += "#L" + i + "#" + mapname[i] + "\r\n";
	}
	cm.sendSimple(text);
    } else if (status == 1) {
	select = selection;
	cm.sendYesNo("#e<오늘은 #b" + String(Packages.tools.CurrentTime.요일()) + "#k입니다.>\r\n\r\n선택 던전 : #b" + mapname[select] + "#k\r\n\r\n#k던전에 입장하시겠습니까?#n");
    } else if (status == 2) {
	cm.dispose();
	if (cm.getClient().getKeyValue(201820, "mc_"+data) >= 7) {
		cm.sendOk("오늘의 클리어 횟수를 모두 이용하셨습니다.");
	} else {
	for (i = 0; i < 6; i++) {
		if (cm.getClient().getChannelServer().getMapFactory().getMap(map[select] + (i * 100)).getCharactersSize() > 0) {
			cm.sendOk("이미 누군가가 입장하였습니다.");
			return;
		}
	}
		cm.warp(map[select], 0);
		cm.resetMap(map[select]);
		cm.getPlayer().setMparkexp(exp[select]);
		//cm.writeLog("Log/몬파입장.log", cm.getPlayer().getName()+"가 몬스터파크 입장함.\r\n", true);
	}
    }
}


function getData() {
	time = new Date();
	year = time.getFullYear();
	month = time.getMonth() + 1;
	if (month < 10) {
		month = "0"+month;
	}
	date = time.getDate() < 10 ? "0"+time.getDate() : time.getDate();
	data = year+""+month+""+date;
	day = time.getDay();
}