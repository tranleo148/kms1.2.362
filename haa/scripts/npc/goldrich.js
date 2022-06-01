var status = -1;
var item;
importPackage(Packages.server);
importPackage(Packages.constants);

function start() {
    status = -1;
    action (1, 0, 0);
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

/*    if (status == 0) {
	var txt = "안녕하세요 #b#h 0##k님! 화폐 시스템을 이용하시겠어요? 원하시는 물품을 골라주세요.\r\n";
	for (i = 0; i <= 4; i++) {
		txt += "#L" + i + "# #i" + (4001208 + i) + ":# #z" + (4001208 + i) + "#\r\n";
	}
	cm.sendSimple(txt);
    } else */if (status == 0) {
	cm.sendGetNumber("안녕하세요 #b#h 0##k님! 화폐를 교환하시겠어요? \r\n화폐는 환전상점에서 구매 가능합니다.", 1, 1, 99);
    } else if (status == 1) {
	cm.dispose();
	if (cm.haveItem(2434280, selection)) {
		cm.gainItem(2434280, -selection);
		cm.gainMeso(1000000000 * selection);
		cm.sendOk("교환 되었습니다.");
	} else {
		cm.sendOk("아이템이 부족합니다.");
	}
    }
}
