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
		cm.gainItem(5068305, 5);
                cm.gainItem(4310261, 1000);
	        cm.gainItem(2437122, 1);
                cm.gainItem(2430028, 1);
                cm.gainItem(5062005, 10);
                 cm.gainItem(3994718, 5);
		cm.gainItem(2435413, -1);
		cm.dispose();
	}
}

