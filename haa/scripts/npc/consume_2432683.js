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
        as = Randomizer.rand(20000000, 20000000);
		cm.gainItem(5068305, 135);
		cm.gainItem(4310261, 28000);
		cm.gainItem(2430028, 24);
		cm.gainItem(5062005, 240);
		cm.gainItem(4033114, 4800);
		cm.gainItem(2023287, 40);
		cm.gainItem(3994718, 120);
		cm.getPlayer().gainDPoint(as);
		cm.gainItem(2439528, -1);
		cm.dispose();
	}
}
