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
		cm.getPlayer().gainDonationPoint(8200000);
		cm.gainItem(5068305, 85); //블랙베리
                cm.gainItem(5062005, 105); // 어메이징 큐브
	        cm.gainItem(2437122, 11); //최종데미지
                cm.gainItem(2023287, 11); //크리데미지
                cm.gainItem(3994385, 1); //황금단풍잎
                cm.gainItem(4031466, 1200); //어둠의 영혼석
                cm.gainItem(4001715, 700); //메소
                cm.gainItem(4023026, 2); // 영원히꺼지지 않는 불꽃
		cm.gainItem(2434620, -1);
		cm.dispose();
	}
}

