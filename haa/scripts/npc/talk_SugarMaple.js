var status = -1;
importPackage(Packages.server);

function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
	var txt = "안녕하세요 #b#h 0##k님! 25성 스타포스 아이템 또는 15성 놀라운 장비강화 주문서의 아이템은 30성의 옵션 달성이 가능하다는 사실을 알고 계시나요? 강화 시 #b각 스탯 30, HP 900, 공격력/마력 15#k가 부여됩니다. 옵션을 부여할 아이템을 골라주세요.\r\n";
	for (i = 0; i < cm.getInventory(1).getSlotLimit(); i++) {
           if (cm.getInventory(1).getItem(i) != null) {
		if (cm.getInventory(1).getItem(i).getEnhance() >= 25 || (cm.getInventory(1).getItem(i).getEnhance() >= 15 && cm.getInventory(1).getItem(i).isAmazingequipscroll())) {
                	txt += "#L"+i+"# #i"+cm.getInventory(1).getItem(i).getItemId()+"# #b#z"+cm.getInventory(1).getItem(i).getItemId()+"#\r\n";
		}
            }
        }
	cm.sendSimple(txt);
    } else if (status == 1) {
	cm.dispose();
	item = cm.getInventory(1).getItem(selection);
	if (item == null) {
		return;
	}
	if (item.getEnhance() >= 30) {
		cm.sendOk("이미 30성을 달성한 아이템입니다.");
	} else if (!cm.getPlayer().haveItem(4001109, 1)) {
		cm.sendOk("#i4001109##z4001109# 아이템이 필요합니다.\r\n#b보스 몬스터 #e#r스우, 데미안, 루시드#k#n#b에게 얻을 수 있습니다.");
	} else {
		item.addStr(30);
		item.addDex(30);
		item.addInt(30);
		item.addLuk(30);
		item.addHp(900);
		item.addWatk(15);
		item.addMatk(15);
		item.setEnhance(item.getEnhance() + 1);
		cm.gainItem(4001109, -1);
		cm.getPlayer().forceReAddItem(item, Packages.client.inventory.MapleInventoryType.getByType(1));
		cm.sendOk("아이템을 강화하였습니다. #b현재 강화 : #k#e" + item.getEnhance() + "성#n");
	}
    }
}

