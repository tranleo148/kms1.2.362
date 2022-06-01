var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        cm.sendYesNo("지금 나가면 게임에 다시 입장할 수 없어. 정말로 나갈래?");
    } else if (status == 1) {
        cm.warp(910048200, 0);
        cm.getPlayer().cancelEffectFromBuffStat(Packages.client.MapleBuffStat.RideVehicle, 80001013);
        cm.dispose();
    }
}