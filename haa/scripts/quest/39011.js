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
        leftslot1 = qm.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot();
        if (leftslot < 2 && leftslot1 < 3) {
            qm.sendOk("인벤토리 공간을 확인하고 다시 말을 걸어주게.");
            qm.dispose();
            return;
         }
        qm.sendNext("자, 이건 #b약속한 선물#k이다.\r\n#i4001868# #b#z4001868#\r\n우리 종족이 뿌린 #r악의 씨앗#k을 거두는 일을 도와줘서 고맙다..");
    } else if (status == 1) {
        qm.gainItem(4001868, 2);
        qm.gainItem(2435719, 2);
        qm.forceCompleteQuest();
        str = qm.getPlayer().getV("ArcQuest7");
        ab = str.split(",");
        var clear = 0;
        for (var a = 0; a < ab.length; a++) {
            if (qm.getPlayer().getQuestStatus(ab[a]) == 2) {
                clear++
            }
        }
        qm.getPlayer().setKeyValue(15708, "cq", clear + "");
        qm.dispose();
    }
}