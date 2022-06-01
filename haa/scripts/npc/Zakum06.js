var status = -1;

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
	cm.sendSimple("#e#dPixi 유저 #h 0##k#n님 어서오세요~ 언제나 환영합니다!#b\r\n\r\n#L0#불의 눈 아이템 받기\r\n#L1#마을로 돌아갈래요.");
    } else if (status == 1) {
	cm.dispose();
	if (selection == 0) {
		cm.gainItem(4001017, 1);
		cm.sendOk("지급되었습니다.");
	} else {
		cm.setDeathcount(0);
		cm.warp(1000000, 0);
	}
    }
}

