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
                cm.sendPacket(455, "F4 F3 FF FF A6 00 00 00 F4 F3 FF FF D0 FE FF FF F4 F3 FF FF A6 00 00 00 00 00 00 00 FA FF FF FF 01 07 00 75 70 64 6F 77 6E 31");
                cm.sendPacket(455, "85 F6 FF FF D0 FE FF FF 85 F6 FF FF A6 00 00 00 85 F6 FF FF D0 FE FF FF 00 00 00 00 05 00 00 00 01 07 00 75 70 64 6F 77 6E 32");
                cm.sendPacket(455, "8B F7 FF FF A6 00 00 00 8B F7 FF FF D0 FE FF FF 8B F7 FF FF A6 00 00 00 00 00 00 00 FB FF FF FF 01 07 00 75 70 64 6F 77 6E 33");
            } else {
                cm.sendPacket(455, "F4 F3 FF FF D0 FE FF FF F4 F3 FF FF A6 00 00 00 F4 F3 FF FF D0 FE FF FF 00 00 00 00 06 00 00 00 01 07 00 75 70 64 6F 77 6E 31");
                cm.sendPacket(455, "85 F6 FF FF A6 00 00 00 85 F6 FF FF D0 FE FF FF 85 F6 FF FF A6 00 00 00 00 00 00 00 FB FF FF FF 01 07 00 75 70 64 6F 77 6E 32");
                cm.sendPacket(455, "8B F7 FF FF D0 FE FF FF 8B F7 FF FF A6 00 00 00 8B F7 FF FF D0 FE FF FF 00 00 00 00 05 00 00 00 01 07 00 75 70 64 6F 77 6E 33");
            }
            tick++;
        }, 3000)
        cm.dispose()
    }

}