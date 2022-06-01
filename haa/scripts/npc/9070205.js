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
        var mapid = cm.getPlayer().getMapId();
        var schedule = Packages.server.Timer.MapTimer.getInstance().register(function () {
            if (cm.getPlayer().getMapId() != mapid) {
                schedule.cancel(true);
            }
            arr = [];
            p1 = new Point(Randomizer.rand(-4000, 100), Randomizer.rand(-2100, -1500));
            p2 = new Point(Randomizer.rand(-4000, 100), 572);
            delay = Randomizer.rand(400, 700);
            VperSec = Randomizer.rand(100, 200);
            angle = Randomizer.rand(0, 359);
            length = Randomizer.rand(1900, 2500);
            for (i = 0; i < 9; i++) {
                realangle = (angle + (i * 12)) % 360;
                var ob = new Obstacle(54, p1, p2, 10, 0, 0, 3, 4000, realangle);
                ob.setDelay(100);
                ob.setVperSec(VperSec);
                arr.push(ob);
            }
            cm.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.CreateObstaclePlatformer(arr));
        }, 200)
        cm.dispose()
    }

}