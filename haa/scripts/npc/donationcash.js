var enter = "\r\n";
var seld = -1;

var price = 30000;
var allstat = 20, atk = 20; // 1회당 올스텟, 공마 증가치

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
		var txt = "#fs11#안녕하세요 #b#h 0##k님! 캐시아이템을 도네이션 포인트로 강화할 수 있다는 사실을 알고 계시나요? 원하시는 캐시아이템을 골라주세요.\r\n#r(1회당 올스탯 20 / 공 마 20 증가 중첩가능)#k\r\n";
		for (i = 0; i < cm.getInventory(6).getSlotLimit(); i++) {
			if (cm.getInventory(6).getItem(i) != null) {
				if (cm.isCash(cm.getInventory(6).getItem(i).getItemId())) {
					txt += "#L" + i + "# #i" + cm.getInventory(6).getItem(i).getItemId() + "# #b#z" + cm.getInventory(6).getItem(i).getItemId() + "#\r\n";
				}
			}
		}
		cm.sendSimple(txt);
	} else if (status == 1) {
		if (cm.getPlayer().getDonationPoint() < 30000) {
			cm.sendOk("#fs11# 1회 강화하기 위해선 " + price + " 도네이션 포인트가 필요합니다.");
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
			cm.getPlayer().gainDonationPoint(-30000);
		cm.sendOk("#fs11#아이템에 스텟을 부여했습니다. 이용해 주셔서 감사합니다.");
		cm.dispose();
		cm.getPlayer().forceReAddItem(item, Packages.client.inventory.MapleInventoryType.getByType(6));
	}
}
