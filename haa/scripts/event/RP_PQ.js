importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.util);
 
var EtcTimer = Packages.server.Timer.EtcTimer;

var outmap = 221023300;
var outmap2 = 993189200;
var time = 0;

function init() {}

function setup(mapid) {
    map = parseInt(mapid);
    var eim = em.newInstance("RP_PQ");
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
    return;
}

function playerRevive(eim, player) {
    return false;
}

function scheduledTimeout(eim) {
    end(eim);
}

    

function changedMap(eim, player, mapid) {
    if (mapid != 922010100 && mapid != 922010400 && mapid != 922010401 && mapid != 922010402 && mapid != 922010403 && mapid != 922010404 && mapid != 922010405 && mapid != 922010600 && mapid != 922010700 && mapid != 922010800 && mapid != 922010900) {
        eim.unregisterPlayer(player);
        eim.disposeIfPlayerBelow(0, 0);
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