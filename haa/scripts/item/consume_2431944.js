
var status;
var select = -1;
var book  = new Array(1242042,1332130,1342036,1362019,1472122,1212014,1372084,1382104,1452111,1462099,1522018,1232014,1302152,1312065,1322096,1402095,1412065,1422066,1432086,1442116,1222014,1242014,1482084,1492085,1532018);

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
	    var text = "받고 싶은 여제 무기 아이템을 선택해줘 #r.#l\r\n\r\n#b";
		for (var i = 0; i < book.length; i++) {
		    text+="#L"+i+"##i"+book[i]+"# #z"+book[i]+"##l\r\n";
		}
		cm.sendSimple(text);
	} else if (status == 1) {
		select = selection;
		cm.sendYesNo("받을 여제 무기 아이템 #b#z"+book[select]+"##k 맞아?");
	} else if (status == 2) {
	    if (cm.haveItem(2431944, 1)) {
		if (cm.canHold(1242042)) {
		    cm.sendOk("인벤토리를 확인하세요");
		    cm.gainItem(2431944, -1);
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






