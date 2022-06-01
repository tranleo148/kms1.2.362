var enter = "\r\n";
var seld = -1;
var seld2 = -1;

var 뱃지후포 = 30000, 뱃지홍포 = 9000;

var 포켓후포 = 30000, 포켓홍포 = 9000;

var 캐시후포 = 20000, 캐시홍포 = 50000;

var p = -1, need = -1;
var pt = "";

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
		var msg ="#fs11#안녕하세요 #b#h 0##k님! 잠재능력이 없는 아이템에 잠재능력 부여가 가능하다는 사실을 알고 계시나요? #fs11##b"+enter;
		//msg += "#L1#뱃지 아이템 잠재능력 부여"+enter;
		//msg += "#L2#포켓 아이템 잠재능력 부여"+enter;
		//msg += "#L6#캐시 아이템 잠재능력 부여"+enter;
		cm.sendSimple(msg);
	} else if (status == 1) {
		seld = sel;
		var msg = "#fs11# 어떤 포인트를 이용해 잠재능력을 부여하시겠습니까?\r\n";
		후포 = sel == 1 ? 뱃지후포 : sel == 2 ? 포켓후포 : 캐시후포;
		홍포 = sel == 1 ? 뱃지홍포 : sel == 2 ? 포켓홍포 : 캐시홍포;
		msg += "#L1#후원포인트 ("+후포+"P)\r\n";
		msg += "#L2#홍보포인트 ("+홍포+"P)\r\n";
		cm.sendSimple(msg);
	} else if (status == 2) {
		seld2 = sel;
		p = seld2 == 1 ? cm.getPlayer().getDonationPoint() : cm.getPlayer().getHPoint();
		pt = seld2 == 1 ? "후원" : "홍보";
		need = (seld2 == 1 && seld == 1) ? 뱃지후포 : (seld2 == 1 && seld == 2) ? 포켓후포 : (seld2 == 1 && seld == 3) ? 캐시후포 : (seld2 == 2 && seld == 1) ? 뱃지홍포 : (seld2 == 2 && seld == 2) ? 포켓홍포 : 캐시홍포;
		seldi = seld == 1 ? "뱃지" : seld == 2 ? "포켓" : "캐시";
		ic = seld == 1 ? 118 : 116;

		if (p < need) {
			cm.sendOk("#fs11#포인트가 부족합니다.");
			cm.dispose();
			return;
		}

		

		var msg ="#fs11#안녕하세요 #b#h 0##k님! "+seldi+"에 잠재능력 부여가 가능하다는 사실을 알고 계시나요? 원하시는 아이템을 골라주세요.\r\n#r(에디셔널은 개방되지 않습니다)#k"+enter;
		msg += "#fs11#현재 #b#h 0##k님의 #d"+pt+"포인트#k는 "+p+"P 입니다.#fs12#"+enter+enter;
		switch (seld) {
			case 1:
				for (i = 0; i < cm.getInventory(1).getSlotLimit(); i++) {
					item = cm.getInventory(1).getItem(i);
           					if (item == null) continue;
					if (Math.floor(item.getItemId() / 10000) == ic) msg += "#L"+i+"# #i"+item.getItemId()+"# #b#z"+item.getItemId()+"#\r\n";
				}
			break;
			case 2:
				for (i = 0; i < cm.getInventory(1).getSlotLimit(); i++) {
					item = cm.getInventory(1).getItem(i);
           					if (item == null) continue;
					if (Math.floor(item.getItemId() / 10000) == ic) msg += "#L"+i+"# #i"+item.getItemId()+"# #b#z"+item.getItemId()+"#\r\n";
				}
			break;
			case 3:
				for (i = 0; i < cm.getInventory(1).getSlotLimit(); i++) {
					item = cm.getInventory(1).getItem(i);
           					if (item == null) continue;
					if (cm.isCash(item.getItemId())) msg += "#L"+i+"# #i"+item.getItemId()+"# #b#z"+item.getItemId()+"#\r\n";
				}
			break;
		}
		cm.sendSimple(msg);
	} else if (status == 3) {
		if (p < need) {
			cm.sendOk("#fs11#포인트가 부족합니다.");
			cm.dispose();
			return;
		}
		if (seld2 == 1) cm.getPlayer().gainDonationPoint(-30000);
		else cm.getPlayer().gainHPoint(-9000);
		item = cm.getInventory(1).getItem(sel);

		if (item == null) {
			cm.dispose();
			return;
		}
		item.setState(17);
		item.setLines(3);
		item.setPotential1(10041);
		item.setPotential2(10042);
		item.setPotential3(10043);
		cm.sendOk("#fs11#아이템에 잠재능력을 부여했습니다. 이용해 주셔서 감사합니다.");
		cm.getPlayer().forceReAddItem(item, Packages.client.inventory.MapleInventoryType.getByType(1));
		cm.dispose();
	}
}