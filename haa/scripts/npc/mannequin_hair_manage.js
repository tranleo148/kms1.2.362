var status = 0;
var result = -1;
var slot = -1;
var selsave = -1;
var lastbyte = -1;
var banhair = [30070, 30071, 30072, 30073, 30074, 30075, 30076, 30077, 30080, 30081, 30082, 30083, 30084, 30085, 30086, 30087, 39000, 39010, 39020, 39030, 39040, 39050, 39060, 39070, 39080, 39100, 39110, 39120, 39130, 39140, 39150, 39160, 39170, 39180, 39190, 39200, 39210, 39220, 39230, 39240, 39250, 39260, 39270, 39280, 39290, 39300, 39310, 39320, 39330, 39340, 39350, 39360, 39370, 39380, 39390, 39400, 39410, 39420, 39430, 39440, 39450, 39460];

function start(res, slt, lastbyt) {
    status = -1;
	result = res;
	slot = slt;
	lastbyte = lastbyt;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 1) 
        status++;
    else 
        status--;
    if (status == 0) {
		if (result == 0) {
			// 넣기
			if (cm.isAngelicBuster()) {
				if (lastbyte == 0) {
					for (i = 0; i < banhair.length; i++) {
						if (banhair[i] == cm.getPlayerStat("HAIR")) {
					                cm.hairroom(0, slot, true);
							cm.sendOk("현재 헤어는 등록할 수 없는 헤어입니다.");
							cm.dispose();
							return;
						}
					}
					cm.setKeyValue(26544, "h" + slot, cm.getPlayerStat("HAIR"));
					cm.hairroom(0, slot, true);
					cm.dispose();
				} else {
					for (i = 0; i < banhair.length; i++) {
						if (banhair[i] == cm.getPlayerStat("SECONDHAIR")) {
					                cm.hairroom(0, slot, true);
							cm.sendOk("현재 헤어는 등록할 수 없는 헤어입니다.");
							cm.dispose();
							return;
						}
					}
					cm.setKeyValue(26544, "h" + slot, cm.getPlayerStat("SECONDHAIR"));
					cm.hairroom(0, slot, true);
					cm.dispose();
				}
			} else if (cm.isZero()) {
				if (cm.getPlayer().getGender() == 0) {
					for (i = 0; i < banhair.length; i++) {
						if (banhair[i] == cm.getPlayerStat("HAIR")) {
					                cm.hairroom(0, slot, true);
							cm.sendOk("현재 헤어는 등록할 수 없는 헤어입니다.");
							cm.dispose();
							return;
						}
					}
					cm.setKeyValue(26544, "h" + slot, cm.getPlayerStat("HAIR"));
					cm.hairroom(0, slot, true);
				} else if (cm.getPlayer().getGender() == 1) {
					for (i = 0; i < banhair.length; i++) {
						if (banhair[i] == cm.getPlayerStat("SECONDHAIR")) {
					                cm.hairroom(0, slot, true);
							cm.sendOk("현재 헤어는 등록할 수 없는 헤어입니다.");
							cm.dispose();
							return;
						}
					}
					cm.setKeyValue(26544, "h" + slot, cm.getPlayerStat("SECONDHAIR"));
					cm.hairroom(0, slot, true);
				}
				cm.dispose();
			} else {
					for (i = 0; i < banhair.length; i++) {
						if (banhair[i] == cm.getPlayerStat("HAIR")) {
					                cm.hairroom(0, slot, true);
							cm.sendOk("현재 헤어는 등록할 수 없는 헤어입니다.");
							cm.dispose();
							return;
						}
					}
				
				cm.setKeyValue(26544, "h" + slot, cm.getPlayerStat("HAIR"));
				cm.hairroom(0, slot, true);
				cm.dispose();
			}
		} else if (result == 1) {
			// 변경
			if (cm.isAngelicBuster()) {
				cm.sendNext("어떤 머리를 스타일링 하시겠어요?\r\n\r\n#b#L1#변신전의 머리를 마네킹과 서로 바꾼다#l\r\n#L2#변신후의 머리를 마네킹과 서로 바꾼다#l#k");
			} else if (cm.isZero()) {
				if (cm.getPlayer().getGender() == 0) {
					for (i = 0; i < banhair.length; i++) {
						if (banhair[i] == cm.getPlayerStat("HAIR")) {
					                cm.hairroom(0, slot, true);
							cm.sendOk("현재 헤어는 바꾸실 수 없는 헤어입니다.");
							cm.dispose();
							return;
						}
					}
					cm.sendAcceptDecline("아래와 같이 스타일링 하시겠어요?\r\n\r\n#b#e캐릭터#n: #t" + cm.getPlayerStat("HAIR") + "#\r\n#e마네킹#n: #t" + cm.getKeyValue(26544, "h" + slot) + "#");
				} else if (cm.getPlayer().getGender() == 1) {
					for (i = 0; i < banhair.length; i++) {
						if (banhair[i] == cm.getPlayerStat("SECONDHAIR")) {
					                cm.hairroom(0, slot, true);
							cm.sendOk("현재 헤어는 바꾸실 수 없는 헤어입니다.");
							cm.dispose();
							return;
						}
					}
					cm.sendAcceptDecline("아래와 같이 스타일링 하시겠어요?\r\n\r\n#b#e캐릭터#n: #t" + cm.getPlayerStat("SECONDHAIR") + "#\r\n#e마네킹#n: #t" + cm.getKeyValue(26544, "h" + slot) + "#");
				}
			} else {
					for (i = 0; i < banhair.length; i++) {
						if (banhair[i] == cm.getPlayerStat("HAIR")) {
					                cm.hairroom(0, slot, true);
							cm.sendOk("현재 헤어는 바꾸실 수 없는 헤어입니다.");
							cm.dispose();
							return;
						}
					}
				cm.sendAcceptDecline("아래와 같이 스타일링 하시겠어요?\r\n\r\n#b#e캐릭터#n: #t" + cm.getPlayerStat("HAIR") + "#\r\n#e마네킹#n: #t" + cm.getKeyValue(26544, "h" + slot) + "#");
			}
		} else if (result == 2) {
			// 삭제
			cm.removeKeyValue(26544, "h" + slot);
			cm.removeKeyValue(26544, "m" + slot);
			cm.hairroom(0, slot, false);
			cm.dispose();
		}
    } else if (status == 1) {
		if (result == 1) {
			if (cm.isAngelicBuster()) {
				selsave = selection;
				if (selection == 1) {
					for (i = 0; i < banhair.length; i++) {
						if (banhair[i] == cm.getPlayerStat("HAIR")) {
					                cm.hairroom(0, slot, true);
							cm.sendOk("현재 헤어는 바꾸실 수 없는 헤어입니다.");
							cm.dispose();
							return;
						}
					}
					cm.sendAcceptDecline("아래와 같이 스타일링 하시겠어요?\r\n\r\n#b#e캐릭터#n: #t" + cm.getPlayerStat("HAIR") + "#\r\n#e마네킹#n: #t" + cm.getKeyValue(26544, "h" + slot) + "#");
				} else if (selection == 2) {
					for (i = 0; i < banhair.length; i++) {
						if (banhair[i] == cm.getPlayerStat("SECONDHAIR")) {
					                cm.hairroom(0, slot, true);
							cm.sendOk("현재 헤어는 바꾸실 수 없는 헤어입니다.");
							cm.dispose();
							return;
						}
					}
					cm.sendAcceptDecline("아래와 같이 스타일링 하시겠어요?\r\n\r\n#b#e캐릭터#n: #t" + cm.getPlayerStat("SECONDHAIR") + "#\r\n#e마네킹#n: #t" + cm.getKeyValue(26544, "h" + slot) + "#");
				}
			} else if (cm.isZero()) {
				if (cm.getPlayer().getGender() == 0) {
					var temp = cm.getPlayerStat("HAIR");
					cm.setHair(cm.getKeyValue(26544, "h" + slot));
					cm.setKeyValue(26544, "h" + slot, temp);
					cm.hairroom(0, slot, false);
					cm.sendOk("마네킹과 캐릭터의 헤어가 서로 변경되었습니다.");
					cm.dispose();
				} else if (cm.getPlayer().getGender() == 1) {
					var temp = cm.getPlayerStat("SECONDHAIR");
					cm.setZeroSecondHair(cm.getKeyValue(26544, "h" + slot));
					cm.setKeyValue(26544, "h" + slot, temp);
					cm.hairroom(0, slot, false);
					cm.sendOk("마네킹과 캐릭터의 헤어가 서로 변경되었습니다.");
					cm.dispose();
				}
			} else {
				var temp = cm.getPlayerStat("HAIR");
				cm.setHair(cm.getKeyValue(26544, "h" + slot));
				cm.setKeyValue(26544, "h" + slot, temp);
				cm.hairroom(0, slot, false);
				cm.sendOk("마네킹과 캐릭터의 헤어가 서로 변경되었습니다.");
				cm.dispose();
			}
		}
    } else if (status == 2) {
		if (result == 1) {
			if (cm.isAngelicBuster()) {
				if (selsave == 1) {
					var temp = cm.getPlayerStat("HAIR");
					cm.setHair(cm.getKeyValue(26544, "h" + slot));
					cm.setKeyValue(26544, "h" + slot, temp);
					cm.hairroom(0, slot, false);
					cm.sendOk("마네킹과 캐릭터의 헤어가 서로 변경되었습니다.");
					cm.dispose();
				} else if (selsave == 2) {
					var temp = cm.getPlayerStat("SECONDHAIR");
					cm.setAngelicSecondHair(cm.getKeyValue(26544, "h" + slot));
					cm.setKeyValue(26544, "h" + slot, temp);
					cm.hairroom(0, slot, false);
					cm.sendOk("마네킹과 캐릭터의 헤어가 서로 변경되었습니다.");
					cm.dispose();
				}
			}
		}
	}
}
