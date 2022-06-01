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
                cm.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.ClearObstacles());
                schedule.cancel(true);
                return;
            }
            else {
                arr = [];
                for (i = 0; i < 4; i++) {
                    var x = Randomizer.rand(-3290, -2470);
                    p1 = new Point(x, -4260);
                    p2 = new Point(x, 2850);
                    var ob = new Obstacle(54, p1, p2, 10, 0, 0, 3, 10000, 90);
                    ob.setDelay(0);
                    ob.setVperSec(400);
                    arr.push(ob);
                }
                cm.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.CreateObstaclePlatformer(arr));
            }
        }, 1500)
        cm.dispose()
    }

}