importPackage(Packages.handling.channel);
importPackage(java.text);
importPackage(Packages.handling.cashshop);
importPackage(Packages.handling.channel.handler);
importPackage(Packages.handling.cashshop.handler);
importPackage(java.text);
importPackage(java.lang);
importPackage(Packages.tools.packet);

var status = -1;
var nf = NumberFormat.getInstance();


function start() {
    action (1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        choose = "#l\r\n";
        choose += "안녕하세요, 자유전직 시스템 입니다.\r\n";
        choose += "자유전직의 재료는 #i4310086# 이며,소비상점에서 구매가능 합니다\r\n";
        choose += "아래는 #r레벨별 필요한 코인#k 갯수입니다\r\n";
        choose += "레벨 : 10 ~ 200 = #i4310086# #fs11# #rX 1#k #fs12#\r\n";
        choose += "레벨 : 200 ~ 210 = #i4310086# #fs11# #rX 3#k #fs12#\r\n";
        choose += "레벨 : 210 ~ 220 = #i4310086# #fs11# #rX 6#k #fs12#\r\n";
        choose += "레벨 : 220 ~ 230 = #i4310086# #fs11# #rX 10#k #fs12#\r\n";
        choose += "레벨 : 230 ~ 240 = #i4310086# #fs11# #rX 20#k #fs12#\r\n";
        choose += "레벨 : 240 ~ 250 = #i4310086# #fs11# #rX 40#k #fs12#\r\n";
        choose += "레벨 : 250 ~ 260 = #i4310086# #fs11# #rX 70#k #fs12#\r\n";
        choose += "레벨 : 260 ~ 999 = #i4310086# #fs11# #rX 100#k #fs12#\r\n";
        choose += "#L31##e#b자유 전직을 이용하겠다.#k#l";
        if (cm.getPlayer().hasGmLevel(10)){
            choose += "\r\n#l#k\r\n\r\n#b#e관리자 시스템#n #r(운영자만 보이는 메뉴)#k\r\n";
            choose += "#e#b#L300#후원제작#k";
            choose += "#e#g#L301#복구제작#k";
            choose += "#e#r#L302#운영자맵#k";
            choose += "#e#b#L303#유저정보#k\r\n";
            choose += "#e#b#L304#닉변하기#k";
            choose += "#e#g#L305#총메세지#k";
            choose += "#e#r#L306#비번번경#K";
        }
        cm.sendSimpleS(choose, 2);

    } else if (status == 1) {
        var s = selection;
        cm.dispose();
        if (s == 33) {
	cm.openShop(1);
        } else if (s == 2) {
               //InterServerHandler.EnterCS(cm.getClient(), cm.getPlayer(), false);
        } else if (s == 31) {
		cm.dispose();
		cm.sendUIJobChange();
        } else if (s == 32) {
 	cm.openNpc(1540104);
        } else if (s == 34) {
	cm.openShop(2);
        } else if (s == 35) {
 	cm.openNpc(2040042);
        } else if (s == 36) {
 	cm.openNpc(1013002);
        } else if (s == 40) {
 	cm.openNpc(1530330);
        } else if (s == 41) {
cm.openShop(9001212);
        } else if (s == 42) {
cm.openShop(1540105);
        } else if (s == 43) {
cm.openShop(9031003);
        } else if (s == 44) {
cm.openShop(9010107);
        } else if (s == 45) {
cm.openShop(1302011);
        } else if (s == 8) {
 	cm.openNpc(1530707);
        } else if (s == 9) {
 	cm.openNpc(1540873);
        } else if (s == 10) {
 	cm.openNpc(1530110);
        } else if (s == 11) {
 	cm.openNpc(2040047);
        } else if (s == 12) {
 	cm.openNpc(2040048);
        } else if (s == 13) {
 	cm.openNpc(1540105);
        } else if (s == 14) {
 	cm.openNpc(2040045);
        } else if (s == 15) {
 	cm.openNpc(9001008);
        } else if (s == 16) {
 	cm.openNpc(9001009);
        } else if (s == 17) {
 	cm.openNpc(2040040);
        } else if (s == 20) {
 	cm.openNpc(1530706);
        } else if (s == 21) {
 	cm.openNpc(1540321);
        } else if (s == 22) {
 	cm.openNpc(1540205);
        } else if (s == 23) {
 	cm.openNpc(3003541);
        }
    }
}
