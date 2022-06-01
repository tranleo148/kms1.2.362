
var status;
var select = -1;
var itemid  = new Array(1119000, 2435748, 1113070, 1662115, 1662116, 1122148, 1672077, 1032232, 1190301, 1162046, 1182079, 2438145, 2432408, 1142249, 1113070, 1662115, 1662116, 1122148, 1672077, 1032232, 1190301, 1162046, 1182079, 2438145, 2432408, 4031868, 4036531, 2591640, 2630127, 2630291, 2630782, 2437158);

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
        cm.dispose();
	return;
    }
    if (mode == 1) {
        status++;
    }
        if (status == 0) {
		cm.sendYesNoS("안녕하세요! 이달의 아이템 상자를 이용해 주셔서 감사합니다. 예를 누르시면 랜덤으로 보상 1종이 지급됩니다",0x24);
	} else if (status == 1) {
		아이템1 = itemid[Math.floor(Math.random() * itemid.length)];
		//아이템2 = itemid[Math.floor(Math.random() * itemid.length)];
		//아이템3 = itemid[Math.floor(Math.random() * itemid.length)];
		cm.gainItem(아이템1, 1);
		//cm.gainItem(아이템2, 1);
		//cm.gainItem(아이템3, 1);
		cm.gainItem(2630654, -1);
		cm.sendOkS("다음 아이템이 수령되었습니다:\r\n\r\n#i" + 아이템1 + "##z" + 아이템1 + "#\r\n#i" + 아이템2 + "##z" + 아이템2 + "#\r\n#i" + 아이템3 + "##z" + 아이템3 + "#", 0x24);
		cm.dispose();
    	}
}
