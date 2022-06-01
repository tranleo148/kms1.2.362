
var status;
var select = -1;
var book  = new Array(1102481,1102482,1102483,1102484,1102485,1072743,1072744,1072745,1072746,1072747,1132174,1132175,1132176,1132177,1132178,1082543,1082544,1082545,1082546,1082547);

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
	    var text = "받고 싶은 타일런트 방어구를 선택해줘#l\r\n\r\n#b";
		for (var i = 0; i < book.length; i++) {
		    text+="#L"+i+"##i"+book[i]+"# #z"+book[i]+"##l\r\n";
		}
		cm.sendSimple(text);
	} else if (status == 1) {
		select = selection;
		cm.sendYesNo("받을 타일런트 방어구는 #b#z"+book[select]+"##k 맞아?");
	} else if (status == 2) {
	    if (cm.haveItem(2432069, 1)) {
		if (cm.canHold(1102481)) {
		    cm.sendOk("인벤토리를 확인하세요");
		    cm.gainItem(2432069, -1);
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






