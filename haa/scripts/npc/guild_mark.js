importPackage(Packages.client);

var status = -1, select;
var skill = new Array(/*91000009, */91000012, 91000013, 91000014/*, 91001019*/);
var nob = new Array(91001022, 91001023, 91001024);

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
	if (cm.getPlayer().getGuild() == null) {
		cm.dispose();
		cm.sendOk("가입하신 길드가 존재하지 않습니다.");
	} else if (cm.getPlayer().getGuild().getLeaderId() != cm.getPlayer().getId()) {
		cm.dispose();
		cm.sendOk("길드장 만이 길드 스킬에 관여할 수 있습니다.");
        } else {
		cm.sendSimple("#d" + cm.getPlayer().getGuild().getName() + "#k 길드의 #b" + cm.getPlayer().getGuild().getLeaderName() + "#k님 안녕하세요!\r\n현재 보유하고 계신 GP는 " + cm.getGP() + " 입니다. 원하시는 항목을 골라주세요.\r\n" +
		"#L0#일반 길드 스킬 레벨을 올리고 싶어요.\r\n#L1#일반 길드 스킬을 사용할래요.\r\n#L2#노블레스 길드 스킬 레벨을 올리고 싶어요.\r\n#L3#노블레스 길드 스킬을 사용할래요.");
	}
    } else if (status == 1) {
    select = selection;
	if (select == 0) {
		txt = "원하시는 스킬을 골라주세요. 모든 스킬에는 1000GP가 소모됩니다.\r\n";
		for (i = 0; i < skill.length; i++) {
			txt += "#b#L" + i + "##q" + skill[i] + "# : #dLv." + cm.getPlayer().getGuild().getSkillLevel(skill[i]) + "#k\r\n";
		}
		cm.sendSimple(txt);
	} else if (select == 1) {
//		txt = "사용하실 스킬을 골라주세요. 모든 스킬 사용에는 3000GP가 소모됩니다.\r\n";
//		txt += "#b#L0##q" + skill[0] + "# : #dLv." + cm.getPlayer().getGuild().getSkillLevel(skill[0]) + "#k\r\n";
//		txt += "#b#L4##q" + skill[4] + "# : #dLv." + cm.getPlayer().getGuild().getSkillLevel(skill[4]) + "#k\r\n";
		cm.sendOk("준비중입니다.");
		cm.dispose();
	} else if (select == 2) {
		txt = "원하시는 스킬을 골라주세요. 모든 스킬에는 5000GP가 소모됩니다.\r\n";
		for (i = 0; i < nob.length; i++) {
			txt += "#b#L" + i + "##q" + nob[i] + "# : #dLv." + cm.getPlayer().getGuild().getSkillLevel(nob[i]) + "#k\r\n";
		}
		cm.sendSimple(txt);
	} else if (select == 3) {
		txt = "사용하실 스킬을 골라주세요.\r\n";
		for (i = 0; i < nob.length; i++) {
			txt += "#b#L" + i + "##q" + nob[i] + "# : #dLv." + cm.getPlayer().getGuild().getSkillLevel(nob[i]) + "#k\r\n";
		}
		cm.sendSimple(txt);
	}
    } else if (status == 2) {
	cm.dispose();
	if (select == 0) {
		if (cm.getGP() < 1000) {
			cm.sendOk("GP가 부족합니다.");
		} else {
			cm.gainGP(-1000);
			cm.getPlayer().getGuild().purchaseSkill(skill[selection], cm.getPlayer().getName(), cm.getPlayer().getId());
			cm.sendOk("#q" + skill[selection] + "# 스킬을 " + cm.getPlayer().getGuild().getSkillLevel(skill[selection]) + "레벨로 레벨업 하였습니다.");
		}
	} else if (select == 1) {
		if (cm.getGP() < 3000) {
			cm.sendOk("GP가 부족합니다.");
		} else {
			cm.gainGP(-3000);
			cm.sendOk("#q" + skill[selection] + "# 스킬을 사용하였습니다.");
			if (selection == 0) {
				cm.gainItem(2432290, cm.getPlayer().getGuild().getSkillLevel(skill[selection]));
			} else {
				cm.gainItem(5077000, cm.getPlayer().getGuild().getSkillLevel(skill[selection]));
			}
		}
	} else if (select == 2) {
		if (cm.getGP() < 5000) {
			cm.sendOk("GP가 부족합니다.");
		} else {
			cm.gainGP(-5000);
			cm.getPlayer().getGuild().purchaseSkill(nob[selection], cm.getPlayer().getName(), cm.getPlayer().getId());
			cm.sendOk("#q" + nob[selection] + "# 스킬을 " + cm.getPlayer().getGuild().getSkillLevel(nob[selection]) + "레벨로 레벨업 하였습니다.");
		}
	} else if (select == 3) {
		SkillFactory.getSkill(nob[selection]).getEffect(cm.getPlayer().getGuild().getSkillLevel(nob[selection])).applyTo(cm.getPlayer());
		cm.sendOk("#q" + nob[selection] + "# 스킬을 사용하였습니다.");
	}
    }
}