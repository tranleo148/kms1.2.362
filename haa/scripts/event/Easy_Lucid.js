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
    while (em.getInstance("Easy_Lucid" + a) != null) {
        a = Randomizer.nextInt();
    }
    var eim = em.newInstance("Easy_Lucid" + a);
    eim.setInstanceMap(map).resetFully();
    eim.setInstanceMap(map + 40).resetFully();
    eim.setInstanceMap(map + 80).resetFully();
    eim.setInstanceMap(map + 120).resetFully();
    eim.setInstanceMap(map + 160).resetFully();
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
    if (stage == 4) {
        WarptoNextStage(eim);
    } else {
        end(eim);
    }
}

function changedMap(eim, player, mapid) {
    stage = parseInt(eim.getProperty("stage"));
    if (mapid != 450003800 && mapid != 450003840 && mapid != 450003880 && mapid != 450003920 && mapid != 450003960) {
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
    if (mobId == 8880142 && stage == 1) {
        eim.setProperty("stage", "2");
        eim.schedule("WarptoNextStage", 5000);
    } else if (mobId == 8880155 && stage == 3) {
        eim.setProperty("stage", "4");
        eim.restartEventTimer(10000);
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
        map = eim.getMapInstance(stage);
        pl.changeMap(map.getId(), 0);
        if (stage == 2) {
            playAnimation(pl, eim);
        } else if (stage == 1 || stage == 3) {
            pl.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(false, true, false, false));
        }
    }
    if (stage == 1) {
        spawnMonster(eim, stage, 8880142, 1021, 48);
        spawnMonster(eim, stage, 8880158, 1021, 48);
    } else if (stage == 3) {
        spawnMonster(eim, stage, 8880155, 292, -125);
    } else if (stage == 4) {
        spawnMonster(eim, stage, 8880156, 80, 36)
        eim.restartEventTimer(300000);
        eim.setProperty("stage", "5");
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