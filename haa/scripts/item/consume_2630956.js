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
		cm.getPlayer().gainDonationPoint(1);
                cm.gainItem(4260003, 5); // 블랙 베리
                cm.gainItem(4260004, 5); // 어메이징 큐브랜덤
                cm.gainItem(4260005, 5); // 강화석랜덤
                cm.gainItem(4310261, 600); // 예쁜돌맹이
                cm.gainItem(4001715, 15); // 예쁜돌맹이
		cm.gainItem(2630956, -1);
		cm.dispose();
	}
}

