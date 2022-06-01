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
        as = Randomizer.rand(2800000, 4200000);
		cm.gainItem(5068304, 120);
                cm.gainItem(2430026, 6);
                cm.gainItem(4031868, 6);
                cm.gainItem(4033114, 2000);
	        cm.gainItem(4310261, 4000);
                cm.gainItem(2438145, 3);
                cm.gainItem(3994718, 30);
		cm.gainItem(2437589, -1);
		cm.dispose();
	}
}
