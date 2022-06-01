importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server);

var status = -1;
var npc1 = 0;
var check = false;

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
		if (!check) {
			check = true;
			cm.removeNpc(9062537);
			npc1 = cm.spawnNpc2(9062537, new Point(-200, 570), false);
			cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "summon", 0, false));
			cm.getClient().send(CField.UIPacket.getDirectionStatus(true));
			cm.getClient().send(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
			cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 15));
			cm.getClient().send(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 0, 2));
			cm.getClient().send(SLFCGPacket.cMakeBlind(0x19, 0, 0, 0, 0, 1000, 3000, 1, 0));
			cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 0, 1000, -1, -1, -1));
			cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 5, 1, 0, 0));
			statusplus(300);
		}
	} else if (status == 1) {
		cm.AllInvisibleName(true);
		cm.getClient().send(SLFCGPacket.cMakeBlind(0x1A, 0xE8, 3, 0, 0, 0, 0, 0, 0));
		cm.getClient().send(CField.fireBlink(cm.getPlayer().getId(), new java.awt.Point(-900, 545)));
		statusplus(1000);
	} else if (status == 2) {
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 0, 1500, 0, -630, 600));
		cm.getClient().send(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 0, 2));
		statusplus(100);
	} else if (status == 3) {
		cm.getClient().send(SLFCGPacket.MakeBlind(0, 0, 0, 0, 0, 250, 0));
		statusplus(300);
	} else if (status == 4) {
		cm.getClient().send(CField.setMapOBJ("all", 0, 0, 0));
		cm.getClient().send(CField.setMapOBJ("0", 1, 0, 0));//꽃 활성화
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 17, 2, 550, 0));
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 3000, 1500, 3000, -380, 600));
		cm.getClient().send(SLFCGPacket.BlackLabel("#fn나눔고딕 ExtraBold##fs18#<블루밍 포레스트>   #fs15##fn나눔고딕#꽃 피는 숲", 100, 2200, 6, -50, -50, 1, 4));
		statusplus(3300);
	} else if (status == 5) {
		cm.sendNextS("이곳이 #b블루밍 포레스트#k구나!", 57);
	} else if (status == 6) {
		cm.sendNextS("#b아르카나#k에 이렇게 많은 꽃이라니...", 57);
	} else if (status == 7) {
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 1000, 1500, 1000, -310, 600));
		statusplus(1000);
	} else if (status == 8) {
		cm.getClient().send(CField.NPCPacket.setNPCMotion(npc1, -1));
		statusplus(500);
	} else if (status == 9) {
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("Effect/OnUserEff.img/emotion/oh", 2, 0, 0, -20, 0, 1, npc1, 0, 0, 0));
		statusplus(2000);
	} else if (status == 10) {
		cm.sendNextS("오! 안녕하세여!!\r\n처음 보는 얼굴이에여!", 37, 9062537);
	} else if (status == 11) {
		cm.sendNextS("저는 #r들꽃의 정령#k이에여! 반가워여!\r\n꽃이 피는 숲! #b<블루밍 포레스트>#k에 오신 것을 환영해여", 37, 9062537);
	} else if (status == 12) {
		cm.sendNextS("저기 너무 아름다운 꽃들 좀 보세여!", 37, 9062537);
	} else if (status == 13) {
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 4000, 820, 4000, 5, 495));
		statusplus(4000);
	} else if (status == 14) {
		cm.sendNextS("어때여? 너무 예쁘죠!", 37, 9062537);
	} else if (status == 15) {
		cm.sendNextS("제가 낮잠을 엄청 좋아하거든여? \r\n얼마 전까지 계속 땅속에 숨어서 잠을 자고 있었어여!", 37, 9062537);
	} else if (status == 16) {
		cm.sendNextS("갑자기 간지러워서 눈을 떠보니 숲에 #r봄기운#k이 가득한 거예여!\r\n정말! 세상은 이렇게 #b아름답고 찬란한 곳#k이었군여!?", 37, 9062537);
	} else if (status == 17) {
		cm.sendNextS("맡지 않아도 느낄 수 있지~ 오, 무엇을~?", 37, 9062515);
	} else if (status == 18) {
		cm.sendNextS("#r#fs20#향긋한 꽃내음~", 37, 9062515);
	} else if (status == 19) {
		cm.sendNextS("...", 37, 9062537);
	} else if (status == 20) {
		cm.sendNextS("그런데...", 37, 9062537);
	} else if (status == 21) {
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 2200, 1500, 3000, 60, 590));
		statusplus(2200);
	} else if (status == 22) {
		cm.sendNextS("여기 아직 #r#e잠들어 있는 친구#n#k들이 있어여... 저는 너무! 슬퍼여!", 37, 9062537);
	} else if (status == 23) {
		cm.sendNextS("그래서여... 용사님께 부탁이 있어여!", 37, 9062537);
	} else if (status == 24) {
		cm.sendNextS("#b블루밍 포레스트#k에서 저랑 같이 잠들어 있는 친구들을 깨우고 \r\n이 #r행복#k을 나누는 거예여!", 37, 9062537);
	} else if (status == 25) {
		cm.sendNextS("제가 #b멋진 선물#k도 팍팍! 드릴게여!\r\n그리고 잠들어 있는 친구들이 깨어나면 #r특별한 축복#k도 받을 수 있다구여?", 37, 9062537);
	} else if (status == 26) {
		cm.sendNextS("#b(잠시 블루밍 포레스트를 구경하는 것도 좋겠다.)", 57);
	} else if (status == 27) {
		cm.sendNextS("좋아! 잠들어 있는 꽃이 피어나도록 도와줄게.", 57);
	} else if (status == 28) {
		cm.sendNextS("도와주실 줄 알았어여! \r\n그럼 저한테 다시 말을 걸어주세여!", 37, 9062537);
	} else if (status == 29) {
		cm.getClient().send(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 250, 2));
		statusplus(500);
	} else if (status == 30) {
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 5, 1, 0, 0));
		statusplus(300);
	} else if (status == 31) {
		cm.removeNpc(9062537);
		cm.getClient().send(SLFCGPacket.cMakeBlind(0x19, 0, 0, 0, 0, 1000, 3000, 1, 0));
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 0, 1000, -1, -1, -1));
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 5, 1, 0, 0));
		cm.getClient().send(SLFCGPacket.cMakeBlind(0x1A, 0xE8, 3, 0, 0, 0, 0, 0, 0));
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 5, 1, 0, 0));
		cm.getClient().send(CField.UIPacket.getDirectionStatus(false));
		cm.getClient().send(SLFCGPacket.SetIngameDirectionMode(false, false, false, false));
		cm.getClient().send(SLFCGPacket.SetStandAloneMode(false));
		cm.forceCompleteQuest(100790);
		cm.getClient().setKeyValue("BloomingTuto", "1");
		cm.getPlayer().setKeyValue(100790, "lv", "1");
		cm.getPlayer().setKeyValue(501378, "sp", "0");
		for (var a = 0; a < 10; a++) {
			cm.getPlayer().setKeyValue(501378, a+"", "0");
		}
		cm.getPlayer().setKeyValue(501387, "flower", "0");
		cm.getPlayer().setKeyValue(501387, "mapTuto", "4");
		cm.getPlayer().setKeyValue(501367, "bloom", "0");
		cm.getPlayer().setKeyValue(501367, "giveSun", "0");
		cm.getPlayer().setKeyValue(501367, "week", "1");
		cm.getPlayer().setKeyValue(501367, "start", "1");
		if (cm.getClient().getKeyValue("getReward") != null) {
			cm.getPlayer().setKeyValue(501367, cm.getClient().getKeyValue("getReward"));
		} else {
			cm.getPlayer().setKeyValue(501367, "getReward","000000000000000000");
		}
		cm.dispose();
		cm.warp(993192000);
	}
}

function statusplus(millsecond) {
	cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}