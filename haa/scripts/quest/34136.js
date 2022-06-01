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
        if (leftslot < 3 && leftslot1 < 3) {
            qm.sendOk("인벤토리를 비우고 다시 말을 걸어주세요.");
            qm.dispose();
            return;
        }
        qm.sendNextS("#b(로나에게 강인한 영혼의 에르다스를 가까이서 관찰하면서 얻을 정보를 전달해 줬다.)", 2);
    } else if(status == 1) {
        qm.sendNext("자, 여기 #i1712001# #z1712001# 3개\r\n#i2435719# #z2435719# 2개 \r\n#i4310006# #z4310006# 25개\r\n드렸습니다.당신의 도움으로 이 공간의 정체를 파헤치는 날이 조금 더 앞당겨지겠군요.");
    } else if (status == 2) {
        for (a = 0; a < 3; a++) {
            qm.gainItem(1712001, 1);
        }
qm.getPlayer().setKeyValue(100592, "point", "" + (qm.getPlayer().getKeyValue(100592, "point") + 1));
        qm.gainItem(2435719, 2);
qm.gainItem(4310006, 25);
        qm.forceCompleteQuest();
        str = qm.getPlayer().getV("ArcQuest0");
        ab = str.split(",");
        var clear = 0;
        for (var a = 0; a < ab.length; a++) {
            if (qm.getPlayer().getQuestStatus(ab[a]) == 2) {
                clear++
            }
        }
        qm.getPlayer().setKeyValue(34127, "count", (clear + 2) + "");
        qm.dispose();
    }
}