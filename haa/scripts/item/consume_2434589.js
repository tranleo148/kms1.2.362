var enter = "\r\n";
var seld = -1;
var seld2 = -1;
var s1, s2;

var need = 2434589;

var key = "cygnus_1";

var items = [
	[1003172, 1102275, 1082295, 1052314, 1072485, 1152108],
	[1003174, 1102277, 1082297, 1052316, 1072487, 1152111],
	[1003175, 1102278, 1082298, 1052317, 1072488, 1152112],
	[1003173, 1102276, 1082296, 1052315, 1072486, 1152110],
	[1003176, 1102279, 1082299, 1052318, 1072489, 1152113]
]

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, sel) {
	if (mode == -1) {
		cm.dispose();
	}
	if (mode == 0) {
		cm.dispose();
		return;
	}
	if (mode == 1) {
		status++;
	}
	if (status == 0) {
        		getClass = getJob(cm.getPlayer().getJob());

    		if (cm.getPlayer().getKeyValue(20190211, key) < 0)
        			cm.getPlayer().setKeyValue(20190211, key, 0);

        		if (!cm.canHold(items[0][0])) {
            		cm.sendOk("장비창을 1칸 이상 비운 뒤 다시 사용해 주세요.");
            		cm.dispose();
            		return;
        		}
        		if (cm.itemQuantity(need) < 5) {
            		cm.sendOk("조각이 모자랍니다");
            		cm.dispose();
            		return;
        		}
		var msg = "#b#e찬스타임 게이지:#r"+cm.getPlayer().getKeyValue(20190211, key)+"%#k#n"+enter+enter;

        		if (cm.getPlayer().getKeyValue(20190211, key) >= 100) {
            		msg+= "#r#e찬스타임 발동!#k#n"+enter;
            		msg+= "#b#e더 좋은 추가옵션#k#n이 나올 확률이 증가합니다."+enter+enter;
        		}

        		msg += "현재 직업이 착용 가능한 장비를 우선 추천해 드립니다.\r\n받으실 방어구를 선택해 주세요.\r\n";

		for (i = 0; i < items[getClass].length; i++)
        			msg += "#L" + i + "# #i" + items[getClass][i] + "# #b#z" + items[getClass][i] + "##k#l"+enter;

        		msg += "\r\n#L100##b전체 아이템 리스트를 본다.#k#l\r\n";
        		msg += "#L101##b사용 취소#k#l";
        		cm.sendSimple(msg);
	} else if (status == 1) {
		seld = sel;
		if (seld == 100) {
			var msg = "#b#e찬스타임 게이지:#r"+cm.getPlayer().getKeyValue(20190211, key)+"%#k#n"+enter+enter;
        			if (cm.getPlayer().getKeyValue(20190211, key) >= 100) {
            			msg+= "#r#e찬스타임 발동!#k#n"+enter;
            			msg+= "#b#e더 좋은 추가옵션#k#n이 나올 확률이 증가합니다."+enter+enter;
        			}
			for (i2 = 0; i2 < items.length; i2++) {
				for (i = 0; i < items[i2].length; i++)
        					msg += "#L" + (((i2 + 1) * 100) + i) + "# #i" + items[i2][i] + "# #b#z" + items[i2][i] + "##k#l"+enter;
			}

        			msg += "\r\n#L100##b전체 아이템 리스트를 본다.#k#l\r\n"
        			msg += "#L101##b사용 취소#k#l"
        			cm.sendSimple(msg);
		} else if (seld == 101) {
			cm.dispose();
		} else if (seld < 100) {
        			var msg = "받고싶은 장비가 이 장비가 확실한가요?\r\n"
        			msg += "#i"+items[getClass][seld]+"# #z"+items[getClass][seld]+"#\r\n\r\n"
        			msg += "#r#e※주의※#k#n\r\n"
        			msg += "한 번 조각을 아이템으로 교환하면 #r5개의 조각이 차감#k되며, 다시 다른 아이템으로 교환 할 수 없습니다.\r\n\r\n"
        			msg += "#L0##b네 맞습니다.\r\n"
        			msg += "#L1##b아닙니다. 다시 선택하겠습니다.";
        			cm.sendSimple(msg);
		}
	} else if (status == 2) {
		seld2 = sel;
		if (seld == 100) {
			s1 = Math.floor(seld2 / 100) - 1;
			s2 = seld2 % 100;
        			var msg = "받고싶은 장비가 이 장비가 확실한가요?\r\n"
        			msg += "#i"+items[s1][s2]+"# #z"+items[s1][s2]+"#\r\n\r\n"
        			msg += "#r#e※주의※#k#n\r\n"
        			msg += "한 번 조각을 아이템으로 교환하면 #r5개의 조각이 차감#k되며, 다시 다른 아이템으로 교환 할 수 없습니다.\r\n\r\n"
        			msg += "#L0##b네 맞습니다.\r\n"
        			msg += "#L1##b아닙니다. 다시 선택하겠습니다.";
        			cm.sendSimple(msg);
		} else if (seld < 100) {
			if (seld2 == 1) {
            			cm.sendOk("다시 조각을 사용해주세요.");
				cm.dispose();
				return;
			}
        			if (cm.getPlayer().getKeyValue(20190211, key) == 100) cm.getPlayer().setKeyValue(20190211, key, 0);

        			cm.getPlayer().setKeyValue(20190211, key, cm.getPlayer().getKeyValue(20190211, key) + Packages.server.Randomizer.rand(7,11));

        			if (cm.getPlayer().getKeyValue(20190211, key) >= 100) cm.getPlayer().setKeyValue(20190211, key, 100);

        			cm.gainItem(items[getClass][seld], 1);
        			cm.gainItem(need, -5);
        			cm.dispose();
		}
	} else if (status == 3) {
		if (seld == 100) {
			if (sel == 1) {
            			cm.sendOk("다시 조각을 사용해주세요.");
				cm.dispose();
				return;
			}
        			if (cm.getPlayer().getKeyValue(20190211, key) == 100) cm.getPlayer().setKeyValue(20190211, key, 0);

        			cm.getPlayer().setKeyValue(20190211, key, cm.getPlayer().getKeyValue(20190211, key) + Packages.server.Randomizer.rand(7,11));

        			if (cm.getPlayer().getKeyValue(20190211, key) >= 100) cm.getPlayer().setKeyValue(20190211, key, 100);

        			cm.gainItem(items[s1][s2], 1);
        			cm.gainItem(need, -5);
        			cm.dispose();
		}
	}
}

function getJob(job) {
	var ret = -1;
	if (Packages.constants.GameConstants.isWarrior(job))
		ret = 0;
	else if (Packages.constants.GameConstants.isArcher(job))
		ret = 1;
	else if (Packages.constants.GameConstants.isThief(job))
		ret = 2;
	else if (Packages.constants.GameConstants.isMagician(job))
		ret = 3;
	else if (Packages.constants.GameConstants.isPirate(job))
		ret = 4;
	return ret;
}