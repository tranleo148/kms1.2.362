importPackage(Packages.server);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(java.lang);

var outmap = 680000715;

var xy = [
    [792, -457],
    [813, -1963],
    [3107, -237],
    [838, 1485],
    [-1450, -237]];

var orgel = [
    [9833080, 9833070], // 보라, 노랑
    [9833081, 9833071],
    [9833082, 9833072],
    [9833083, 9833073],
    [9833084, 9833074]
];

var setting = [
    [9833090, 9833091],
    [9833092, 9833093],
    [9833094, 9833095],
    [9833096, 9833097],
    [9833098, 9833099]];

var time = 0;


function init() {}

function setup(mapid) {
    var a = Randomizer.nextInt();
    map = parseInt(mapid);
    while (em.getInstance("DreamBreaker" + a) != null) {
        a = Randomizer.nextInt();
    }
    var eim = em.newInstance("DreamBreaker" + a);
    eim.setInstanceMap(map).resetFully();
    return eim;
}

function playerEntry(eim, player) {
    var map = eim.getMapInstance(0);
    player.changeMap(map, map.getPortal(0));

    eim.setProperty("gaugeHold", "false");
    eim.setProperty("stopSpawn", "false");
    eim.setProperty("gauge", "500");

    player.getClient().getSession().writeAndFlush(SLFCGPacket.SetDreamBreakerUI(player.getKeyValue(15901, "stage")));
    player.getClient().getSession().writeAndFlush(SLFCGPacket.DreamBreakerDisableTimer(true, 180 * 1000));
    player.getClient().getSession().writeAndFlush(SLFCGPacket.DreamBreakerCountdown(player.getKeyValue(15901, "stage")));


    eim.schedule("startGame", 3000);
}

function spawnOrigin(eim, player) {
    var map = eim.getMapInstance(0);

    stage = player.getKeyValue(15901, "stage");

    for (i = 0; i < xy.length; i++) {
	mob = em.getMonster(setting[i][0]);
        if (mob.getId() >= 9833080) {
	    mob.setHp(Packages.constants.GameConstants.getDreamBreakerHP(stage) * Math.max(1, stage - 200));
	}
	map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(xy[i][0], xy[i][1]));

	mob = em.getMonster(setting[i][1]);
        if (mob.getId() >= 9833080) {
	    mob.setHp(Packages.constants.GameConstants.getDreamBreakerHP(stage) * Math.max(1, stage - 200));
	}
	map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(xy[i][0], xy[i][1]));
    }

    var purple = [0, 1, 2, 3, 4];
    var yellow = [];

    for (i = 0; i < 2; i++) {
        rd = Math.floor(Math.random() * purple.length);
        yellow.push(purple[rd]);
        purple.splice(rd, 1);
    }

    for (i = 0; i < purple.length; i++) {
	mob = em.getMonster(orgel[purple[i]][0]);
	if (mob.getId() >= 9833080) {
	    mob.setHp(Packages.constants.GameConstants.getDreamBreakerHP(stage) * Math.max(1, stage - 200));
	}
	map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(xy[purple[i]][0], xy[purple[i]][1]));
    }

    for (i = 0; i < yellow.length; i++) {
	mob = em.getMonster(orgel[yellow[i]][1]);
	if (mob.getId() >= 9833080) {
	    mob.setHp(Packages.constants.GameConstants.getDreamBreakerHP(stage) * Math.max(1, stage - 200));
	}
	map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(xy[yellow[i]][0], xy[yellow[i]][1]));
    }
}

function startGame(eim) {
    eim.restartEventTimer(1000 * 3 * 60);
    var players = eim.getPlayers();
    if (players != null && !players.isEmpty()) {
	player = players.get(0);
	player.getClient().getSession().writeAndFlush(SLFCGPacket.DreamBreakerDisableTimer(false, 180 * 1000));
	updateGauge(eim);
        spawnOrigin(eim, player);
    }
}

function updateGauge(eim) {
    var timer = Packages.server.Timer.MapTimer.getInstance().register(function () {
	try {
	time++;
	if (eim.getProperty("gaugeHold") == null) {
	    timer.cancel(true);
	    return;
	}
        stop = eim.getProperty("gaugeHold").equals("true");
	if (!stop) {
	    if (eim.getMapInstance(0) != null && eim.getPlayers() != null && !eim.getPlayers().isEmpty()) {
		var player = eim.getPlayers().get(0);
    		stage = player.getKeyValue(15901, "stage");
		purple = eim.getMapInstance(0).countOrgelById(true);
		yellow = eim.getMapInstance(0).countOrgelById(false);
		var calc = yellow - purple;
		if (calc < 0) {
		    if (stage < 100) {
			calc *= -10;
		    } else {
			calc *= (-10 * (stage / 100));
		    }
		} else {
		    calc *= -10;
		}
		eim.setProperty("gauge", (parseInt(eim.getProperty("gauge")) + calc) + "");
		player.getClient().getSession().writeAndFlush(SLFCGPacket.DreamBreakerGaugePacket(parseInt(eim.getProperty("gauge"))));
		if (eim.getProperty("gauge") <= 0) {
		    timer.cancel(true);
		    nextStage(eim, player);
		    return;
		} else if (eim.getProperty("gauge") >= 1000) {
		    timer.cancel(true);
		    clear(eim);
		    return;
		}
	    } else {
		timer.cancel(true);
		return;
	    }
	}
	} catch (e) {
	    e.printStackTrace();
	    timer.cancel(true);
	}
    }, 1000);
}

function nextStage(eim, player) {
    player.getClient().getSession().writeAndFlush(SLFCGPacket.DreamBreakerDisableTimer(true, (180 - time) * 1000));

    stage = player.getKeyValue(15901, "stage");
    if (stage == parseInt(player.getKeyValue(15901, "best"))) {
        var besttime = parseInt(player.getKeyValue(15901, "besttime"));
        if (besttime > time) {
            player.setKeyValue(15901, "besttime_b", "" + besttime);
            player.setKeyValue(15901, "best_b", "" + stage);
            player.setKeyValue(15901, "besttime", "" + time);
        }
    } else if (stage >= parseInt(player.getKeyValue(15901, "best"))) {
        player.setKeyValue(15901, "besttime_b", "" + player.getKeyValue(15901, "besttime"));
        player.setKeyValue(15901, "best_b", "" + player.getKeyValue(15901, "best"));
        player.setKeyValue(15901, "best", "" + stage);
        player.setKeyValue(15901, "besttime", "" + time);
    }

    var pluspoint = stage - (stage % 10);
    if (stage < 10) {
	pluspoint = 10;
    }
    player.getClient().getSession().writeAndFlush(SLFCGPacket.DreamBreakerMsg("드림포인트 " + pluspoint + " 획득!"));
    player.setKeyValue(15901, "dream", (parseInt(Math.min(3000, player.getKeyValue(15901, "dream") + pluspoint))) + "");
    player.setKeyValue(15901, "clearTime", "" + time);
    eim.setProperty("gauge", "500");
    player.setKeyValue(15901, "stage", ++stage);
    player.getClient().getSession().writeAndFlush(SLFCGPacket.DreamBreakerGaugePacket(parseInt(eim.getProperty("gauge"))));
    player.getMap().killAllMonsters(true);
    
    player.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.DreamBreakerResult(time));
    var rank = Packages.client.DreamBreakerRank.getRank(player.getName());
    player.setKeyValue(15901, "rank_b", "" + rank);

    eim.setProperty("gaugeHold", "false");
    eim.setProperty("stopSpawn", "false");

    time = 0;

    player.getClient().getSession().writeAndFlush(SLFCGPacket.SetDreamBreakerUI(player.getKeyValue(15901, "stage")));
    player.getClient().getSession().writeAndFlush(SLFCGPacket.DreamBreakerDisableTimer(true, 180 * 1000));
    player.getClient().getSession().writeAndFlush(SLFCGPacket.DreamBreakerCountdown(player.getKeyValue(15901, "stage")));
    eim.schedule("startGame", 3000);
}


function spawnMob(eim, mobid, x, y) {
    var map = eim.getMapInstance(0);
    var mob1 = em.getMonster(mobid);
    eim.registerMonster(mob1);
    map.spawnMonsterOnGroundBelow(mob1, new java.awt.Point(x, y));
}

function playerRevive(eim, player) {
    return false;
}

function scheduledTimeout(eim) {
    end(eim);
}

function changedMap(eim, player, mapid) {
    if (mapid != 921171000) {
        eim.unregisterPlayer(player);
    }
}

function playerDisconnected(eim, player) {
    return 0;
}

function monsterValue(eim, mobId) {
    var map = eim.getMapInstance(0);
    var mob = em.getMonster(mobId);

    players = eim.getPlayers();
    if (players == null || players.isEmpty()) {
	return 0;
    }

    player = players.get(0);

    if (eim.getProperty("stopSpawn") == null || eim.getProperty("stopSpawn").equals("true")) {
	return 0;
    }

    for (i = 0; i < xy.length; i++) {
	if (mobId == setting[i][0] || mobId == setting[i][1]) {
	    if (mob.getId() >= 9833080) {
		mob.setHp(Packages.constants.GameConstants.getDreamBreakerHP(player.getKeyValue(15901, "stage")) * Math.max(1, stage - 200));
	        map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(xy[i][0], xy[i][1]));
	    }
	    break;
	}
    }
    return 1;
}

function playerExit(eim, player) {
    eim.unregisterPlayer(player);
    eim.disposeIfPlayerBelow(0, outmap);
}

function end(eim) { // fail
    var iter = eim.getPlayers().iterator();
    while (iter.hasNext()) {
	player = iter.next();
	player.changeMap(921171100, 0);
	player.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.environmentChange("Map/Effect3.img/hungryMuto/TimeOver", 16));
    }
}

function clear(eim) {
    var iter = eim.getPlayers().iterator();
    while (iter.hasNext()) {
	player = iter.next();
	player.changeMap(921171100, 0);
	player.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.environmentChange("Map/Effect3.img/hungryMuto/Clear", 16));
    }
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
    // disposeAll(eim);
}

function disbandParty(eim) {
    // disposeAll(eim);
}

function playerDead(eim, player) {}

function cancelSchedule() {}