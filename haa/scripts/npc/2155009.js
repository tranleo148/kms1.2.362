var enter = "\r\n";
var seld = -1;

var need = [
	{'itemid' : 4001842, 'qty' : 20},
	{'itemid' : 4001843, 'qty' : 1}
]
var tocoin = 4310156, toqty = 1;

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
		var msg = "코어가 필요해...이유는 비밀이지만..."+enter;

		for (i = 0; i < need.length; i++) {
			if (i != need.length - 1) msg += "#i"+need[i]['itemid']+"##z"+need[i]['itemid']+"# "+need[i]['qty']+"개랑"+enter;
			else msg += "#i"+need[i]['itemid']+"##z"+need[i]['itemid']+"# "+need[i]['qty']+"개 정도는 있어야"+enter;
		}

		msg += "나랑 거래가 가능해."+enter;
		msg += "삼손이가 원하는...#b#z"+tocoin+"##k...내가 가지고 있어...";
		if (haveNeed(1))
			cm.sendNext(msg);
		else {
			msg += enter+enter+"음... 근데 넌 교환 할 수 있는 아이템이 없군....";
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
		cm.sendGetNumber("넌 최대 #b"+max+"개#k 교환할 수 있어...\r\n몇 개 교환할거야...?", 1, 1, max);
	} else if (status == 2) {
		if (!haveNeed(sel)) {
			cm.sendOk("뭐야... 사기를 친거야...? 아이템이 모자라잖아...!");
			cm.dispose();
			return;
		}
		for (i = 0; i < need.length; i++) {
			cm.gainItem(need[i]['itemid'], -(need[i]['qty'] * sel));
		}
		cm.gainItem(tocoin, (toqty * sel));
		cm.sendOk("좋은 거래였어...");
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
