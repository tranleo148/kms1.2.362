var status = -1;
importPackage(Packages.server);
importPackage(Packages.constants);

var 후포 = 5000;
var 홍포 = 10000;

var p, point, need;

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
	var txt = "#fs11#안녕하세요 #b#h 0##k님! 뱃지, 엠블렘, 보조무기(블레이드 제외)의 기본 옵션 재설정이 가능하다는 사실을 알고 계시나요? 강화 시 #b각 스탯당 최대 30~150, HP는 10000, 공격력/마력은 150#k까지 부여될 수 있습니다. 옵션을 재설정할 아이템을 골라주세요.\r\n";
	for (i = 0; i < cm.getInventory(1).getSlotLimit(); i++) {
           if (cm.getInventory(1).getItem(i) != null) {
		if ((cm.getInventory(1).getItem(i).getItemId() >= 1190000 && cm.getInventory(1).getItem(i).getItemId() < 1200000) || (cm.getInventory(1).getItem(i).getItemId() >= 1182000 && cm.getInventory(1).getItem(i).getItemId() < 1183000) || (cm.getInventory(1).getItem(i).getItemId() >= 1098000 && cm.getInventory(1).getItem(i).getItemId() < 1100000) || GameConstants.isSecondaryWeapon(cm.getInventory(1).getItem(i).getItemId())) {
                	txt += "#L"+i+"# #i"+cm.getInventory(1).getItem(i).getItemId()+"# #b#z"+cm.getInventory(1).getItem(i).getItemId()+"#\r\n";
		}
            }
        }
	cm.sendSimple(txt);
    } else if (status == 1) {
		var msg = "어떤 포인트를 이용해 잠재능력을 부여하시겠습니까?\r\n";
		msg += "#L1#후원포인트 ("+후포+"P)\r\n";
		msg += "#L2#홍보포인트 ("+홍포+"P)\r\n";
		cm.sendSimple(msg);
	} else if (status == 2) {
		sel = selection;
		p = sel;
		point = sel == 1 ? cm.getPlayer().getDonationPoint() : sel == 2 ? cm.getPlayer().getHPoint() : -1;
		need = sel == 1 ? 후포 : sel == 2 ? 홍포 : -1;

		if (point < 0 || need < 0) {
			cm.dispose();
			return;
		}
		if (point < need) {
			cm.sendOk("포인트가 부족하네요.");
			cm.dispose();
			return;
		}
		item = cm.getInventory(1).getItem(selection);
		if (item == null) {
			return;
		}
		item.setStr(Randomizer.rand(30, 150));
		item.setDex(Randomizer.rand(30, 150));
		item.setInt(Randomizer.rand(30, 150));
		item.setLuk(Randomizer.rand(30, 150));
		item.setHp(Randomizer.rand(3000, 10000));
		item.setWatk(Randomizer.rand(30, 150));
		item.setMatk(Randomizer.rand(30, 150));
			if (p == 1)
				cm.getPlayer().gainDonationPoint(-need);
			else
				cm.getPlayer().gainHPoint(-need);
		cm.getPlayer().forceReAddItem(item, Packages.client.inventory.MapleInventoryType.getByType(1));
		cm.sendOk("아이템의 옵션을 재설정했습니다. 이용해 주셔서 감사합니다.");
		cm.dispose();
    }
}
