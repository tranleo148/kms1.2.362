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
                cm.sendPacket(455, "AC F3 FF FF E3 00 00 00 AC F3 FF FF 3E FE FF FF AC F3 FF FF E3 00 00 00 00 00 00 00 FB FF FF FF 01 07 00 75 70 64 6F 77 6E 31")
                cm.sendPacket(455, "94 F7 FF FF E3 00 00 00 94 F7 FF FF 3E FE FF FF 94 F7 FF FF E3 00 00 00 00 00 00 00 FB FF FF FF 01 07 00 75 70 64 6F 77 6E 32")
            } else {
                cm.sendPacket(455, "AC F3 FF FF 3E FE FF FF AC F3 FF FF E3 00 00 00 AC F3 FF FF 3E FE FF FF 00 00 00 00 05 00 00 00 01 07 00 75 70 64 6F 77 6E 31");
                cm.sendPacket(455, "94 F7 FF FF 3E FE FF FF 94 F7 FF FF E3 00 00 00 94 F7 FF FF 3E FE FF FF 00 00 00 00 05 00 00 00 01 07 00 75 70 64 6F 77 6E 32");
            }
            tick++;
        }, 3500)
        cm.dispose()
    }

}