importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server);
importPackage(Packages.scripting);
importPackage(Packages.client);
 
var status = -1;
var index = 0;
var skillid = 80003036;
var sel = 0;
var skillname = "";
var needpoint = 0;
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
		d = status;
		status++;
	}

	if (status == 0) {
		index = cm.getPlayer().getSkillCustomValue0(501378);
		skillid = skillid + index;
		if (SkillFactory.getSkillName(skillid) != null) {
			sel = 1;
			skillname = SkillFactory.getSkillName(skillid).replace('블루밍', '').replace(' 스킬 : ', '');
			needpoint = cm.getPlayer().getSkillLevel(skillid) + 1;
			if (skillname.equals("보스 데미지")) {
				skillname = "보스 몬스터 데미지";
			}
			if (needpoint > cm.getPlayer().getKeyValue(501378, "sp")) {
				need = needpoint - cm.getPlayer().getKeyValue(501378, "sp");
				cm.sendOkS("힝..!\r\n#e#r"+need+"#k 포인트#n가 부족해서 #e#r"+skillname+" 증가#n#k 스킬을 \r\n올려드릴 수가 없네여...", 4, 9062508);
				cm.dispose();
				return;
			}
			txt = "#b#e[" + skillname + " 증가]#n#k 스킬을 올리시겠어여?\r\n\r\n";
			txt += "- 필요한 스킬 포인트 : #r#e" + needpoint + "#n#k\r\n"
			txt += "- 보유한 스킬 포인트 : #b#e" + cm.getPlayer().getKeyValue(501378, "sp") + "#n#k\r\n\r\n\r\n"
			txt += "#r#e#fs14#※ 주의 : 사용한 블루밍 스킬 포인트는\r\n다시 되돌릴 수 없습니다.#n#k";
			cm.sendYesNoS(txt, 4, 9062508);
		} else {
			cm.sendOkS("오류가 발생 하였습니다.", 4, 9062508);
			cm.dispose();
		}
	} else if (status == 1) {
		if (sel == 1) {
			//스킬 찍기
			if (cm.getClient().getKeyValue("BloomingSkill") == null) {
				cm.getClient().setKeyValue("BloomingSkill", "0000000000");
			}
			var str = cm.getClient().getKeyValue("BloomingSkill");
			var ab = str.split("");
			var fi = "";
			cm.getPlayer().changeSkillLevel(skillid, cm.getPlayer().getSkillLevel(skillid) + 1, 3);
			ab[index] = cm.getPlayer().getSkillLevel(skillid);
			for (var a = 0; a < ab.length; a++) {
				fi += ab[a];
			}
			cm.getPlayer().setKeyValue(501378, "sp", (cm.getPlayer().getKeyValue(501378, "sp") - needpoint) + "");
			cm.getPlayer().setKeyValue(501378, index + "", (cm.getPlayer().getKeyValue(501378, index + "") + 1) + "");
			cm.getClient().setKeyValue("BloomingSkill", fi);
			cm.getClient().setKeyValue("BloomingSkillPoint", cm.getPlayer().getKeyValue(501378, "sp")+"");
			cm.getClient().send(CField.UIPacket.detailShowInfo("블루밍 축복을 받아 " + skillname + " 증가 스킬이 " + cm.getPlayer().getSkillLevel(skillid) + " 레벨이 되었습니다!", 3, 20, 20));
			cm.dispose();
		}
	}
}