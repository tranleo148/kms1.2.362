
var status;
var select = -1;
var book  = new Array(1003175,1102278,1082298,1052317,1072488,1003173,1102276,1082296,1052315,1072486,1003174,1102277,1082297,1052316,1072487,1003172,1102275,1082295,1052314,1072485,1003176,1102279,1082299,1052318,1072489);

function start() {    status = -1;
    action(1, 1, 0);
}

function action(mode, type, selection) {
    if (mode <= 0) {
        cm.dispose();
    	return;
    } else {
        if (mode == 1)
            status++;
        if (status == 0) {
	    var text = "받고 싶은 여제 방어구 아이템을 선택해줘 #r.#l\r\n\r\n#b";
		for (var i = 0; i < book.length; i++) {
		    text+="#L"+i+"##i"+book[i]+"# #z"+book[i]+"##l\r\n";
		}
		cm.sendSimple(text);
	} else if (status == 1) {
		select = selection;
		cm.sendYesNo("받을 여제 방어구 아이템 #b#z"+book[select]+"##k 맞아?");
	} else if (status == 2) {
	    if (cm.haveItem(2431945, 1)) {
		if (cm.canHold(1003175)) {
		    cm.sendOk("인벤토리를 확인하세요");
		    cm.gainItem(2431945, -1);
		    cm.gainItem(book[select], 1);
		    cm.dispose();
		} else {
		    cm.sendOk("장비칸에 빈 공간이 없습니다.");
		    cm.dispose();
		}
            } else {
		cm.sendOk("부족합니다.");
		cm.dispose();

}
	}
    }
}






