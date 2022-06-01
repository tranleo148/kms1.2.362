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
        if (cm.getPlayer().getKeyValue(191017, "travel") > 313) {
            iswarp = true;
            cm.sendYesNo("야시장으로 돌아가겠는가?");
        } else {
            cm.dispose();
            cm.openNpc(cm.getNpc(), "travel");
        }
    } else if (status == 1) {
        if (iswarp) {
            cm.dispose();
            cm.warp(741000000);
        }
    }
}