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
		as = Randomizer.rand(600000, 1000000);
		cm.gainItem(5060048, 20);
                cm.gainItem(2630654, 1);
	        cm.gainItem(4001715, 30);
	        cm.gainItem(4031868, 1);
                cm.gainItem(5062005, 10);
		cm.gainItem(4310261, 1000);
		cm.gainItem(2630690, -1);
		cm.dispose();
	}
}

