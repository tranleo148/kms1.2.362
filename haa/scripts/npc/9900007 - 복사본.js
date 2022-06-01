importPackage(Packages.constants);
var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
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
        talk = "놀장강 수치를 복구받고 싶으신 아이템을 선택해 주세요.\r\n";
        talk += "#r(오류가 있는 아이템만 리스트에 나타납니다.)#k\r\n\r\n"
        for (i = 0; i < cm.getInventory(1).getSlotLimit(); i++) {
            if (cm.getInventory(1).getItem(i) != null && cm.getInventory(1).getItem(i).getOwner() != "복구" && Packages.server.MapleItemInformationProvider.getInstance().getReqLevel(cm.getInventory(1).getItem(i).getItemId()) < 150 && Packages.server.MapleItemInformationProvider.getInstance().getReqLevel(cm.getInventory(1).getItem(i).getItemId()) % 10 != 0 && !cm.isCash(cm.getInventory(1).getItem(i).getItemId())) {
                if (cm.getInventory(1).getItem(i).isAmazingequipscroll()) {
                    talk += "#L" + i + "# #i" + cm.getInventory(1).getItem(i).getItemId() + "# #b#z" + cm.getInventory(1).getItem(i).getItemId() + "# #r[" + cm.getInventory(1).getItem(i).getEnhance() + "성 강화 적용]\r\n";
                }
            }
        }
        cm.sendSimple(talk);
    } else if (status == 1) {
        st = selection;
        nEquip = cm.getInventory(1).getItem(st);
   	statplus = [0, 2, 5, 9, 14, 20, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27];
        atkplus = [0, 0, 0, 0, 0, 2, 5, 9, 14, 20, 27, 36, 45, 55, 66];
        reallevel = Packages.server.MapleItemInformationProvider.getInstance().getReqLevel(cm.getInventory(1).getItem(st).getItemId()) - Packages.server.MapleItemInformationProvider.getInstance().getReqLevel(cm.getInventory(1).getItem(st).getItemId()) % 10
        switch (reallevel) {
            case 80:
                data = [2, 3, 5, 8, 12, 2, 3, 4, 5, 6, 7, 9, 10, 11]
                break;
            case 90:
                data = [4, 5, 7, 10, 14, 3, 4, 5, 6, 7, 8, 10, 11, 12, 13]
                break;
            case 100:
                data = [7, 8, 10, 13, 17, 4, 5, 6, 7, 8, 9, 11, 12, 13, 14]
                break;
            case 110:
                data = [9, 10, 12, 15, 19, 5, 6, 7, 8, 9, 10, 12, 13, 14, 15]
                break;
            case 120:
                data = [12, 13, 15, 18, 22, 6, 7, 8, 9, 10, 11, 13, 14, 15, 16]
                break;
            case 130:
                data = [14, 15, 17, 20, 24, 7, 8, 9, 10, 11, 12, 14, 15, 16, 17]
                break;
            case 140:
                data = [17, 18, 20, 23, 27, 8, 9, 10, 11, 12, 13, 15, 16, 17, 18]
                break;
            case 150:
                data = [19, 20, 22, 25, 29, 9, 10, 11, 12, 13, 14, 16, 17, 18, 19]
                break;
            default: // 80제 미만
                data = [1, 2, 4, 7, 11, 1, 2, 3, 4, 5, 6, 8, 9, 10, 11]
                break;
        }
        nEquip.addStr(-statplus[parseInt(nEquip.getEnhance())]);
        nEquip.addDex(-statplus[parseInt(nEquip.getEnhance())]);
        nEquip.addInt(-statplus[parseInt(nEquip.getEnhance())]);
        nEquip.addLuk(-statplus[parseInt(nEquip.getEnhance())]);
        nEquip.addWatk(-atkplus[parseInt(nEquip.getEnhance())]);
        nEquip.addMatk(-atkplus[parseInt(nEquip.getEnhance())]);
        for (i = 0; i < nEquip.getEnhance(); i++) {
            if (i < 5) {
                if (isAccessory(nEquip.getItemId())) { // 악세서리 보너스스탯
                    if (reallevel < 120) {
                        bonus = 1;
                    } else {
                        bonus = 2;
                    }
                    nEquip.addStr(bonus);
                    nEquip.addDex(bonus);
                    nEquip.addInt(bonus);
                    nEquip.addLuk(bonus);
                }
                nEquip.addStr(data[i]);
                nEquip.addDex(data[i]);
                nEquip.addInt(data[i]);
                nEquip.addLuk(data[i]);
            } else {
		if (GameConstants.isAccessory(nEquip.getItemId())) { // 악세서리 보너스스탯
                    bonus = 2;
                    nEquip.addStr(bonus);
                    nEquip.addDex(bonus);
                    nEquip.addInt(bonus);
                    nEquip.addLuk(bonus);
                }
                nEquip.addWatk(data[i]);
                nEquip.addMatk(data[i]);
            }
        }
	nEquip.setOwner("복구");
	cm.getPlayer().forceReAddItem(nEquip, Packages.client.inventory.MapleInventoryType.EQUIP);
	cm.sendOk("복구가 완료되었습니다.");
	cm.dispose();
	return;
    }
}

function isAccessory(itemId) {
        return (Math.floor(itemId/10000) == 101 || Math.floor(itemId/10000) == 102 || Math.floor(itemId/10000) == 103 || Math.floor(itemId/10000) == 104 || Math.floor(itemId/10000) == 111 || Math.floor(itemId/10000) == 112 || Math.floor(itemId/10000) == 113 || Math.floor(itemId/10000) == 114 || Math.floor(itemId/10000) == 115);
}