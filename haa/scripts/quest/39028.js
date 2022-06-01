importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server);
importPackage(Packages.constants);
importPackage(Packages.client.inventory);

status = -1;
function end(mode, type, selection) {

    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        if (status == 1) {
            qm.sendOk("나는 항상 같은자리에 있으니 언제라도 다시 말을 걸어주게.");
            qm.dispose();
            return;
        } else {
            status--;
        }
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        leftslot = qm.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot();
        leftslot1 = qm.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot(); leftslot2 = qm.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot();
        if (leftslot < 2 && leftslot1 < 2  && leftslot2 < 2) {
            qm.sendOk("인벤토리에 자리가 없다네 핥핥!");
            qm.dispose();
            return;
         }
        qm.sendNext("아주 훌륭하군! 핥! #i1712002# #z1712002# 3개,\r\n#i2435719# #z2435719# 2개,\r\#i4310006# #z4310006#n 25개야. 핥핥!");
    } else if (status == 1) {
        for (a = 0; a < 3; a++) {
            qm.gainItem(1712002, 1);
        }
        qm.gainItem(2435719, 2);qm.gainItem(4310006, 25);
        qm.forceCompleteQuest();
        str = qm.getPlayer().getV("ArcQuest1");
        ab = str.split(",");
        var clear = 0;
        for (var a = 0; a < ab.length; a++) {
            if (qm.getPlayer().getQuestStatus(ab[a]) == 2) {
                clear++
            }
        }
        qm.getPlayer().setKeyValue(39016, "count", clear + "");
        qm.dispose();
    }
}