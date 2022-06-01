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
		cm.getPlayer().gainDonationPoint(2500000);
                cm.gainItem(5068305, 27); // 블랙 베리
                cm.gainItem(5062005, 30); // 어메이징 큐브
                cm.gainItem(4031466, 400); // 어둠의 영혼석
                cm.gainItem(4310261, 2100); // 사냥코인
                cm.gainItem(4001715, 170); // 1억코인
                cm.gainItem(2439653, 33); // 영환불 10개
                cm.gainItem(2437122, 7); // 최종 데미지
                cm.gainItem(2023287, 7); // 크리데미지
                cm.gainItem(3994545, 7); // 도시락
		cm.gainItem(2437134, -1);
		cm.dispose();
	}
}

