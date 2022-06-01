importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server);
importPackage(Packages.constants);

var status = -1;

function start(mode, type, selection) {
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


    if (status == 0) {
        qm.getClient().send(SLFCGPacket.ExpPocket(qm.getPlayer(), GameConstants.ExpPocket(qm.getPlayer().getLevel()), 10));
        if (qm.getClient().getCustomData(247, "T") != null) {
            qm.getClient().send(CWvsContext.InfoPacket.updateClientInfoQuest(247, "T=" + qm.getClient().getCustomData(247, "T")));
        }
        qm.getClient().send(CField.UIPacket.openUI(1207));
        qm.dispose();
    }
}
function statusplus(millsecond) {
    qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}