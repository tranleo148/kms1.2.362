var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) 
        status++;
    else 
        status--;
    if (status == 0) {
	cm.sendAcceptDecline("수락을 누르시면 마네킹이 #b헤어룸에 뿅!#k 하고 생긴답니다~!\r\n지금 바로 마네킹을 사용하시겠어요?\r\n\r\n#r(사용 즉시 아이템은 인벤토리에서 사라집니다.)#k");
    } else if (status == 1) {
		if(!cm.haveItem(5680222, 1)) {
				cm.sendOk("errcode -31");
			cm.dispose();
			return;
		}
		if (cm.getKeyValue(26544, "sc") == -1) {
			if (cm.getKeyValue(26544, "c") < 3) {
				if (cm.getKeyValue(26544, "c") == -1) {
					cm.setKeyValue(26544, "c", "1");
					cm.setKeyValue(26544, "0", "0");
				} else {
					cm.setKeyValue(26544, "c", (cm.getKeyValue(26544, "c") + 1) + "");
					cm.setKeyValue(26544, (cm.getKeyValue(26544, "c") - 1) + "", "0");
				}
				cm.sendNext("짠~마네킹이 헤어룸에 보관되었어요.\r\n장비창(단축키E)의 #b캐시탭#k에 생긴 #fUI/UIWindow4.img/Equip/Cash/BtShop/normal/0# 을 통해 언제든 마네킹을 확인하실 수 있답니다.\r\n\r\n지금 바로 마네킹의 신비한 기능을 사용해 보세요~");
				cm.gainItem(5680222, -1);
				cm.dispose();
			} else {
				cm.sendOk("헤어룸에 빈공간이 없습니다.");
				cm.dispose();
			}
		} else {
			if (cm.getKeyValue(26544, "c") < cm.getKeyValue(26544, "sc")) {
				cm.setKeyValue(26544, "c", (cm.getKeyValue(26544, "c") + 1) + "");
				cm.setKeyValue(26544, (cm.getKeyValue(26544, "c") - 1) + "", "0");
				cm.sendNext("짠~마네킹이 헤어룸에 보관되었어요.\r\n장비창(단축키E)의 #b캐시탭#k에 생긴 #fUI/UIWindow4.img/Equip/Cash/BtShop/normal/0# 을 통해 언제든 마네킹을 확인하실 수 있답니다.\r\n\r\n지금 바로 마네킹의 신비한 기능을 사용해 보세요~");
				cm.gainItem(5680222, -1);
				cm.dispose();
			} else {
				cm.sendOk("헤어룸에 빈공간이 없습니다.");
				cm.dispose();
			}
		}
    }
}
