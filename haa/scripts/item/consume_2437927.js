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
        as = Randomizer.rand(8000000, 8000000);
		cm.gainItem(2430027, 3);
                cm.gainItem(2430026, 20);
                cm.gainItem(4001168, 1);
                cm.gainItem(4033114, 6000);
                cm.gainItem(4310261, 5000);
	        cm.gainItem(3994718, 53);
		cm.gainItem(2437927, -1);
		cm.dispose();
	}
}
