importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.util);

var outmap = 450011990;
var time = 0;

function init() {}

function setup(mapid) {
    var a = Randomizer.nextInt();
    map = parseInt(mapid);
    while (em.getInstance("Normal_JinHillah" + a) != null) {
        a = Randomizer.nextInt();
    }
    var eim = em.newInstance("Normal_JinHillah" + a);
    eim.setInstanceMap(map).resetFully();
    return eim;
}

function playerEntry(eim, player) {
    eim.startEventTimer(1800000);
    eim.setProperty("stage", "0");
    var map = eim.getMapInstance(0);

    if (player.getParty().getLeader().getId() == player.getId()) {
        eim.schedule("WarptoNextStage", 15000);
    }
}

function spawnMonster(eim, instance, mobid, x, y) {
    var map = eim.getMapInstance(instance);
    var mob = em.getMonster(mobid);
    eim.registerMonster(mob);
    map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(x, y));
}

function playerRevive(eim, player) {
    return false;
}

function scheduledTimeout(eim) {
    end(eim);
}

function changedMap(eim, player, mapid) {
    stage = parseInt(eim.getProperty("stage"));
    if (mapid != 450010500 && mapid != 450010400) {
        player.setDeathCount(0);
        eim.unregisterPlayer(player);
        eim.disposeIfPlayerBelow(0, 0);
    }
}

function playerDisconnected(eim, player) {
    return 0;
}

function monsterValue(eim, mobId) {

    if (mobId == 8880405) {
        stage = parseInt(eim.getProperty("stage"));
        eim.setProperty("stage", "2");
        eim.restartEventTimer(300000);
        var iter = eim.getPlayers().iterator();
        while (iter.hasNext()) {
            var player = iter.next();
            player.getMap().killAllMonsters(true);
            player.getClient().getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(3003771, 3000, "#face4#내… 내 시간이 흘러가고 있어!", ""));
        }
    }
    return 1;
}

function WarptoNextStage(eim) {
    var stage = parseInt(eim.getProperty("stage"));
    var iter = eim.getPlayers().iterator();
    while (iter.hasNext()) {
        var player = iter.next();
        map = eim.getMapInstance(stage);
        player.resetDeathCounts();
        player.changeMap(map.getId(), 0);
        player.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(false, true, false, false));
        player.getClient().getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(3003771, 3000, "#face0#어디… 네 영혼은 다른 것들과 어떻게 다른지 한번 볼까!", ""));
    }
    spawnMonster(eim, stage, 8880405, 0, 266);
    spawnMonster(eim, stage, 8880406, 0, 266);
    spawnMonster(eim, stage, 8880407, 0, 266);
}

function playerExit(eim, player) {
    eim.unregisterPlayer(player);
    eim.disposeIfPlayerBelow(0, 0);
}

function end(eim) {
    eim.disposeIfPlayerBelow(100, outmap);
}


function clearPQ(eim) {
    end(eim);
}


function disposeAll(eim) {
    var iter = eim.getPlayers().iterator();
    while (iter.hasNext()) {
        var player = iter.next();
        eim.unregisterPlayer(player);
        player.setDeathCount(0);
        player.changeMap(outmap, 0);
    }
    end(eim);
}

function allMonstersDead(eim) {
    //after ravana is dead nothing special should really happen
}

function leftParty(eim, player) {
    disposeAll(eim);
}

function disbandParty(eim) {
    disposeAll(eim);
}

function playerDead(eim, player) {}

function cancelSchedule() {}