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
        leftslot1 = qm.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot();
        if (leftslot < 2 && leftslot1 < 3) {
            qm.sendOk("인벤토리에 자리가 없군요.");
            qm.dispose();
            return;
         }
        qm.sendNext("임무 수행에 대한 보상으로 #i1712006# #z1712006# 3개,\r\n#i2435719# #z2435719# 2개를 드리겠습니다.");
    } else if (status == 1) {
        for (a = 0; a < 3; a++) {
            qm.gainItem(1712006, 1);
        }
        qm.gainItem(2435719, 2);
        qm.forceCompleteQuest();
        str = qm.getPlayer().getV("ArcQuest5");
        ab = str.split(",");
        var clear = true;
        for (var a = 0; a < ab.length; a++) {
            if (qm.getPlayer().getQuestStatus(ab[a]) == 1) {
                clear = false;
                break;
            }
        }
        if (clear) {
            qm.getPlayer().setKeyValue(34775, "count", "3");
        }
        qm.dispose();
    }
}