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
        cm.dispose();
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        if (cm.getPlayer().getParty().getLeader().getId() == cm.getPlayer().getId()) {
            if (cm.getMap().getNumMonsters() > 0) {
                cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CWvsContext.getTopMsg("필드 내에 남아있는 적이 있습니다."));
                cm.dispose();
            } else {
                cm.sendYesNoS("그럼 이동해볼까?", 2);
            }
        } else {
            cm.getPlayer().dropMessage(5, "파티장만 입장을 신청할 수 있습니다.");
            cm.dispose();
            return;
        }
    } else if (status == 1) {
        var eim = cm.getPlayer().getEventInstance();
        if (eim != null) {
            var map = eim.getMapInstance(1).getId();
            eim.setProperty("stage","1");
            cm.warpParty(map);
            cm.dispose();
        }
        cm.dispose();
    }
}