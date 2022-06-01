importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server);

var status = -1;
var check = false;
var check1 = false;
var check2 = false;
var check3 = false;
var check4 = false;
var check5 = false;
var check6 = false;
var check7 = false;
var check8 = false;
var check9 = false;
var check10 = false;
var check11 = false;
var npc1 = 0;
var npc2 = 0;

function start(mode, type, selection) {
	dialogue = [
		["next", 4, "#e#b#h0##k#n! 잘 지냈어?\r\n\r\n정말 #r#e엄청난 소식#n#k이 있어서 알려주려고 왔어!", 9010010],
		["nextprev", 4, "#b#e그란디스#n#k와 #r#e메이플 월드#n#k가 원래 #e하나의 차원#n이었다는 ..소식은 들었지", 9010010],
		["nextprev", 4, "두 차원이 합쳐지면서 메이플 월드와 그란디스의 #b#e바다가 만나는 곳#n#k에 아주 #r#e신비한 성#n#k이 생겨났어!\r\n\r\n#e우주를 담은 듯 아주 아름다운 성#n이라고 하더군!", 9010010],
		["nextprev", 4, "바다 위의 신비한 성!\r\n\r\n#fs15##r#e성의 이름은 바로... 네오 캐슬!", 9010010],
		["nextprev", 4, "네오 캐슬의 #b#e정령들#n#k이 메이플 월드와 그란디스의 사람들을 #r#e초대#n#k하고 있어!", 9010010],
		["nextprev", 2, "네오 캐슬의 #b#e정령들#n#k이 초대를...?", 9010010],
		["yesno", 4, "그래! #e#b#h0##k#n! \r\n다들 초대를 받고 벌써 네오 캐슬로 갔는걸?\r\n어때? #r#e네오 캐슬#n#k에 관심 있어?\r\n\r\n #r※ 수락 시 이벤트 맵으로 이동합니다.", 9010010]
	]
	if (mode == -1) {
		qm.dispose();
		return;
	}
	if (mode == 0) {
		status--;
	}
	if (mode == 1) {
		d = status;
		status++;
	}

	if (status < dialogue.length) {
		sendByType(dialogue[status][0], dialogue[status][1], dialogue[status][2], dialogue[status][3]);
	} else if (status == 7) {
		if (qm.getPlayer().getMapId() != 993189101) {
			qm.warp(993189101);
		}
		qm.giveBuff(80002996);
		qm.getClient().send(CField.UIPacket.getDirectionStatus(true));
		qm.getClient().send(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
		qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x10));
		qm.getClient().send(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 0, 0));
		statusplus(1200);
	} else if (status == 8) {
		qm.getClient().send(SLFCGPacket.MakeBlind(1, 0, 0, 0, 0, 0, 0));
		statusplus(2000);
	} else if (status == 9) {
		qm.sendCustom("거의 다 온 것 같다...", 0x39, 0);
	} else if (status == 10) {
		statusplus(1000);
	} else if (status == 11) {
		qm.sendCustom("정령들이 보내준 순록을 타고 오니 금방 도착하는군.", 0x39, 0);
	} else if (status == 12) {
		statusplus(2000);
	} else if (status == 13) {
		qm.getClient().send(SLFCGPacket.MakeBlind(1, 0, 0, 0, 0, 0, 2000, 0));
		qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x16, 700));
		qm.getClient().send(SLFCGPacket.SetIngameDirectionMode(false, true, false, false));
		if (qm.getPlayer().getMapId() != 993189102) {
			qm.warp(993189102);
			qm.getClient().send(CField.UIPacket.getDirectionStatus(true));
			qm.getClient().send(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
			qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x0F));
			qm.getClient().send(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 0, 0));
			statusplus(500);
		}
	} else if (status == 14) {
		qm.getClient().send(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 0, 0));
		statusplus(100);
	} else if (status == 15) {
		qm.getClient().send(SLFCGPacket.MakeBlind(0, 0, 0, 0, 0, 250, 0));
		statusplus(300);
	} else if (status == 16) {
		qm.getClient().send(SLFCGPacket.BlackLabel("#fn나눔고딕 ExtraBold##fs18#<네오 캐슬> 입구", 100, 1000, 6, -50, -50, 1, 4));
		statusplus(4000);
	} else if (status == 17) {
		qm.sendCustom("저곳이 네오 캐슬이구나.", 0x39, 0);
	} else if (status == 18) {
		qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x11, 2, 300, 0));
		statusplus(3000);
	} else if (status == 19) {
		qm.sendCustom("듣던 대로 정말 아름답다.\r\n어서 가보자.", 0x39, 0);
	} else if (status == 20) {
		statusplus(1000);
	} else if (status == 21) {
		//	if (check == false) {
		//	check = true;
		qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x07, 2000, 1000, 2000, -520, -130));
		qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x11, 2, 300, 0));
		statusplus(1000);
		//	}
	} else if (status == 22) {
		qm.getClient().send(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 500, 2));
		statusplus(2000);
	} else if (status == 23) {
		qm.warp(993189103);
		qm.getClient().send(CField.UIPacket.getDirectionStatus(true));
		qm.getClient().send(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
		qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x10, 0, 0, 0, 0, 0, 0));
		qm.getClient().send(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 0, 0));
		
		statusplus(1000);
	} else if (status == 24) {
		if (check == false) {
			check = true;
			qm.getClient().send(CField.UIPacket.getDirectionStatus(true));
			qm.getClient().send(SLFCGPacket.cMakeBlind(0x19, 0, 0, 0, 0, 1000, 3000, 1));
			qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x07, 0, 1000, -1, -1, -1));
			qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x05, 1, 0, 0));
			qm.getClient().send(SLFCGPacket.cMakeBlind(0x1A, 1000, 0, 0, 0, 0, 0, 0));
			statusplus(1000);
		}
	} else if (status == 25) {
		if (check1 == false) {
			check1 = true;
			qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x07, 0, 2000, 0, -1500, 100));
			npc1 = qm.spawnNpc2(9062474, new Point(-1000, 30));
			qm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "summon", 0, false));
			npc2 = qm.spawnNpc2(9062475, new Point(-1080, 30));
			qm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc2, "summon", 0, false));
			qm.getClient().send(SLFCGPacket.MakeBlind(0, 0, 0, 0, 0, 2000, 0));
			statusplus(2000);
		}
	} else if (status == 26) {
		if (check2 == false) {
			check2 = true;
			qm.getClient().send(SLFCGPacket.BlackLabel("#fn나눔고딕 ExtraBold##fs18#<네오 캐슬>", 100, 1000, 6, -50, -50, 1, 4));
			statusplus(4000);
		}
	} else if (status == 27) {
		qm.sendCustom("우와...", 0x39, 0);
	} else if (status == 28) {
		qm.sendCustom("가까이서 보니 정말 #r#e거대한 성#n#k이잖아...?", 0x39, 0);
	} else if (status == 29) {
		statusplus(1000);
	} else if (status == 30) {
		qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x11, 2, 400, 0));
		qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x07, 4000, 2000, 4000, -1100, 100));
		statusplus(4000);
	} else if (status == 31) {
		qm.getClient().send(SLFCGPacket.InGameDirectionEvent("Effect/OnUserEff.img/emotion/whatl", 0x02, 0, 0, 0, 0, 1, 0, 0, 0, 0));
		statusplus(500);
	} else if (status == 32) {
		qm.getClient().send(SLFCGPacket.InGameDirectionEvent("Effect/OnUserEff.img/emotion/oh", 0x02, 0, 0, 0, 0, 1, npc1, 0, 0, 0));
		statusplus(500);
	} else if (status == 33) {
		qm.getClient().send(SLFCGPacket.InGameDirectionEvent("Effect/OnUserEff.img/emotion/oh", 0x02, 0, 0, 0, 0, 1, npc2, 0, 0, 0));
		qm.getClient().send(CField.NPCPacket.setNPCMotion(npc2, -1));
		statusplus(1500);
	} else if (status == 34) {
		qm.sendCustom("와! #e#b#h0##k!!!", 0x25, 9062475);
	} else if (status == 35) {
		qm.sendCustom("헤헤! 기다렸어요!\r\n#fs30##r반가워요! 반갑죠? 환영해요!", 0x25, 9062475);
	} else if (status == 36) {
		qm.sendCustom("#b제가 보낸 순록#k은 잘 타고 왔어요?", 0x25, 9062475);
	} else if (status == 37) {
		statusplus(1000);
	} else if (status == 38) {
		qm.getClient().send(SLFCGPacket.getNpcMoveAction(npc1, -1, 10, 100));
		statusplus(2000);
	} else if (status == 39) {
		qm.sendCustom("진정해, 리오.\r\n모처럼 찾아온 사람을 놀라게 하면 못써", 0x25, 9062474);
	} else if (status == 40) {
		qm.sendCustom("앗... 미안해요!\r\n저는 #b리오#k라고 해요!", 0x25, 9062475);
	} else if (status == 41) {
		qm.sendCustom("후후. #e#b#h0##k. \r\n드디어 왔구나. 나는 #b르네#k라고 해.", 0x25, 9062474);
	} else if (status == 42) {
		qm.sendCustom("이곳은 #r<네오 캐슬>#k이야.\r\n차원이 겹쳐진 곳에 비치는 덧없는 #b#e환상#n#k이지.", 0x25, 9062474);
	} else if (status == 43) {
		qm.sendCustom("너희가 네오 캐슬의 정령이구나.\r\n그런데... #b환상#k이라니?", 0x39, 0);
	} else if (status == 44) {
		qm.sendCustom("여긴 #b서로 다른 두 차원#k이 합쳐지면서 생겨난 공간이에요!\r\n차원이 안정을 찾으면 네오 캐슬은 다시 사라질 거예요!", 0x25, 9062475);
	} else if (status == 45) {
		qm.sendCustom("후후. 그래서 다들 초대한 거야...이미 많은 사람들이 초대를 받고 네오 캐슬에 도착했어!", 0x25, 9062474);
	} else if (status == 46) {
		qm.sendCustom("그럼 먼저 온 사람들을 소개해 줄게.", 0x25, 9062474);
	} else if (status == 47) {
		if (check3 == false) {
			check3 = true;
			qm.getClient().send(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 250, 2));
			statusplus(250);
		}
	} else if (status == 48) {
		if (check4 == false) {
			check4 = true;
			qm.getClient().send(SLFCGPacket.cMakeBlind(0x19, 0, 0, 0, 0, 1000, 3000, 1));
			qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x07, 0, 1000, -1, -1, -1));
			qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x05, 1, 0, 0));
			statusplus(300);
		}
	} else if (status == 49) {
		if (check5 == false) {
			check5 = true;
			qm.getClient().send(SLFCGPacket.cMakeBlind(0x1A, 1000, 0, 0, 0, 0, 0, 0));
			qm.removeNpc(9062474);
			qm.removeNpc(9062475);
			qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x07, 0, 2000, 0, -500, -100));
			statusplus(1000);
		}
	} else if (status == 50) {
		if (check6 == false) {
			check6 = true;
			qm.getClient().send(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 0, 2));
			statusplus(100);
		}
	} else if (status == 51) {
		if (check7 == false) {
			check7 = true;
			qm.getClient().send(SLFCGPacket.MakeBlind(0, 0, 0, 0, 0, 0, 2));
			statusplus(300);
		}
	} else if (status == 52) {
		qm.sendCustom("혼자서도 잘 노는 사람! 여기 붙어라~", 0x25, 9062461);
	} else if (status == 53) {
		qm.sendCustom("나보다 게임을 잘하는 사람은 없을걸?!", 0x25, 9062462);
	} else if (status == 54) {
		qm.sendCustom("여기는 모두들 #b모여서 게임#k을 하고 있어요!", 0x25, 9062475);
	} else if (status == 55) {
		qm.sendCustom("게임이라니... 유치한 어린이들 같으니. ..나는 #r성공한 상인#k이 될 거야!", 0x25, 9062463);
	} else if (status == 56) {
		qm.sendCustom("#b게임#k을 즐기면 #i4310307:##b#t4310307##k을 얻을 수 있어요", 0x25, 9062475);
	} else if (status == 57) {
		qm.sendCustom("#i4310307:##b#t4310307##k을 사용해서 저 상인 꿈나무 친구에게 #r멋진 물건#k을 구할 수 있을 거야.", 0x25, 9062474);
	} else if (status == 58) {
		qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x07, 1500, 2000, 1500, -250, -100));
		statusplus(1500);
	} else if (status == 59) {
		qm.sendCustom("흠... 정말 강한 힘이 느껴지는군. 만족스러워.", 0x25, 9062459);
	} else if (status == 60) {
		qm.sendCustom("저 사람은 아주 강한 힘을 가진 보석, \r\n#i4310308:##r#t4310308##k를 구하고 있어", 0x25, 9062474);
	} else if (status == 61) {
		qm.sendCustom("#b강한 보스#k를 처치하면 #i4310308:##r#t4310308##k를 만들 수 있대요!", 0x25, 9062475);
	} else if (status == 62) {
		qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x07, 3000, 2000, 3000, 400, -100));
		statusplus(3000);
	} else if (status == 63) {
		qm.sendCustom("오호... 당신 꽤나 좋은 물건을 가지고 있군?", 0x25, 9062457);
	} else if (status == 64) {
		qm.sendCustom("후후. 메이플 월드에는 진귀한 물건이 많답니다.\r\n그란디스에도 신비한 물건이 참 많군요.", 0x25, 9062455);
	} else if (status == 65) {
		qm.sendCustom("여기에는 #r다양한 상인분들#k이 있답니다!", 0x25, 9062475);
	} else if (status == 66) {
		qm.sendCustom("저 상인들은 네오 캐슬이 생겨난 힘의 원천, \r\n#i4310306:##b#t4310306##k를 주면 #r진귀한 물건#k을 줄 거야.", 0x25, 9062474);
	} else if (status == 67) {
		qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x07, 1500, 2000, 1500, 700, -100));
		statusplus(1500);
	} else if (status == 68) {
		npc1 = qm.spawnNpc2(9062453, new Point(-230, -320));
		qm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "summon", 0, false));
		npc2 = qm.spawnNpc2(9062454, new Point(230, -320));
		qm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc2, "summon", 0, false));
		qm.sendCustom("꾸준한 훈련만이 평화를 지킬 힘을 기를 수 있습니다.", 0x25, 9062451);
	} else if (status == 69) {
		qm.sendCustom("초대받은 곳에서도 열심히 훈련하는 사람도 있지.", 0x25, 9062474);
	} else if (status == 70) {
		qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x07, 2000, 2000, 2000, 0, -300));
		statusplus(2000);
	} else if (status == 71) {
		qm.sendCustom("정말 #b메이플 월드#k와 #r그란디스#k의 다양한 사람들이 모여 있구나...", 0x39, 0);
	} else if (status == 72) {
		qm.sendCustom("그럼요! 너무 늦게 오셨어요!", 0x25, 9062475);
	} else if (status == 73) {
		qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x07, 4000, 800, 4000, 0, -360));
		statusplus(4000);
	} else if (status == 74) {
		qm.sendCustom("후후. #r네오 캐슬#k이 부디 너에게도 좋은 추억으로 남길 바랄게.", 0x25, 9062474);
	} else if (status == 75) {
		qm.sendCustom("저희와 함께 #r네오 캐슬#k에서 좋은 시간 보내요!", 0x25, 9062475);
	} else if (status == 76) {
		qm.warp(993189100);
		qm.getPlayer().getClient().send(SLFCGPacket.MakeBlind(0, 0, 0, 0, 0, 250, 0));
		qm.getPlayer().getClient().send(SLFCGPacket.SetStandAloneMode(false));
		qm.getPlayer().getClient().send(SLFCGPacket.InGameDirectionEvent("", 22, 700));
		qm.getPlayer().getClient().send(SLFCGPacket.SetIngameDirectionMode(false, true, false, false));
		qm.forceCompleteQuest();
	}
}

function sendByType(type, type2, text) {
	switch (type) {
		case "next":
			qm.sendNextS(text, type2, 1012110, 0);
			break;
		case "nextprev":
			qm.sendNextPrevS(text, type2, 1012110, 0);
			break;
		case "yesno":
			qm.sendYesNoS(text, type2, 1012110);
			break;
		default:
			qm.sendOk(text, 1012110);
			break;
	}
}

function sendByType(type, type2, text, npc) {
	switch (type) {
		case "next":
			qm.sendNextS(text, type2, npc, 0);
			break;
		case "nextprev":
			qm.sendNextPrevS(text, type2, npc, 0);
			break;
		case "yesno":
			qm.sendYesNoS(text, type2, npc);
			break;
		default:
			qm.sendOk(text, 1012110);
			break;
	}
}


function statusplus(millsecond) {
	qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}