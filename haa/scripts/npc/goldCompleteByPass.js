var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
            cm.sendYesNoS("후원포인트 3000을 이용하여 골든패스를 사용하시겠습니까?", 0x4, 9076130);
        } else if (status == 1) {
            cm.dispose();
            if (cm.getPlayer().getDonationPoint() < 3000) {
                cm.sendOkS("후원포인트 3000이 필요합니다.", 0x4, 9076130);
            } else {
                Packages.handling.channel.handler.PlayerHandler.goldCompleteByPass(cm.getClient());
            }
        }
    }
}