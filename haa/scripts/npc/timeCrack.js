var status = -1;

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
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        talk = "#e<시간의 균열>#n\r\n"
        talk+= "과거와 미래, 그리고 그 사이의 어딘가... 가고자 하는 곳은 어디인가?\r\n\r\n"
        talk+= "#L0# #b차원의 틈#l";
        cm.sendSimple(talk);
    } else if (status == 1) {
        cm.dispose();
        cm.warp(272020000, 0);
    }
}