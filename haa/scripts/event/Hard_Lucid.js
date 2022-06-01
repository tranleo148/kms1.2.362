importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.util);

var outmap = 450004000;
var time = 0;

function init() { }

function setup(mapid) {
    var a = Randomizer.nextInt();
    map = parseInt(mapid);
    while (em.getInstance("Hard_Lucid" + a) != null) {
        a = Randomizer.nextInt();
    }
    var eim = em.newInstance("Hard_Lucid" + a);
    eim.setInstanceMap(map).resetFully();
    eim.setInstanceMap(map + 50).resetFully();
    eim.setInstanceMap(map + 100).resetFully();
    eim.setInstanceMap(map + 150).resetFully();
    eim.setInstanceMap(map + 200).resetFully();
    return eim;
}

function playerEntry(eim, player) {
    eim.startEventTimer(1800000);
    eim.setProperty("stage", "0");
    var map = eim.getMapInstance(0);
    player.setDeathCount(10);
    player.changeMap(map, map.getPortal(0));
    playAnimation(player, eim);
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
    stage = parseInt(eim.getProperty("stage"));
    if (stage == 5) {
        eim.restartEventTimer(300000);
        WarptoNextStage(eim);
    } else if (stage == 4) {
        var iter = eim.getPlayers().iterator();
        while (iter.hasNext()) {
            var player = iter.next();
            player.getClient().send(MobPacket.Lucid3rdPhase(1));
        }
        Packages.server.Timer.MapTimer.getInstance().schedule(function () {
            disposeAll(eim);
        }, 8000);
    } else {
        disposeAll(eim);
    }
}

function changedMap(eim, player, mapid) {
    stage = parseInt(eim.getProperty("stage"));
    if (mapid != 450004400 && mapid != 450004450 && mapid != 450004500 && mapid != 450004550 && mapid != 450004600) {
        player.setDeathCount(0);
        eim.unregisterPlayer(player);
        eim.disposeIfPlayerBelow(0, 0);
    }
}

function playerDisconnected(eim, player) {
    return 0;
}

function monsterValue(eim, mobId) {
    var stage = parseInt(eim.getProperty("stage"));
    if (mobId == 8880141 && stage == 1) {
        eim.setProperty("stage", "2");
        eim.schedule("WarptoNextStage", 5000);
    } else if (mobId == 8880151 && stage == 3) {
        eim.setProperty("stage", "4");
        eim.restartEventTimerMillSecond(45 * 1000);
        eim.getMapInstance(3).spawnMonsterDelayid(8880153, -2, 1200, 601, -60);
        eim.getMapInstance(3).broadcastMessage(MobPacket.Lucid3rdPhase(0));
    } else if (mobId == 8880153 && stage == 4) {
        eim.setProperty("stage", "5");
        eim.getMapInstance(4).killAllMonsters(true);
        eim.restartEventTimer(5000);
    }
    return 1;
}

function playAnimation(player, eim) {
    var stage = parseInt(eim.getProperty("stage"));
    if (stage == 0) {
        eim.setProperty("stage", "1");
        player.getClient().getSession().writeAndFlush(CField.showSpineScreen(true, false, true, "Map/Effect3.img/BossLucid/Lucid/lusi", "animation", 0, false, ""));
        player.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
    } else if (stage == 2) {
        eim.setProperty("stage", "3");
        player.getClient().removeClickedNPC();
        Packages.scripting.NPCScriptManager.getInstance().dispose(player.getClient());
        player.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
        Packages.scripting.NPCScriptManager.getInstance().start(player.getClient(), 3003250, "Lucid_phase2");
    }
    if (player.getParty().getLeader().getId() == player.getId()) {
        eim.schedule("WarptoNextStage", 9000);
    }
}

function WarptoNextStage(eim) {
    var stage = parseInt(eim.getProperty("stage"));
    var iter = eim.getPlayers().iterator();
    while (iter.hasNext()) {
        var pl = iter.next();
        if (stage == 5) {
            map = eim.getMapInstance(4);
        } else {
            map = eim.getMapInstance(stage);
        }
        pl.changeMap(map.getId(), 0);
        if (stage == 2) {
            playAnimation(pl, eim);
        } else if (stage == 1 || stage == 3) {
            pl.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(false, true, false, false));
        }
    }
    if (stage == 1) {
        spawnMonster(eim, stage, 8880141, 1021, 43);
        spawnMonster(eim, stage, 8880166, 1021, 43);
    } else if (stage == 3) {
        spawnMonster(eim, stage, 8880151, 292, -135);
    } else if (stage == 5) {
        spawnMonster(eim, 4, 8880177, 80, 36);
        eim.restartEventTimer(300000);
        eim.setProperty("stage", "6");
    }
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

function playerDead(eim, player) { }

function cancelSchedule() { }