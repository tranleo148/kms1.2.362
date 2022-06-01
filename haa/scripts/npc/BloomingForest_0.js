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
			cm.getClient().send(CField.UIPacket.getDirectionStatus(true));
			cm.getClient().send(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
			cm.getClient().send(SLFCGPacket.SetStandAloneMode(true));
			cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 15));
			cm.getClient().send(CField.fireBlink(cm.getPlayer().getId(), new java.awt.Point(-1450, 79)));
			cm.getClient().send(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 0, 2));
			cm.getClient().send(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 250, 2));
			statusplus(250);
		}
	} else if (status == 1) {
		cm.getClient().send(CField.UIPacket.getDirectionStatus(true));
		cm.getClient().send(SLFCGPacket.cMakeBlind(0x19, 0, 0, 0, 0, 1000, 3000, 1, 0));
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 0, 1000, -1, -1, -1));
		statusplus(300);
	} else if (status == 2) {
		cm.getClient().send(SLFCGPacket.cMakeBlind(0x1A, 0xE8, 3, 0, 0, 0, 0, 0, 0));
		statusplus(1000);
	} else if (status == 3) {
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 0, 1000, 0, -950, -30));
		cm.getClient().send(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 0, 2));
		statusplus(100);
	} else if (status == 4) {
		cm.getClient().send(SLFCGPacket.MakeBlind(0, 0, 0, 0, 0, 250, 0));
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 5, 1, 0, 0));
		statusplus(300);
	} else if (status == 5) {
		cm.getClient().send(SLFCGPacket.BlackLabel("#fn나눔고딕 ExtraBold##fs18#<아르카나 깊은 곳>", 100, 1000, 6, -50, -50, 1, 4));
		statusplus(2500);
	} else if (status == 6) {
		cm.sendNextS("분명 아르카나에 #r#e에르다의 빛#n#k이 생겼다고 했는데...", 57);
	} else if (status == 7) {
		cm.sendNextS("너무 어두운걸...?", 57);
	} else if (status == 8) {
		statusplus(1000);
	} else if (status == 9) {
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 17, 2, 400, 0));
		statusplus(4000);
	} else if (status == 10) {
		cm.sendNextS("흠... 대체 #r밝은 빛#k이 어디 있다는 거야?", 57);
	} else if (status == 11) {
		statusplus(1000);
	} else if (status == 12) {
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("Effect/OnUserEff.img/emotion/whatl", 2, 0, 20, -20, 0, 1, 0, 0, 0, 0));
		statusplus(2000);
	} else if (status == 13) {
		cm.removeNpc(9062536);
		npc1 = cm.spawnNpc2(9062536, new Point(-580, -110), true);
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "summon", 0, false));
		statusplus(1000);
	} else if (status == 14) {
		cm.sendNextS("보이지 않아도 느낄 수 있지~ 오, 무엇을~?", 37, 9062536);
	} else if (status == 15) {
		cm.sendNextS("#r#fs20#따듯한 마음을~ 빛나는 태양을~", 37, 9062536);
	} else if (status == 16) {
		cm.sendNextS("모두가 축하하지~ 오, 무엇을~?", 37, 9062536);
	} else if (status == 17) {
		cm.sendNextS("#r#fs20#메이플스토리의 18번째 생.일.을~!", 37, 9062536);
	} else if (status == 18) {
		cm.sendNextS("바람의 정령! 혹시 #b<블루밍 포레스트>#k로 가는 길을 알아?", 57);
	} else if (status == 19) {
		statusplus(1000);
	} else if (status == 20) {
		cm.sendNextS("자아, 이미 도착했어~ 흠…", 37, 9062536);
	} else if (status == 21) {
		cm.sendNextS("우훗! 길은 #r눈앞#k에~ 있다네", 37, 9062536);
	} else if (status == 22) {
		cm.sendNextS("그럼 이만! #b바람#k처럼 나타난 것은~ #b바람#k처럼 사라지는 게 인지상정~", 37, 9062536);
	} else if (status == 23) {
		cm.removeNpc(9062536);
		statusplus(1000);
	} else if (status == 24) {
		cm.sendNextS("뭐라는 거야...", 57);
	} else if (status == 25) {
		statusplus(1000);
	} else if (status == 26) {
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("Effect/OnUserEff.img/emotion/oh", 2, 0, 0, -20, 0, 1, 0, 0, 0, 0));
		statusplus(2000);
	} else if (status == 27) {
		cm.removeNpc(9062536);
		cm.sendNextS("아! 혹시... 이 안으로 들어가야 하나?", 57);
	} else if (status == 28) {
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 17, 2, 120, 0));
		statusplus(1200);
	} else if (status == 29) {
		cm.getClient().send(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 500, 2));
		statusplus(500);
	} else if (status == 30) {
		cm.getClient().send(SLFCGPacket.cMakeBlind(0x19, 0, 0, 0, 0, 1000, 3000, 1, 0));
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 0, 1000, -1, -1, -1));
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 5, 1, 0, 0));
		cm.getClient().send(SLFCGPacket.cMakeBlind(0x1A, 0xE8, 3, 0, 0, 0, 0, 0, 0));
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 5, 1, 0, 0));
		cm.getClient().send(CField.UIPacket.getDirectionStatus(false));
		cm.getClient().send(SLFCGPacket.SetIngameDirectionMode(false, false, false, false));
		cm.getClient().send(SLFCGPacket.SetStandAloneMode(false));
		cm.dispose();
		cm.warp(993192003);
	}
}

function statusplus(millsecond) {
	cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}