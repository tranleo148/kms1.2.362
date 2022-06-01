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
            cm.sendYesNo("자네라면 포장마차를 퇴치할 수 있을 것 같군! 그럼 포장마차가 있는 곳으로 보내주겠네.");
        } else {
            cm.dispose();
            cm.openNpc(cm.getNpc(), "travel");
        }
    } else if (status == 1) {
        if (iswarp) {
            cm.dispose();
            cm.warp(741020101);
            cm.spawnMonster(9410014, 2, 340);
            cm.getPlayer().dropMessage(5, "대만의 주민들을 괴롭히는 포장마차가 나타났다.");
        }
    }
}