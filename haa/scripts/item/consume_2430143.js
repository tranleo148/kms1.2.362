/*
제작자 : 퐁퐁(pongpong___@naver.com / unfix_@naver.com)
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
	    if (!cm.canHold(2430143)) {
                cm.sendOk("인벤토리 공간이 부족합니다.");
		cm.dispose();
		return;
	    }
            cm.gainItem(2430143, -1);
		cm.getPlayer().addFame(10);
                cm.fakeRelog();
                cm.updateChar();
	    cm.getPlayer().dropMessage(1, "누군가가 "+cm.getPlayer().getName()+"님을위해 쓴 러브레터 덕분에 인기도가 상승하였습니다.");
	    cm.dispose();
            
        } else { 
            cm.dispose();
        }
    }
}