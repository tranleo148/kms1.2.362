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
        if (status == 0) {
            qm.sendOkS("나중에 확인해봐야겠다.",2, 9010010);
            qm.dispose();
            return;
        }
        status--;
    }
    if (mode == 1) {
        d = status;
        status++;
    }


    if (status == 0) {
        qm.sendYesNoS("#r#e붉은색 꽃봉오리#n#k가 곧 피어날 것처럼 빛나고 있다.\r\n확인해볼까?\r\n\r\n #r※ 수락 시 이벤트 맵으로 이동합니다.", 2);
    } else if (status == 1) {
        qm.dispose();
        qm.warp(993192004);
    }
}
function statusplus(millsecond) {
    qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}