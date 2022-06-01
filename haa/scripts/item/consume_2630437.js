var enter = "\r\n";

var itemid = 1712001;
var itemid2 = 2630437;
var size = 1;
var size2 = 6;
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
		var msg = "#fn나눔고딕##fs11##fc0xFF000000#아케인 심볼 1개 교환권입니다. 1개 교환권 1개당 원하는 심볼 1개를 얻으실 수 있습니다.#b"+enter;
		
		for (i = 0; i < size; i++) {
			var id = itemid2;
			if (cm.haveItem(2630437, 1))
				msg += "#b#L" + i + " ##i"+ id +"##z"+ id +"##l #r(#c"+ id +"#개 보유중)#fc0xFF000000#"+enter;
		}

		cm.sendSimple(msg);
	} else if (status == 1) {
		
		first = sel;
		
		var msg = "#fs11##fc0xFF000000#어떤 심볼로 교환할래?"+enter;

		for (i = 0; i < size2; i++) {
			if (first != i) {
				var id = itemid + i;
				msg += "#b#L" + i + "##i"+ id +"##z"+ id +"##k"+ enter;
			}
		}

		cm.sendSimple(msg);
	} else if (status == 2) {
		second = sel;
		var id = itemid + first;
                        var id2 = itemid2;
		var max = cm.itemQuantity(id2);
		cm.sendGetNumber("#fs11##fc0xFF000000#넌 최대 #b"+max+"개#k 교환할 수 있어 \r\n몇 개 교환할거야?", 1, 1, max);
	} else if (status == 3) {
		var id = itemid + first;
                        var id2 = itemid2;
		if (!cm.haveItem(id2, 1)) {
			cm.sendOk("#fs11##fc0xFF000000#아이템이 모자란데?");
			cm.dispose();
			return;
		}
		cm.gainItem(id2, -sel)
		cm.gainItem(itemid + second, sel * 1);
		cm.sendOk("#fs11##fc0xFF000000#교환이 완료되었어!");
		cm.dispose();
	}
}
