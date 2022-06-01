var iswarp = false;

function start() {
    status = -1;
    action(1, 0, 0);
}
function action(mode, type, sel) {
    if (mode == 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }
    if (status == 0) {
        if (cm.getPlayer().getMapId() == 701010320 && cm.getPlayer().getKeyValue(191017, "travel") >= 210) {
            cm.sendYesNo("이 안으로 들어가시겠습니까?");
            iswarp = true;
        } else {
            cm.getPlayer().dropMessage(5, cm.getPlayer().getKeyValue(191017, "travel"));
            cm.dispose();
            cm.openNpc(cm.getNpc(), "travel");
        }
    } else if (status == 1) {
        if (iswarp) {
            cm.dispose();
            cm.warp(701010321);
        }
    }
}