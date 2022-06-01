var status = -1;
var s1;
var core;

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
	cm.sendSimple("#e#dPixi 유저 #h 0##k#n님 어서오세요~ 언제나 환영합니다!#b\r\n저는 V매트릭스 관리자입니다. 원하시는 항목을 선택하세요.\r\n#L98#모든 스킬 코어 삭제 (미착용 코어 전용)\r\n#L99#모든 특수 코어 삭제 (미착용 코어 전용)\r\n#L0#모든 강화 코어 삭제 (미착용 코어 전용)\r\n#L1#코어 분해 일시불로 하기\r\n#L2#코어 이동 시스템\r\n#L3#매트릭스 시스템");
    } else if (status == 1) {
	s1 = selection;
	if (s1 == 3) {
		cm.dispose();
		cm.getClient().getSession().write(Packages.tools.packet.CField.UIPacket.openUI(1131));
	} else if (s1 == 0) {
		cm.dispose();
		for (i = 0; i < cm.getPlayer().getCore().size(); i++) {
			if (cm.getPlayer().getCore().get(i).getCoreid() / 10000000 >= 2 && cm.getPlayer().getCore().get(i).getCoreid() / 10000000 < 3 && cm.getPlayer().getCore().get(i).getState() == 1) {
				lv = cm.getPlayer().getCore().get(i).getLevel();
				count = ((3 * lv * lv) + (13 * lv) + 4) / 2;
				cm.getPlayer().setKeyValue(1477, "count", cm.getPlayer().getKeyValue(1477, "count") + count);
                		cm.getPlayer().getCore().remove(i);
			}
        	}
		cm.sendOk("완료되었습니다.");
		cm.getClient().getSession().write(Packages.tools.packet.CWvsContext.UpdateCore(cm.getPlayer()));
	} else if (s1 == 98) {
		cm.dispose();
		for (i = 0; i < cm.getPlayer().getCore().size(); i++) {
			if (cm.getPlayer().getCore().get(i).getCoreid() / 10000000 >= 1 && cm.getPlayer().getCore().get(i).getCoreid() / 10000000 < 2 && cm.getPlayer().getCore().get(i).getState() == 1) {
				lv = cm.getPlayer().getCore().get(i).getLevel();
				count = 2 * lv * (lv + 19);
				cm.getPlayer().setKeyValue(1477, "count", cm.getPlayer().getKeyValue(1477, "count") + count);
                		cm.getPlayer().getCore().remove(i);
			}
        	}
		cm.sendOk("완료되었습니다.");
		cm.getClient().getSession().write(Packages.tools.packet.CWvsContext.UpdateCore(cm.getPlayer()));
	} else if (s1 == 99) {
		cm.dispose();
		for (i = 0; i < cm.getPlayer().getCore().size(); i++) {
			if (cm.getPlayer().getCore().get(i).getCoreid() / 10000000 >= 3 && cm.getPlayer().getCore().get(i).getState() == 1) {
				lv = cm.getPlayer().getCore().get(i).getLevel();
				cm.getPlayer().setKeyValue(1477, "count", cm.getPlayer().getKeyValue(1477, "count") + 50);
                		cm.getPlayer().getCore().remove(i);
			}
        	}
		cm.sendOk("완료되었습니다.");
		cm.getClient().getSession().write(Packages.tools.packet.CWvsContext.UpdateCore(cm.getPlayer()));
	} else {
		말 = "#d" + (s1 == 0 ? "강화할" : s1 == 1 ? "분해할" : "다른 캐릭터에게 옮길") + " 코어를 선택해주세요.\r\n특수 코어 및 장착중인 코어는 표시되지 않습니다.#b\r\n";
		for (i = 0; i < cm.getPlayer().getCore().size(); i++) {
			if (cm.getPlayer().getCore().get(i).getCoreid() < 30000000 && cm.getPlayer().getCore().get(i).getState() == 1) {
                		말+= "#L"+ i +"##q" + cm.getPlayer().getCore().get(i).getSkill1() + "#\r\n";
			}
        	}
		cm.sendSimple(말);
	}
    } else if (status == 2) {
	core = selection;
	if (s1 == 2) {
		말 = "#d옮길 캐릭터를 선택해주세요. 자신은 표시되지 않습니다.#b\r\n";
		for (i = 0; i < cm.getClient().loadCharacters(0).size(); i++) {
		chr = cm.getClient().loadCharacters(0).get(i);
			if (!cm.getPlayer().getName().equals(chr.getName())) {
				말 += "#L" + chr.getId() + "#" + chr.getName() + "\r\n";
			}
		}
		cm.sendSimple(말);
	} else if (s1 == 1) {
		cm.sendYesNo("해당 코어와 같은 종류의 강화 코어 및 스킬 코어는 전부 삭제됩니다. 계속하시겠습니까? \r\n(자신이 착용중인 코어는 사용되지 않습니다.)");
	} else if (s1 == 0) {
		cm.sendYesNo("해당 코어와 같은 종류의 강화 코어 및 스킬 코어는 전부 강화 재료로 사용됩니다. 계속하시겠습니까? \r\n(자신이 착용중인 코어는 사용되지 않습니다.)");
	}
    } else if (status == 3) {
	cm.dispose();
	rvcore = cm.getPlayer().getCore().get(core);
	if (s1 == 2) {
		cm.getPlayer().getCore().get(core).setCharid(selection);
	} else if (s1 == 1) {
		for (i = 0; i < cm.getPlayer().getCore().size(); i++) {
			if (cm.getPlayer().getCore().get(i).getCoreid() == rvcore.getCoreid() && cm.getPlayer().getCore().get(i).getState() == 1) {
				lv = cm.getPlayer().getCore().get(i).getLevel();
				count = ((3 * lv * lv) + (13 * lv) + 4) / 2;
				cm.getPlayer().setKeyValue(1477, "count", cm.getPlayer().getKeyValue(1477, "count") + count);
                		cm.getPlayer().getCore().remove(i);
			}
        	}
		lv = rvcore.getLevel();
		count = ((3 * lv * lv) + (13 * lv) + 4) / 2;
		cm.getPlayer().setKeyValue(1477, "count", cm.getPlayer().getKeyValue(1477, "count") + count);
//		cm.getPlayer().getCore().remove(core);
	}
	cm.sendOk("완료되었습니다.");
	cm.getClient().getSession().write(Packages.tools.packet.CWvsContext.UpdateCore(cm.getPlayer()));
    }
}