importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server);

var status = -1;
var check = false;
var check2 = false;
var witch = 0;

function start(mode, type, selection) {
	dialogue = [
		["next", 4, "휴우... 그... 그럼 말씀드릴게요... 왜 #h0#님을 불렀는지..."],
		["nextprev", 4, "여느 때와 다르지 않은 날이었어요..."],
		["nextprev", 4, "그날도 전 혼자 종이비행기를 날리며 놀던 중이었는데..."]
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
		sendByType(dialogue[status][0], dialogue[status][1], dialogue[status][2]);
	} else if (status == 3) {
		if (qm.getPlayer().getMapId() != 910143000) {
			qm.warp(910143000);
		}
		qm.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
		qm.getClient().getSession().writeAndFlush(SLFCGPacket.SetStandAloneMode(true));
		qm.getClient().getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("", 0x0E));
		qm.getClient().getSession().writeAndFlush(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 0, 0));
		qm.getPlayer().setKeyValue(37152, "plane", "on");
		qm.sendPacket(435, "0B 00 00 00 00")
		statusplus(1000);
	} else if (status == 4) { // 무한반복 하는데 왜 그런지 봐주셈 (일단 check로 임시처리함.)
		if (check == false) {
			check = true;
			qm.getClient().getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("", 0x07, 0, 1000, 0, -873, 100));
			statusplus(1000);
		}
	} else if (status == 5) {
		qm.sendPacket(435, "0C")
		qm.getClient().getSession().writeAndFlush(SLFCGPacket.MakeBlind(0, 0, 0, 0, 0, 1000, 0));
		statusplus(1000);
	} else if (status == 6) {
		qm.getClient().getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("", 0x10, 2, 350));
		statusplus(3000);
	} else if (status == 7) {
		sendByType("next", 0, "히잉... 이쪽으로 날아간 것 같은데... 어디 갔지?");
	} else if (status == 8) {
		qm.getClient().getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("", 0x11, -1));
		statusplus(2000);
	} else if (status == 9) {
		sendByType("next", 0, "앗! 저기 떨어져 있네.");
	} else if (status == 10) {
		qm.getClient().getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("", 0x03, 8));
		statusplus(1000);
	} else if (status == 11) {
		qm.getClient().getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("", 0x10, 1, 600));
		statusplus(6000);
	} else if (status == 12) {
		sendByType("next", 0, "어쩌다 여기까지 날아온 거지... 어서 돌아가야지... 슬라임이라도 나타나면...");
	} else if (status == 13) {
		qm.getPlayer().setKeyValue(37152, "plane", "off");
		statusplus(500);
	} else if (status == 14) {
		qm.getClient().getSession().writeAndFlush(CField.musicChange("Bgm00/Silence"));
		statusplus(500);
	} else if (status == 15) {
		qm.getClient().getSession().writeAndFlush(SLFCGPacket.PlayAmientSound("SoundEff.img/Elodin/forest", 40, 60));
		statusplus(2500);
	} else if (status == 16) {
		qm.getClient().getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("Effect/OnUserEff.img/emotion/oh", 0x02, 0, 0, 0, 0, 0));
		statusplus(2000);
	} else if (status == 17) {
		sendByType("next", 0, "으으... 뭔가 나타날 것만 같아... 빨리 가자...");
	} else if (status == 18) {
		statusplus(500);
	} else if (status == 19) {
		qm.getClient().getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("", 0x11, 1));
		statusplus(500);
	} else if (status == 20) {
		qm.getClient().getSession().writeAndFlush(SLFCGPacket.PlayAmientSound("SoundEff.img/Elodin/scream_far", 100, 60));
		statusplus(2000);
	} else if (status == 21) {
		qm.getClient().getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("Effect/OnUserEff.img/emotionBalloon/exclamation3", 0x02, 0, 0, 0, 0, 0));
		statusplus(2000);
	} else if (status == 22) {
		sendByType("next", 0, "누군가의... 비명소리?");
	} else if (status == 23) {
		statusplus(1000);
	} else if (status == 24) {
		if (check2 == false) {
			check2 = true;
			qm.getClient().getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("", 0x07, 1000, 1000, 1000, -873, 415));
			qm.sendPacket(435, "19 00 00 00 00 E8 03 00 00 B8 0B 00 00 01")
			qm.getClient().getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("", 0x07, 0, 2000, 0, -873, 530));
			statusplus(1300);
		}
	} else if (status == 25) {
		qm.sendPacket(435, "1A E8 03 00 00")
		statusplus(3000);
	} else if (status == 26) {
		witch = qm.spawnNpc2(1501012, new Point(-886, 533));
		qm.getClient().getSession().writeAndFlush(SLFCGPacket.SetNpcSpecialAction(witch, "summon", 0, false));
		statusplus(2000);
	} else if (status == 27) {
		sendByType("next", 0, "으..... 으아아아아아!!! 마... 마녀다!!!");
	} else if (status == 28) {
		statusplus(500);
	} else if (status == 29) {
		qm.getClient().getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("", 0x10, 2, 350));
		qm.sendPacket(435, "0B 00 00 00 00")
		statusplus(2000);
	} else if (status == 30) {
		qm.sendPacket(743, "02 2B 00 45 66 66 65 63 74 2F 4F 6E 55 73 65 72 45 66 66 2E 69 6D 67 2F 65 6D 6F 74 69 6F 6E 42 61 6C 6C 6F 6F 6E 2F 6E 6F 53 70 65 61 6B 00 00 00 00 8A FC FF FF 47 02 00 00 01 00 00 00 00 01 00 00 00 00 01 00");
		statusplus(3000);
	} else if (status == 31) {
		qm.getClient().getSession().writeAndFlush(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 500, 0));
		statusplus(500);
	} else if (status == 32) {
		statusplus(500);
	} else if (status == 33) {
		qm.getClient().getSession().writeAndFlush(SLFCGPacket.StopAmientSound("SoundEff.img/Elodin/scream_far"));
		qm.getClient().getSession().writeAndFlush(SLFCGPacket.StopAmientSound("SoundEff.img/Elodin/forest"));
		statusplus(500);
	} else if (status == 34) {
		statusplus(1000);
	} else if (status == 35) {
		qm.sendPacket(435, "0C")
		qm.sendPacket(435, "19 00 00 00 00 E8 03 00 00 B8 0B 00 00 01")
		qm.sendPacket(743, "07 00 00 00 00 E8 03 00 00 FF FF FF 7F FF FF FF 7F FF FF FF 7F");
		qm.getClient().getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("", 0x05, 1, 0, 0));
		statusplus(300);
	} else if (status == 36) {
		qm.sendPacket(435, "1A E8 03 00 00")
		qm.getClient().getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("", 0x05, 1, 0, 0));
		qm.removeNpc(1501012);
		qm.forceStartQuest(37152);
		qm.warp(101000000);
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

function statusplus(millsecond) {
	qm.getClient().getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}