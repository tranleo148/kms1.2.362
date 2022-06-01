var status = -1;

function start() {
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
        cm.getPlayer().getOlaOlaGame().OlaOlaResult(cm.getPlayer());
        cm.dispose();
    } 
}