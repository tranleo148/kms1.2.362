importPackage(java.sql);
importPackage(java.lang);
importPackage(Packages.database);
importPackage(Packages.handling.world);
importPackage(Packages.constants);
importPackage(java.util);
importPackage(java.io);
importPackage(Packages.client.inventory);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools.packet);

var day = 1;

전사 = [1152196, 1004808, 1102940, 1082695, 1073158, 1042254, 1062165, 1190555];
법사 = [1152197, 1004809, 1102941, 1082696, 1073159, 1042255, 1062166, 1190556];
궁수 = [1152198, 1004810, 1102942, 1082697, 1073160, 1042256, 1062167, 1190557];
도적 = [1152199, 1004811, 1102943, 1082698, 1073161, 1042257, 1062168, 1190558];
해적 = [1152200, 1004812, 1102944, 1082699, 1073162, 1042258, 1062169, 1190559];
캐시템 = [1002186, 1072153, 1082102, 1012289, 1022079, 1032024, 1102039, 1802653,1802653,1802653, 1115095,1115196, 1115210, 1115311];
악세 = [1113306, 1012632, 1132308, 1122430, 1032316, 1022278, 1122150, 1113070,1113055, 1162083, 1182263, 1113085, 1672040 ];
심볼 = [1712001, 1712002, 1712003, 1712004, 1712005, 1712006];

function start() {
    status = -1;
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
        cm.sendSimple("계열 선택\r\n"
                + "#L0#전사#l\r\n"
                + "#L1#궁수#l\r\n"
                + "#L2#마법사#l\r\n"
                + "#L3#도적#l\r\n"
                + "#L4#해적(STR)#l\r\n"
                + "#L5#해적(DEX)#l");
    } else if (status == 1) {
	if (selection == 0){
	 for(var i in 전사){
	var inz = MapleItemInformationProvider.getInstance().getEquipById(전사[i]);
	inz.setStr(1000);
	inz.setDex(1000);
	inz.setInt(1000);
	inz.setLuk(1000);
	inz.setWatk(1000);
	inz.setMatk(1000);
            inz.setReqLevel(-100);
	inz.setBossDamage(100);
	inz.setTotalDamage(100);
	inz.setUpgradeSlots(0);
	inz.setState(20);
	inz.setLevel(10);
	inz.setEnhance(30);
	inz.setPotential1(40041);
	inz.setPotential2(40041);
	inz.setPotential3(40041);
	inz.setPotential4(40041);
	inz.setPotential5(40041);
	inz.setPotential6(40041);
	MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
	    }
	 for(var i in 악세){
	var inz = MapleItemInformationProvider.getInstance().getEquipById(악세[i]);
	inz.setStr(1000);
	inz.setDex(1000);
	inz.setInt(1000);
	inz.setLuk(1000);
	inz.setWatk(1000);
	inz.setMatk(1000);
            inz.setReqLevel(-100);
	inz.setBossDamage(100);
	inz.setTotalDamage(100);
	inz.setUpgradeSlots(0);
	inz.setState(20);
	inz.setLevel(10);
	inz.setEnhance(30);
	inz.setPotential1(40041);
	inz.setPotential2(40041);
	inz.setPotential3(40041);
	inz.setPotential4(40041);
	inz.setPotential5(40041);
	inz.setPotential6(40041);
	MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
	    }
	for(var i in 캐시템){
	var inz = MapleItemInformationProvider.getInstance().getEquipById(캐시템[i]);
	inz.setWatk(1000);
	inz.setMatk(1000);
	inz.setState(20);
	inz.setPotential1(40041);
	inz.setPotential2(40041);
	inz.setPotential3(40041);
	inz.setPotential4(40041);
	inz.setPotential5(40041);
	inz.setPotential6(40041);
	MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
	    }
}
	if (selection == 1){
	 for(var i in 궁수){
	var inz = MapleItemInformationProvider.getInstance().getEquipById(궁수[i]);
	inz.setStr(1000);
	inz.setDex(1000);
	inz.setInt(1000);
	inz.setLuk(1000);
	inz.setWatk(1000);
	inz.setMatk(1000);
            inz.setReqLevel(-100);
	inz.setBossDamage(100);
	inz.setTotalDamage(100);
	inz.setUpgradeSlots(0);
	inz.setState(20);
	inz.setLevel(10);
	inz.setEnhance(30);
	inz.setPotential1(40042);
	inz.setPotential2(40042);
	inz.setPotential3(40042);
	inz.setPotential4(40042);
	inz.setPotential5(40042);
	inz.setPotential6(40042);
	MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
	    }
	 for(var i in 악세){
	var inz = MapleItemInformationProvider.getInstance().getEquipById(악세[i]);
	inz.setStr(1000);
	inz.setDex(1000);
	inz.setInt(1000);
	inz.setLuk(1000);
	inz.setWatk(1000);
	inz.setMatk(1000);
            inz.setReqLevel(-100);
	inz.setBossDamage(100);
	inz.setTotalDamage(100);
	inz.setUpgradeSlots(0);
	inz.setState(20);
	inz.setLevel(10);
	inz.setEnhance(30);
	inz.setPotential1(40042);
	inz.setPotential2(40042);
	inz.setPotential3(40042);
	inz.setPotential4(40042);
	inz.setPotential5(40042);
	inz.setPotential6(40042);
	MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
	    }
	for(var i in 캐시템){
	var inz = MapleItemInformationProvider.getInstance().getEquipById(캐시템[i]);
	inz.setWatk(1000);
	inz.setMatk(1000);
	inz.setState(20);
	inz.setPotential1(40042);
	inz.setPotential2(40042);
	inz.setPotential3(40042);
	inz.setPotential4(40042);
	inz.setPotential5(40042);
	inz.setPotential6(40042);
	MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
	    }
}
	if (selection == 2){
	 for(var i in 법사){
	var inz = MapleItemInformationProvider.getInstance().getEquipById(법사[i]);
	inz.setStr(1000);
	inz.setDex(1000);
	inz.setInt(1000);
	inz.setLuk(1000);
	inz.setWatk(1000);
	inz.setMatk(1000);
            inz.setReqLevel(-100);
	inz.setBossDamage(100);
	inz.setTotalDamage(100);
	inz.setUpgradeSlots(0);
	inz.setState(20);
	inz.setLevel(10);
	inz.setEnhance(30);
	inz.setPotential1(40043);
	inz.setPotential2(40043);
	inz.setPotential3(40043);
	inz.setPotential4(40043);
	inz.setPotential5(40043);
	inz.setPotential6(40043);
	MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
	    }
	 for(var i in 악세){
	var inz = MapleItemInformationProvider.getInstance().getEquipById(악세[i]);
	inz.setStr(1000);
	inz.setDex(1000);
	inz.setInt(1000);
	inz.setLuk(1000);
	inz.setWatk(1000);
	inz.setMatk(1000);
            inz.setReqLevel(-100);
	inz.setBossDamage(100);
	inz.setTotalDamage(100);
	inz.setUpgradeSlots(0);
	inz.setState(20);
	inz.setLevel(10);
	inz.setEnhance(30);
	inz.setPotential1(40043);
	inz.setPotential2(40043);
	inz.setPotential3(40043);
	inz.setPotential4(40043);
	inz.setPotential5(40043);
	inz.setPotential6(40043);
	MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
	    }
	for(var i in 캐시템){
	var inz = MapleItemInformationProvider.getInstance().getEquipById(캐시템[i]);
	inz.setWatk(1000);
	inz.setMatk(1000);
	inz.setState(20);
	inz.setPotential1(40043);
	inz.setPotential2(40043);
	inz.setPotential3(40043);
	inz.setPotential4(40043);
	inz.setPotential5(40043);
	inz.setPotential6(40043);
	MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
	    }
}
	if (selection == 3){
	 for(var i in 도적){
	var inz = MapleItemInformationProvider.getInstance().getEquipById(도적[i]);
	inz.setStr(1000);
	inz.setDex(1000);
	inz.setInt(1000);
	inz.setLuk(1000);
	inz.setWatk(1000);
	inz.setMatk(1000);
            inz.setReqLevel(-100);
	inz.setBossDamage(100);
	inz.setTotalDamage(100);
	inz.setUpgradeSlots(0);
	inz.setState(20);
	inz.setLevel(10);
	inz.setEnhance(30);
	inz.setPotential1(40044);
	inz.setPotential2(40044);
	inz.setPotential3(40044);
	inz.setPotential4(40044);
	inz.setPotential5(40044);
	inz.setPotential6(40044);
	MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
	    }
	 for(var i in 악세){
	var inz = MapleItemInformationProvider.getInstance().getEquipById(악세[i]);
	inz.setStr(1000);
	inz.setDex(1000);
	inz.setInt(1000);
	inz.setLuk(1000);
	inz.setWatk(1000);
	inz.setMatk(1000);
            inz.setReqLevel(-100);
	inz.setBossDamage(100);
	inz.setTotalDamage(100);
	inz.setUpgradeSlots(0);
	inz.setState(20);
	inz.setLevel(10);
	inz.setEnhance(30);
	inz.setPotential1(40044);
	inz.setPotential2(40044);
	inz.setPotential3(40044);
	inz.setPotential4(40044);
	inz.setPotential5(40044);
	inz.setPotential6(40044);
	MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
	    }
	for(var i in 캐시템){
	var inz = MapleItemInformationProvider.getInstance().getEquipById(캐시템[i]);
	inz.setWatk(1000);
	inz.setMatk(1000);
	inz.setState(20);
	inz.setPotential1(40044);
	inz.setPotential2(40044);
	inz.setPotential3(40044);
	inz.setPotential4(40044);
	inz.setPotential5(40044);
	inz.setPotential6(40044);
	MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
	    }
}
	if (selection == 4){
	 for(var i in 해적){
	var inz = MapleItemInformationProvider.getInstance().getEquipById(해적[i]);
	inz.setStr(1000);
	inz.setDex(1000);
	inz.setInt(1000);
	inz.setLuk(1000);
	inz.setWatk(1000);
	inz.setMatk(1000);
            inz.setReqLevel(-100);
	inz.setBossDamage(100);
	inz.setTotalDamage(100);
	inz.setUpgradeSlots(0);
	inz.setState(20);
	inz.setLevel(10);
	inz.setEnhance(30);
	inz.setPotential1(40041);
	inz.setPotential2(40041);
	inz.setPotential3(40041);
	inz.setPotential4(40041);
	inz.setPotential5(40041);
	inz.setPotential6(40041);
	MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
	    }
	 for(var i in 악세){
	var inz = MapleItemInformationProvider.getInstance().getEquipById(악세[i]);
	inz.setStr(1000);
	inz.setDex(1000);
	inz.setInt(1000);
	inz.setLuk(1000);
	inz.setWatk(1000);
	inz.setMatk(1000);
            inz.setReqLevel(-100);
	inz.setBossDamage(100);
	inz.setTotalDamage(100);
	inz.setUpgradeSlots(0);
	inz.setState(20);
	inz.setLevel(10);
	inz.setEnhance(30);
	inz.setPotential1(40041);
	inz.setPotential2(40041);
	inz.setPotential3(40041);
	inz.setPotential4(40041);
	inz.setPotential5(40041);
	inz.setPotential6(40041);
	MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
	    }
	for(var i in 캐시템){
	var inz = MapleItemInformationProvider.getInstance().getEquipById(캐시템[i]);
	inz.setWatk(1000);
	inz.setMatk(1000);
	inz.setState(20);
	inz.setPotential1(40041);
	inz.setPotential2(40041);
	inz.setPotential3(40041);
	inz.setPotential4(40041);
	inz.setPotential5(40041);
	inz.setPotential6(40041);
	MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
	    }
}
	if (selection == 5){
	 for(var i in 해적){
	var inz = MapleItemInformationProvider.getInstance().getEquipById(해적[i]);
	inz.setStr(1000);
	inz.setDex(1000);
	inz.setInt(1000);
	inz.setLuk(1000);
	inz.setWatk(1000);
	inz.setMatk(1000);
            inz.setReqLevel(-100);
	inz.setBossDamage(100);
	inz.setTotalDamage(100);
	inz.setUpgradeSlots(0);
	inz.setState(20);
	inz.setLevel(10);
	inz.setEnhance(30);
	inz.setPotential1(40042);
	inz.setPotential2(40042);
	inz.setPotential3(40042);
	inz.setPotential4(40042);
	inz.setPotential5(40042);
	inz.setPotential6(40042);
	MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
	    }
	 for(var i in 악세){
	var inz = MapleItemInformationProvider.getInstance().getEquipById(악세[i]);
	inz.setStr(1000);
	inz.setDex(1000);
	inz.setInt(1000);
	inz.setLuk(1000);
	inz.setWatk(1000);
	inz.setMatk(1000);
            inz.setReqLevel(-100);
	inz.setBossDamage(100);
	inz.setTotalDamage(100);
	inz.setUpgradeSlots(0);
	inz.setState(20);
	inz.setLevel(10);
	inz.setEnhance(30);
	inz.setPotential1(40042);
	inz.setPotential2(40042);
	inz.setPotential3(40042);
	inz.setPotential4(40042);
	inz.setPotential5(40042);
	inz.setPotential6(40042);
	MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
	    }
	for(var i in 캐시템){
	var inz = MapleItemInformationProvider.getInstance().getEquipById(캐시템[i]);
	inz.setWatk(1000);
	inz.setMatk(1000);
	inz.setState(20);
	inz.setPotential1(40042);
	inz.setPotential2(40042);
	inz.setPotential3(40042);
	inz.setPotential4(40042);
	inz.setPotential5(40042);
	inz.setPotential6(40042);
	MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
	    }
}
 	        cm.gainItem(2431938, -1);
	        cm.sendSimple(text);
	        cm.dispose();
		
	}
}