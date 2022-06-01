


/*

	퓨어 온라인 소스 팩의 스크립트 입니다.

        제작 : 주크블랙

	엔피시아이디 : 
	
	엔피시 이름 :

	엔피시가 있는 맵 : 

	엔피시 설명 : 


*/

var scroll = new Array(1002140,1002240,1002140,1002140,1002140,1002140,1002140,1002140,1002140);
var item = new Array(1002140);
var status = -1;
importPackage(java.lang);
importPackage(Packages.client.items);

function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    
    if (status == 0) {
        cm.sendYesNo("해피타임 상자를 여시겠습니까? 인벤토리 소비탭에 공간이 한칸 이상 필요합니다.");
    } else if (status == 1) {
        var leftslot = cm.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot();
        if (leftslot < 1) {
            cm.sendOk("인벤토리 공간이 최소한 1칸은 필요합니다. 소비 탭의 공간을 1칸이상 비워주신 후 다시 열어주세요.");
            cm.dispose();
            return;
        }
        var item = "1002140";
        for (var i = 0;i <= 2 + Math.floor(Math.random() * 3); i++) {
            var rand = Math.floor(Math.random() * 7);
            var itemid = 0;
            if (rand < 2) {
                itemid = scroll[Math.floor(Math.random() * scroll.length)];
            item += "#b#i"+itemid+"# #t"+itemid+"# 1개#k\r\n";
            cm.gainItem(itemid, 1);
        }
        cm.gainItem(2431110, -1);
        cm.sendOk("봉인된 자물쇠에서 아래의 아이템이 나왔습니다.\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n\r\n"+item);
        
        cm.dispose();
    }
}
}

