var enter = "\r\n";

function start() {
	status = -1;
	action(1, 0, 0);
}
var sel = -1;
function action(mode, type, selection) {
	cm.getPlayer().dropMessageGM(6, mode + ", " + type + ", " + sel);
	if (mode == 1) {
		status++;
	} else if (mode == 0 && type == 3) {
		cm.sendOk("하하! #r#e네오 포스#n#k를 받고 싶다면 언제든 찾아오게.");
		cm.dispose();
		return;
	} else if (mode == 0) {
		status--;
	} else {
		cm.dispose();
		return;
    	}
	if (status == 0) {
		var msg = "강한 보스를 처치한 사람만 #r#e#i4310308:# #t4310308##n#k를 얻을 수 있네."+enter;
		msg += "#L0# #r#e<네오 코어샵>#n을 이용하고 싶습니다.#k#l"+enter;
		//msg += "#L1# #b#e<네오 코어>#n를 확인하고 싶습니다.#k#l"+enter;
		// 현재 주간 처치를 사용안하므로 확인제거
		msg += "#L2# #b#e<네오 포스>#n를 받고 싶습니다.#k#l"+enter;
		msg += "#L3# #e<네오 코어>#n에 대해 알려주세요.#k#l"+enter;
		msg += "#L4# #e<네오 포스>#n에 대해 알려주세요.#k#l";
		cm.sendNext(msg);
	} else if (status == 1) {
		if (sel == -1)
		sel = selection;
		if (sel == 0) {
			cm.dispose();
			cm.openShop(29);
		} else if (sel == 1) {
			if (cm.getInfoQuest(123456, "").equals("")) {
				cm.sendOk("이번 주 자네가 처치한 보스는 없군!\r\n주간 보스를 처치하면 #r#e#i4310308:# #t4310308##n#k를 주겠네!");
				cm.dispose();
				return;
			}
			
		} else if (sel == 2) {
			cm.sendNext("네오 코어를 많이 얻을수록 #r#e네오 포스#n#k라는 특별한 힘을 얻게 된다.");
		} else if (sel == 3) {
			cm.sendNext("하하! #r#e네오 코어#n#k에 대해 궁금하다고?");
		} else if (sel == 4) {
			cm.sendNext("네오 코어를 많이 얻을수록 #r#e네오 포스#n#k라는 특별한 힘을 얻게 된다.\r\n\r\n\r\n#r ※ 네오 포스는 캐릭터별로 받을 수 있습니다.");
		}
	} else if (status == 2) {
		if (sel == 1) {
			if (cm.getKeyValue(504721).equals("")) {
				cm.sendOk("이번 주 자네가 처치한 보스는 없군!\r\n주간 보스를 처치하면 #r#e#i4310308:# #t4310308##n#k를 주겠네!");
				cm.dispose();
				return;
			}
			cm.dispose();
			return;
		} else if (sel == 2) {
			cm.sendNextPrev("#r#e네오 포스#n#k는 최소 #b#e100개#n#k 이상의 네오 코어를 모은 사람만 얻을 수 있네.");
		} else if (sel == 3) {
			cm.sendNextPrev("이 성은 신비한 힘을 가지고 있다.\r\n바로 #b#e다양한 힘#n#k을 #r#e보석의 형태#n#k로 만드는 힘!\r\n\r\n그중 #i4310308# #r#e#t4310308##n#k는 아주 강한 #b#e몬스터의 힘#n#k으로만 만들 수 있지.");
		} else if (sel == 4) {
			cm.sendNextPrev("네오 포스는 획득한 네오 코어 개수가 많으면 더 강해지지.\r\n\r\n#e[네오 포스 레벨]#n#k#e\r\n 누적 네오 코어 #b#e100개#n#k - #r#e#s80000654# #q80000654##n#k#e\r\n 누적 네오 코어 #b#e200개#n#k - #r#e#s80000655# #q80000655##n#k#e\r\n 누적 네오 코어 #b#e400개#n#k - #r#e#s80000656# #q80000656##n#k#e\r\n 누적 네오 코어 #b#e800개#n#k - #r#e#s80000657# #q80000657##n#k#e\r\n 누적 네오 코어 #b#e1200개#n#k - #r#e#s80000658# #q80000658##n#k#e\r\n 누적 네오 코어 #b#e1800개#n#k - #r#e#s80000659# #q80000659##n#k#e\r\n 누적 네오 코어 #b#e2400개#n#k - #r#e#s80000660# #q80000660##n#k#e\r\n 누적 네오 코어 #b#e3000개#n#k - #r#e#s80000661# #q80000661##n#k");
		}
	} else if (status == 3) {
		if (sel == 2) {
			if (cm.getKeyValue(501215, "point") == -1) {
				cm.setKeyValue(501215, "point", "0");
			}
			var zz = 80000654;
			for (var i = 0; i < 9; i++) {
			    zz = 80000654 + i;
			    if (!cm.hasSkill(zz)) {
				break;
			    }
			}
			if (zz == 80000662) {
				cm.sendOk("자네는 이미 네오 포스를 8단계까지 마스터했군!\r\n자네에겐 더 이상 알려줄 스킬이 없네..");
				cm.dispose();
				return;
			}
			var vq = ((zz - 80000654) + 1) * 100;
			if (cm.getKeyValue(501215, "point") >= vq) {
				cm.sendYesNo("자네는 네오 코어가 충분히 있군.\r\n#r#e네오 포스#n#k를 레벨 " + ((zz - 80000654) + 1) + "로 업그레이드 하겠나?");
			} else {
				cm.sendOk("자네는 아직 네오 코어 개수가 부족하군.\r\n네오 코어를 더 모으고 찾아오게.");
				cm.dispose();
			}
		} else if (sel == 3) {
			cm.sendNextPrev("#b#e강한 몬스터#n#k를 처치하면 그 힘이 처치한 사람에게 잠시 유지된다. 그 힘이 사라지기 전에 나에게 오면 #r#e네오 코어#n#k를 만드는 것을 도와주지.\r\n\r\n\r\n#r ※ #e주간 보스#n를 처치하고 콜렉터에게 말을 걸면 네오 코어를 받을 수 있습니다.");
		} else if (sel == 4) {
			cm.sendNextPrev("네오 포스가 가장 높은 단계인 #r#e8단계#n#k에 도달하면 엄청난 힘을 가질 수 있다네.\r\n\r\n#r#e#s80000661# #q80000661##n#k\r\n#e\r\n- 일반 몬스터 공격 시 데미지 #b#e20%#n#k 증가#e\r\n- 경험치 획득량 #b#e10%#n#k 증가#e\r\n- 스타 포스 #b#e40#n#k 증가#e\r\n- 아케인 포스 #b#e40#n#k 증가#e\r\n- 크리티컬 확률 #b#e20%#n#k 증가#e\r\n- 공격력/마력 #b#e20#n#k 증가#e\r\n- 올스탯 #b#e40#n#k 증가#e\r\n- 최대 HP/MP #b#e2000#n#k 증가#e\r\n- 몬스터 방어율 무시 #b#e30%#n#k 증가#e\r\n- 보스 몬스터 공격 시 데미지 #b#e30%#n#k 증가\r\n");
		}
	} else if (status == 4) {
		if (sel == 2) {
			if (cm.getKeyValue(501215, "point") == -1) {
				cm.setKeyValue(501215, "point", "0");
			}
			var zz = 80000654;
			for (var i = 0; i < 9; i++) {
			    zz = 80000654 + i;
			    if (!cm.hasSkill(zz)) {
				break;
			    }
			}
			if (zz == 80000662) {
				cm.sendOk("자네는 이미 네오 포스를 8단계까지 마스터했군!\r\n자네에겐 더 이상 알려줄 스킬이 없네..");
				cm.dispose();
				return;
			}
			var vq = ((zz - 80000654) + 1) * 100;
			if (cm.getKeyValue(501215, "point") >= vq) {
				if (zz != 80000654) {
					cm.getPlayer().changeSkillLevel(zz - 1, 0, 0);
				}
				cm.getPlayer().changeSkillLevel(zz, 1, 1);
				cm.setKeyValue(501215, "point", (cm.getKeyValue(501215, "point") - vq) + "");
				cm.sendOk("자네의 네오 포스를 #r#e" + ((zz - 80000654) + 1) + "단계#n#k로 올려주었네, 확인해보게나!");
				cm.dispose();
			} else {
				cm.sendOk("자네는 아직 네오 코어 개수가 부족하군.\r\n네오 코어를 더 모으고 찾아오게.");
				cm.dispose();
			}
		} else if (sel == 3) {
			cm.sendNextPrev("몬스터를 처치한 힘은 #b#e매주 수요일#n#k까지만 유지되니,\r\n강한 몬스터를 처치하면 바로 오는 게 좋아.\r\n\r\n#r ※ 주간 보스 클리어 기록은 매주 #e목요일 0시#n에 리셋됩니다.");
		} else if (sel == 4) {
			cm.sendNext("#b#e네오 포스#n#k는 습득하면 #r#e2021년 3월 10일(수) 23시 59분#n#k까지 효과가 유지된다.\r\n그럼 강한 몬스터를 처치하고 찾아오도록!");
			cm.dispose();
		}
	} else if (status == 5) {
		if (sel == 3) {
			cm.sendNextPrev("그리고 #i4310308# #r#e#t4310308##n#k는 일주일에 #b#e월드 당 최대 400개#n#k까지만 만들 수 있다.\r\n\r\n#r ※ 네오 코어는 월드 내 공유되며, 월드 당 주간 400개까지 획득할 수 있습니다.");
		}
	} else if (status == 6) {
		if (sel == 3) {
			cm.sendNextPrev("당연한 말이지만 #b#e강한 몬스터#n#k일수록 더 많은 #r#e네오 코어#n#k를 만들 수 있다는 것도 알아두도록.\r\n#e\r\n\r\n#r#i4310308# #t4310308# 5개#k#e\r\n 하드 힐라#e\r\n 카오스 핑크빈#e\r\n 이지 시그너스#e\r\n 노멀 시그너스#e\r\n\r\n#r#i4310308# #t4310308# 10개#k#e\r\n 카오스 자쿰#e\r\n 카오스 피에르#e\r\n 카오스 반반#e\r\n 카오스 블러디퀸#e\r\n\r\n#r#i4310308# #t4310308# 20개#k#e\r\n 하드 매그너스#e\r\n 카오스 벨룸#e\r\n\r\n#r#i4310308# #t4310308# 30개#k#e\r\n 카오스 파풀라투스#e\r\n 노멀 스우#e\r\n 노멀 데미안#e\r\n 이지 루시드#e\r\n\r\n#r#i4310308# #t4310308# 40개#k#e\r\n 노멀 루시드#e\r\n 노멀 윌#e\r\n 노멀 더스크#e\r\n 노멀 듄켈#e\r\n\r\n#r#i4310308# #t4310308# 60개#k#e\r\n 하드 데미안 #e\r\n 하드 스우#e\r\n 하드 루시드#e\r\n 하드 윌#e\r\n\r\n#r#i4310308# #t4310308# 70개#k#e\r\n 카오스 더스크#e\r\n 하드 듄켈#e\r\n 진 힐라");
		}
	} else if (status == 6) {
		if (sel == 3) {
			cm.sendNextPrev("그리고 몬스터를 처치하다가 실패한 경우에는 #r#e네오 코어#n#k를 받을 수 없다는 것 명심해!\r\n\r\n #b ※ 처치 시점에 #e사망 상태인 경우#n 네오 코어를 받을 수 없습니다.\r\n\r\n  ※ 처치 전에 #e퇴장하는 경우#n 경우 네오 코어를 받을 수 없습니다.");
		}
	} else if (status == 7) {
		if (sel == 3) {
			cm.sendNext("그럼 강한 몬스터를 처치하고 찾아오도록!");
			cm.dispose();
		}
	}
}