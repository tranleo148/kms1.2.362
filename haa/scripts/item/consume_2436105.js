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
		cm.getPlayer().gainDonationPoint(500000);
		cm.gainItem(5068305, 3); //블랙베리
                cm.gainItem(4001715, 30); //메소
                cm.gainItem(4031868, 2); // 최상급 강화석
		cm.gainItem(2436105, -1);
		cm.dispose();
	}
}

