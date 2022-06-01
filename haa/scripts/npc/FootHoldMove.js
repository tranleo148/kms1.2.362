importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server);
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
        var tick = 0;
        var mapid = cm.getPlayer().getMapId();
        var schedule = Packages.server.Timer.MapTimer.getInstance().register(function () {
            if (cm.getPlayer().getMapId() != mapid) {
                schedule.cancel(true);
                return;
            }
            if (tick % 2 == 0) {
                cm.sendPacket(455, "A0 F4 FF FF 70 FE FF FF A0 F4 FF FF 80 FF FF FF A0 F4 FF FF 70 FE FF FF 00 00 00 00 05 00 00 00 01 07 00 75 70 64 6F 77 6E 30");
                cm.sendPacket(455, "CF FE FF FF ED FD FF FF CF FE FF FF 24 FF FF FF CF FE FF FF ED FD FF FF 00 00 00 00 05 00 00 00 01 07 00 75 70 64 6F 77 6E 31");
            } else {
                cm.sendPacket(455, "A0 F4 FF FF 80 FF FF FF A0 F4 FF FF 70 FE FF FF A0 F4 FF FF 80 FF FF FF 00 00 00 00 FB FF FF FF 01 07 00 75 70 64 6F 77 6E 30");
                cm.sendPacket(455, "CF FE FF FF 24 FF FF FF CF FE FF FF ED FD FF FF CF FE FF FF 24 FF FF FF 00 00 00 00 FB FF FF FF 01 07 00 75 70 64 6F 77 6E 31");
            }
            tick++;
        }, 1500)
        cm.dispose()
    }

}