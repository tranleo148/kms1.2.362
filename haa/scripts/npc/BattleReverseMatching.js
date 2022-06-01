var status = -1;

function start() {
    action(1, 0, 0);
}
var namearr = [];

function action(mode, type, selection) {
    if (mode <= 0) {
        if (cm.getPlayer().getMapId() == 993022200) {
            cm.sendOk("조금 더 쉬었다 가세YO!");
        }
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        if (cm.getPlayer().getMapId() == 993022200) {
            cm.sendYesNo("#b<어드벤처 암호추리>#k 재미있었나YO?\r\n마을로 돌려보내 줄게YO");
        }
    } else if (status == 1) {
        if (cm.getPlayer().getMapId() == 993022200) {
            cm.warp(100000000);
            cm.dispose();
        }
    }
}