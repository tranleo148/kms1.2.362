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
		cm.getPlayer().gainDonationPoint(2000000);
                cm.gainItem(4033114, 1000); // 응고된 악의정수
                cm.gainItem(4001715, 2000); // 1억
                cm.gainItem(2023287, 10); // 크리데미지
                cm.gainItem(4023026, 3); // 영꺼불
                //cm.gainItem(4023026, 1); // 25성 초월
                cm.gainItem(3994385, 20); // 황금단풍잎
                //cm.gainItem(4001715, 100); // 메소
		cm.gainItem(2435939, -1);
		cm.dispose();
	}
}

