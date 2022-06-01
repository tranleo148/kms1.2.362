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
		cm.getPlayer().gainDonationPoint(2600000);
                cm.gainItem(5068305, 35); // 블랙 베리
                cm.gainItem(5062005, 70); // 어메이징 큐브
                cm.gainItem(4031466, 2000); // 어둠의영혼석
                cm.gainItem(3994385, 40); // 황금단풍잎
                cm.gainItem(4033114, 2500); // 악의정수
                cm.gainItem(3994718, 15); // 예쁜돌맹이
		cm.gainItem(2437850, -1);
		cm.dispose();
	}
}

