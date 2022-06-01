importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server);

var status = -1;
var npc1 = 0, npc2 = 0, npc3 = 0; 
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
			cm.removeNpc(9062530);
			cm.removeNpc(9062533);
			cm.removeNpc(9062537);
			cm.getClient().send(CField.UIPacket.getDirectionStatus(true));
			cm.getClient().send(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
			cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 10, 1));
			cm.getClient().send(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 0, 0));
			cm.getClient().send(SLFCGPacket.SetStandAloneMode(true));
			cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 0, 1500, 0, 36, 550));
			cm.getClient().send(CField.fireBlink(cm.getPlayer().getId(), new java.awt.Point(-145, 514)));
			npc1 = cm.spawnNpc2(9062530, new Point(43, 539), true);
			cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "summon", 0, false));
			npc2 = cm.spawnNpc2(9062537, new Point(-253, 545), false);
			cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc2, "summon", 0, false));
			npc3 = cm.spawnNpc2(9062533, new Point(45, 572), true);
			cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc3, "summon", 0, false));
			cm.getClient().send(SLFCGPacket.SetNpcSpecialAction2(npc3, 0, 10));
			cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "special3", -1, true));
			cm.getClient().send(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 0, 2));
			statusplus(1200);
		}
	} else if (status == 1) {
		cm.getClient().send(SLFCGPacket.MakeBlind(0, 0, 0, 0, 0, 1000, 0));
		statusplus(1400);
	} else if (status == 2) {
		cm.sendNextS("#face0#우와~ #r붉은 꽃#k을 봐여!", 37, 9062537);
	} else if (status == 3) {
		cm.sendNextS("#face0#봉오리가 아름답게 빛나고 있어여!", 37, 9062537);
	} else if (status == 4) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction4(npc1, "special", "special2", 1, 2, 1, -1));
		cm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/fullMoonParty_magicWand/userEff"));
		statusplus(4000);
	} else if (status == 5) {
		cm.sendNextS("#face0#붉은 꽃이 활짝 폈어여!", 37, 9062537);
	} else if (status == 6) {
		cm.sendNextS("#face0#꽃이 피어나면 #r붉은 꽃#k의 정령이 곧 깨어날 거예여!", 37, 9062537);
	} else if (status == 7) {
		cm.getClient().send(SLFCGPacket.SetNpcSpecialAction2(npc3, 255, 1000));
		statusplus(1500);
	} else if (status == 8) {
		cm.sendNextS("#face0##b#h0##k님! #r붉은 꽃#k의 정령, 하나에여!", 37, 9062537);
	} else if (status == 9) {
		cm.sendNextS("#face0#헤헤. 들꽃의 정령!", 37, 9062533);
	} else if (status == 10) {
		cm.sendNextS("#face0##b#h0##k님도 안녕하세요?!", 37, 9062533);
	} else if (status == 11) {
		cm.sendNextS("#face0#정말... 따듯하고 아름다운 봄이네요!", 37, 9062533);
	} else if (status == 12) {
		cm.sendNextS("#face0#얼른 세상을 구경하고 싶어요!", 37, 9062533);
	} else if (status == 13) {
		statusplus(500);
	} else if (status == 14) {
		cm.getClient().send(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 500, 2));
		statusplus(500);
	} else if (status == 15) {
		statusplus(500);
	} else if (status == 16) {
		cm.removeNpc(9062530);
		cm.removeNpc(9062533);
		cm.removeNpc(9062537);
		cm.getClient().send(CField.UIPacket.getDirectionStatus(false));
		cm.getClient().send(SLFCGPacket.SetIngameDirectionMode(false, false, false, false));
		cm.getClient().send(SLFCGPacket.SetStandAloneMode(false));
		cm.forceCompleteQuest(501375);
		cm.getPlayer().setKeyValue(501375, "start", "1");
		cm.getPlayer().setKeyValue(501387, "flower", "2");
		cm.getClient().setKeyValue("BloomingTuto", "3");
		cm.warp(993192000);
		cm.dispose();
	}
}

function statusplus(millsecond) {
	cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}