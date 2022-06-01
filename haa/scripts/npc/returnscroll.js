var status = -1;
var item;

function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {
       status++;

    if (status == 0) {
	text = "#fc0xFFFF9933#리턴 스크롤의 효과로 원래의 주문서 사용을 그만두시겠습니까? 사라진 주문서는 복구되지 않습니다.#k\r\n#b<예>를 누르면 주문서 사용 이전으로 돌아갑니다.#k\r\n\r\n";
	if (cm.getPlayer().returnscroll.getPosition() < 0) {
		item = cm.getPlayer().getInventory(-1).getItem(cm.getPlayer().returnscroll.getPosition());
	} else {
		item = cm.getPlayer().getInventory(1).getItem(cm.getPlayer().returnscroll.getPosition());
	}
	if (cm.getPlayer().returnscroll.getStr() - item.getStr() != 0) {
		text += "#d#e변동된 힘 수치 : " + -(cm.getPlayer().returnscroll.getStr() - item.getStr()) + "#n#k\r\n";
	}
	if (cm.getPlayer().returnscroll.getDex() - item.getDex() != 0) {
		text += "#d#e변동된 민첩 수치 : " + -(cm.getPlayer().returnscroll.getDex() - item.getDex()) + "#n#k\r\n";
	}
	if (cm.getPlayer().returnscroll.getInt() - item.getInt() != 0) {
		text += "#d#e변동된 지능 수치 : " + -(cm.getPlayer().returnscroll.getInt() - item.getInt()) + "#n#k\r\n";
	}
	if (cm.getPlayer().returnscroll.getLuk() - item.getLuk() != 0) {
		text += "#d#e변동된 행운 수치 : " + -(cm.getPlayer().returnscroll.getLuk() - item.getLuk()) + "#n#k\r\n";
	}
	if (cm.getPlayer().returnscroll.getWatk() - item.getWatk() != 0) {
		text += "#d#e변동된 공격력 수치 : " + -(cm.getPlayer().returnscroll.getWatk() - item.getWatk()) + "#n#k\r\n";
	}
	if (cm.getPlayer().returnscroll.getMatk() - item.getMatk() != 0) {
		text += "#d#e변동된 마력 수치 : " + -(cm.getPlayer().returnscroll.getMatk() - item.getMatk()) + "#n#k\r\n";
	}
	if (cm.getPlayer().returnscroll.getHp() - item.getHp() != 0) {
		text += "#d#e변동된 체력 수치 : " + -(cm.getPlayer().returnscroll.getHp() - item.getHp()) + "#n#k\r\n";
	}
	if (cm.getPlayer().returnscroll.getMp() - item.getMp() != 0) {
		text += "#d#e변동된 마나 수치 : " + -(cm.getPlayer().returnscroll.getMp() - item.getMp()) + "#n#k\r\n";
	}
	cm.sendYesNoS(text, 1);
    } else if (status == 1) {
	cm.getPlayer().returnscroll.setFlag(cm.getPlayer().returnscroll.getFlag() - 0x8000);
	if (mode == 1) {
		item.set(cm.getPlayer().returnscroll);
		cm.getPlayer().forceReAddItem(item, Packages.client.inventory.MapleInventoryType.getByType(item.getPosition() < 0 ? -1 : 1));
		cm.sendOk("주문서 사용 이전으로 아이템을 되돌렸습니다.");
	} else {
		item.setUpgradeSlots(item.getUpgradeSlots() - 1);
		cm.getPlayer().forceReAddItem(item, Packages.client.inventory.MapleInventoryType.getByType(item.getPosition() < 0 ? -1 : 1));
		cm.sendOk("아이템을 되돌리지 않았습니다. ");
	}
	cm.getPlayer().returnscroll = null;
	cm.dispose();
    }
}

