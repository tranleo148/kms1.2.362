
var status;
var select = -1;
var book  = new Array(2591031,2591032,2591033,2591034,2591035,2591036,2591037,2591110,2591111,2591112,2591113,2591114,2591115,2591116);

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
	    if(!cm.haveItem(2431658, 10)) { 
		cm.sendOk("#e소울조각#n이 10개가 없는것같습니다. #e소울조각#n은 #b카인스타일#k에서 후원&홍보를하면 모을 수 있다고 합니다.");
		cm.dispose();
		} else {
	    var text = "#b#h ##k님 벌써 #e소울조각#n을 10개나 모으셨네요!\r\n축하드립니다, 받고 싶은 소울을 선택해주세요.\r\n\r\n#b";
		for (var i = 0; i < book.length; i++) {
		    text+="#L"+i+"##i"+book[i]+"# #z"+book[i]+"##l\r\n";
		}
				cm.sendSimple(text);
		}
	} else if (status == 1) {
		select = selection;
		cm.sendYesNo("받을 소울은 #b#t"+book[select]+"##k 맞아?");
	} else if (status == 2) {
		cm.gainItem(book[select], 1);
		cm.gainItem(2431658, -10);
		cm.dispose();
			}
    		}
	}
