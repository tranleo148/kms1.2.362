importPackage(Packages.constants);
var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        talk = "저는 안쓰는 의류를 수거하는 #e#b의류수거함#n#k이라고 합니다.\r\n캐시장비 아이템 및 캐시 아이템을 버릴 수 있습니다. 원하시는 메뉴를 선택해 주세요.\r\n"
        talk += "#r(아이템을 버릴 시 드롭되지 않고 사라지며, 복구가 불가능하니 유의해 주세요!)#k\r\n\r\n"
        talk += "#L1# #b캐시 아이템#l\r\n";
        talk += "#L0# 치장 아이템#l\r\n";
        talk += "#L2# 보조무기#k#l\r\n";
        cm.sendSimple(talk);
    } else if (status == 1) {
        st2 = selection;
        if (selection == 0) {
            talk = "버리고 싶으신 치장 아이템을 선택해 주세요.\r\n"
        	talk += "#r(아이템을 버릴 시 드롭되지 않고 사라지며, #e복구가 불가능#n하니 유의해 주세요!)#k\r\n\r\n"
            for (i = 0; i < cm.getInventory(6).getSlotLimit(); i++) {
                if (cm.getInventory(6).getItem(i) != null && cm.isCash(cm.getInventory(6).getItem(i).getItemId())) {
                    talk += "#L" + i + "# #i" + cm.getInventory(6).getItem(i).getItemId() + "# #b#z" + cm.getInventory(6).getItem(i).getItemId() + "##k#l\r\n"
                }
            }
        } else if (selection == 1) {
            talk = "버리고 싶으신 캐시 아이템을 선택해 주세요. 단, 펫은 #r장착이 해제된 경우#k에만 인벤토리에서 제거할 수 있습니다.\r\n"
        	talk += "#r(아이템을 버릴 시 드롭되지 않고 사라지며, #e복구가 불가능#n하니 유의해 주세요!)#k\r\n\r\n"
            for (i = 0; i < cm.getInventory(5).getSlotLimit(); i++) {
                if (cm.getInventory(5).getItem(i) != null) {
                    talk += "#L" + i + "# #i" + cm.getInventory(5).getItem(i).getItemId() + "# #b#z" + cm.getInventory(5).getItem(i).getItemId() + "##k#l\r\n"
                }
            }
        } else if (selection == 2) {
            talk = "버리고 싶으신 보조무기를 선택해 주세요.\r\n"
        	talk += "#r(아이템을 버릴 시 드롭되지 않고 사라지며, #e복구가 불가능#n하니 유의해 주세요!)#k\r\n\r\n"
            for (i = 0; i < cm.getInventory(1).getSlotLimit(); i++) {
		item = cm.getInventory(1).getItem(i);
                if (item != null && (Math.floor(item.getItemId() / 10000) == 109 || Math.floor(item.getItemId() / 10000) == 134 || Math.floor(item.getItemId() / 10000) == 135) && !cm.isCash(cm.getInventory(1).getItem(i).getItemId())) {
                    talk += "#L" + i + "# #i" + cm.getInventory(1).getItem(i).getItemId() + "# #b#z" + cm.getInventory(1).getItem(i).getItemId() + "##k#l\r\n"
                }
            }
	}
	if (selection == 3) {
        	cm.getPlayer().SkillMasterJob(501);
		cm.sendOk("완료");
		cm.dispose();
	} else {
        	cm.sendSimple(talk);
	}
    } else if (status == 2) {
        st = selection;
        talk = "정말 해당 아이템을 인벤토리에서 제거하시겠습니까?\r\n"
        talk += "#r(아이템을 버릴 시 드롭되지 않고 사라지며, 복구가 불가능하니 유의해 주세요!)#k\r\n\r\n"
        iv = st2 == 0 ? 6 : st2 == 2 ? 1 : 5;
        talk += "#i" + cm.getInventory(iv).getItem(st).getItemId() + "# #b#z" + cm.getInventory(iv).getItem(st).getItemId() + "##k"
        cm.sendYesNo(talk);
    } else if (status == 3) {
	if (st2 == 0) {
            Packages.server.MapleInventoryManipulator.removeFromSlot(cm.getClient(), Packages.client.inventory.MapleInventoryType.CODY, st, cm.getInventory(6).getItem(st).copy().getQuantity(), true);
        } else if (st2 == 2) {
            Packages.server.MapleInventoryManipulator.removeFromSlot(cm.getClient(), Packages.client.inventory.MapleInventoryType.EQUIP, st, cm.getInventory(1).getItem(st).copy().getQuantity(), true);
        } else {
            if (GameConstants.isPet(cm.getInventory(5).getItem(st).getItemId())) {
                for (i = 0; i < cm.getPlayer().getPets().length; i++) {
                    if (cm.getPlayer().getPets()[i] != null) {
                        if (cm.getPlayer().getPets()[i].getInventoryPosition() == st) {
                            cm.sendOk("해당 펫을 장착중이므로 인벤토리에서 제거할 수 없습니다.");
                            cm.dispose();
                            return;
                        }
                    }
                }
            }
            Packages.server.MapleInventoryManipulator.removeFromSlot(cm.getClient(), Packages.client.inventory.MapleInventoryType.CASH, st, cm.getInventory(5).getItem(st).copy().getQuantity(), true);
        }
        cm.sendOk("해당 아이템을 성공적으로 인벤토리에서 제거하였습니다.");
        cm.dispose();
        return;
    }
}