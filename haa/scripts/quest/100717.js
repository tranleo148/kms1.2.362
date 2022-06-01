importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server);

var status = -1;

function start(mode, type, selection) {
	if (mode == -1) {
		qm.dispose();
		return;
	}
	if (mode == 0) {
		if (status == 4) {
			qm.sendNextS("정말 서운해요...", 4, 9062475);
		}
		status--;
		qm.dispose();
		return;
	}
	if (mode == 1) {
		d = status;
		status++;
	}

	if (status == 0) {
		qm.sendNextS("#e#b#h0##k#n님! 안녕하세요!", 4, 9062475);
	} else if (status == 1) {
		qm.sendNextPrevS("제가 네오 캐슬과 함께 태어난 정령인 것은 말씀드렸죠...?", 4, 9062475);
	} else if (status == 2) {
		qm.sendNextPrevS("지금은 인간의 모습을 하고 있지만 시간이 흐르면 다시 에르다의 흐름으로 되돌아가게 된답니다...", 4, 9062475);
	} else if (status == 3) {
		qm.sendNextPrevS("그러니까 에르다로 돌아가기 전에 이 세계를 잔뜩 살펴보고, 실컷 즐기고 싶어요!", 4, 9062475);
	} else if (status == 4) {
		qm.sendYesNoS("#e#b#h0##k#n님! 저를 좀 도와주시지 않으시겠어요?", 4, 9062475);
	} else if (status == 5) {
		qm.sendUI(1290);
		qm.sendNextS("와아! 정말 고마워요!", 4, 9062475);
		qm.forceCompleteQuest();
	} else if (status == 6) {
		qm.sendNextPrevS("궁금한 것들을 써둔 #r#e탐험일지#n#k를 #e#b#h0##k#n님에게 맡길게요!\r\n네오 캐슬 밖으로 나갈 수 없는 저 대신 조사해 주세요", 4, 9062475);
	} else if (status == 7) {
		qm.sendNextPrevS("탐험일지는 4가지를 쓸 거예요! \r\n\r\n#e#b[리오의 탐험일지]#k\r\n\r\n 첫 번째 - 레벨 범위 몬스터 처치\r\n 두 번째 - 엘리트 몬스터&챔피언 처치\r\n 세 번째 - 룬 사용\r\n 네 번째 - 폴로&프리토 클리어", 4, 9062475);
	} else if (status == 8) {
		qm.sendNextPrevS("탐험일지를 완료하면 #b#e#i4310306# #t4310306##n#k을 드릴게요!\r\n\r\n#e[리오의 탐험일지]\r\n 첫 번째 - #b#e#i4310306# #t4310306##k 100개\r\n 두 번째 - #b#e#i4310306# #t4310306##k 100개\r\n 세 번째 - #b#e#i4310306# #t4310306##k 200개\r\n 네 번째 - #b#e#i4310306# #t4310306##k 400개", 0x04, 9062475);
	} else if (status == 9) {
		qm.sendNextPrevS("#b#e매주 목요일 오전 10시#n#k에 새로운 탐험일지를 쓸 거니까 그전에 조사를 완료해 주세요!", 0x04, 9062475);
	} else if (status == 10) {
		qm.sendOkS("그럼, 이번 주도 저를 위해 힘내주세요!", 0x04, 9062475);
		qm.dispose();
	}
}

function statusplus(millsecond) {
	qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}