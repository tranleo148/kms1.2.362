
var status;
var select = -1;
var book  = new Array(1482217,1522139,1232110,1452253,1322251,1422185,1212116,1302334,1222110,1492232,1382260,1472262,1432215,1312200,1372223,1242117,1532145,1332275,1362136,1462240,1402252,1412178,1442269,1342102,1262027,1582021);

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
		var text = "안녕하세요! 블랙 무기 상자를 이용해 주셔서 감사합니다. 원하시는 무기를 선택해주세요.\r\n";
		for (var i = 0; i < book.length; i++) {
		    text+="#L"+i+"##i"+book[i]+"# #z"+book[i]+"##l\r\n";
		}
		cm.sendIllustSimple(text,0x24);
	} else if (status == 1) {
		select = selection;
		cm.sendYesNoS("선택한 무기는 #b#t"+book[select]+"##k 다음과 같습니다. 정말로 수령하시겠습니까?", 2, false);
	} else if (status == 2) {
		cm.gainItem(book[select], 1);
		cm.gainItem(2433509, -1);
		cm.cm.sendOkS("수령되었습니다.",0x24);
		cm.dispose();
    	}
}
