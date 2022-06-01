var status;

function start() {
    status = -1;
    action(1, 1, 0);
}

function action(mode, type, selection) {
    if (mode < 0) {
        cm.dispose();
    return;
    } else {
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
cm.특별아이템설정(1142072,10,15,0);
cm.gainItem(2430368, -1);
cm.dispose();
}
}
}