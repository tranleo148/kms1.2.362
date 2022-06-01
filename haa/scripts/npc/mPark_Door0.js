//importPackage(java.util);
//importPackage(java.lang);

var status = -1;

var data;
var day;

function start() {
    status = -1;
    action(1, 0, 0);
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

var map = new Array(953020000);
var mapname = new Array("자동 경비 구역(Lv.105~114)");
var exp = new Array(3517411);
var select;
var needitem = 4310012;
var needcount = 30;
var check = 0;

function action(mode, type, selection) {
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
        if (!(cm.getPlayer().getLevel() > 104 && cm.getPlayer().getLevel() < 115)&& !cm.getPlayer().isGM()) {//초급
            cm.sendOk("초급 던전은 #b레벨 105 이상, 115 미만#k이신 분만\r\n이용하실 수 있습니다.", 9071004);
            cm.dispose();
            return;
        }
        getData();
        if (cm.getClient().getKeyValue("mPark") == null) {
            cm.getClient().setKeyValue("mPark", "0");
        }
        if (cm.getClient().getKeyValue("mPark_t") == null) {
            cm.getClient().setKeyValue("mPark_t", "0");
        }
        var totalclear = (parseInt(cm.getClient().getKeyValue("mPark")) + parseInt(cm.getClient().getKeyValue("mPark_t")));
        if (totalclear > 7) {
            totalclear = 7;
        }
        if (totalclear >= 7) {
            var text = "#e<오늘 클리어 횟수 #b" + totalclear + " / 7회#k (현재 월드 기준)>\r\n#e<나의 #z" + needitem + "# 개수 : " + cm.itemQuantity(needitem) + ">#n\r\n\r\n";
            text += "오늘의 클리어 가능 횟수 7회를 모두 사용하셨습니다.\r\n";
            cm.sendOk(text, 9071004);
            cm.dispose();
            return;
        }
        if (cm.getClient().getKeyValue("mPark") >= 2 && !cm.haveItem(needitem, needcount)) {
            var text = "#e<오늘 클리어 횟수 #b" + totalclear + " / 7회#k (현재 월드 기준)>\r\n#e<나의 #z" + needitem + "# 개수 : " + cm.itemQuantity(needitem) + ">#n\r\n\r\n";
            text += "오늘의 무료 클리어 가능 횟수 2회를 모두 사용하셨습니다.\r\n";
            text += "추가 클리어에는 #b#z" + needitem + "# " + needcount + "개#k가 필요합니다.\r\n";
            cm.sendOk(text, 9071004);
            cm.dispose();
        } else if (cm.getClient().getKeyValue("mPark") >= 2 && cm.haveItem(needitem, needcount)) {
            var text = "#e<오늘은 #b" + String(Packages.tools.CurrentTime.getDay()) + "#k입니다.>\r\n<오늘 클리어 횟수 #b" + totalclear + " / 7회#k (현재 월드 기준)>\r\n#e<나의 #z" + needitem + "# 개수 : " + cm.itemQuantity(needitem) + ">\r\n\r\n";
            text += "던전 클리어 시 #b#z" + needitem + "# " + needcount + "개#k가 사용됩니다.\r\n\r\n"
            text += "#L953020000# 자동 경비 구역(Lv.105~114)\r\n";
            check = 1;
            cm.sendSimple(text, 9071004);
        } else {
            var text = "#e<오늘은 #b" + String(Packages.tools.CurrentTime.getDay()) + "#k입니다.>\r\n<오늘 클리어 횟수 #b" + totalclear + " / 7회#k (현재 월드 기준)>\r\n\r\n#e오늘의 무료 클리어 가능 횟수가 #b" + (2 - cm.getClient().getKeyValue("mPark")) + "회#k 남았습니다.#n#b\r\n";
            text += "#L953020000# 자동 경비 구역(Lv.105~114)\r\n";
            cm.sendSimple(text, 9071004);
        }
    } else if (status == 1) {
        select = selection;
        cm.sendYesNo("#e<오늘은 #b" + String(Packages.tools.CurrentTime.getDay()) + "#k입니다.>\r\n\r\n선택 던전 : #b자동 경비 구역(Lv.105~114)#k\r\n\r\n#k던전에 입장하시겠습니까?#n", 9071004);
    } else if (status == 2) {
        if (check == 1) {
            cm.getPlayer().setMparkCharged(true);
        }
        cm.dispose();
        for (i = 0; i < 6; i++) {
            if (cm.getClient().getChannelServer().getMapFactory().getMap(953020000 + (i * 100)).getCharactersSize() > 0) {
                cm.sendOk("이미 누군가가 입장하였습니다.", 9071004);
                return;
            }
        }
        cm.getPlayer().setSkillCustomInfo(9110, 0, 600000);
        cm.warp(953020000, 0);
        cm.resetMap(953020000);
        cm.getPlayer().setMparkcount(0);
        cm.getPlayer().setMparkkillcount(0);
        cm.EnterMonsterPark(953020000);
        cm.getPlayer().setMparkexp(3517411);
        //cm.writeLog("Log/몬파입장.log", cm.getPlayer().getName()+"가 몬스터파크 입장함.\r\n", true);
    }
}


function getData() {
    time = new Date();
    year = time.getFullYear();
    month = time.getMonth() + 1;
    if (month < 10) {
        month = "0" + month;
    }
    date = time.getDate() < 10 ? "0" + time.getDate() : time.getDate();
    data = year + "" + month + "" + date;
    day = time.getDay();
}