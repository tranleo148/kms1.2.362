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
        if (mode == 1) {
            status++;
        } else {
            status--;
        }
        if (status == 0) {
            var chat = "테스트\r\n";
            chat += "#L1#O#l";
            chat += "#L2#X#l";
            cm.sendSimple(chat);
        } else if (status == 1) {
            if (selection == 1) {
                cm.getPlayer().getOlaOlaGame().initGame(100000202);
                cm.dispose();
            } else {
                cm.getPlayer().getOlaOlaGame().ClearOlaOlaGame(cm.getPlayer().getId());
                cm.dispose();
            }
        }
    }
}