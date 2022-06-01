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
		cm.gainItem(2437121, 20);
                cm.gainItem(5068305, 60);
	        cm.gainItem(2430027, 4);
                cm.gainItem(2430026, 30);
                cm.gainItem(2023287, 10);
                cm.gainItem(4310261, 700);
                cm.gainItem(3994718, 50);
                cm.gainItem(3994385, 4);
		cm.gainItem(2431651, -1);
		cm.dispose();
	}
}

