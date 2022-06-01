importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.util);

var outmap = 993189201;
var time = 0;

function init() {}

function setup(mapid) {
    var a = Randomizer.nextInt();
    map = parseInt(mapid);
    while (em.getInstance("NeoCrystal" + a) != null) {
        a = Randomizer.nextInt();
    }
    var eim = em.newInstance("NeoCrystal" + a);
    eim.setProperty("stage", "1");
    eim.setProperty("point", "0");
    eim.setInstanceMap(map).resetFully();
    return eim;
}

function playerEntry(eim, player) {
    var map = eim.getMapInstance(0);
    player.changeMap(map, map.getPortal(0));
    eim.restartEventTimer(120000, true);
        eim.sendPunchKing(map, 0, 3);
        eim.sendPunchKing(map, 1, 1);
        eim.sendPunchKing(map, 2, 0);
    player.setKeyValue(108124, "today", "0");
}

function spawnMonster(eim, instance, mobid, x, y) {
    var map = eim.getMapInstance(0);
    var mob = em.getMonster(mobid);
    map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(x, y));
}

function playerRevive(eim, player) {
    return false;
}

function scheduledTimeout(eim) {
    end(eim);
}

function changedMap(eim, player, mapid) {
    if (mapid != 993189300) {
        eim.unregisterPlayer(player);
        eim.disposeIfPlayerBelow(0, 0);
    } else {
        spawnMonster(eim, 0, 9833881, 125, 17);
	for (var i = 0; i < 5; i++) {
	    spawnMonster(eim, 0,  9833893, 50 + (75 * i), 17);
	}
    }
}

function playerDisconnected(eim, player) {
    return 0;
}

function monsterValue(eim, mobId) {
    var stage = parseInt(eim.getProperty("stage"));
    var point = parseInt(eim.getProperty("point"));
    var map = eim.getMapInstance(0);
    if (mobId >= 9833881 && mobId <= 9833892) {
        eim.setProperty("point", (point+60)+"");
        setPoint(eim, (point+60));
        eim.sendPunchKing(map, 2, point+60);
    }
    if (mobId >= 9833893 && mobId <= 9833904) {
        eim.setProperty("point", (point+8)+"");
        setPoint(eim, (point+8));
        eim.sendPunchKing(map, 2, point+8);
    }
    if (map.getNumMonsters() <= 0) {
        if (stage == 12) {
             end(eim);
             return 1;
        }
        eim.setProperty("stage", (stage+1)+"")
        eim.sendPunchKing(map, 1, stage+1);
        
	spawnMonster(eim, stage, 9833881 + stage, 125, 17);
	for (var i = 0; i < 5; i++) {
	    spawnMonster(eim, stage,  9833893 + stage, 50 + (75 * i), 17);
	}
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


function setPoint(eim, point) {
    var iter = eim.getPlayers().iterator();
    while (iter.hasNext()) {
        var player = iter.next();
        player.setKeyValue(108124, "today", point + "");
    }
}

function givePoint(eim, point) {
    var iter = eim.getPlayers().iterator();
    while (iter.hasNext()) {
        var player = iter.next();
        if (player.getKeyValue(108124, "today") == -1) {
	    player.setKeyValue(108124, "today", "0");
        }
        player.setKeyValue(108124, "today", (player.getKeyValue(108124, "today") + point) + "");
    }
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