var enter = "\r\n";
var seld = -1, seld2 = -1;

var 개당메소 = 5000;
var max = -1;

var item;

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
		if (!cm.getPlayer().isGM()) {
			cm.dispose();
			return;
		}
		var msg = "메소로 교환하실 전리품을 선택해주세요."+enter;
		msg += "모든 전리품은 개당 #b"+개당메소+" 메소#k입니다."+enter+enter;
		for (i = 0; i < cm.getInventory(4).getSlotLimit(); i++) {
			item2 = cm.getInventory(4).getItem(i);
           			if (item2 == null) continue;
			msg += "#L"+i+"# #i"+item2.getItemId()+"##l";
			if (i % 5 == 0) msg += enter;
			else msg += "   ";
		}
		cm.sendSimple(msg);
	} else if (status == 1) {
		seld = sel;
		item = cm.getInventory(4).getItem(seld);

		if (item == null) {
			cm.dispose();
			return;
		}
		max = item.getQuantity();
		var msg = "선택하신 아이템은 #i"+item.getItemId()+"##b#z"+item.getItemId()+"##k입니다."+enter;
		msg += "최대 #b"+max+"#k개 판매하실 수 있습니다."+enter;
		msg += "몇 개 판매하시겠습니까?"+enter;
		msg += "#r※판매하실 아이템을 반드시 다시 확인해주세요. 어떤 경우에서도 복구해드리지 않습니다."+enter;
		cm.sendGetNumber(msg, 1, 1, max);
	} else if (status == 2) {
		seld2 = sel;
		if (seld2 > max) {
			cm.dispose();
			return;
		}
		item = cm.getInventory(4).getItem(seld);

		if (item == null) {
			cm.dispose();
			return;
		}
		var msg = "선택하신 아이템은 #i"+item.getItemId()+"##b#z"+item.getItemId()+"##k입니다."+enter;
		msg += "판매하려고 하는 개수가 #b"+seld2+"#k개 맞다면 '예'를 눌러주세요."+enter;
		msg += "총 #b"+(개당메소 * seld2)+"#k 메소가 지급됩니다."+enter;
		msg += "#r※판매하실 아이템을 반드시 다시 확인해주세요. 어떤 경우에서도 복구해드리지 않습니다."+enter;
		cm.sendYesNo(msg);
	} else if (status == 3) {
		if (seld2 > max) {
			cm.dispose();
			return;
		}
		item = cm.getInventory(4).getItem(seld);

		if (item == null) {
			cm.dispose();
			return;
		}
		cm.getInventory(4).removeItem(seld, seld2, false);
		cm.getPlayer().fakeRelog();
		cm.gainMeso(개당메소 * seld2);
		cm.sendOk("거래가 완료되었습니다.");
		cm.dispose();
	}
}