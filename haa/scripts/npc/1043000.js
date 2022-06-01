var status = -1;

function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        cm.sendYesNo("#fn나눔고딕 Extrabold##d꽃 무더기#k 너머로 꽃이 #d반짝#k 입니다.\r\n#b꽃#k 을 가져 가시겠습니까?");
    } else if (status == 1) {
        cm.sendNextS("#fn나눔고딕 Extrabold##b좋아, 이제 #fs14#존#fs12# 님께 가볼까..?#k", 2);
   } else if (status == 2) {
        cm.warp(100030301,0);
        cm.gainItem(4033970,1);
   
    }
}
