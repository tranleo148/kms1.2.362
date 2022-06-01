
importPackage(Packages.client.inventory);
importPackage(Packages.server);
importPackage(Packages.tools);
importPackage(java.util);
importPackage(Packages.handling.world);


/* 3개 필요 */ NEED_3 = [1003142];
/* 2개 필요 */ NEED_2 = [1212100];

function N3_CHECK(getId) {
	for(i = 0; i < NEED_3.length; i++) {
		if(getId == NEED_3[i]) {
		return true;
		}
	}
	return false;
}

function N2_CHECK(getId) {
	for(i = 0; i < NEED_2.length; i++) {
		if(getId == NEED_2[i]) {
		return true;
		}
	}
	return false;
}



var status = -1;

function start() {
	action(1, 0, 0);
}

function action(mode, type, selection) {

if (mode == 1) {
	status++;
} else {
	cm.dispose();
	return;
}

if (status == 0) {
	cm.sendYesNoS("#i5520001# #b#z5520001##k를 사용하여 당신과 연결된 끈을 끊어 #r1회에 한해 다른 사람과 교한이 가능#k하게 만드시겠습니까?\r\n\r\n\r\n"
		+ "#e※ 주의사항#n\r\n"
		+ "1. 무제한으로 거래 가능한 아이템에도 #r#e사용이 가능#n#k합니다.\r\n"
		+ "2. 가위를 사용하면 #e#r재지급해드릴 수 없습니다.#n#k\r\n"
		+ "3. 훈장 등 #e#r일부 아이템#k#n에는 사용할 수 없습니다.\r\n"
		+ "4. 아이템에 따라 #e#r가위 요구량#k#n이 다를 수 있습니다.\r\n"
		+ "5. 가위 사용 후, 아이템을 버릴 경우 #e#r증발#k#n할 수 있습니다.\r\n"
		+ "6. #e#r계정 내 1회 이동 가능#k#n는 #r#e교환 불가#k#n 상태로 변경됩니다.", 4, 9010060);

} else if (status == 1) {
	var chat = "";
	var inven = cm.getInventory(1);
	var inventory = cm.getInventory(1).getSlotLimit();
	for (var slot = 0; slot < inventory; slot++) {
		if (inven.getItem(slot) == null || cm.isCash(inven.getItem(slot).getItemId()) || Math.floor(inven.getItem(slot).getItemId() / 10000) == 114) { continue; }
		var itemid = inven.getItem(slot).getItemId();
		getNumber = 
				inven.getItem(slot).getOwner() == "7강" ? 2 : 
				inven.getItem(slot).getOwner() == "8강" ? 2 :
				N3_CHECK(inven.getItem(slot).getItemId()) == true ? 3 :
				N2_CHECK(inven.getItem(slot).getItemId()) == true ? 2 :
				inven.getItem(slot).getOwner() == "9강" ? 3 :
				inven.getItem(slot).getOwner() == "10강" ? 4 : 1;

		chat += "#b#L" + slot + "##i" + itemid + "# #z" + itemid +"# #r("+getNumber+"개 필요)#k#l\r\n";
	}
	cm.sendSimpleS(cm.getPlayer().getName() + "님이 가지고 계신 아이템 목록입니다. 원하시는 아이템을 선택해주세요.\r\n" + chat, 4, 9010060);

} else if (status == 2) {
	item = cm.getInventory(1).getItem(selection);

		getNumber = 
				item.getOwner() == "7강" ? 2 : 
				item.getOwner() == "8강" ? 2 :
				N3_CHECK(item.getItemId()) == true ? 3 :
				N2_CHECK(item.getItemId()) == true ? 2 :
				item.getOwner() == "9강" ? 3 :
				item.getOwner() == "10강" ? 4 : 1;

		if(!cm.haveItem(5520001, getNumber)) {
		cm.sendOkS("#i5520001# #b#z5520001# "+getNumber+"#k개가 없으면 끈을 자를 수 없습니다.", 4, 9010060);
		cm.dispose();
		return;
		}

		if(ItemFlag.KARMA_EQ.check(item.getFlag()))
		{
			cm.sendOkS("선택한 아이템은 이미 교환이 가능한 상태입니다. 다른 아이템을 선택해 주세요.", 4, 9010060);
			cm.dispose();
			return;
		}

	cm.gainItem(5520001, -getNumber);
	flag = item.flag |= ItemFlag.KARMA_EQ.getValue();
	item.setFlag(flag);
	cm.getPlayer().forceReAddItem(item, MapleInventoryType.EQUIP);
	cm.sendOkS("선택하신 항목을 아이템에 적용 해드렸습니다. 그럼, 즐거운 메이플스토리 되세요!", 4, 9010060);
	cm.dispose();
}
}