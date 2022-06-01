importPackage(Packages.constants);
importPackage(Packages.client.custom.inventory);

var status = -1;
var seld = -1;
var bitcoin = 0;
var inven;
var item;
var equipped;
var ori;
var types = CustomItem.CustomItemType.values();
var type;
var size;
var effects;
var selType;

function start() {
	status = -1;
	action(1, 0, 0);
}

function float2int (value) {
    return value | 0;
}

function getImage(id) {
	var image = 0;
	switch (id) {
	case 0:
	image = 2430732;
	break;
	case 1:
	image = 2430885;
	break;
	case 2:
	image = 2430886;
	break;
	case 3:
	image = 2430887;
	break;
	case 4:
	image = 2430888;
	break;
	case 5:
	image = 2430889;
	break;
	case 6:
	image = 2430890;
	break;
	case 7:
	image = 2430891;
	break;
	case 8:
	image = 2430892;
	break;
	case 9:
	image = 2430893;
	break;
	case 10:
	image = 2430894;
	break;
	case 11:
	image = 2430945;
	break;
	case 12:
	image = 2430946;
	break;
	}
	return image;
}

function action(mode, type, selection) {
	if (status >= 0 && mode == 0) {
		cm.dispose();
		return;
	}
	if (mode == 1)
		status++;
	else
		status--;

	if (status == 0) {
		inven = cm.getPlayer().getCustomInventory();
		var msg = "";
		for (i = 0; i < types.length; i++) {
			type = types[i];
			name = type.name();
			if (i == 0) {
				msg += "                                                              #b#fs13##L" + i + "#인벤토리#l\r\n\r\n";
			} else {
				var id = cm.getPlayer().equippedCustomItem(type);
				var image = 0;
				if (id > -1)
					image = getImage(id);

				if (image > 0) {
					msg += "#fs11##fc0xFF6600CC##i" + image + "#  \t" + name + " #r(착용 중)\r\n\r\n";
				} else {
					msg += "#fc0xFF6600CC##fUI/UIWindow.img/IconBase/0# \t" + name + "#fc0xFF000000# (미착용)\r\n\r\n";
				}
			}
		}
		cm.sendSimple(msg);
	} else if (status == 1) {
		inven = cm.getPlayer().getCustomInventory();
		var msg = "#fs11##b장비 목록을 선택해주세요.\r\n";
		for (i = 0; i < types.length; i++) {
			type = types[i];
			name = type.name();
			if (i > 0) {
				msg += "#fs11##fc0xFF6600CC##L" + i + "# " + name + "\r\n";
			}
		}
		cm.sendSimple(msg);
	} else if (status == 2) {
		selType = selection;
		inven = cm.getPlayer().getCustomInventory();
		name = types[selection].name();
		if (selection == 0) {
			name = "Inventory";
		}
		var msg = "#fs11##fc0xFF000000#보유하고 있는 #fc0xFF6600CC#" + name + " #fc0xFF000000#목록 #b(클릭 시 장착 및 장착해제)#fc0xFF000000#\r\n";
		for (i = 0; i < inven.size(); i++) {
			item = GameConstants.customItems.get(i);
			size = inven.get(i);
			type = item.getType();
			if (size > 0 && selType == type.ordinal()) {
				msg += "#fc0xFF6600CC##L" + i + "# ";
				var image = getImage(i);
				msg += "#i" +  image + "# ";

				msg +=  item.getName();
				if (cm.getPlayer().equippedCustomItem(type) == i)	
					msg += " #r(장착 중)#fc0xFF000000#";
				msg += "#l\r\n\r\n";
				
				effects = item.getEffects();
				for (j = 0; j < effects.size(); j++) {
					var effect = effects.get(j).getLeft().name();
					if (effect == "BdR") {
						effect = "보스 공격시 데미지 +";
					} else if (effect == "AllStatR") {
						effect = "올스탯 +";
					} else if (effect == "CrD") {
						effect = "크리데미지 +";
					} else if (effect == "DropR") {
						effect = "아이템 드롭률 +";
					} else if (effect == "MesoR") {
						effect = "메소 획득량 +";
					}
					msg +=  "#r\t" + effect + " " + effects.get(j).getRight() +"%\r\n";
				}
				msg +=  "\r\n";
			}
		}
		cm.sendSimple(msg);
	} else if (status == 3) {
		inven = cm.getPlayer().getCustomInventory();
		var size = inven.get(selection);
		if (size == 0) { // 테스트용
			cm.getPlayer().addCustomItem(selection);
			cm.sendOk("무기 얻음.");
			cm.dispose();
			return;
		}
		
		item = GameConstants.customItems.get(selection);
		equipped = cm.getPlayer().equippedCustomItem(item.getType());
		if (equipped == selection) { // 고른 템을 장착 중일때
			cm.getPlayer().unequipCustomItem(equipped);
			cm.sendOk(item.getName() + " 장착 해제");
		} else if (equipped >= 0) { // 같은 위치 다른 템 장착 중일때
			ori = GameConstants.customItems.get(equipped);
			cm.getPlayer().unequipCustomItem(equipped);
			cm.getPlayer().equipCustomItem(selection);	
			cm.sendOk(ori.getName() + " 장착을 해제하였습니다. " + item.getName() + " 장착");
		} else {
			cm.getPlayer().equipCustomItem(selection);	
			cm.sendOk(item.getName() + " 장착");
		}
		cm.dispose();
		return;	
	}
}