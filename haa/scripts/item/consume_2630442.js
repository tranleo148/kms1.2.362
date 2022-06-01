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
		cm.gainItem(4001716, 1); //정착금 10억
		cm.gainItem(2048716, 40); // 강환불
		cm.gainItem(5062010, 150); //블큐
		cm.gainItem(5062500, 100); //에큐
		cm.gainItem(4310237, 200); // 사냥코인
		cm.gainItem(2049360, 2); // 놀장강
		cm.gainItem(2450064, 2); // 겸치2배쿠폰
		cm.gainItem(5060048, 3); // 골드애플
		cm.gainItem(4021031, 500); // 뒤틀린 시간의 정수
		cm.gainItem(4009005, 100); // 반마력석
		cm.gainItem(2630442, -1);
		cm.dispose();
	}
}

