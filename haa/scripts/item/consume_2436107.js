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
		cm.getPlayer().gainDonationPoint(2000000);
		cm.gainItem(5068305, 13); //블랙베리
                cm.gainItem(5062005, 12); // 어메이징 큐브
	        cm.gainItem(4023026, 2); //불꽃
                cm.gainItem(2437122, 2); //최종데미지
                cm.gainItem(4031466, 300); // 영혼석
		cm.gainItem(2436107, -1);
		cm.dispose();
	}
}

