importPackage(Packages.server);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(java.lang);

var outmap = 680000715;

var xy = [[-224, -380], [-58, -380], [124, -380], [300, -380], [530, -380], [2320, -380], [2510, -380], [2730, -380], [2860, -380], [3050, -380], [-1287, -380], [-1042, -380], [-939, -380], [-796, -380], [-583, -380], [3363, -380], [3511, -380], [3748, -380], [3964, -380], [4177, -380], [-1530, -890], [-1270, -890], [-980, -890], [-870, -890], [-700, -890], [3536, -890], [3711, -890], [3942, -890], [4189, -890], [4365, -890], [1120, -800], [1400, -800], [1730, -800], [1413, -970], [1413, -1100], [1113, -1650], [1422, -1650], [1720, -1650], [1370, -1830], [1646, -1950]];
var setting = [[9833245, 9833246, 9833247, 9833248, 9833249, 9833250, 9833251, 9833252, 9833253,
		 9833254, 9833255, 9833256, 9833257, 9833258, 9833259, 9833260, 9833261
        ],
        [9833030, 9833031, 9833032, 9833033, 9833034, 9833035, 9833036, 9833037, 9833038,
		 9833039, 9833040, 9833041, 9833042, 9833043, 9833044, 9833045, 9833046
        ],
        [9833050, 9833051, 9833052, 9833053, 9833054, 9833055, 9833056, 9833057, 9833058,
		 9833059, 9833060, 9833061, 9833062, 9833063, 9833064, 9833065, 9833066
        ]];

var recipes = [[4034959, 0, 1, 130000, 45000], [4034960, 1, 1, 130000, 45000], [4034961, 2, 1, 130000, 45000], [4034962, 3, 1, 130000, 45000], [4034963, 4, 1, 130000, 45000], [4034964, 5, 1, 130000, 45000], [4034965, 6, 2, 130000, 45000], [4034966, 7, 1, 130000, 45000], [4034967, 8, 1, 130000, 45000], [4034968, 9, 1, 130000, 45000], [4034969, 10, 1, 130000, 45000], [4034970, 11, 1, 130000, 45000], [4034971, 12, 1, 130000, 45000], [4034972, 13, 1, 130000, 45000], [4034973, 14, 1, 130000, 45000], [4034974, 15, 1, 130000, 45000]];

var recipeItems = [ [[4034942, 5], [4034946, 10]],
		[[4034944, 5], [4034948, 10]],
		[[4034942, 5], [4034946, 5], [4034950, 10]],
		[[4034944, 5], [4034948, 5], [4034952, 10]],
		[[4034946, 5], [4034950, 5], [4034954, 10]],
		[[4034950, 5], [4034952, 5], [4034956, 10]],
		[[4034958, 1], [4034944, 5], [4034948, 5], [4034954, 10]],
		[[4034958, 5], [4034946, 5], [4034952, 5], [4034956, 10]],
		[[4034943, 5], [4034947, 10]],
		[[4034945, 5], [4034949, 10]],
		[[4034943, 5], [4034947, 5], [4034951, 10]],
		[[4034945, 5], [4034949, 5], [4034953, 10]],
		[[4034947, 5], [4034951, 5], [4034955, 10]],
		[[4034951, 5], [4034953, 5], [4034957, 10]],
		[[4034958, 1], [4034945, 5], [4034949, 5], [4034955, 10]],
		[[4034958, 5], [4034947, 5], [4034953, 5], [4034957, 10]]
		];

function init() {}

function setup(mapid) {
    var a = Randomizer.nextInt();
    map = parseInt(mapid);
    while (em.getInstance("Muto" + a) != null) {
        a = Randomizer.nextInt();
    }
    var eim = em.newInstance("Muto" + a);
    eim.setInstanceMap(map).resetFully();
    return eim;
}

function playerEntry(eim, player) {
    eim.startEventTimer(1000 * 60 * 10);
    var map = eim.getMapInstance(0);
    player.changeMap(map, map.getPortal(0));


    if (map.getId() == 921171200) { 
	diff = "easy";
    } else if (map.getId() == 921170050) {
	diff = "normal";
    } else {
	diff = "hard";
    }

    eim.setProperty("enhance", "false");
    eim.setProperty("diff", diff);
    eim.setProperty("score", "500");
    eim.setProperty("bonus", "true");


    if (player.getParty().getLeader().getId() == player.getId()) {
	spawnOrigin(eim);


	eim.schedule("recipe", 1000);
	eim.schedule("effect", 1000);
    }
}

function setTimer(eim) {

    eim.setProperty("bonus", "true");

    eim.setProperty("finishTime", "" + (System.currentTimeMillis() + recipes[eim.getProperty("mutoRecipe")][3]));
    eim.setProperty("finishBonusTime", "" + (System.currentTimeMillis() + recipes[eim.getProperty("mutoRecipe")][4]));

    var bonusTimer = Packages.server.Timer.MapTimer.getInstance().register(function () {
	try {
	if (eim.getMapInstance(0).getAllCharactersThreadsafe().size() == 0) {
	    bonusTimer.cancel(true);
	    return;
	} else if (System.currentTimeMillis() > Number(eim.getProperty("finishTime"))) {
	    bonusTimer.cancel(true);
	    recipe(eim);
	    return;
	} else {
	    if (parseInt(eim.getProperty("score")) <= 0) {
		end(eim);
		return;
	    }
	    if (System.currentTimeMillis() > Number(eim.getProperty("finishBonusTime"))) {
		eim.setProperty("bonus", "false");
	    }
	    eim.getMapInstance(0).broadcastMessage(Packages.tools.packet.CWvsContext.onFieldSetVariable("time", "" + (eim.getProperty("finishTime") - System.currentTimeMillis())));
	}
	} catch (e) {
	    e.printStackTrace();
	    bonusTimer.cancel(true);
	}
    }, 1000);


    st = eim.getProperty("diff");
    if (st.equals("easy")) { 
	t = 60000;
    } else if (st.equals("normal")) {
	t = 45000;
    } else {
	t = 30000;
    }

    var scoreTimer = Packages.server.Timer.MapTimer.getInstance().register(function () {
	try {
	eim.setProperty("score", eim.getProperty("score") - 10);
	if (eim.getMapInstance(0).getAllCharactersThreadsafe().size() == 0) {
	    scoreTimer.cancel(true);
	    return;
	} else if (parseInt(eim.getProperty("score")) <= 0) {
	    scoreTimer.cancel(true);
	    end(eim);
	    return;
	} else {
	    eim.getMapInstance(0).broadcastMessage(Packages.tools.packet.CWvsContext.onFieldSetVariable("score", eim.getProperty("score")));
	}
	} catch (e) {
	    e.printStackTrace();
	    scoreTimer.cancel(true);
	}
    }, t);
}

function effect(eim) {

    var iter = eim.getPlayers().iterator();
    while (iter.hasNext()) {
        var player = iter.next();

	player.setKeyValue(34222, "diff", eim.getProperty("diff"));
        player.setKeyValue(34222, "count", "0");
        player.setKeyValue(34222, "inGame", "1");
        player.setKeyValue(34222, "date", "19/11/01");
        player.setKeyValue(34222, "score", "0");
        player.setKeyValue(34222, "ptime", "0");
        player.setKeyValue(34222, "clear", "0");
        	
        player.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.mutoSetTime(1000 * 60 * 10));
        player.getClient().getSession().writeAndFlush(Packages.tools.packet.CWvsContext.onSessionValue("rType", "0"));
        player.getClient().getSession().writeAndFlush(Packages.tools.packet.CWvsContext.onSessionValue("rCount", "0"));
        player.getClient().getSession().writeAndFlush(Packages.tools.packet.CWvsContext.onFieldSetVariable("phase", "1"));
    }
}

function spawnOrigin(eim) {
    var map = eim.getMapInstance(0);

    if (map.getId() == 921171200) { 
	mobid = setting[0];
    } else if (map.getId() == 921170050) {
	mobid = setting[1];
    } else {
	mobid = setting[2];
    }

    if (eim.getProperty("enhance").equals("true")) {
	for (i = 0; i < xy.length; i++) {
	    map.spawnMonsterOnGroundBelow(em.getMonster(mobid[parseInt(i / 5) + 8]), new java.awt.Point(xy[i][0], xy[i][1]));
	}
	rand = Math.floor(Math.random() * xy.length);
	map.spawnMonsterOnGroundBelow(em.getMonster(mobid[mobid.length - 1]), new java.awt.Point(xy[rand][0], xy[rand][1]));
    } else {
	for (i = 0; i < xy.length; i++) {
	    map.spawnMonsterOnGroundBelow(em.getMonster(mobid[parseInt(i / 5)]), new java.awt.Point(xy[i][0], xy[i][1]));
	}
	rand = Math.floor(Math.random() * xy.length);
	map.spawnMonsterOnGroundBelow(em.getMonster(mobid[mobid.length - 1]), new java.awt.Point(xy[rand][0], xy[rand][1]));
    }
}

function recipe(eim) {
    var map = eim.getMapInstance(0);

    mutoRecipe = Math.floor(Math.random() * 7); // 기본 난이도만 뜨게


    if (eim.getProperty("enhance").equals("true") && parseInt(eim.getProperty("score")) <= 340) {
	map.killAllMonster(eim.getPlayers().get(0)); // 야매
	eim.setProperty("enhance", "false");
	spawnOrigin(eim);
	map.broadcastMessage(Packages.tools.packet.CWvsContext.onFieldSetVariable("phase", "1"));
    }

    if (eim.getProperty("enhance").equals("false") && parseInt(eim.getProperty("score")) >= 670) {
	map.killAllMonster(eim.getPlayers().get(0)); // 야매
	eim.setProperty("enhance", "true");
	spawnOrigin(eim);
	map.broadcastMessage(Packages.tools.packet.CWvsContext.onFieldSetVariable("phase", "2"));
	map.broadcastMessage(Packages.tools.packet.CField.environmentChange("Map/Effect3.img/hungryMutoMsg/msg2", 16));
    }

    if (eim.getProperty("enhance").equals("true")) {
	mutoRecipe += 8;
    }

    if (eim.getProperty("mutoRecipe") != null) {
	map.removeDrops();
	if (eim.getProperty("complete") != null && eim.getProperty("complete").equals("1")) {
	    eim.setProperty("score", "" + (parseInt(eim.getProperty("score")) + 150));
	    map.broadcastMessage(Packages.tools.packet.CField.environmentChange("Map/Effect3.img/hungryMuto/perfect", 16));
	    map.broadcastMessage(Packages.tools.packet.CField.environmentChange("Map/Effect3.img/hungryMutoMsg/msg5", 16));
    	    if (eim.getProperty("bonus").equals("true")) {
		eim.setProperty("score", "" + (parseInt(eim.getProperty("score")) + 150));
    	    }
	} else {
	    map.broadcastMessage(Packages.tools.packet.CField.environmentChange("Map/Effect3.img/hungryMuto/failed", 16));
	    map.broadcastMessage(Packages.tools.packet.CField.environmentChange("Map/Effect3.img/hungryMutoMsg/msg6", 16));
	}
    }



    if (parseInt(eim.getProperty("score")) >= 1000) {
	clear(eim);
	return;
    }

    recipeId = recipes[mutoRecipe][0];
    type = recipes[mutoRecipe][1];
    unk = recipes[mutoRecipe][2];
    time = recipes[mutoRecipe][3];
    bonus = recipes[mutoRecipe][4];

    eim.setProperty("mutoRecipe", "" + mutoRecipe);

    map.broadcastMessage(Packages.tools.packet.CWvsContext.onFieldSetVariable("foodType", recipes[mutoRecipe][1]));

    rep = recipeItems[type];
    for (i = 0; i < rep.length; ++i) {
	eim.setProperty("recipeItem" + i, "" + rep[i][0]);
	eim.setProperty("recipeReq" + i, "" + rep[i][1]);
	eim.setProperty("recipeCount" + i, "0");
	if (eim.getProperty("enhance") != null && eim.getProperty("enhance").equals("true")) {
	    if (Math.floor(Math.random() * 3) == 1) { // 33%
		eim.setProperty("recipeHidden" +i, "1");
	    }
	}
    }
    map.broadcastMessage(Packages.tools.packet.CField.setMutoNewRecipe(recipes[mutoRecipe], rep.length, eim));
    setTimer(eim);
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
    if (mapid != 921171200 && mapid != 921170050 && mapid != 921170100 && mapid != 450002024) {
        eim.unregisterPlayer(player);
    }
}

function playerDisconnected(eim, player) {
    return 0;
}

function monsterValue(eim, mobId) {
    var map = eim.getMapInstance(0);
    var mob = em.getMonster(mobId);

    st = eim.getProperty("diff");
    if (st.equals("easy")) { 
	mobidz = setting[0];
    } else if (st.equals("normal")) {
	mobidz = setting[1];
    } else {
	mobidz = setting[2];
    }

    if (mobId == 9833261 || mobId == 9833046 || mobId == 9833066) {
	map.broadcastMessage(Packages.tools.packet.CField.environmentChange("Map/Effect3.img/hungryMutoMsg/msg7", 16));
	Packages.server.Timer.MapTimer.getInstance().schedule(function () {
	    for (i = 0; i < mobidz.length; ++i) {
		if (mobidz[i] == mobId) {
		    zz = xy[Math.floor(Math.random() * xy.length)];
		    map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(zz[0], zz[1]));
		    map.broadcastMessage(Packages.tools.packet.CField.environmentChange("Map/Effect3.img/hungryMutoMsg/msg8", 16));
		    break;
		}
	    }
	}, 3000);
    } else {
	for (i = 0; i < mobidz.length; ++i) {
	    if (eim.getProperty("enhance").equals("true")) {
		if (mobidz[i] == mobId - 8) {
		    zz = xy[5 * i + Math.floor(Math.random() * 5)];
	    	    map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(zz[0], zz[1]));
	    	    break;
		}
	    } else {
		if (mobidz[i] == mobId) {
		    zz = xy[5 * i + Math.floor(Math.random() * 5)];
	    	    map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(zz[0], zz[1]));
	    	    break;
		}
	    }
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
	player.changeMap(450002024, 0);
	player.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.environmentChange("Map/Effect3.img/hungryMuto/TimeOver", 16));
    }
}

function clear(eim) { // clear
    eim.setProperty("clear", "1");
    var iter = eim.getPlayers().iterator();
    while (iter.hasNext()) {
	player = iter.next();
	player.changeMap(450002024, 0);
	player.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.finishMuto());
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
    disposeAll(eim);
}

function disbandParty(eim) {
    disposeAll(eim);
}

function playerDead(eim, player) {}

function cancelSchedule() {}