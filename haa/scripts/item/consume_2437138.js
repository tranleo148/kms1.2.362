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
		cm.getPlayer().gainDonationPoint(2700000);
                cm.gainItem(5068305, 35); // 블랙 베리
                cm.gainItem(2430026, 25); // 어메이징 큐브랜덤
                cm.gainItem(2430028, 35); // 강화석랜덤
                cm.gainItem(2023287, 5); // 크리데미지
                cm.gainItem(2439530, 4); // 기운석뽑기
                cm.gainItem(3994718, 35); // 예쁜돌맹이
		cm.gainItem(2437138, -1);
		cm.dispose();
	}
}

