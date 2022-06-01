importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.util);

var outmap = 910002000;
var outmap2 = 993189200;
var time = 0;

function init() {}

function setup(mapid) {
    map = parseInt(mapid);
    var eim = em.newInstance("EscapePQ");
    eim.setInstanceMap(map).resetFully();
    return eim;
}

function playerEntry(eim, player) {
    eim.startEventTimer(1200000);
    var map = eim.getMapInstance(0);
    player.changeMap(map, map.getPortal(0));
}

function monsterValue(eim, mobId) {
    var stage = parseInt(eim.getProperty("stage"));
    return 1;
}

function spawnMonster(eim) {
    var map = eim.getMapInstance(0);
    var mob = em.getMonster(9300454);
    map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(-954, -181));
}

function playerRevive(eim, player) {
    return false;
}

function scheduledTimeout(eim) {
    end(eim);
}

    

function changedMap(eim, player, mapid) {
    if (mapid != 921160100 && mapid != 921160200 && mapid != 921160300 && mapid != 921160310 && mapid != 921160320 && mapid != 921160330 && mapid != 921160340 && mapid != 921160350 && mapid != 921160400 && mapid != 921160500 && mapid != 921160600 && mapid != 921160700) {
        eim.unregisterPlayer(player);
        eim.disposeIfPlayerBelow(0, 0);
        em.disposeInstance("EscapePQ");
        return;
    }
}

function playerDisconnected(eim, player) {
    return 0;
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
    var map = eim.getMapInstance(0);
    while (iter.hasNext()) {
        var player = iter.next();
        eim.unregisterPlayer(player);
        player.changeMap(outmap, map.getPortal(0));
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