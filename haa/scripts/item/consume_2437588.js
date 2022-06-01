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
        as = Randomizer.rand(1400000, 2800000);
		cm.gainItem(2430027, 1);
                cm.gainItem(2430026, 8);
                cm.gainItem(4033114, 2700);
                cm.gainItem(4310261, 2500);
                cm.gainItem(3994718, 24);
		cm.gainItem(2437588, -1);
		cm.dispose();
	}
}
