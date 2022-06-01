importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.util);

var outmap = 240050400;
var time = 0;

function init() {}

function setup(mapid) {
    var a = Randomizer.nextInt();
    map = parseInt(mapid);
    while (em.getInstance("Chaos_Horntail" + a) != null) {
        a = Randomizer.nextInt();
    }
    var eim = em.newInstance("Chaos_Horntail" + a);
    eim.setProperty("stage", "0")
    eim.setInstanceMap(map).resetFully();
    eim.setInstanceMap(240060101).resetFully();
    eim.setInstanceMap(240060201).resetFully();
    return eim;
}

function playerEntry(eim, player) {
    eim.startEventTimer(9000000);
    var map = eim.getMapInstance(0);
    player.setDeathCount(5);
    player.changeMap(map, map.getPortal(0));
    changedMap(eim, player, map.getId());
}

function spawnMonster(eim, mobid, Instance, x, y, player) {
    var map = eim.getMapInstance(Instance);
    var mob = em.getMonster(mobid);
    map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(x, y));
    map.killMonster(mob, player, false, false, 1);
}

function playerRevive(eim, player) {
    return false;
}

function scheduledTimeout(eim) {
    end(eim);
}

function changedMap(eim, player, mapid) {
    if (mapid != 240060001 && mapid != 240060101 && mapid != 240060201) {
        player.setDeathCount(0);
        eim.unregisterPlayer(player);
        player.setDeathCount(0);
        eim.disposeIfPlayerBelow(0, 0);
    }
    if (player.getParty().getLeader().getId() == player.getId()) {
        if (mapid == 240060001) {
            spawnMonster(eim, 8810128, 0, 900, 225, player);
        } else if (mapid == 240060101) {
            spawnMonster(eim, 8810129, 1, -370, 251, player);
        }
    }
}

function playerDisconnected(eim, player) {
    return 0;
}

function monsterValue(eim, mobId) {
    player = eim.getPlayers().get(0);
    stage = parseInt(eim.getProperty("stage"));
    if (mobId == 8810100 && stage == 0) {
        eim.setProperty("stage", "1");
    } else if (mobId == 8810101 && stage == 1) {
        eim.setProperty("stage", "2");
    } else if (mobId == 8810122 && stage == 2) {
        eim.restartEventTimer(300000);
    }
    return 1;
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