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
		qm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.openUI(1290));
		qm.dispose();
	}
}

function statusplus(millsecond) {
	qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}