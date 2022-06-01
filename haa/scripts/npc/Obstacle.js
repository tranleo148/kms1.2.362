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
                for (i = 0; i < 6; i++) {
                    var ob = new Obstacle(54, new Point(Randomizer.rand(-4000, 100), Randomizer.rand(-2100, -1500)), new Point(Randomizer.rand(-4000, 100), 92), 10, 0, 0, 3, 3000, Randomizer.rand(10, 360));
                    ob.setDelay(Randomizer.rand(400, 700));
                    ob.setVperSec(Randomizer.rand(100, 200));
                    arr.push(ob)
                }
                cm.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.CreateObstaclePlatformer(arr));
            }
        }, 1500)
        cm.dispose()
    }

}