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
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        talk = "잠깐!! 어느 자쿰의 제단으로 이동하시겠소?\r\n\r\n"
        talk+= "#L0# #b노멀 자쿰#l\r\n";
        talk+= "#L1# #b카오스 자쿰#l\r\n";
        cm.sendSimple(talk);
    } else if (status == 1) {
        if (cm.itemQuantity(4001017) == 0) {
            nameplus = selection == 0 ? "노멀" : "카오스"
            cm.getPlayer().dropMessage(5, nameplus+" 자쿰에게 바칠 제물이 없어 이동할 수 없습니다.");
            cm.dispose();
            return;
        }
        cm.warp(211042400 + selection, 0);
        cm.dispose();
    }
}