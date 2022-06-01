
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
        if (cm.getPlayer().getMapId() == 701010323) {
            iswarp = true;
            cm.sendYesNo("중원 산악지대2로 돌아가시겠습니까?");
        } else {
            if (cm.getPlayer().getKeyValue(191017, "travel") > 210) {
                iswarp = true;
                cm.sendYesNo("충성! 이 안으로 들어가시겠습니까!?");
            } else {
                cm.dispose();
                cm.openNpc(cm.getNpc(), "travel");
            }
        }
    } else if (status == 1) {
        if (cm.getPlayer().getMapId() == 701010323) {
            if (iswarp) {
                cm.dispose();
                cm.warp(701010320);
            }
        } else {
            if (iswarp) {
                //2277 823
                cm.dispose();
                cm.warp(701010323);
                cm.spawnMonster(9600009, 2277, 823);
                cm.getPlayer().dropMessage(5, "중국의 주민들을 괴롭히는 대왕지네가 나타났다.");
            }
        }
    }
}