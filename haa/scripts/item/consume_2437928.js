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
         as = Randomizer.rand(14400000, 14400000);
		cm.gainItem(5068304, 480);
	        cm.gainItem(3994718, 110);
	        cm.gainItem(2430026, 48);
		cm.gainItem(2437121, 60);
                cm.gainItem(4001716, 80);
                cm.gainItem(2437122, 8);
                cm.gainItem(2023072, 20);
                cm.gainItem(4310261, 20000);
                cm.gainItem(4001168, 4);
		cm.gainItem(2437928, -1);
		cm.dispose();
	}
}
