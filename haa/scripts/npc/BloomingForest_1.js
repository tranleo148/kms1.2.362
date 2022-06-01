importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server);
 
var status = -1;
var npc1 = 0;

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
		cm.getClient().send(CField.UIPacket.getDirectionStatus(true));
		cm.getClient().send(SLFCGPacket.SetIngameDirectionMode(true, true, false, false));
		cm.getClient().send(SLFCGPacket.SetStandAloneMode(true));
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 15));
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 10, 1));
		cm.getClient().send(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 500, 2));
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 0, 1000, 0, -460, -50));
		cm.AllInvisibleName(true);
		statusplus(100);
	} else if (status == 1) {
		cm.getClient().send(SLFCGPacket.MakeBlind(0, 0, 0, 0, 0, 1000, 0));
		statusplus(1000);
	} else if (status == 2) {
		cm.getClient().send(CField.UIPacket.detailShowInfo("울창한 나무 사이를 지나자, 밝은 햇살이 드러납니다.", 3, 20, 20));
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 7000, 1000, 7000, -200, -50));
		statusplus(7000);
	} else if (status == 3) {
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 5000, 800, 5000, 50, 500));
		statusplus(5000);
	} else if (status == 4) {
		statusplus(1500);
	} else if (status == 5) {
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 22, 700));
		statusplus(700);
	} else if (status == 6) {
		cm.getClient().send(SLFCGPacket.cMakeBlind(0x19, 0, 0, 0, 0, 1000, 3000, 1, 0));
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 0, 1000, -1, -1, -1));
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 5, 1, 0, 0));
		cm.getClient().send(SLFCGPacket.cMakeBlind(0x1A, 0xE8, 3, 0, 0, 0, 0, 0, 0));
		cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 5, 1, 0, 0));
		cm.getClient().send(CField.UIPacket.getDirectionStatus(false));
		cm.getClient().send(SLFCGPacket.SetIngameDirectionMode(false, false, false, false));
		cm.getClient().send(SLFCGPacket.SetStandAloneMode(false));
		cm.getPlayer().changeSkillLevel(80003035, 1, 1);
		cm.dispose();
		cm.warp(993192007);
	}
}

function statusplus(millsecond) {
	cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}