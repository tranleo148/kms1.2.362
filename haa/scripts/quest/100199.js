importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server);
importPackage(Packages.server.games);

var status = -1;
var sel = 0;

function start(mode, type, selection) {
    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        qm.dispose();
        return;
    }
    if (mode == 1) {
        d = status;
        status++;
    }


    if (status == 0) {
        if (BattleGroundGameHandler.isAlready()) {
            qm.sendOkS("이런.. #b<싸워라! 전설의 귀환>#k이 이미 시작 되어서 참가 할 수가 없어...");
            qm.dispose();
            return;
        }

        qm.sendYesNoS("#e#b<싸워라! 전설의 귀환>#n#k에 참여해라..\r\n\r\n#b(승낙 시 대기맵으로 이동됩니다.)", 4, 2142111);
    } else if (status == 1) {
        //대기열추가
        if (qm.getClient().getChannel() != 1) {
            var id = qm.getPlayer().getId();
            qm.getPlayer().changeChannel(1);
            Packages.server.Timer.EtcTimer.getInstance().schedule(function () {
                BattleGroundGameHandler.AddChangeChannelChr(id);
            }, 1000);
            qm.dispose();
        } else {
            BattleGroundGameHandler.AddChr(qm.getPlayer());
            qm.getPlayer().addKV("returnM", qm.getPlayer().getMapId() + "")
            qm.dispose();
        }
    }
}
function statusplus(millsecond) {
    qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}