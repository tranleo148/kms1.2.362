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
        leftslot = qm.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot();
        if (leftslot < 2) {
            qm.sendOk("인벤토리를 비우고 다시 말을 걸어주세요.");
            qm.dispose();
            return;
        }
        qm.sendNext("#i4001893# #z4001893# 1개\r\n#i4310006# #i4310006# 35개를 지급 해 드렸습니다. 기타창을 확인해 주세요.");
    } else if (status == 1) {
        qm.gainItem(4001893, 5);qm.forceCompleteQuest();
        qm.gainItem(4310006, 35);
        str = qm.getPlayer().getV("ArcQuest8");
        ab = str.split(",");
        var clear = 0;
        for (var a = 0; a < ab.length; a++) {
            if (qm.getPlayer().getQuestStatus(ab[a]) == 2) {
                clear++
            }
        }
        qm.dispose();
    }
}