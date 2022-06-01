importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server);
importPackage(Packages.client.inventory);

var status = -1;

function start(mode, type, selection) {
    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        status++;
    }


    if (status == 0) {
        qm.sendNext("말랑이, 자네가 이곳저곳의 의뢰를 도와줄 때마다\r\n우리 로봇들이 더 오래 정찰할 수 있다네.\r\n그렇게 되면 #b희귀한 물건#k들도 모아올 수 있지.");
    } else if (status == 1) {
        qm.sendNextPrev("그래서 우리가 얘기를 했는데,\r\n자네가 #b매주 의뢰를 4번 도와줄 때마다#k\r\n#i4001842# #b#z4001842##k를 보답으로 주기로 했네.");
    } else if (status == 2) {
        qm.sendNextPrev("매주 #b의뢰를 4번 완료#k하면 나에게 알려주게, 핫핫핫.");
    } else if (status == 3) {
        qm.forceStartQuest();
        qm.dispose();
    }
}

status = -1;

function end(mode, type, selection) {
    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        leftslot = qm.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot();
        if (leftslot < 2) {
            qm.sendOk("이런, 말랑이! 인벤토리 공간을 비우고 다시 말을 거는게 좋겠다네.");
            qm.dispose();
            return;
        }
        qm.sendNext("말랑이, 자네가 꾸준히 도와줘서 정찰이 훨씬 편해졌다네.\r\n여기, 이건 그에 대한 보상일세.\r\n#i4001842# #b#t4001842##k\r\n\r\n앞으로도 매주 의뢰를 4번 완료하면 나에게 알려주게. 핫핫핫.");
        qm.gainItem(4001842, 14);
        qm.forceCompleteQuest();
        qm.getPlayer().setKeyValue(39100, "FC", "0");
        qm.dispose();
    }

}