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
		as = Randomizer.rand(400000, 600000);
		cm.gainItem(5068304, 22);
                cm.gainItem(2437121, 3);
	        cm.gainItem(4023026, 1);
                cm.gainItem(4031466, 300);
                cm.gainItem(3994718, 6);
		cm.gainItem(2437127, -1);
		cm.dispose();
	}
}

