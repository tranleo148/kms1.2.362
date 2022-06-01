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
    }
    if (mode == 1) {
        d = status;
        status++;
    }


    if (status == 0) {
        qm.sendYesNoS("지금 바로 #b#e<블루밍 포레스트>#n#k로 이동하시겠어여?", 4, 9062537);
    } else if (status == 1) {
        if (qm.getPlayer().getMapId() == 993192000) {
            qm.getPlayer().dropMessage(5, "이미 <블루밍 포레스트>에 있습니다.");
        } else {
            qm.getPlayer().setKeyValue(100793, "rMap", qm.getPlayer().getMapId() +"");
            qm.warp(993192000);
        }
        qm.dispose();
    }
}
function statusplus(millsecond) {
    qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}