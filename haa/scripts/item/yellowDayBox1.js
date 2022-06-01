


/*

	퓨어 온라인 소스 팩의 스크립트 입니다.

        제작 : 주크블랙

	엔피시아이디 : 
	
	엔피시 이름 :

	엔피시가 있는 맵 : 

	엔피시 설명 : 


*/

var status = -1;
importPackage(Packages.tools.RandomStream);
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
        cm.sendYesNo("봉인된 상자를 여시겠습니까? 최대 캐시 한도는 50만 캐시 입니다.");
    } else if (status == 1) {
        var leftslot = cm.getPlayer().getNX();
        if (leftslot >= 500000) {
            cm.sendOk("캐시 최대 한도는 50만 캐시 입니다. 현재 캐시 범위를 초과하여 이 상자를 열 수 없습니다.");
            cm.dispose();
            return;
        }
        cm.gainItem(2430026, -1);
        var cash = Randomizer.rand(10000, 100000);
        cm.getPlayer().modifyCSPoints(1, cash, true);
        cm.sendOk("봉인된 자물쇠에서 아래의 아이템이 나왔습니다.\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n\r\n#e#b"+cash+"캐시");
        
        cm.dispose();
    }
}


