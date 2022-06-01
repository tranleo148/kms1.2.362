var status;

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
        status--;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
	if (cm.getKeyValue(10, "muto") == -1) {
		cm.setKeyValue(10, "muto", "0");
	}
	if (cm.getPlayer().getParty() == null || cm.getPlayer().mutoEnterTime == 0) {
		cm.sendNext("마을로 돌려보내드리겠습니다.");
	} else {
		cm.sendSimple("#b#e<배고픈 무토>#n#k\r\n #b무토#k를 도와주고 오셨군요? 감사해요!\r\n#b#L0# <배고픈 무토>보상을 받는다.#l\r\n#b#L2# 보상을 받지 않고 돌아간다.#l");
	}
    } else if (status == 1) {
	if (selection == 0) {
		cm.sendNextPrev("자! 여기 제가 준비한 선물을 받아주세요.\r\n#b#e클리어 등급: #n#k#r#eS#n#k급\r\n#i1712002##b#e#t1712002:##n#k #e10개#n");
	} else {
		cm.dispose();
		cm.warp(450002023);
	}
    } else if (status == 2) {
		cm.dispose();
		cm.warp(450002023);
		cm.setKeyValue(10, "muto", cm.getKeyValue(10, "muto") + 1);
		cm.gainItem(1712002, 1);
		cm.gainItem(1712002, 1);
		cm.gainItem(1712002, 1);
		cm.gainItem(1712002, 1);
		cm.gainItem(1712002, 1);
		cm.gainItem(1712002, 1);
		cm.gainItem(1712002, 1);
		cm.gainItem(1712002, 1);
		cm.gainItem(1712002, 1);
		cm.gainItem(1712002, 1);
    }
}