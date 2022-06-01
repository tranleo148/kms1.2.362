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
	cm.askCustomMixHairAndProb("원하시는 값을 선택해주세요.");
    } else if (status == 1) {
	cm.cm.sendOkS("적용되었습니다.",0x24);
	cm.dispose();
    }
}

