﻿var status;
importPackage(Packages.server);
importPackage(Packages.client.inventory);
importPackage(Packages.server);
importPackage(Packages.server.items);
one = Math.floor(Math.random() * 5) + 1 // 최소 10 최대 35 , 혼테일
function start() {
    status = -1;
    action(1, 1, 0);
}

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
		        item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(1712003);
		       		        item.setArcLevel(20);
		        item.setStr(2200);
		        item.setDex(2200);
		        item.setInt(2200);
		        item.setLuk(2200);
                                     item.setArc(220);
		        Packages.server.MapleInventoryManipulator.addbyItem(cm.getClient(), item, false);
		        cm.sendOk("#b만렙 #i1712003##z1712003##b 1개#fc0xFF000000#를 획득 하였습니다.");
		cm.gainItem(2632132, -1);
		cm.dispose();
	}
}

