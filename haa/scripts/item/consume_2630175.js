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
		cm.gainItem(5068305, 24); // 블랙베리 
                cm.gainItem(5062005, 10);
	     cm.gainItem(4033114, 500);
                cm.gainItem(3994718, 15);
		cm.gainItem(2630175, -1);
		cm.dispose();
	}
}

