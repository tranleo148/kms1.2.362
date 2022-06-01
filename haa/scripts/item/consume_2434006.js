
var status;
var select = -1;
var itemid  = new Array(1012478,1022231,1022232,1032136,1032241,1113149,1113282,1132296,1122000,1122076,1122254,1122150,1132272,1152170,1162025,1162009,1182087);

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
		cm.sendYesNoS("안녕하세요! 보스 장신구 상자를 이용해 주셔서 감사합니다. 예를 누르시면 랜덤한 보스 장신구 3종이 지급됩니다.",0x24);
	} else if (status == 1) {
		아이템1 = itemid[Math.floor(Math.random() * itemid.length)];
		아이템2 = itemid[Math.floor(Math.random() * itemid.length)];
		아이템3 = itemid[Math.floor(Math.random() * itemid.length)];
		cm.gainItem(아이템1, 1);
		cm.gainItem(아이템2, 1);
		cm.gainItem(아이템3, 1);
		cm.gainItem(2434006, -1);
		cm.sendOkS("다음 아이템이 수령되었습니다:\r\n\r\n#i" + 아이템1 + "##z" + 아이템1 + "#\r\n#i" + 아이템2 + "##z" + 아이템2 + "#\r\n#i" + 아이템3 + "##z" + 아이템3 + "#",0x24);
		cm.dispose();
    	}
}
