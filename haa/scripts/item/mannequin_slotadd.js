var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 1) 
        status++;
    else 
        status--;
    if (status == 0) {
		cm.sendAcceptDecline("수락을 누르시면 헤어룸 슬롯 1칸을 확장하실 수 있어요.\r\n\r\n#e지금 바로 헤어룸 슬롯 확장권 1개를 사용하여 헤어룸 슬롯 1칸을 확장 하시겠어요?#n\r\n\r\n#r(헤어룸 슬롯은 무료로 제공된 3개를 포함해 최대 5개까지 이용할 수 있습니다.)#k");
    } else if (status == 1) {
		if(!cm.haveItem(5680531, 1)) {
			cm.sendOk("errcode -31");
			cm.dispose();
			return;
		}
		if (cm.getKeyValue(26544, "sc") < 5) {
			if (cm.getKeyValue(26544, "sc") == -1) {
				cm.setKeyValue(26544, "sc", "4");
			} else {
				cm.setKeyValue(26544, "sc", (cm.getKeyValue(26544, "sc") + 1) + "");
			}
			cm.sendOk("헤어룸 슬롯이 1칸 확장되어 마네킹을 추가로 보관할 수 있게 되었습니다~!");
			cm.gainItem(5680531, -1);
		} else {
			cm.sendOk("헤어룸 슬롯이 이미 최대입니다.");
		}
		cm.dispose();
    }
}
