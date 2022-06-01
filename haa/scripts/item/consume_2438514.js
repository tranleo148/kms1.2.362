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
		cm.getPlayer().gainDonationPoint(1);
                cm.gainItem(4033114, 3000);
                cm.gainItem(2048753, 1000);
                 cm.gainItem(4310261, 5000);
                 cm.gainItem(4023026, 3);
                 cm.gainItem(4001715, 300);
		cm.gainItem(2438514, -1);
		cm.dispose();
	}
}

