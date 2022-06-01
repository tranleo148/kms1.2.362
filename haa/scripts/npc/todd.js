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
        status--;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
	cm.sendSimple("토드의 망치 설명서입니다. 원하시는 항목을 선택해주세요.\r\n#b#L0#토드의 망치가 무엇이죠?\r\n#L1#전승 가능한 옵션\r\n#L2#그만두기");
    } else if (status == 1) {
	status = -1;
	cm.dispose();
	if (selection == 0) {
		cm.sendNext("한 아이템의 옵션을 다른 아이템으로 이동시키는 것을 말합니다. 이동된 옵션을 가졌던 아이템은 사라집니다.");
	} else if (selection == 1) {
		cm.sendNext("#d전승되는 옵션 : 스타포스 강화, 잠재능력 (에픽 등급으로 강등) #k \r\n#b유지되는 옵션 : 추가 옵션(올스탯, 총데미지, 보스 데미지), 가위 사용 횟수\r\n남은 주문서 횟수 : 자동 100% 주문서 사용, 전승되는 잠재옵션이 #b에픽 등급#k에 존재하지 않을 경우, 새로운 잠재능력으로 대체.");
	}
    }
}

