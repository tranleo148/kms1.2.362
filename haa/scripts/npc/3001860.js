


/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    블랙 에 의해 만들어 졌습니다.

    엔피시아이디 : 3001860

    엔피시 이름 : 세드릭

    엔피시가 있는 맵 : The Black : Night Festival (100000000)

    엔피시 설명 : MISSINGNO


*/

importPackage(Packages.server);
importPackage(Packages.client);

검정 = "#fc0xFF191919#"
레드 = "#fc0xFFF15F5F#"
블루 = "#fc0xFF6B66FF#"
남색 = "#fc0xFF2457BD#"
노랑 = "#fc0xFF998A00#"
개수 = 0;
강화갯수 = 0;
강화 = -1;
구매 = 0;

var skillid = 0;
var skillname = "";
var skilltesc = "";
var level = 0;

var status = -1;

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
        말 = "#fs11#" + 검정 + "#h #! 크크크 자네도 소식을듣고 여기까지 찾아온거군, 그렇다네! #r블랙 서버#k" + 검정 + "에선 신비한 힘이 담긴 특별한 스킬들이 있다네! \r\n";
        말 += "#fc0xFFD5D5D5#────────────────────────────#k#fs11\r\n";
        말 += "#L0##s80000587# #b아케인 포스 스킬#k을 구매하고 싶습니다.#k#l\r\n";
        말 += "#L1##s80000577# #b블랙 가호 스킬#k을 구매하고 싶습니다.#k#l\r\n";
        말 += "#L2##s80000578# #b무극 스킬#k을 구매하고 싶습니다.#k#l\r\n";
        말 += "#L3##s80000588# #b탐욕스러운자 스킬#k을 구매하고 싶습니다.#k#l\r\n";
		말 += "#L4##s80001829# #b비연 스킬#k을 구매하고 싶습니다.#k#l\r\n\r\n";
        cm.sendSimpleS(말, 0x04, 9401232);
    } else if (status == 1) {

        if (selection == 0) {
            skillid = 80000587;
            level = 10;
            skillname = "아케인 포스";
            skilltesc = "에르다의 흐름을 느끼게 되어 " + 남색 + "아케인 포스#k가 증가한다.";
        } else if (selection == 1) {
            skillid = 80000577;
            level = 10;
            skillname = "블랙 가호";
            skilltesc = "특별한 힘을 지니고 있는 " + 남색 + "블랙 가호#k다.";
        } else if (selection == 2) {
            skillid = 80000578;
            level = 5;
            skillname = "무극";
            skilltesc = "끝없이 성장하는 특별한 힘을 발휘한다. 성장할수록 높은 효율을 자랑하며 끝을 도달하기 힘들다.";
		} else if (selection == 4) {
            skillid = 80001829;
            level = 5;
            skillname = "비연";
            skilltesc = "검으로 뛰어오름!";
        } else if (selection == 3) {
            skillid = 80000588;
            level = 10;
            skillname = "탐욕스러운자";
            skilltesc = "탐욕에 눈이 멀어 보다 더 많은 이득을 추구하게 되었다.";
        }
        nextskillv = cm.getPlayer().getSkillLevel(skillid);
        nextskillv++;

        말 = "#fs11##s" + skillid + "# " + 노랑 + "" + skillname + "#k " + 검정 + "스킬 정보라네!\r\n"
        말 += "#fc0xFFD5D5D5#────────────────────────────#k\r\n";
        말 += "" + 검정 + "" + skilltesc + "\r\n\r\n"
        말 += "" + 검정 + "─ 스킬 효과 ─\r\n"
        말 += "" + 검정 + "현재 레벨 : Lv.#r" + (nextskillv - 1) + "#k\r\n"
        말 += "" + 검정 + "마스터 레벨 : Lv.#r" + level + "#k\r\n\r\n"
        eff = SkillFactory.getSkill(skillid).getEffect(cm.getPlayer().getSkillLevel(nextskillv));
        if (eff == null) {
            cm.sendOk("오류 발생");
            cm.dispose();
            return;
        }
        말 += "#e다음 스킬레벨 효과#n\r\n"
        if (skillid == 80000587) {
            말 += "" + 검정 + "" + 남색 + "아케인 포스 + " + eff.getArcX() + "#k " + 검정 + "증가#k\r\n\r\n"
        } else if (skillid == 80000577) {
            말 += "" + 검정 + "" + 남색 + "올스탯 + " + eff.getStrX() + "#k " + 검정 + "증가#k\r\n"
            말 += "" + 검정 + "" + 남색 + "공격력/마력 + " + eff.getMagicX() + "#k " + 검정 + "증가#k\r\n"
            말 += "" + 검정 + "" + 남색 + "일반 몬스터 공격 시 데미지 + " + eff.getNbdR() + "%#k " + 검정 + "증가#k\r\n"
            말 += "" + 검정 + "" + 남색 + "보스 몬스터 공격 시 데미지 + " + eff.getBdR() + "%#k " + 검정 + "증가#k\r\n"
            말 += "" + 검정 + "" + 남색 + "몬스터 방어율 무시 + " + eff.getIgnoreMob() + "%#k " + 검정 + "증가#k\r\n"
            말 += "" + 검정 + "" + 남색 + "경험치 획득량 + " + eff.getEXPRate() + "%#k " + 검정 + "증가#k\r\n\r\n"
        } else if (skillid == 80000578) {
            말 += "" + 검정 + "" + 남색 + "크리티컬 데미지 + " + eff.getCriticalDamage() + "% " + 검정 + "증가#k\r\n\r\n"
        } else if (skillid == 80000588) {
            말 += "" + 검정 + "" + 남색 + "아이템 드랍률 + " + eff.getDropR() + "% " + 검정 + "증가#k\r\n\r\n"
		} else if (skillid == 80001829) {
            말 += "" + 검정 + "" + 남색 + "아이템 드랍률 + " + eff.getDropR() + "% " + 검정 + "증가#k\r\n\r\n"	
        }
        말 += "" + 블루 + "#L100#스킬을 업그레이드 하고 싶습니다.#l\r\n"
        말 += "" + 레드 + "#L" + skillid + "#스킬을 구매하겠습니다.#l"

        cm.sendSimpleS(말, 0x04, 9401232);

    } else if (status == 2) {
        if (skillid != 80000587 && skillid != 80000577 && skillid != 80000578 && skillid != 80000588 && skillid != 80001829) {
            cm.sendOk("오류 발생");
            cm.dispose();
            return;
        }
        if (selection == 100) {
            강화 = 0
            강화갯수 = skillid == 80000587 ? 1500 : skillid == 80000577 ? 4000 : skillid == 80000578 ? 15000 : skillid == 80000588 ? 6500 : skillid == 80001829 ? 99999999 : "";
            스킬이름 = skillid == 80000587 ? "아케인 포스" : skillid == 80000577 ? "블랙 가호" : skillid == 80000578 ? "무극" : skillid == 80000588 ? "탐욕스러운자" : skillid == 80001829 ? "비연" :"";
            if (cm.getPlayer().getSkillLevel(skillid) <= 0) {
                cm.sendOkS("#fs11#" + 검정 + "자네는 " + 스킬이름 + " 스킬이 없는거 같군, 스킬을 구매해야 업그레이드가 가능하다네.", 0x04, 9401232);
                cm.dispose();
            } else {
                말 = "#fs11# " + 노랑 + "#s" + skillid + "# " + 스킬이름 + "#k" + 검정 + " 스킬을 정말 강화하겠나?\r\n";
                말 += "#fc0xFFD5D5D5#────────────────────────────#k\r\n";
                말 += "" + 남색 + "#i4310012# #z4310012# #r" + 강화갯수 + "" + 검정 + "개\r\n\r\n\r\n"
                말 += "#r'예'#k" + 검정 + "를 누를 시, 위 아이템이 차감되며 스킬 레벨이 올라갑니다."
                cm.sendYesNoS(말, 0x04, 9401232);
            }
        } else {
            구매 = 1
            개수 = selection == 80000587 ? 30 : selection == 80000577 ? 300 : selection == 80000578 ? 250 : selection == 80000588 ? 100 : selection == 80001829 ? 1 : "";
            sel = selection == 80000587 ? "아케인 포스" : selection == 80000577 ? "블랙 가호" : selection == 80000578 ? "무극" : selection == 80000588 ? "탐욕스러운자" : selection == 80001829 ? "비연" : "";
            말 = "#fs11#" + 노랑 + "#s" + selection + "# " + sel + "#k 스킬" + 검정 + "을 정말로 구매하겠나?\r\n"
            말 += "#fc0xFFD5D5D5#────────────────────────────#k\r\n";
            말 += "" + 남색 + "#i4310012# #z4310012# #r" + 개수 + "" + 검정 + "개\r\n\r\n\r\n"
            말 += "#r'예'#k" + 검정 + "를 누를 시, 위 아이템이 차감됩니다."
            cm.sendYesNoS(말, 0x04, 9401232);
        }
    } else if (status == 3) {
        if (구매 == 1) {
            if (cm.getPlayer().getSkillLevel(skillid) > 0) {
                cm.sendOkS("#fs11#" + 검정 + "이미 스킬을 보유하고 있는거 같네.", 0x04, 9401232);
                cm.dispose();
                return;
            }
            if (!cm.haveItem(4310012, 개수)) {
                cm.sendOkS("#fs11#" + 검정 + "구매에 필요한 코인이 부족한거 같네.", 0x04, 9401232);
                cm.dispose();
                return;
            }
            //if (cm.getClient().getQuestStatus(50001) == 1 || cm.getPlayer().getQuestStatus(50001) == 1) {
                if (cm.getClient().getQuestStatus(50001) == 1) {
                cm.getClient().setCustomKeyValue(50001, "1", "1");
            }
            cm.teachSkill(skillid, 1, 1);
            cm.sendOkS("#fs11#" + 검정 + "자네 스킬창에 " + 노랑 + "#s" + skillid + "#" + sel + "" + 검정 + "을(를) 지급했다네!", 0x04, 9401232);
            cm.gainItem(4310012, -개수);
            cm.dispose();
        } else {
            if (강화 == 0) {
                스킬이름 = skillid == 80000587 ? "아케인 포스" : skillid == 80000577 ? "블랙 가호" : skillid == 80000578 ? "무극" : skillid == 80000588 ? "탐욕스러운자" : skillid == 80001829 ? "비연" : "";
                if (cm.getPlayer().getSkillLevel(skillid) >= level) {
                    cm.sendOkS("#fs11##s" + skillid + "# " + 노랑 + "" + 스킬이름 + " " + 검정 + "스킬은 이미 " + 레드 + "마스터레벨#k을 달성했네!", 0x04, 9401232);
                    cm.dispose();
                } else {
                    //if (cm.getClient().getQuestStatus(50001) == 1 || cm.getPlayer().getQuestStatus(50001) == 1) {
                        if (cm.getClient().getQuestStatus(50001) == 1) {
                        cm.getClient().setCustomKeyValue(50001, "1", "1");
                    }
                    if (!cm.haveItem(4310012, 강화갯수)) {
                        cm.sendOkS("#fs11#" + 검정 + "자네는 #z4310012# 아이템이 부족한거 같군.", 0x04, 9401232);
                        cm.dispose();
                        return;
                    }
                    cm.gainItem(4310012, -강화갯수);
                    cm.teachSkill(skillid, cm.getPlayer().getSkillLevel(skillid) + 1, level);
                    cm.sendOkS("#fs11##s" + skillid + "# " + 노랑 + "" + 스킬이름 + "#k " + 검정 + "업그레이드가 성공하여 #r" + (nextskillv) + "#k " + 검정 + "레벨이 되었다네!", 0x04, 9401232);
                    cm.dispose();
                }
            }
        }
    }
}
