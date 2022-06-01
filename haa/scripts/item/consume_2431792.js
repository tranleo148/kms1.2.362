importPackage(Packages.server.items);
importPackage(Packages.client.items);
importPackage(java.lang);
importPackage(Packages.launch.world);
importPackage(Packages.packet.creators);

var status = 0;
var itemid = new Array(1232014, 1302152, 1312065, 1322096, 1402095, 1412065, 1422066, 1432086, 1442116, 1212014, 1372084, 1382104, 1452111, 1462099, 1522018, 1242042, 1332130, 1342036, 1362019, 1472122)
var itemNed = 2431792

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
	cm.sendYesNo("여제 무기 랜덤 상자를 정말로 까시겠습니까?");
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