	importPackage(Packages.client.inventory);
	importPackage(Packages.server);
	importPackage(Packages.constants);
	importPackage(java.lang);

function start() { Status = -1; action(1, 0, 0); }

function action(M, T, S) {
	if(M == -1) { cm.dispose(); } else {
		if(M == 0) { cm.dispose(); return; }
		if(M == 1) Status++; else Status--;


		if(Status == 0) {
		cm.sendSimple("#fs12#인벤토리에 있는 캐시 아이템을 버릴 수 있다. 버리고 싶은 아이템이 있는 인벤토리를 선택하자…"
			+ "\r\n#L0#캐시 장비 인벤토리"
//			+ "\r\n#L2#장비 인벤토리"
			+ "\r\n#L1#캐시 인벤토리");
		}


		else if(Status == 1) {
		Say = "#fs12#버릴 아이템을 선택하자… 한 번 버린 아이템은 다시 되찾을 수 없다. 똑같은 것이 여러 개라면, 선택한 것을 버리게 된다.";
		S1 = S;
			switch(S1) {
			case 0: // 장비 인벤토리
			EQUIP = cm.getInventory(1);
				for (i = 0; i < EQUIP.getSlotLimit(); i++) {
					if(EQUIP.getItem(i) == null || !cm.isCash(EQUIP.getItem(i).getItemId())) { continue; }
				Say += "#L" + i + "##i"+EQUIP.getItem(i).getItemId()+"# #z"+EQUIP.getItem(i).getItemId()+"##l\r\n";
				}
			cm.sendSimple(Say);
			break;
			case 2: // 장비 인벤토리
			EQUIP = cm.getInventory(1);
				for (i = 0; i < EQUIP.getSlotLimit(); i++) {
					if(EQUIP.getItem(i) == null) { continue; }
				Say += "#L" + i + "##i"+EQUIP.getItem(i).getItemId()+"# #z"+EQUIP.getItem(i).getItemId()+"##l\r\n";
				}
			cm.sendSimple(Say);
			break;
			case 1: // 캐시 인벤토리
			CASH = cm.getInventory(5);
				for (i = 0; i < CASH.getSlotLimit(); i++) {
					if(CASH.getItem(i) == null || !cm.isCash(CASH.getItem(i).getItemId())) { continue; }
				Say += "#L" + i + "##i"+CASH.getItem(i).getItemId()+"# #z"+CASH.getItem(i).getItemId()+"##l\r\n";
				}
			cm.sendSimple(Say);
			break;
			}
		}


		else if(Status == 2) {
		S2 = S;
		cm.sendSimple("#fs12#선택한 아이템을 정말 처리할까?\r\n#b"
			+ "#L1#아이템을 삭제한다.\r\n\r\n\r\n#l"
			//+ "#L0#아이템을 버린다.\r\n\r\n\r\n#l"
			+ "#e#r(이번 결정이 마지막입니다. 결정하는 순간 버리거나 삭제한 아이템은 다시 되찾을 수 없습니다.)#k#n");
		}

		
		else if(Status == 3) {
		S3 = S;
			switch(S3) {
			case 0: // 필드에 버린다.
			if (GameConstants.isPet(cm.getInventory(5).getItem(S2).getItemId())) {
			cm.getPlayer().dropMessage(1, "애완동물은 절대 버릴 수 없습니다.\r\n그들도 하나의 생명입니다.\r\n\r\n무책임한 당신의 행동은 이 작은 생명에게 큰 불행일 수 있습니다.");
			cm.dispose(); return;
			}

				switch(S1) {
				case 0: cm.dropItem(S2, 1, 1); break;
				case 1: cm.dropItem(S2, 5, 1); break;
				}
			break;

			case 1: // 삭제한다.
				switch(S1) {
                                case 2:
				case 0: MapleInventoryManipulator.removeFromSlot(cm.getClient(), MapleInventoryType.EQUIP, S2, 1, false); break;
				case 1: MapleInventoryManipulator.removeFromSlot(cm.getClient(), MapleInventoryType.CASH, S2, cm.getInventory(5).getItem(S2).getQuantity(), false); break;
				}
			break;
			}

		cm.sendNext("선택한 아이템을 성공적으로 처리했다!");
		cm.dispose();
		}
	}
}
