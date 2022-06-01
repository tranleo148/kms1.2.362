var status = -1;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	datecheck = true;

	if (mode == -1 || mode == 0) {
		cm.dispose();
		return;
	}
	if (mode == 1) {
		status++;
	}

	if (status == 0) {
		cm.sendSimple("#e<아스완 해방전>#n\\r\n여전히 아스완 지역을 배회하고 있는 힐라의 잔당들을 소탕하시겠습니까?\r\n\r\n\r\n" +
			"#L0# 힐라의 잔당을 소탕한다.\r\n" +
			"#L1# 힐라를 직접 처치한다. (레벨 120이상)\r\n");
	} else if (status == 1) {
		if (selection == 0) {
			cm.getPlayer().dropMessage(5, "아스완 해방전은 임시 소강 상태입니다. 다음 해방전 시즌까지 기다려 주세요.");
			cm.dispose();
		} else {
			if (cm.getPlayer().getLevel() < 120) {
				cm.sendOk("힐라의 탑에 입장하기 위한 레벨이 부족합니다. <보스: 힐라>는 레벨 120이상만 도전하실 수 있습니다.");
				cm.dispose();
			} else {
				cm.sendNext("힐라의 탑 입구로 보내드리겠습니다. 힐라를 꼭 물리쳐주세요.");
			}
		}
	} else if (status == 2) {
		cm.warp(262030000);
		cm.dispose();
		return;
	}
}