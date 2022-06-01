var status = -1;
var item;
var 추옵;

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
	cm.dispose();
	if (cm.getPlayer().isGM()) {
		cm.warp(910130102,4);
	}
/*	text = "환생의 불꽃 추가옵션 옵션 설정 엔피시입니다. 아이템을 골라주세요.\r\n";
	for (i = 0; i < cm.getInventory(1).getSlotLimit(); i++) {
           if (cm.getInventory(1).getItem(i) != null) {
                text += "#L "+i+"# #i"+cm.getInventory(1).getItem(i).getItemId()+"# #b#z"+cm.getInventory(1).getItem(i).getItemId()+"#\r\n";
           }
        }
	cm.sendSimple(text);*/
    } else if (status == 1) {
	item = cm.getInventory(1).getItem(selection);
	cm.sendSimple("원하시는 추옵을 선택하세요.\r\n#L0#STR\r\n#L1#DEX\r\n#L2#INT\r\n#L3#LUK\r\n#L4#STR + DEX\r\n#L5#STR + INT\r\n#L6#STR + LUK\r\n#L7#DEX + INT\r\n#L8#DEX + LUK\r\n#L9#INT + LUK\r\n#L10#HP\r\n#L11#MP\r\n#L12#WDEF\r\n#L13#MDEF\r\n#L14#ACC\r\n#L15#AVOID\r\n#L16#HANDS\r\n#L17#PAD\r\n#L18#MAD\r\n#L19#SPEED\r\n#L20#JUMP\r\n#L21#BOSSDMG\r\n#L22#REQLEVEL\r\n#L23#DAMAGE\r\n#L24#ALLSTAT");
    } else if (status == 2) {
	추옵 = selection;
	cm.sendGetNumber("추옵 단계 설정 1 ~ 9", 1, 1, 9);
    } else if (status == 3) {
	cm.dispose();
	for (i = 0; i <= 24; i++) {
		item.setFireStat(i, 0);
	}
	item.setFireStat(추옵, selection);
	cm.getPlayer().forceReAddItem(item, Packages.client.inventory.MapleInventoryType.getByType(1));
	cm.sendOk("추옵 설정 완료.");
    }
}