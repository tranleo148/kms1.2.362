var status;
var book  = new Array(2591640, 2591659, 2591590, 2591572, 2591427, 2591088, 2591264, 2591296, 2591418, 2591409, 2438396, 2439567, 2436039, 2435369, 2433593, 2431711, 2431964, 2432138, 2431659, 2433592, 2433591);

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
		var text = "다음 물품 중 하나가 랜덤으로 드롭됩니다.\r\n";
		for (var i = 0; i < book.length; i++) {
		    text+="#i"+book[i]+":# #z"+book[i]+":##l\r\n";
		}
		text += "상자를 개봉하시겠습니까?";
		cm.sendYesNo(text);
	} else if (status == 1) {
		item = book[Math.floor(Math.random() * book.length)];
		cm.gainItem(item, 1);
		cm.gainItem(2435899, -1);
		if (item == 2438396) {
		cm.gainItem(2438396, 9);
		}
		if (item == 2439567) {
		cm.gainItem(2439567, 9);
		}
		if (item == 2436039) {
		cm.gainItem(2436039, 9);
		}
		if (item == 2435369) {
		cm.gainItem(2435369, 9);
		}
		if (item == 2433593) {
		cm.gainItem(2433593, 9);
		}
		if (item == 2431711) {
		cm.gainItem(2431711, 9);
		}
		if (item == 2431964) {
		cm.gainItem(2431964, 9);
		}
		if (item == 2432138) {
		cm.gainItem(2432138, 9);
		}
		if (item == 2431659) {
		cm.gainItem(2431659, 9);
		}
		if (item == 2433592) {
		cm.gainItem(2433592, 9);
		}
		if (item == 2433591) {
		cm.gainItem(2433591, 9);
		}
		cm.sendOk("#i" + item + ":##z" + item + ":# 아이템이 수령되었습니다.");
		cm.dispose();
    	}
}
