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
		cm.getPlayer().gainDonationPoint(100000);
                cm.gainItem(5068305, 2); // 블랙 베리
                cm.gainItem(5062005, 5); // 어메이징큐브
                cm.gainItem(4031868, 3); // 최상급강화석
                cm.gainItem(3994718, 1); // 예쁜돌맹이
                cm.gainItem(4001715, 50); // 메소
		cm.gainItem(2630694, -1);
		cm.dispose();
	}
}

