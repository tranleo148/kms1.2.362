var status;
var select;
function start() {
    status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode != 1)
        cm.dispose();
    else {
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
		cm.sendSimple("으음.. 하고 싶은게 있는가?\r\n\r\n#b#L0#사자왕의 메달과 정화토템 50개 교환#l\r\n#L1#사자왕의 메달과 정화토템 100개 교환#l");
	} else if (status == 1) {
		select = selection;
		if (selection == 0) {
			cm.sendYesNo("정화토템 50개와 사자왕의 메달 1개로 #b노블 사자왕의 메달#k 로 교환해 주겠네. 정말 교환하시겠는가?");
		} else if (selection == 1) {
			cm.sendYesNo("정화토템 100개와 사자왕의 메달 1개로 #b로얄 사자왕의 메달#k 로 교환해 주겠네. 정말 교환하시겠는가?");
		}
	} else if (status == 2) {
		if (select == 0) {
			if (cm.haveItem(4000630,50) && cm.haveItem(2430158,1) && cm.canHold(4310009)) {
				cm.gainItem(4000630,-50);
				cm.gainItem(2430158,-1);
				cm.gainItem(4310009,1);
				cm.sendOk("교환해 드렸다네. 이제 머트에게 반레온 세트 방어구를 구매할 수 있을걸세.");
				cm.dispose();
			} else {
				cm.sendOk("자네... 분명 필요한 아이템을 제대로 갖고 있는건가? 또는 인벤토리가 부족한건 아닌지 확인해 주시게.");
				cm.dispose();
			}
		} else if (select == 1) {
			if (cm.haveItem(4000630,100) && cm.haveItem(2430158,1) && cm.canHold(4310009)) {
				cm.gainItem(4000630,-100);
				cm.gainItem(2430158,-1);
				cm.gainItem(4310010,1);
				cm.sendOk("교환해 드렸다네. 이제 머트에게 반레온 세트 무기를 구매할 수 있을걸세.");
				cm.dispose();
			} else {
				cm.sendOk("자네... 분명 필요한 아이템을 제대로 갖고 있는건가? 또는 인벤토리가 부족한건 아닌지 확인해 주시게.");
				cm.dispose();
			}
		}
	}


else { 
		cm.dispose();
	}
    }
}