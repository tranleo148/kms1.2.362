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
        status --;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
	if (cm.getPlayer().getParty() == null) {
		cm.sendOkS("파티를 맺은 뒤 입장하자.", 2);
		cm.dispose();
	} else if (!cm.isLeader()) {
		cm.sendOkS("파티장만이 입장을 시도할 수 있다.", 2);
		cm.dispose();
	} else {
		cm.sendSimpleS("난이도를 선택해주세요.\r\n#b#L0#노말 모드\r\n#L1#하드 모드", 2);
	}
    } else if (status == 1) {
	
    }
}