// Made By MelonK
var status = -1;
function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode <= 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        stage = cm.getQuestRecord(12345).getCustomData();
        if (stage == null) {
            cm.getQuestRecord(12345).setCustomData("0");
            stage = "1";
        }
        if (parseInt(stage) <= 2) {
            talk = cm.getPlayer().getName() + "님의 이번 LAP은 " + stage + " LAP 입니다. 앞으로 " + (3 - stage) + "바퀴 남았습니다.";
            cm.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CWvsContext.getTopMsg(talk));
            if (cm.getPlayer().getMapId() == 932200200) {
                cm.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.CharReLocationPacket(-343, 2196));
            } else {
                cm.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.CharReLocationPacket(-2203, 2563));
            }
            cm.getQuestRecord(12345).setCustomData((parseInt(stage) + 1) + "");
        } else {
            cm.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.startMapEffect(cm.getPlayer().getName() + " 님께서 1등으로 골인하셨습니다. 1분 후에 플래그 레이스가 종료됩니다.", 5120003, true));
            if (cm.getPlayer().getMapId() == 932200200) {
                cm.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.CharReLocationPacket(-4, 813));
            } else {
                cm.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.CharReLocationPacket(-2069, 813));
            }
            cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.getClock(60));
            cm.getQuestRecord(12345).setCustomData("1");
        }
        cm.dispose();
    }
}