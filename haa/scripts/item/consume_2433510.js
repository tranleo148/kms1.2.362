var status;
var select = -1;
var book  = new Array(1352247,1352237,1352257,1352507,1352946,1352217,1352907,1352010,1352708,1352958,1352207,1352287,1352929,1352607,1352976,1352407,1352968,1352277,1352936,1352227,1352297,1352917,1352267,1353007,1098007,1099013,1352110,1353106,1353204,1353404);

function start() {    status = -1;
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
		var text = "안녕하세요! 블랙 보조 무기 상자를 이용해 주셔서 감사합니다. 원하시는 무기를 선택해주세요.\r\n";
		for (var i = 0; i < book.length; i++) {
		    text+="#L"+i+"##i"+book[i]+"# #z"+book[i]+"##l\r\n";
		}
		cm.sendIllustSimple(text, 2, false);
	} else if (status == 1) {
		select = selection;
		cm.sendYesNoS("선택한 보조 무기는 #b#t"+book[select]+"##k 다음과 같습니다. 정말로 수령하시겠습니까?",0x24);
	} else if (status == 2) {
		cm.gainItem(book[select], 1);
		cm.gainItem(2433510, -1);
		cm.cm.sendOkS("수령되었습니다.", 3, true);
		cm.dispose();
    	}
}
