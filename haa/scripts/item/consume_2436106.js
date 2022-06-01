var status;
importPackage(Packages.server);
importPackage(Packages.client.inventory);
importPackage(Packages.server);
importPackage(Packages.server.items);
one = Math.floor(Math.random() * 5) + 1 // 최소 10 최대 35 , 혼테일
function start() {
    status = -1;
    action(1, 1, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }
        if (status == 0) {
		cm.getPlayer().gainDonationPoint(1000000);
		cm.gainItem(5068305, 6); //블랙베리
                cm.gainItem(5062005, 5); // 어메이징 큐브
	        cm.gainItem(4023026, 1); //불꽃
                cm.gainItem(2437122, 1); //최종데미지
                cm.gainItem(4001715, 60); //메소
                cm.gainItem(4031466, 100); // 영혼석
		cm.gainItem(2436106, -1);
		cm.dispose();
	}
}

