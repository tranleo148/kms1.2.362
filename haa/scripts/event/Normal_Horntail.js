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
    while (em.getInstance("Normal_Horntail" + a) != null) {
        a = Randomizer.nextInt();
    }
    var eim = em.newInstance("Normal_Horntail" + a);
    eim.setProperty("stage", "0")
    eim.setInstanceMap(map).resetFully();
    eim.setInstanceMap(240060100).resetFully();
    eim.setInstanceMap(240060200).resetFully();
    return eim;
}

function playerEntry(eim, player) {
    eim.startEventTimer(9000000);
    var map = eim.getMapInstance(0);
    player.setDeathCount(10);
    player.changeMap(map, map.getPortal(0));
    changedMap(eim, player, map.getId());
}

function spawnMonster(eim, mobid, Instance, x, y, player) {
    var map = eim.getMapInstance(Instance);
    var mob = em.getMonster(mobid);
    eim.registerMonster(mob);
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
    if (mapid != 240060000 && mapid != 240060100 && mapid != 240060200) {
        player.setDeathCount(0);
        eim.unregisterPlayer(player);
        player.setDeathCount(0);
        eim.disposeIfPlayerBelow(0, 0);
    }
    if (player.getParty().getLeader().getId() == player.getId()) {
        if (mapid == 240060000) {
            spawnMonster(eim, 8810024, 0, 900, 225, player);
        } else if (mapid == 240060100) {
            spawnMonster(eim, 8810025, 1, -370, 251, player);
        }
    }
	if (mapid == 240060200) {
		if (player.getV("d_n_horn_clear") == "0") 
			player.addKV("d_n_horn_clear", "1");
	}
}

function playerDisconnected(eim, player) {
    return 0;
}

function monsterValue(eim, mobId) {
    player = eim.getPlayers().get(0);
    stage = parseInt(eim.getProperty("stage"));
    if (mobId == 8810000 && stage == 0) {
        eim.setProperty("stage", "1");
    } else if (mobId == 8810001 && stage == 1) {
        eim.setProperty("stage", "2");
    } else if (mobId == 8810018 && stage == 2) {
        eim.restartEventTimer(300000);
	if (player.getV("d_n_horn_clear") == "0") 
		player.addKV("d_n_horn_clear", "1");
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