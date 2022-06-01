var enter = "\r\n";
var seld = -1;

var need = [
	{'itemid' : 4001878, 'qty' : 10},
	{'itemid' : 4001879, 'qty' : 1}
]
var tocoin = 4310218, toqty = 1;

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
		var msg = "저희 시간의 신관들은 아케인 리버 지역을 조사하고 있습니다. 혹시 에르다가 응집된 물방울석을 가지고 계신가요? 레헬른 지역의 몬스터들이 희귀하게 가지고 있다는 얘기를 들었습니다."+enter;

		for (i = 0; i < need.length; i++) {
			if (i != need.length - 1) msg += "#i"+need[i]['itemid']+"##z"+need[i]['itemid']+"# "+need[i]['qty']+"개와"+enter;
			else msg += "#i"+need[i]['itemid']+"##z"+need[i]['itemid']+"# "+need[i]['qty']+"개를 주신다면 조사에 큰 도움이 될겁니다. 대신 제가 가진 #b#z"+tocoin+"##k을 드리겠습니다."+enter;
		}

		
		if (haveNeed(1))
			cm.sendNext(msg);
		else {
			msg += enter+enter+"허나.. 당신이 교환할 수 있는 아이템이 없군요..";
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
		cm.sendGetNumber("당신은 최대 #b"+max+"개를#k 교환할 수 있군요..\r\n몇 개를 교환하시겠습니까...?", 1, 1, max);
	} else if (status == 2) {
		if (!haveNeed(sel)) {
			cm.sendOk("당신이 소지한 아이템이 부족합니다.");
			cm.dispose();
			return;
		}
		for (i = 0; i < need.length; i++) {
			cm.gainItem(need[i]['itemid'], -(need[i]['qty'] * sel));
		}
		cm.gainItem(tocoin, (toqty * sel));
		cm.sendOk("판타즈마 코인을 지급해드렸습니다.");
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
