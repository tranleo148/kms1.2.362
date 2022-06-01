var enter = "\r\n";
var seld = -1;

var need = [
	{'itemid' : 4001326, 'qty' : 1},
	{'itemid' : 4001327, 'qty' : 1},
	{'itemid' : 4001328, 'qty' : 1},
	{'itemid' : 4001329, 'qty' : 1},
	{'itemid' : 4001330, 'qty' : 1},
	{'itemid' : 4001331, 'qty' : 1},
	{'itemid' : 4001332, 'qty' : 1}
]
var tocoin = 2433979, toqty = 1;

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
		var msg = "#fs11##fc0xFF000000#으아앙~ 무지개 그림을 그려야하는데 크레파스가 없어서 못그리고있어.. 훌쩍\r\n"+enter;

		for (i = 0; i < need.length; i++) {
			if (i != need.length - 1) msg += "#b#i"+need[i]['itemid']+"##z"+need[i]['itemid']+"# "+need[i]['qty']+"개 / #r#c "+need[i]['itemid']+"#개 보유중#b"+enter;
			else msg += "#i"+need[i]['itemid']+"##z"+need[i]['itemid']+"# "+need[i]['qty']+"개 / #r#c "+need[i]['itemid']+"#개 보유 중#fc0xFF000000#\r\n\r\n를 구해다줄 수 있어? 훌쩍..."+enter;
		}

		msg += "#b보상 : #i"+tocoin+"# #z"+tocoin+"#\r\n";
		if (haveNeed(1))
			cm.sendNext(msg);
		else {
			msg +="\r\n#fc0xFF000000#아직 못가져 왔어? 빨리 가져다 줘 후에엥~";
			cm.sendOk(msg);
			cm.dispose();
		}
	} else if (status == 1) {
		temp = [];
		for (i = 0; i < need.length; i++) {
			temp.push(Math.floor(cm.itemQuantity(need[i]['itemid']) / need[i]['qty']));
		}
		temp.sort();
		max = temp[0];
		cm.sendGetNumber("#fs11##fc0xFF000000#최대 #b"+max+"개#fc0xFF000000# \r\n몇 번 바꿔줄까?", 1, 1, max);
	} else if (status == 2) {
		if (!haveNeed(sel)) {
			cm.sendOk("#fc0xFF000000#뭐야... 크레파스가 없잖아!!");
			cm.dispose();
			return;
		}
		for (i = 0; i < need.length; i++) {
			cm.gainItem(need[i]['itemid'], -(need[i]['qty'] * sel));
		}
		cm.gainItem(tocoin, (toqty * sel));
		cm.sendOk("#fs11##fc0xFF000000#고마워! 덕분에 그림을 그릴 수 있겠어!");
		cm.dispose();
	}
}

function haveNeed(a) {
	var ret = true;
	for (i = 0; i < need.length; i++) {
		if (!cm.haveItem(need[i]['itemid'], (need[i]['qty'] * a)))
			ret = false;
	}
	return ret;
}
