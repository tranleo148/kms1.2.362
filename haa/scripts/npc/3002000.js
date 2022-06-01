var enter = "\r\n";

var itemid = 4001326;
var size = 7;

var first = 0;
var second = 0;

function start() {
	status = -1;
	action(1, 0, 0);
}
function action(mode, type, sel) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
    	}
	if (status == 0) {
		var msg = "#fs11##fc0xFF000000#아래 목록은 너가 가지고 있는 크레파스 목록이야! 뭐로 바꿔먹을래?#b"+enter;
		
		for (i = 0; i < size; i++) {
			var id = itemid + i;
			if (cm.haveItem(id, 1))
				msg += "#b#L" + i + " ##i"+ id +"##z"+ id +"##l #r(#c"+ id +"#개 보유중)#fc0xFF000000#"+enter;
		}

		cm.sendSimple(msg);
	} else if (status == 1) {
		
		first = sel;
		
		var msg = "#fs11##fc0xFF000000#어떤 크레파스로 교환할래?"+enter;

		for (i = 0; i < size; i++) {
			if (first != i) {
				var id = itemid + i;
				msg += "#b#L" + i + "##i"+ id +"##z"+ id +"##k"+ enter;
			}
		}

		cm.sendSimple(msg);
	} else if (status == 2) {
		second = sel;
		var id = itemid + first;
		var max = cm.itemQuantity(id);
		cm.sendGetNumber("#fs11##fc0xFF000000#넌 최대 #b"+max+"개#k 교환할 수 있어 \r\n몇 개 교환할거야?", 1, 1, max);
	} else if (status == 3) {
		var id = itemid + first;
		if (!cm.haveItem(id, 1)) {
			cm.sendOk("#fs11##fc0xFF000000#아이템이 모자란데?");
			cm.dispose();
			return;
		}
		cm.gainItem(id, -sel)
		cm.gainItem(itemid + second, sel);
		cm.sendOk("#fs11##fc0xFF000000#교환이 완료되었어!");
		cm.dispose();
	}
}
