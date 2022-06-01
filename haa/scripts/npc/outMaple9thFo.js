var status = -1;
var book = new Array("Clear_NCygnus", "Clear_HMangus", "Clear_CVanVan", "Clear_CPierre", "Clear_CBloodQueen", "Clear_CVellum");


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

    if (status == 0) {
	text = "#e#dPixi 유저 #h 0##k#n님 어서오세요~ 언제나 환영합니다! 교환하실 아이템을 골라주세요.#b\r\n\r\n";
	for (var i = 0; i < 6; i++) {
	    text+="#L" + i + "# #i" + (2431968 + i) + "# #z" + (2431968 + i) + ":#\r\n(환생포인트 100,000) #l\r\n";
	}
	text += "#L6##i5680151##z5680151:# (환생포인트 1,000)#l\r\n";
	text += "#L7##i5680148##z5680148:# (환생포인트 5,000)#l\r\n";
	text += "#L8##i5680149##z5680149:# (환생포인트 10,000)#l\r\n";
	text += "#L9##i5680150##z5680150:# (환생포인트 30,000)#l\r\n";
	text += "#L10##i5680193##z5680193:# (환생포인트 50,000)#l\r\n";
	text += "#L11##i5680194##z5680194:# (환생포인트 100,000)#l\r\n";
	cm.sendSimple(text);
    } else if (status == 1) {
	cm.dispose();
	if (selection < 6) {
		if (cm.getPlayer().getKeyValue(1, "reborns") < 100000) {
			cm.sendOk("환생 포인트가 부족합니다.");
			return;
		} else {
			cm.getPlayer().setKeyValue(1, "reborns", cm.getPlayer().getKeyValue(1, "reborns") - 100000);
			cm.getPlayer().setKeyValue(3, book[selection], 0);
			cm.sendOk("해당 보스의 클리어횟수가 초기화되었습니다.");
		}
	} else if (selection == 6) {
		if (cm.getPlayer().getKeyValue(1, "reborns") < 1000) {
			cm.sendOk("환생 포인트가 부족합니다.");
			return;
		} else {
			cm.getPlayer().setKeyValue(1, "reborns", cm.getPlayer().getKeyValue(1, "reborns") - 1000);
			cm.gainItem(5680151, 1);
			cm.sendOk("아이템을 구매하였습니다.");
		}
	} else if (selection == 7) {
		if (cm.getPlayer().getKeyValue(1, "reborns") < 5000) {
			cm.sendOk("환생 포인트가 부족합니다.");
			return;
		} else {
			cm.getPlayer().setKeyValue(1, "reborns", cm.getPlayer().getKeyValue(1, "reborns") - 5000);
			cm.gainItem(5680148, 1);
			cm.sendOk("아이템을 구매하였습니다.");
		}
	} else if (selection == 8) {
		if (cm.getPlayer().getKeyValue(1, "reborns") < 10000) {
			cm.sendOk("환생 포인트가 부족합니다.");
			return;
		} else {
			cm.getPlayer().setKeyValue(1, "reborns", cm.getPlayer().getKeyValue(1, "reborns") - 10000);
			cm.gainItem(5680149, 1);
			cm.sendOk("아이템을 구매하였습니다.");
		}
	} else if (selection == 9) {
		if (cm.getPlayer().getKeyValue(1, "reborns") < 3000) {
			cm.sendOk("환생 포인트가 부족합니다.");
			return;
		} else {
			cm.getPlayer().setKeyValue(1, "reborns", cm.getPlayer().getKeyValue(1, "reborns") - 30000);
			cm.gainItem(5680150, 1);
			cm.sendOk("아이템을 구매하였습니다.");
		}
	} else if (selection == 10) {
		if (cm.getPlayer().getKeyValue(1, "reborns") < 50000) {
			cm.sendOk("환생 포인트가 부족합니다.");
			return;
		} else {
			cm.getPlayer().setKeyValue(1, "reborns", cm.getPlayer().getKeyValue(1, "reborns") - 50000);
			cm.gainItem(5680193, 1);
			cm.sendOk("아이템을 구매하였습니다.");
		}
	} else if (selection == 11) {
		if (cm.getPlayer().getKeyValue(1, "reborns") < 100000) {
			cm.sendOk("환생 포인트가 부족합니다.");
			return;
		} else {
			cm.getPlayer().setKeyValue(1, "reborns", cm.getPlayer().getKeyValue(1, "reborns") - 100000);
			cm.gainItem(5680194, 1);
			cm.sendOk("아이템을 구매하였습니다.");
		}
	}
    }
}

