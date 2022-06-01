importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.util);

var outmap = 680000715;
var time = 0;

function init() {}

function setup(mapid) {
    var a = Randomizer.nextInt();
    map = parseInt(mapid);
    while (em.getInstance("Hard_Demian" + a) != null) {
        a = Randomizer.nextInt();
    }
    var eim = em.newInstance("Hard_Demian" + a);
    eim.setInstanceMap(map + 20).resetFully();
    eim.setInstanceMap(map).resetFully();
    eim.setInstanceMap(map + 60).resetFully();
    eim.setInstanceMap(map + 40).resetFully();
    return eim;
}

function playerEntry(eim, player) {
    eim.startEventTimer(1800000);
    eim.setProperty("stage", "0");
    var map = eim.getMapInstance(0);
    player.setDeathCount(10);
    player.changeMap(map, map.getPortal(0));
    player.setExtremeMode(true);
    eim.setProperty("stage","1");
    player.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
    player.getClient().getSession().writeAndFlush(CField.playSound("Sound/SoundEff.img/BossDemian/phase1"));
    player.getClient().getSession().writeAndFlush(CField.showSpineScreen(false, false, true, "Map/Effect2.img/DemianIllust/1pahseSp/demian", "animation", 0, false, ""));
    player.Stigma = 0;
    if (player.getParty().getLeader().getId() == player.getId()) {
        eim.schedule("WarptoNextStage", 8500);
    }
}

function spawnMonster(eim, instance, mobid, x, y) {
    var map = eim.getMapInstance(instance);
    var mob = em.getMonster(mobid, true);
    map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(x, y));
    map.broadcastMessage(CField.UIPacket.openUI(1115));
}

function playerRevive(eim, player) {
    return false;
}

function scheduledTimeout(eim) {
    end(eim);
}

function changedMap(eim, player, mapid) {
    stage = parseInt(eim.getProperty("stage"));
    if (mapid != 350160200 && mapid != 350160240 && mapid != 350160260 && mapid != 350160220) {
        player.setDeathCount(0);
        player.setExtremeMode(false);
        eim.unregisterPlayer(player);
        eim.disposeIfPlayerBelow(0, 0);
    }
}

function playerDisconnected(eim, player) {
    return 0;
}

function monsterValue(eim, mobId) {
    stage = parseInt(eim.getProperty("stage"));
    if (mobId == 8880100 && stage == 1) {
        eim.setProperty("stage", "2");
        eim.schedule("WarptoNextStage", 5000);
    } else if (mobId == 8880101 && stage == 3) {
        eim.setProperty("stage", "4");
        eim.restartEventTimer(300000);
    }
    return 1;
}

function WarptoNextStage(eim) {
    var stage = parseInt(eim.getProperty("stage"));
    var iter = eim.getPlayers().iterator();
    while (iter.hasNext()) {
        var player = iter.next();
        map = eim.getMapInstance(stage);
        player.changeMap(map.getId(), 0);
	if (stage == 1) {
	player.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(false, true, false, false));
	} else if (stage == 2) {
            player.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
            player.getClient().getSession().writeAndFlush(CField.showSpineScreen(false, false, true, "Map/Effect2.img/DemianIllust/2pahseSp/003", "animation", 0, false, ""));
            player.getClient().getSession().writeAndFlush(CField.showSpineScreen(false, false, true, "Map/Effect2.img/DemianIllust/2pahseSp/003", "animation", 0, false, ""));
        } else if (stage == 3) {
            player.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(false, true, false, false));
        }
    }
    if (stage == 2) {
        eim.setProperty("stage", "3");
        eim.schedule("WarptoNextStage", 8500);
    } else {
        spawnMonster(eim, stage, 8880100 + Math.floor(stage/2), 696, 17);
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

function playerDead(eim, player) {}

function cancelSchedule() {}