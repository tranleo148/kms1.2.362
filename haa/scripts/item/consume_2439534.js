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
        as = Randomizer.rand(1500000, 1500000);
		cm.gainItem(2430026, 5);
                cm.gainItem(4033114, 1000);
                cm.gainItem(4031868, 2);
                cm.gainItem(4310261, 2000);
	        cm.gainItem(3994718, 10);
                cm.gainItem(2437122, 1);
                cm.gainItem(4001715, 100);
                cm.gainItem(2438145, 1);
		cm.gainItem(2439534, -1);
		cm.dispose();
	}
}
