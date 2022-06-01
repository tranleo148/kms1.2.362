importPackage(Packages.server.items);
importPackage(Packages.client.items);
importPackage(java.lang);
importPackage(Packages.launch.world);
importPackage(Packages.packet.creators);

var status = 0;
var itemid = new Array(1003172, 1003173, 1003174, 1003175, 1003176, 1102275, 1102276, 1102277, 1102278, 1102279, 1082295, 1082296, 1082297, 1082298, 1082299, 1052314, 1052315, 1052316, 1052317, 1052318, 1072485, 1072486, 1072487, 1072488, 1072489)
var itemNed = 2430499

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
	if (status == 0) {
	cm.sendYesNo("여제 방어구 랜덤 상자를 정말로 까시겠습니까?");
	} else if (status == 1) {
	itemSet = itemid[Math.floor(Math.random() * itemid.length)];
	var itemQty = 1
	cm.gainItem(itemNed, -1);
	cm.gainItem(itemSet, itemQty);
	cm.sendOk("#i"+itemSet+":#　#b#z"+itemSet+"# 이(가) 나왔습니다!!");
	cm.dispose();
	}
}
}