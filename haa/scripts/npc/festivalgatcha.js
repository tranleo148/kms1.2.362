


/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    블랙 에 의해 만들어 졌습니다.

    엔피시아이디 : 9110007

    엔피시 이름 : 로보

    엔피시가 있는 맵 : 몬스터파크 : 몬스터파크 (951000000)

    엔피시 설명 : 라면 요리사


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
var status = -1;
var item = [[1113004, 1], [1113005, 1], [1022699, 1], [1032799, 1], [1012949, 1], [1022700, 1], [1032800, 1], [1012950, 1], [2633610, 50], [4310012, 300], [5062010, 100], [5062500, 50], [5062503, 30], [2049751, 1], [2049704, 1], [5068300, 1], [5069100, 1], [2049370, 1], [2049372, 1], [4001832, 500], [5064000, 2], [5064100, 2], [5064300, 2], [5064400, 2], [2048716, 5], [2048717, 5], [2048753, 3], [2049153, 1], [5069000, 1], [5069001, 1], [2435719, 5], [2048836, 1], [2048837, 1], [2633610, 50], [4310012, 300], [5062010, 100], [5062500, 80], [5062503, 30], [2049751, 1], [2049704, 1], [5068300, 1], [5069100, 1], [2049370, 1], [2049372, 1], [4001832, 300], [5064000, 2], [5064100, 2], [5064300, 2], [5064400, 2], [2048716, 5], [2048717, 5], [2048753, 3], [2049153, 1], [5069000, 1], [5069001, 1], [2435719, 5], [2048836, 1], [2048837, 1], [5064400, 2], [2048716, 5], [2048717, 5], [2048753, 3], [2049153, 1], [5069000, 1], [5069001, 1], [2435719, 5], [2048836, 1], [2048837, 1], [2435719, 5], [2048836, 1], [2048837, 1], [5064400, 2], [2048716, 5], [2048717, 5], [2048753, 3], [2049153, 1], [5069000, 1], [5069001, 1], [2435719, 5], [2048836, 1], [2048837, 1], [2435719, 5], [2048836, 1], [2048837, 1], [5064400, 2], [2048716, 5], [2048717, 5], [2048753, 3], [2049153, 1], [5069000, 1], [5069001, 1], [2435719, 5], [2048836, 1], [2048837, 1]]

function start() {
    status = -1;
    action(1, 0, 0);
}
var a = 0;
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
        말 = "#fs11#타르르르륵··· #d오색빛깔 부우 룰렛#k이 쌓여있다.\r\n"
        말 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
        말 += "#L0##b룰렛 아이템을 뽑고 싶어요.\r\n"
        말 += "#L1#룰렛에 등장하는 아이템을 확인하고 싶어요.\r\n"
        말 += "#L99##r대화를 그만두겠습니다."
        cm.sendSimpleS(말, 0x04, 9401420);
    } else if (status == 1) {
        if (selection == 0) {
            if (!cm.haveItem(4310013, 1)) {
                cm.sendOkS("#fs11#룰렛을 이용하기 위한 티켓이 부족하다..", 0x04, 9401420);
                cm.dispose();
                return;
            }
            leftslot = cm.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot();
            leftslot1 = cm.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot();
            leftslot2 = cm.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot();
            leftslot3 = cm.getPlayer().getInventory(MapleInventoryType.SETUP).getNumFreeSlot();
            leftslot4 = cm.getPlayer().getInventory(MapleInventoryType.CASH).getNumFreeSlot();
            leftslot5 = cm.getPlayer().getInventory(MapleInventoryType.CODY).getNumFreeSlot();
            if (leftslot < 2 && leftslot1 < 2 && leftslot2 < 2 && leftslot3 < 2 && leftslot4 < 2 && leftslot5 < 2) {
               cm.sendOkS("#fs11#모든 인벤토리창 2칸 이상을 비워야 한다..", 0x04, 9401420);
               cm.dispose();
               return;
            }
            //var count22 = 0, count1 = 0;
            //for (var a = 0; a < ; a++) {
                var rand = Randomizer.rand(0, item.length - 1);
                var itemid = item[rand][0];
                var count = item[rand][1];
                /*if (itemid == 1113004) {
                    count22++;
                } else if (itemid == 1113005) {
                    count1++;
                }
            }*/
            //cm.sendOk(count22 + " / " + count1);
            cm.gainItem(itemid, count);
            cm.sendOkS("#fs11##i"+itemid+"# #z"+itemid+"# "+count+"개", 0x04, 9401420);
            cm.gainItem(4310013, -1);
            cm.dispose();
        } else if (selection == 1) {
            말 = "#fs11#룰렛 아이템 리스트 입니다.\r\n\r\n"
            for (var a = 0; a < item.length; a++) {
                말 += "#i"+item[a][0]+"# #b#z"+item[a][0]+"##k 수량 : #e"+item[a][1]+"#n개\r\n"
                if (a == 32) break;
            }
            cm.sendOkS(말, 0x04, 9401420);
            cm.dispose();
        } else if (selection == 99) {
            cm.dispose();
        }
    }
}