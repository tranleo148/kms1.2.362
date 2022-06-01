


/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    ★ 에 의해 만들어 졌습니다.

    엔피시아이디 : 9000210

    엔피시 이름 : 병아리

    엔피시가 있는 맵 : 헤네시스 : 헤네시스 (100000000)

    엔피시 설명 : MISSINGNO


*/

importPackage(java.sql);
importPackage(java.lang);
importPackage(Packages.database);
importPackage(Packages.handling.world);
importPackage(Packages.constants);
importPackage(java.util);
importPackage(java.io);
importPackage(Packages.client.inventory);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools.packet);
검정 = "#fc0xFF191919#"
var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        leftslot = cm.getPlayer().getInventory(MapleInventoryType.CASH).getNumFreeSlot();
        if (leftslot < 1) {
            cm.sendOk("#fs11##r캐시창 1 칸 이상을 확보하게.");
            cm.dispose();
            return;
        }
        말 = "#fs11#" + 검정 + "받고싶은 펫을 하나 선택해보게!\r\n";
        말 += "#L0##i5002239# #b#z5002239##k 받기\r\n";
        cm.sendSimpleS(말, 0x04, 2004);
    } else if (status == 1) {
        if (selection == 0) {
            Packages.server.MapleInventoryManipulator.addId_Item(cm.getClient(), 5002239, 1, "", Packages.client.inventory.MaplePet.createPet(5002239, -1), 30, "", false);
            cm.sendOk("#fs11#" + 검정 + " #i5002239# #b#z5002239#" + 검정 + "을 지급했다네! 캐시창을 확인해보게.", 2004);
            cm.gainItem(2633202, -1);
            cm.dispose();
        }
    }
}
