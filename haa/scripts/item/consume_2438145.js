
var status;
var select = -1;
var book  = new Array(1672077, 1113070, 1190528, 1142705, 1162084, 1182155);

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
	    var text = "받고 싶은 아이템을 선택해주세요.\r\n\r\n#b";
		for (var i = 0; i < book.length; i++) {
		    text+="#L"+i+"##i"+book[i]+"# #z"+book[i]+"##l\r\n";
		}
		cm.sendSimple(text);
	} else if (status == 1) {
		select = selection;
		cm.sendYesNo("받을 아이템이 #b#z"+book[select]+"##k 이(가) 맞습니까?");
	} else if (status == 2) {
	    if (cm.haveItem(2438145, 1)) {
		if (cm.canHold(1672077)) {
		    cm.sendOk("수령이 완료되었습니다.");
		    cm.gainItem(2438145, -1);
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






