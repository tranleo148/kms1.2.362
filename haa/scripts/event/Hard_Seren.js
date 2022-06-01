importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.util);

var outmap = 410000670;
var time = 0;

function init() { }

function setup(mapid) {
    var a = Randomizer.nextInt();
    map = parseInt(mapid);
    while (em.getInstance("Hard_Seren" + a) != null) {
        a = Randomizer.nextInt();
    }
    var eim = em.newInstance("Hard_Seren" + a);
    eim.setInstanceMap(map).resetFully();
    eim.setInstanceMap(map + 20).resetFully();
    eim.setInstanceMap(map + 40).resetFully();
    eim.setInstanceMap(map + 60).resetFully();
    eim.setInstanceMap(map + 80).resetFully();
    return eim;
}

function playerEntry(eim, player) {
    eim.startEventTimer(1800000);
    eim.setProperty("stage", "0");
    var map = eim.getMapInstance(0);
    player.setDeathCount(5);
    player.changeMap(map, map.getPortal(0));
    player.setSerenStunGauge(0);
    eim.setProperty("stage", "1");
    player.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
    player.getClient().getSession().writeAndFlush(CField.showSpineScreen(false, false, true, "Effect/Direction20.img/bossSeren/1pahse_spine/skeleton", "animation", 0, false, ""));
    player.getClient().getSession().writeAndFlush(SLFCGPacket.playSE("Sound/SoundEff.img/seren/1phase"));
    if (player.getParty().getLeader().getId() == player.getId()) {
        eim.schedule("WarptoNextStage", 13000);
    }
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
    end(eim);
}

function changedMap(eim, player, mapid) {
    stage = parseInt(eim.getProperty("stage"));
    if (mapid != 410002000 && mapid != 410002020 && mapid != 410002040 && mapid != 410002060 && mapid != 410002080) {
        player.setDeathCount(0);
        eim.unregisterPlayer(player);
        eim.disposeIfPlayerBelow(0, 0);
    }
}

function playerDisconnected(eim, player) {
    return 0;
}

function monsterValue(eim, mobId) {
    stage = parseInt(eim.getProperty("stage"));
    if (mobId == 8880600 && stage == 1) {
        eim.setProperty("stage", "2");
        eim.schedule("WarptoNextStage", 6000);
        map = eim.getMapInstance(stage);
        map.broadcastMessage(SLFCGPacket.ClearObstacles());
        map.broadcastMessage(SLFCGPacket.BlackLabel("#fn나눔고딕 ExtraBold##fs32##r#e태양의 불꽃은 복수를 잊지 않는다.", 100, 1000, 4, 0, 0, 1, 4));
        map.killMonster(8880601);
    } else if (mobId == 8880602) {
        eim.restartEventTimer(300000);
        eim.setProperty("stage", "4");
        eim.schedule("WarptoNextStage", 5000);
        map = eim.getMapInstance(stage);
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
            player.getClient().getSession().writeAndFlush(CField.showSpineScreen(false, false, true, "Effect/Direction20.img/bossSeren/2pahse_spine/skeleton", "animation", 0, false, ""));
            player.getClient().getSession().writeAndFlush(SLFCGPacket.playSE("Sound/SoundEff.img/seren/2phase"));
        } else if (stage == 3) {
            player.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(false, true, false, false));
        }
    }
    if (stage == 2) {
        eim.setProperty("stage", "3");
        eim.schedule("WarptoNextStage", 9500);
    } else if (stage == 3) {
        //2페
        spawnMonster(eim, stage, 8880602, -9, 305);
        spawnMonster(eim, stage, 8880607, -9, 305);
        spawnMonster(eim, stage, 8880608, -9, 305);
        eim.getMapInstance(3).broadcastMessage(CWvsContext.serverNotice(5, "", "태양의 빛으로 가득찬 정오가 시작됩니다."));
    } else if (stage == 1) {
        //1페
        spawnMonster(eim, stage, 8880600, 100, 275);
        spawnMonster(eim, stage, 8880601, 100, 275);
    } else if (stage == 4) {
        spawnMonster(eim, stage, 8880614, 100, 275);
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