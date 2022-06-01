importPackage(Packages.constants);
importPackage(java.lang);
importPackage(java.io);
importPackage(Packages.packet.creators);
importPackage(Packages.client.items);
importPackage(Packages.server.items);
importPackage(Packages.launch.world);
importPackage(Packages.main.world);
importPackage(Packages.database);
importPackage(java.lang);
importPackage(Packages.server);
importPackage(Packages.handling.world);
importPackage(Packages.tools.packet);

var status = -1;
var select = -1;
var inz = new Array(1212131, 1222124, 1232124, 1242145, 1242145, 1262053, 1272043, 1282043, 1302359, 1312215, 1322266, 1332291, 1342110, 1362151, 1372239, 1382276, 1402271, 1412191, 1422199, 1432229, 1442287, 1452269, 1462254, 1472277, 1482234, 1492247, 1522154, 1532159, 1582046);

function start() {
    status = -1;
    action (1, 0, 0);
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
    if (status == 1 && selection < 100) {
	status = 2;
    }

    if (status == 3 && selection == 1) {
	status = 1;
    }
    if (status == 0) {
	txt = "현재 직업이 착용 가능한 장비를 우선 추천해 드립니다.\r\n받으실 무기를 선택해 주세요.\r\n#b";
	/*if (GameConstants.isLuminous(cm.getPlayer().getJob())) {
		txt += "#L0##i" + inz[0] + ":##z" + inz[0] + ":#\r\n";
	} else if (GameConstants.isAngelicBuster(cm.getPlayer().getJob())) {
		txt += "#L1##i" + inz[1] + ":##z" + inz[1] + ":#\r\n";
	} else if (GameConstants.isXenon(cm.getPlayer().getJob())) {
		txt += "#L3##i" + inz[3] + ":##z" + inz[3] + ":#\r\n";
		txt += "#L4##i" + inz[4] + ":##z" + inz[3] + ":#\r\n";
	} else if (GameConstants.isDemonAvenger(cm.getPlayer().getJob())) {
		txt += "#L2##i" + inz[2] + ":##z" + inz[2] + ":#\r\n";
	} else if (GameConstants.isKinesis(cm.getPlayer().getJob())) {
		txt += "#L5##i" + inz[5] + ":##z" + inz[4] + ":#\r\n";
	} else if (cm.getPlayer().getJob() >= 110 && cm.getPlayer().getJob() <= 122) {
		txt += "#L6##i" + inz[6] + ":##z" + inz[6] + ":#\r\n";
		txt += "#L7##i" + inz[7] + ":##z" + inz[7] + ":#\r\n";
		txt += "#L8##i" + inz[8] + ":##z" + inz[8] + ":#\r\n";
		txt += "#L14##i" + inz[14] + ":##z" + inz[14] + ":#\r\n";
		txt += "#L15##i" + inz[15] + ":##z" + inz[15] + ":#\r\n";
		txt += "#L16##i" + inz[16] + ":##z" + inz[16] + ":#\r\n";
	} else if (cm.getPlayer().getJob() >= 1110 && cm.getPlayer().getJob() <= 1112) {
		txt += "#L6##i" + inz[6] + ":##z" + inz[6] + ":#\r\n";
		txt += "#L14##i" + inz[14] + ":##z" + inz[14] + ":#\r\n";
	} else if (GameConstants.isDemonSlayer(cm.getPlayer().getJob())) {
		txt += "#L7##i" + inz[7] + ":##z" + inz[7] + ":#\r\n";
		txt += "#L8##i" + inz[8] + ":##z" + inz[8] + ":#\r\n";
	} else if (GameConstants.isMichael(cm.getPlayer().getJob())) {
		txt += "#L6##i" + inz[6] + ":##z" + inz[6] + ":#\r\n";
	} else if (GameConstants.isKaiser(cm.getPlayer().getJob())) {
		txt += "#L14##i" + inz[14] + ":##z" + inz[14] + ":#\r\n";
	} else if (cm.getPlayer().getJob() >= 420 && cm.getPlayer().getJob() <= 422) {
		txt += "#L9##i" + inz[9] + ":##z" + inz[9] + ":#\r\n";
	} else if (cm.getPlayer().getJob() >= 430 && cm.getPlayer().getJob() <= 434) {
		txt += "#L9##i" + inz[9] + ":##z" + inz[9] + ":#\r\n";
		txt += "#L10##i" + inz[10] + ":##z" + inz[10] + ":#\r\n";
	} else if (GameConstants.isPhantom(cm.getPlayer().getJob())) {
		txt += "#L11##i" + inz[11] + ":##z" + inz[11] + ":#\r\n";
	} else if ((cm.getPlayer().getJob() >= 200 && cm.getPlayer().getJob() <= 232) || (cm.getPlayer().getJob() >= 1200 && cm.getPlayer().getJob() <= 1212) || GameConstants.isEvan(cm.getPlayer().getJob()) || (cm.getPlayer().getJob() >= 3200 && cm.getPlayer().getJob() <= 3212)) {
		txt += "#L12##i" + inz[12] + ":##z" + inz[12] + ":#\r\n";
		txt += "#L13##i" + inz[13] + ":##z" + inz[13] + ":#\r\n";
	} else if (cm.getPlayer().getJob() >= 130 && cm.getPlayer().getJob() <= 132) {
		txt += "#L17##i" + inz[17] + ":##z" + inz[17] + ":#\r\n";
		txt += "#L18##i" + inz[18] + ":##z" + inz[18] + ":#\r\n";
	} else if (GameConstants.isAran(cm.getPlayer().getJob())) {
		txt += "#L18##i" + inz[18] + ":##z" + inz[18] + ":#\r\n";
	} else if ((cm.getPlayer().getJob() >= 310 && cm.getPlayer().getJob() <= 312) || cm.getPlayer().getJob() >= 1300 && cm.getPlayer().getJob() <= 1312) {
		txt += "#L19##i" + inz[19] + ":##z" + inz[19] + ":#\r\n";
	} else if ((cm.getPlayer().getJob() >= 320 && cm.getPlayer().getJob() <= 322) || cm.getPlayer().getJob() >= 3300 && cm.getPlayer().getJob() <= 3312) {
		txt += "#L20##i" + inz[20] + ":##z" + inz[20] + ":#\r\n";
	} else if ((cm.getPlayer().getJob() >= 410 && cm.getPlayer().getJob() <= 412) || cm.getPlayer().getJob() >= 1400 && cm.getPlayer().getJob() <= 1412) {
		txt += "#L21##i" + inz[21] + ":##z" + inz[21] + ":#\r\n";
	} else if ((cm.getPlayer().getJob() >= 510 && cm.getPlayer().getJob() <= 512) || (cm.getPlayer().getJob() >= 1500 && cm.getPlayer().getJob() <= 1512) || GameConstants.isEunWol(cm.getPlayer().getJob())) {
		txt += "#L22##i" + inz[22] + ":##z" + inz[22] + ":#\r\n";
	} else if ((cm.getPlayer().getJob() >= 520 && cm.getPlayer().getJob() <= 522) || (cm.getPlayer().getJob() >= 3500 && cm.getPlayer().getJob() <= 3512)) {
		txt += "#L23##i" + inz[23] + ":##z" + inz[23] + ":#\r\n";
	} else if (GameConstants.isMercedes(cm.getPlayer().getJob())) {
		txt += "#L24##i" + inz[24] + ":##z" + inz[24] + ":#\r\n";
	} else if (GameConstants.isCannon(cm.getPlayer().getJob())) {
		txt += "#L25##i" + inz[25] + ":##z" + inz[25] + ":#\r\n";
	} else if (GameConstants.isBlaster(cm.getPlayer().getJob())) {
		txt += "#L26##i" + inz[26] + ":##z" + inz[26] + ":#\r\n";
	} else if (GameConstants.isKadena(cm.getPlayer().getJob())) {
		txt += "#L26##i" + inz[27] + ":##z" + inz[27] + ":#\r\n";
	} else if (GameConstants.isIllium(cm.getPlayer().getJob())) {
		txt += "#L26##i" + inz[28] + ":##z" + inz[28] + ":#\r\n";
	} else if (GameConstants.isZero(cm.getPlayer().getJob())) {
		txt += "#L26##i" + inz[29] + ":##z" + inz[29] + ":#\r\n";
	}*/
	txt += "\r\n#L100#전체 아이템 리스트를 본다.\r\n#L101#사용 취소";
	cm.sendSimple(txt);
    } else if (status == 1) {
	if (selection == 100) {
		txt = "원하시는 무기를 선택해 주세요.\r\n#b";
		for (i = 0; i < inz.length; i++) {
			txt += "#L" + i + "##i" + inz[i] + ":##z" + inz[i] + ":#\r\n";
		}
		cm.sendSimple(txt);
	} else if (selection == 101) {
		cm.dispose();
	}
    } else if (status == 2) {
	select = selection;
	cm.sendSimple("받고 싶은 장비가 이 장비가 확실한가요?\r\n#i" + inz[selection] + ":# #z" + inz[selection] + ":#\r\n\r\n#e#r※주의※#k#n\r\n한 번 조각을 아이템으로 교환하면 #r상자가 삭제#k되며, 다시 다른 아이템으로 교환할 수 없습니다.\r\n\r\n#b#L0#네 맞습니다.\r\n#L1#아닙니다. 다시 선택하겠습니다.");
    } else if (status == 3) {
	cm.dispose();
	//cm.gainItem(inz[select], 1);
	item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(inz[select]);
	item.setStr(item.getStr() + 64);
	item.setDex(item.getDex() + 64);
	item.setInt(item.getInt() + 64);
	item.setLuk(item.getLuk() + 64);
	item.setWatk(item.getWatk() + 200);
	item.setMatk(item.getMatk() + 200);
	item.setBossDamage(item.getBossDamage() + 35);
	item.setIgnorePDR(item.getIgnorePDR() + 20);
	item.setTotalDamage(item.getTotalDamage() + 12);
	item.setEnhance(item.getEnhance() + 15);
	Packages.server.MapleInventoryManipulator.addbyItem(cm.getClient(), item, false);
	cm.gainItem(2630117, -1);
	cm.sendOk("아이템이 지급되었습니다.");
    }
}