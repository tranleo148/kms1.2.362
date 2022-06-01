importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server);
importPackage(Packages.constants);
importPackage(Packages.client.inventory);

var status = -1;

function start(mode, type, selection) {

    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        qm.sendNext("혹시라도 마음이 변하면 다시 한 번 말씀해주세요.");
        qm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        qm.sendYesNo("이번 주 저희 조사단의 연구를 도와주시겠어요?\r\n(수락 후 #e#r일요일 자정#k#n까지 #e#b[일일 퀘스트] 소멸의 여로 조사 퀘스트#k#n를 #e#b2회 이상#k#n 완료하면 특별한 보상을 받을 수 있습니다.)");
    } else if (status == 1) {
        qm.sendNext("#b#h0##k님의 활약을 기대하겠습니다.\r\n다시 한 번 말씀드리지만 #e#r일요일 자정#k#n이 지나면 퀘스트 기록이 초기화되니 주의해주세요.")
        qm.forceStartQuest();
        qm.dispose();
    }
}

var status = -1;
function end(mode, type, selection) {

    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        qm.sendNext("혹시라도 마음이 변하면 다시 한 번 말씀해주세요.");
        qm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        leftslot = qm.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot();
        leftslot1 = qm.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot();
        if (leftslot < 3 && leftslot1 < 3) {
            qm.sendOk("인벤토리를 비우고 다시 말을 걸어주세요.");
            qm.dispose();
            return;
        }
        qm.sendNext("이번 주도 #b#h0##k님의 활약이 저희 조사단의 연구에 큰 도움이 되었습니다.\r\n저희가 준비한 특별 보상을 받아주세요.\r\n#i2435902# #b#z2435902# 10개#k\r\n#i4310005# #b#z4310005# 10개#k\r\n\r\n#r(주의: 일요일 자정까지 보상을 받지 않으면 퀘스트 기록이 초기화됩니다.)\r\n")
    } else if (status == 1) {
        qm.sendOk("보상을 지급해드렸습니다.\r\n앞으로도 잘 부탁드립니다.");
        qm.gainItem(2435902, 10);
        qm.gainItem(1712001, 10);
qm.getPlayer().setKeyValue(100592, "point", "" + (qm.getPlayer().getKeyValue(100592, "point") + 1));
        qm.forceCompleteQuest();
        qm.dispose();
    }
}