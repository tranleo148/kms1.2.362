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
		status--;
		qm.dispose();
		return;
	}
	if (mode == 1) {
		d = status;
		status++;
	}

	if (status == 0) {
		if (qm.getPlayer().getMapId() == 993189100) {
			qm.getPlayer().dropMessage(5, "이미 <네오 캐슬>에 있습니다.");
			qm.dispose();
		} else {
			qm.sendYesNoS("지금 바로 #b#e<네오 캐슬>#n#k으로 이동할래?", 4, 9062474);
		}
	} else if (status == 1) {
		qm.getPlayer().setKeyValue(210222, "returnMap", "" + qm.getPlayer().getMapId());
		qm.warp(993189100);
		qm.dispose();
	}
}

function statusplus(millsecond) {
	qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}