
var status;
var select = -1;
var itemid  = new Array(1012632, 1022278, 1132308, 1162080, 1162081, 1162082, 1162083, 1122430, 1182285, 1113306, 1032316, 1190555, 1190556, 1190557, 1190558, 1190559);

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
		cm.sendYesNoS("안녕하세요! 랜덤 칠흑보스 장신구 상자입니다. 예를 누르시면 랜덤한 칠흑보스 아이템 1종이 지급됩니다.",0x24);
	} else if (status == 1) {
		아이템1 = itemid[Math.floor(Math.random() * itemid.length)];
		//아이템2 = itemid[Math.floor(Math.random() * itemid.length)];
		//아이템3 = itemid[Math.floor(Math.random() * itemid.length)];
		cm.gainItem(아이템1, 1);
		//cm.gainItem(아이템2, 1);
		//cm.gainItem(아이템3, 1);
		cm.gainItem(2432408, -1);
		cm.cm.sendOkS("다음 아이템이 수령되었습니다:\r\n\r\n#i" + 아이템1 + "##z" + 아이템1 + "#\r\n#i" + 아이템2 + "##z" + 아이템2 + "#\r\n#i" + 아이템3 + "##z" + 아이템3 + "#",0x24);
		cm.dispose();
    	}
}
