var enter = "\r\n";
var seld = -1;

var price = 30;
var allstat = 2, atk = 2; // 1회당 올스텟, 공마 증가치

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
		var txt = "#fs11##fc0xFF000000#캐시아이템을 #b#z4310308# 30개#fc0xFF000000#로 강화할 수 있다는 사실을 알고 계시나요? #b원하시는 캐시아이템#fc0xFF000000#을 골라주세요.\r\n#r(1회당 올스탯 2 / 공 마 2 증가 중첩가능)#k\r\n";
		for (i = 0; i < cm.getInventory(6).getSlotLimit(); i++) {
			if (cm.getInventory(6).getItem(i) != null) {
				if (cm.isCash(cm.getInventory(6).getItem(i).getItemId())) {
					txt += "#L" + i + "# #i" + cm.getInventory(6).getItem(i).getItemId() + "# #b#z" + cm.getInventory(6).getItem(i).getItemId() + "#\r\n";
				}
			}
		}
		cm.sendSimple(txt);
	} else if (status == 1) {
		if (!cm.haveItem(4310308, 30)) {
			cm.sendOk("#fs11##fc0xFF000000#1회 강화하기 위해선 #b" + price + " #z4310308##fc0xFF000000#가 필요합니다.");
			cm.dispose();
			return;
		}

		item = cm.getInventory(6).getItem(sel);
		if (item == null) {
			return;
		}
		if (!cm.isCash(item.getItemId())) {
			cm.sendOk("핵 사용 적발");
			cm.dispose();
			return;
		}
		item.setStr(item.getStr() + allstat);
		item.setDex(item.getDex() + allstat);
		item.setInt(item.getInt() + allstat);
		item.setLuk(item.getLuk() + allstat);
		item.setWatk(item.getWatk() + atk);
		item.setMatk(item.getMatk() + atk);
		cm.gainItem(4310308, -30);
		cm.sendOk("#fs11#아이템에 스텟을 부여했습니다. 이용해 주셔서 감사합니다.");
		cm.dispose();
		cm.getPlayer().forceReAddItem(item, Packages.client.inventory.MapleInventoryType.getByType(6));
	}
}
