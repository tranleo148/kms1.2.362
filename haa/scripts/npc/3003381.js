var status = 0;
/*
정령의 잔해 8644201 (10초에 2마리)
맹독의 추격자 8644301~8644305
속박된 돌의 정령 8644101~8644112
 */
var warpmap = 450005000;
var fullmobxy = [
	[-898, -731],
	[-1645, -971],
	[-2993, -1571],
	[-2142, -1751],
	[-1315, -1991],
	[-830, -2231],
	[488, -731],
	[1369, -971],
	[2661, -1571],
	[1852, -1751],
	[1096, -1991],
	[563, -2231]
];
var sel = 0;
var gifted = false;

function start() {
	status = 0;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == 0) {
		if (status == 2 && sel == 0) {
			cm.sendOk("너무 늦으면 내 친구들을 영영 못 볼 수도 있담..");
			cm.dispose();
			return;
		} else {
			status--;
		}
	}
	if (mode == 1) {
		status++;
	} else {
		status--;
	}

	if (status == 1) {
		if (cm.getPlayer().getMapId() == 921172400) {
			if (!gifted) {
				var point = Number(cm.getPlayer().getKeyValue(16215, "point"));
				var coin = Math.floor(point / 100);
				if(coin > 0){
					cm.getPlayer().AddStarDustCoin(coin);
				}
				cm.getPlayer().setKeyValue(16215, "point", "0");
			}
			if (point < 1000) {
				cm.sendNext("음... 내 친구들을 많이 구하지 못했구남!");
			} else {
				var talk = "내 친구들 많이 구해줬남?\r\n";
				talk += "구출점수는 #b#e" + point + "점#sk#n 이구남!\r\n";
				talk += "이 정도면 #r#e스피릿 코인 " + coin + "개#k#n를 줄 수 있겠담!";
				cm.sendNext(talk);
			}
			gifted = true;
		} else {
			var date = new Date()
			var today = date.getFullYear() + ""
			today += new Date().getMonth() < 9 ? "0" : ""
			today += Number(date.getMonth() + 1) + ""
			today += new Date().getDate() < 10 ? "0" : ""
			today += date.getDate();
			if (today != cm.getPlayer().getKeyValue(20190121, "SpiritSavior_Date")) {
				cm.getPlayer().setKeyValue(20190121, "SpiritSavior_Date", today);
				cm.getPlayer().setKeyValue(16215, "play", "0");
			}
                if (cm.getPlayer().getLevel() >= 500) { 
			var text = "#b#e<스피릿 세이비어>#n#k\r\n내 친구들을 어서 구해줬음 좋겠담!\r\n\r\n";
			text += "#b#L0#<스피릿 세이비어>에 도전한다.#k\r\n";
			text += "#b#L1000#설명을 듣는다.#l#k";
			cm.sendSimple(text);
		    } else {
		        cm.sendOk("#fs11# 현재 준비중인 컨텐츠이며 밸런스 상 출시가 되지 않을 수 있습니다.#k");
		        cm.dispose();
		    }
		}
	} else if (status == 2) {
		if (cm.getPlayer().getMapId() == 921172400) {
			if (point < 1000) {
				cm.sendNextPrev("다음에는 친구들을 많이 구해달람!");
			} else {
				cm.sendNextPrev("다음에 또 도와달람!");
			}
		} else {
			sel = selection;
			if (selection == 0) {
				var play = cm.getPlayer().getKeyValue(16215, "play");
				cm.sendYesNo("어서 내 친구들을 구해주면 좋겠담. 지금 도전 할 건감?\r\n\r\n#b오늘 도전 횟수 " + play + " / 3#k");
			} else {
				var text = "무엇을 알고 싶은감?\r\n\r\n";
				text += "#L0#스피릿 세이비어 규칙\r\n#l";
				text += "#L1#스피릿 세이비어 보상\r\n#l";
				text += "#L2#설명을 듣지 않는다.\r\n#l";
				cm.sendSimple(text);
			}
		}
	} else if (status == 3) {
		if (cm.getPlayer().getMapId() == 921172400) {
			cm.warp(warpmap);
			cm.dispose();
		} else {
			sel2 = selection;
			if (sel == 0) {
				if (cm.getEventManager("Savior").getInstance("Savior") != null) {
					cm.sendOk("이미 누군가 도전 중이담!");
					cm.dispose();
				} else {
					var event = cm.getEventManager("Savior").getInstance("Savior");
					if (event == null) {
						if (Number(cm.getPlayer().getKeyValue(16215, "play")) < 3 || cm.getPlayer().isGM()) {
							cm.getEventManager("Savior").startInstance_Solo("" + 921172300, cm.getPlayer());
							cm.getPlayer().setKeyValue(16215, "point", "0");
							cm.getPlayer().setKeyValue(16215, "play", "" + (Number(cm.getPlayer().getKeyValue(16215, "play")) + 1));
							cm.getPlayer().setKeyValue(16215, "saved", "0");
							cm.getPlayer().setKeyValue(16215, "life", "100");
							cm.getPlayer().setKeyValue(16215, "chase", "0");
							cm.dispose();
						} else {
							cm.sendOk("오늘은 더 이상 #b#e<스피릿 세이비어>#k#n에 도전할 수 없담.\r\n내일 다시 찾아와 달람.\r\n\r\n#r#e(1일 3회 입장 가능)#k#n");
							cm.dispose();
						}
					} else {
						cm.sendOk("이미 누군가 도전 중이담!");
						cm.dispose();
					}
				}
			} else {
				if (selection == 0) {
					cm.sendNext("#e<스피릿 세이비어 규칙>#n\r\n\r\n#e제한시간이 끝나기 전에 / 방어도가 모두 깎이기 전에#n 최대한 많은 #b#e속박된 돌의 정령#k#n을 구출해야 한담!\r\n#b#e속박된 돌의 정령#n#k을 구출하면 #r#e채집/NPC대화키를 눌러서#n#k 데리고 다닐 수 있담!");
				} else if (selection == 1) {
					cm.sendNext("#e<스피릿 세이비어 보상>#n\r\n\r\n친구들을 구출하면 #e1000 포인트#n당 #b#e5코인#n#k을 준담!");
				}
			}

		}
	} else if (status == 4) {
		if (sel == 0) {} else {
			if (sel2 == 0) {
				cm.sendNextPrev("#e<스피릿 세이비어 규칙>#n\r\n\r\n친구들은 #b#e최대 5명 까지#n#k 데리고 다닐 수 있담!\r\n친구들을 처음 시작했던 #b#e구출지점까지 무사히 데려오면#n#k #e'구출점수'#n를 얻을 수 있담!\r\n#b#e한 번에 많은 친구들을 구출할 수록 #n#k 높은 점수를 얻는담!\r\n\r\n#e1명 - 200점#n\r\n#e2명 - 500점#n\r\n#e3명 - 1000점#n\r\n#e4명 - 1500점#n\r\n#e5명 - 2500점#n");
			}
		}
	} else if (status == 5) {
		if (sel2 == 0) {
			cm.sendNextPrev("#e<스피릿 세이비어 규칙>#n\r\n\r\n하지만 #r#e나쁜 정령#n#k들이 친구들을 쉽게 데려가도록 내버려 두지 않을 거담.\r\n일정 시간이 지나면 맵 곳곳을 돌아다니는 #e#r정령의 파편#k#n이 생겨날거담. 그 녀석들에게 맞으면 #b#e방어도#n#k가 깍이게 된담.");
		}
	} else if (status == 6) {
		if (sel2 == 0) {
			cm.sendNextPrev("#e<스피릿 세이비어 규칙>#n\r\n\r\n또 우리 친구들을 구출하면 #r#e맹독의 정령#n#k이 너를 추격하기 시작할거담!\r\n#e#r맹독의 정령#k#n은 많은 친구들이 구출 될 수록 #e점점 커지고 빨라진담.#n 녀석에게 공격 받으면 많은 방어도를 잃게되고 데리고 있던 친구들도 모두 사라지게 되니 녀석에게 부딪히지 않도록 조심하람!");
		}
	}
}