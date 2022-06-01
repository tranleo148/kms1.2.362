/*

*/
var status;

function start() {
    status = -1;
    action(1, 1, 0);
}

function action(mode, type, selection) {
    if (mode < 0) {
        cm.dispose();
    return;
    } else {
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
	    if (!cm.canHold(4032923)) {
                cm.getPlayer().dropMessage(1, "인벤토리 공간이 부족합니다.");
		cm.dispose();
		return;
	    }
            if (!cm.haveItem(4000660, 1) || !cm.haveItem(4000661, 1) || !cm.haveItem(4000662, 1) || !cm.haveItem(4000663, 1)) {
                 cm.getPlayer().dropMessage(5, "다섯 종류의 돌이 모여있지 않습니다.");
                 cm.dispose();
                 return;
            }
            cm.gainItem(4032923, 1);
            cm.gainItem(4000663, -1);
            cm.gainItem(4000660, -1);
            cm.gainItem(4000661, -1);
            cm.gainItem(4000662, -1);
	    cm.getPlayer().dropMessage(5, "다섯 종류의 돌을 모두 합쳐 꿈의 열쇠를 만드는데 성공했습니다.");
	    cm.dispose();
            
        } else { 
            cm.dispose();
        }
    }
}