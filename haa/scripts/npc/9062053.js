importPackage(Packages.packet);
importPackage(Packages.packet.transfer.write);

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
        status--;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        //cm.sendPacket(1319, "00 00 00 00 01 00 00 00 01 00 00 00 01 00 00 00 02 00 31 32 00 00 00 00 00 00 00 00 01 00 00 00");
       //cm.sendPacket(1322, "01 00 00 00 01 00 00 00");
       //cm.sendPacket(710, "A8 04 00 00");
       cm.sendPacket(1320, "00 00 00 00 00 00 00 00");
        cm.dispose();
    }
}