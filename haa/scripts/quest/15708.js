importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server);
importPackage(Packages.constants);
importPackage(Packages.client.inventory);

status = -1;
function start(mode, type, selection) {

    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        if (status == 1) {
            qm.sendNextS("내가 사람을 잘못 봤나 보군...\r\n혹시라도 마음이 바뀌면 다시 말하게나.", 0x04, 1540895);
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
        qm.sendNextS("혹시 일주일 안에 세계수를 #b5회 이상 정화#k 해준다면, 자네의 능력을 인정하고 #r보답#k으로 #b#i4001868# #z4001868##k을 더 주겠네.", 0x04, 1540895);
    } else if (status == 1) {
        qm.sendYesNoS("나의 부하들이 저지르고 있는 만행을 멈추고 세계수의 타락을 막는 데에 힘써주겠나?", 0x04, 1540895);
    } else if (status == 2) {
        qm.sendOkS("그럼 부탁하겠네.", 0x04, 1540895);
        qm.forceStartQuest();
        qm.dispose();
    }
}


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
        qm.sendNext("감사의 표시라고 하기에는 약소하지만 #r보답#k으로 선물을 조금 더 주겠네.");
    } else if (status == 1) {
        qm.gainItem(4001868, 3);
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
        qm.getPlayer().setKeyValue(15708, "count", clear + "");
        qm.dispose();
    }
}