var status = -1;
var s1 = -1;

function start() {
 action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode != 1) {
	cm.getPlayer().getCashWishList().clear();
	cm.dispose();
	return;
	} else if (status == 2 && selection == 1) {
	status = 1;
	} else if (status == 2 && selection != 0) {
	status = 2;
		for (var k = 0; k < cm.getPlayer().getCashWishList().size(); k++) {
			if (cm.getPlayer().getCashWishList().get(k) == selection) {
			cm.getPlayer().getCashWishList().remove(k);
			break;
			}
		}
	cm.getPlayer().addCashWishList(selection);
	} else {
	status++;
	}
	switch (status) {
	case 0:
		if (cm.getPlayer().getCashWishList().size() > 0) {
		for (var k = 0; k < cm.getPlayer().getCashWishList().size(); k++) {
			if (cm.getPlayer().getCashWishList().get(k) < 1212000) {
			cm.sendOk("저장된 캐시 아이템 데이터 값을 삭제했습니다. 엔피시를 다시 클릭하시면 정상적으로 진행됩니다.");
			break;
			}
			if (!cm.canHold(cm.getPlayer().getCashWishList().get(k), 1)) {
			cm.sendOk("장비 창의 저장 공간이 부족하여 아이템을 전부 받지 못하였습니다. 장비 창을 비운 후 엔피시를 다시 클릭하시면 남은 아이템이 정상적으로 수령됩니다.");
			break;
			}
			cm.gainItem(cm.getPlayer().getCashWishList().get(k), 1);
		}
		cm.getPlayer().getCashWishList().clear();
		cm.dispose();
		} else {
		var text = "#b[검색 시스템] 원하시는 항목을 선택하세요.#k\r\n#d";
//		text += "\r\n#L0#성형 검색을 하고 싶습니다.";
//		text += "\r\n#L2#헤어 검색을 하고 싶습니다.";
		text += "\r\n#L1#캐시 검색을 하고 싶습니다.";
		cm.sendSimple(text);
		}
	break;
	case 1:
		if (s1 == -1) {
		s1 = selection;
		}
		var thier = s1 == 0 ? "성형" : s1 == 2 ? "헤어" : "캐시 아이템";
		var text = "원하시는 "+ thier + "의 이름을 입력하세요.";
		cm.sendGetText(text);
	break;
	case 2:
		cm.SearchItem(cm.getText(), s1);
	break;
	case 3:
		switch (s1) {
		case 0:
		if (cm.getPlayer().getCashWishList().size() != 0)
			cm.askAvatar("선택하신 성형 미리보기 입니다. 원하시는 항목을 선택하세요.", cm.getPlayer().getCashWishList());
		else
			cm.dispose();
		break;
		case 1:
		var text = "선택된 아이템 항목입니다. 예를 누르시면 아이템을 차례대로 지급합니다.";
		for (var item = 0; item < cm.getPlayer().getCashWishList().size(); item++) {
			text += "#d\r\n#i" + cm.getPlayer().getCashWishList().get(item) + "# #z" + cm.getPlayer().getCashWishList().get(item) + "#";
		}
		if (cm.getPlayer().getCashWishList().size() != 0)
			cm.sendYesNo(text);
		else
			cm.dispose();
		break;
		case 2:
		if (cm.getPlayer().getCashWishList().size() != 0)
			cm.askAvatar("선택하신 헤어 미리보기 입니다. 원하시는 항목을 선택하세요.", cm.getPlayer().getCashWishList());
		else
			cm.dispose();
		break;
		}
	break;
	case 4:
		switch (s1) {
		case 0:
		case 2:
		cm.gainItem(3010000, 1);
		cm.setAvatar(3010000, cm.getPlayer().getCashWishList().get(selection));
		break;
		case 1:
		for (var item = 0; item < cm.getPlayer().getCashWishList().size(); item++) {
			if (!cm.canHold(cm.getPlayer().getCashWishList().get(item), 1)) {
			cm.sendOk("장비 창의 저장 공간이 부족하여 아이템을 전부 받지 못하였습니다. 장비 창을 비운 후 엔피시를 다시 클릭하시면 남은 아이템이 정상적으로 수령됩니다.");
			break;
			}
			cm.gainItem(cm.getPlayer().getCashWishList().get(item), 1);
		}
		break;
		}
	cm.getPlayer().getCashWishList().clear();
	cm.sendOk("적용 완료되었습니다.");
	cm.dispose();
	break;
	}
}