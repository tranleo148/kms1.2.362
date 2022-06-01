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
        if (cm.getPlayer().getMapId() == 800010100 && cm.getPlayer().getKeyValue(191017, "travel") >= 110) {
            iswarp = true;
            cm.sendYesNo("너, 구름여우를 많이 잡아다준 녀석이잖아! 머쉬맘을 잡아주러 온거야? 그렇다면 내가 머쉬맘을 찾아줄 수도 있는데?");
        } else {
            cm.dispose();
            cm.openNpc(cm.getNpc(), "travel");
        }
    } else if (status == 1) {
        if (iswarp) {
            cm.dispose();
            cm.spawnMonster(9400205, 188, 95);
            cm.getPlayer().dropMessage(5, "일본의 주민들을 괴롭히는 머쉬맘이 등장했다.");
        }
    }
}