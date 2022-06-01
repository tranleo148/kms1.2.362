var status = -1;
var s1;
var s2;

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
		if (status == 1 && s1 == 0) {
			cm.sendOk("변덕이 죽 끓는듯 하군.\r\n이곳은 만만한 곳이 아니야. 잘 생각하고 입장하라고!");
			cm.dispose();
			return;
		} 
		status--;
	}
	if (mode == 1) {
		status++;
	}

	if (status == 0) {
		if (cm.getPlayer().getV("DojoCount") == null) {
			cm.getPlayer().addKV("DojoCount", "0");
		}
		if (cm.getPlayer().getMapId() == 925020001) {
			cm.sendSimple("우리 사부님은 무릉에서 최고로 강한 분이지. 그런 분에게 네가 도전하겠다고? 나중에 후회하지마. #b\r\n#L0# 무릉 도장에 도전해볼게.#l\r\n#L1# 무릉 도장이 뭐지?#l\r\n#L2# 무릉 도장에서 받을 수 있는 보상을 확인하고 싶어.#l\r\n#L3# 오늘 남은 도전 횟수를 확인하고 싶어.#l\r\n");
		} else if (cm.getPlayer().getMapId() == 925080000) {
			cm.sendYesNo("심신 수련장을 퇴장하시겠습니까? 사용한 수련권은 복구되지 않습니다.");
		}
	} else if (status == 1) {
		s1 = selection;
		if (cm.getPlayer().getMapId() != 925020001) {
			cm.dispose();
			cm.warp(925020000);
		} else {
			time = new Date();
			if (selection == 0) {
				if (cm.getPlayer().getParty() != null) {
					cm.sendOk("파티로는 입장할 수 없어! 혼자서 도전하라구! 겁쟁이냐?\r\n\r\n\r\n");
					cm.dispose();
				} else if (parseInt(cm.getPlayer().getV("DojoCount")) >= 3 && cm.getPlayer().isGM() == false) {
					cm.sendOk("오늘은 이미 무릉도장 이용횟수를 초과했어. 내일 다시오도록해!!");
					cm.dispose();
				} else {
					var start = false;
					for (var i = 0; i < 80; i++) {
						if (cm.getPlayerCount(925070000 + (i * 100) + (i == 0 ? 0 : 100)) >= 1) {
							start = true;
							cm.resetMap(925070000 + (i * 100) + (i == 0 ? 0 : 100));
						}
					}
					if (start) {
						cm.sendOk("누군가 이미 도전하고있습니다. 잠시후 시도 해주세요.");
						cm.dispose();
						return;
					}
					cm.sendAcceptDecline("무릉도장에 입장 시 지금 적용되어 있는\r\n#b#fs16##e모든 버프 효과가 해제#n#k#fs12#될거야.\r\n\r\n그래도 진짜로 도전하겠어?");
				}
			} else if (selection == 1) {
				cm.sendNext("우리 사부님은 무릉에서 가장 강한 분이야.\r\n그런 사부님께서 만드신 곳이 바로 이 #b무릉도장#k이지.");
			} else if (selection == 2) {
				cm.sendSimple("무릉도장에서 보상을 얻을 수 있는 방법은 두 가지야. 초절정의 실력과 강한 힘으로 상위 랭커가 되거나, 혹은 꾸준한 정진을 통해 얻을 수 있는 포인트를 통한 물물교환.\r\n\r\n#b#L0#랭커 보상에 대해 묻는다.\r\n#L1#참여 보상(포인트)에 대해 묻는다.");
			} else if (selection == 3) {
				cm.dispose();
				cm.sendNext("오늘 무릉도장에는 3번 참여할 수 있어. 그런 건 알아서 세어보라고.");
			} else if (selection == 4) {
				cm.sendYesNo("무릉 심신수련관이 일반인들에게도 개방하도록 결정되었어.\r\n다만, 강하거나 성실한 자만 들어갈 수 있지. 라오 아저씨가 주는 부적을 가져와. 부적이 내재하고 있는 시간에 따라 들여보내주지.\r\n\r\n입장하겠어?\r\n#b(심신수련관은 입장 시 해당 캐릭터의 레벨에 따라 자동으로 경험치를 습득합니다.)");
			}
		}
	} else if (status == 2) {
		s2 = selection;
		if (s1 == 0) {
			cm.dojowarp(925070000);
			if (cm.getPlayer().getV("DojoCount") == null) {
				cm.getPlayer().addKV("DojoCount", "0");
			}
			cm.getPlayer().addKV("DojoCount", (parseInt(cm.getPlayer().getV("DojoCount")) + 1) + "");
			cm.getPlayer().getMap().startMapEffect("사부님의 특별한 도법으로 모든 버프가 해제되었어. 이래야 좀 공평하지? 30초 줄테니까 준비해서 올라가라고.", 5120024, 3000);
			cm.dispose();
		} else if (s1 == 1) {
			cm.sendNextPrev("무릉도장은 79층에 사부님의 별층까지 #b#e총 80층#n#k의 건물이야.\r\n강한 자일 수록 더 높이 올라갈 수 있지.\r\n물론 너의 실력으로는 끝까지 가기 힘들겠지만.");
		} else if (s2 == 0) {
			cm.sendNext("말 그대로야. 사부님께서 상위권 랭커에게 보상품을 하사하시지. 강함이야말로 우리 무릉도장의 최고 가치니까. 그리고 그 강함에 대한 보상은 당연한거 아니겠어?");
		} else if (s2 == 1) {
			cm.sendNext("너의 무릉도장 참여도에 따라 포인트가 지급될 거야.\r\n\r\n 1. 도전할 때마다 돌파하는 층수에 비례한 포인트 지급\r\n 2. 자신이 속한 랭킹 구간의 지난 주 전체 랭킹 백분율에 따른 포인트 지급\r\n\r\n이 두 가지 기준으로 포인트가 지급될 거야.\r\n\r\n\r\n");
		} else if (s1 == 4) {
			cm.sendOk("심신수련관 입장용 부적을 가지고 있어야 수련관에 입장 가능하다고. 내 옆에 리오 아저씨에게 가 봐.");
		}
	} else if (status == 3) {
		if (s1 == 1) {
			cm.sendNextPrev("사부님이 계신 80층을 제외한 각 층엔 #r메이플 월드의 몬스터#k들이 그곳을 지키고 있지. 자세한 사정은 나도 몰라.\r\n사부님만이 아실 뿐.");
		} else if (s2 == 0) {
			cm.sendNextPrev("좀 더 공정한 경쟁을 위해 레벨에 따라 랭킹 구간도 달라.\r\n\r\n*입문 : 105 ~ 140 레벨\r\n*숙련 : 141 ~ 180 레벨\r\n*통달 : 181 레벨 이상");
		} else if (s2 == 1) {
			cm.sendNextPrev("층수에 비례한 포인트는 1층당 10 포인트가 기본적으로 지급되고, 10층당 100 포인트가 추가적으로 지급되는 방식이야.\r\n\r\n\r\n");
		}
	} else if (status == 4) {
		if (s1 == 1) {
			cm.sendNextPrev("입장하면 초입층에서 네가 가지고 있던 #r모든 버프 효과를 해제#k할거야. 자기 힘만 가지고 경쟁해야 공정하지 않겠어?");
		} else if (s2 == 0) {
			cm.dispose();
			cm.sendNextPrev("당연한 얘기지만 랭킹 구간에 따라 보상도 달라.\r\n지금 네가 어느 랭킹 구간에 속해있는지 꼭 확인하라고.\r\n\r\n설마 네가 지나온 랭킹 구간에서 상위 랭커였다고 보상을 달라고 떼쓰진 않겠지?\r\n#b지금 속해있는 랭킹 구간으로 보상을 지급#k한다는 점을 잊지 말라고. 자세한 내용은 무릉도장 순위표에서 확인 해보도록 해.");
		} else if (s2 == 1) {
			cm.sendNextPrev("랭킹 백분율에 따른 포인트는 더 강한 자들이 속한 랭킹 구간일수록, 그리고 더 좋은 결과를 낼수록 더 많이 가져가게 될 거야. 아니꼬우면 강해지라고.\r\n\r\n");
		}
	} else if (status == 5) {
		if (s1 == 1) {
			cm.sendNextPrev("초입층에 머무르는 건 네 자유지만,\r\n#r타이머는 단 30초만 정지#k하니 더 좋은 기록을 세우고 싶다면 빠르게 준비하고 1층으로 넘어가는 게 좋을거야.");
		} else if (s2 == 1) {
			cm.dispose();
			cm.sendNextPrev("아, 그리고 포인트는 최대 #b10만 포인트#k를 넘게 가지고 있을 순 없어. 재깍재깍 쓰는 습관을 가지라고.\r\n\r\n\r\n");
		}
	} else if (status == 6) {
		if (s1 == 1) {
			cm.sendNextPrev("#e1 ~ 9층#n, #e11 ~ 19층#n엔 #b하나의 보스#k가 등장해.\r\n다음으로 넘어가려면 딱 하나만 처치하면 된다고.");
		}
	} else if (status == 7) {
		if (s1 == 1) {
			cm.sendNextPrev("#e21 ~ 29층#n엔 #b보스 하나#k와 #b부하 다섯#k이 등장해.\r\n보스와 부하를 모두 처치해야 다음 층으로 넘어갈 수 있어.");
		}
	} else if (status == 8) {
		if (s1 == 1) {
			cm.sendNextPrev("#e31 ~ 39층#n엔 #b둘 이상의 보스#k를 상대해야 해.\r\n설마 벌써 지레 겁먹은 건 아니겠지? 흐흐흐...");
		}
	} else if (status == 9) {
		if (s1 == 1) {
			cm.sendNextPrev("#e41층#n부터는 다시 #b보스 하나#k만 등장하니까 너무 걱정하지 마.\r\n과연 그게 더 쉬울지는 모르겠지만 말이야. 흐흐흐흐...");
		}
	} else if (status == 10) {
		if (s1 == 1) {
			cm.sendNextPrev("사부님이 계신 80층을 제외한 70층까지는\r\n#e10층 단위#n로 #b메이플 월드의 네임드 보스#k들이 등장해.\r\n여기선 #r15초#k마다 포션을 사용할 수 있어.");
		}
	} else if (status == 11) {
		if (s1 == 1) {
			cm.sendNextPrev("#e41층 이후#n부터도 #r15초#k마다 포션을 사용할 수 있어. 왜냐고? 그야 네가 들어가보면 알게 되겠지. 흐흐흐...");
		}
	} else if (status == 12) {
		if (s1 == 1) {
			cm.sendNextPrev("각 층에 누가 있냐고? 그런 것은 직접 올라가면서 알아봐...네가 강한만큼 많이 알게되지 않겠어? 흐흐흐...");
		}
	} else if (status == 13) {
		if (s1 == 1) {
			cm.sendNextPrev("뭐, 하나만 알려주자면....\r\n#e74층 ~ 79층#n엔 #b사부님의 제자#k들이 지키고 있어.\r\n어쭙잖은 실력으로 만나면 고생 좀 할 거야.");
		}
	} else if (status == 14) {
		if (s1 == 1) {
			cm.sendNextPrev("알아들었으면 어서 들어가 봐.\r\n몸이 근질근질하지 않아?");
		}
	} else if (status == 15) {
		if (s1 == 1) {
			cm.dispose();
		}
	}
}