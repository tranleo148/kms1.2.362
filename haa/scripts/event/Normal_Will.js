importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(Packages.server.maps);
importPackage(java.lang);
importPackage(java.util);

var outmap = 450007240;
var time = 0;

function init() { }

function setup(mapid) {
    var a = Randomizer.nextInt();
    map = parseInt(mapid);
    while (em.getInstance("Normal_Will" + a) != null) {
        a = Randomizer.nextInt();
    }
    var eim = em.newInstance("Normal_Will" + a);
    eim.setInstanceMap(map).resetFully();
    eim.setInstanceMap(map + 50).resetFully();
    eim.setInstanceMap(map + 100).resetFully();
    eim.setInstanceMap(map + 150).resetFully();
    eim.setInstanceMap(map + 200).resetFully();
    eim.setInstanceMap(map + 250).resetFully();
    return eim;
}

function playerEntry(eim, player) {
    eim.startEventTimer(1800000);
    eim.setProperty("stage", "0");
    var map = eim.getMapInstance(0);
    player.setDeathCount(10);
    player.setMoonGauge(0);
    player.changeMap(map, map.getPortal(map.getId() == 450008750 ? Randomizer.rand(1, 2) : 0));
    playAnimation(player, eim);
}

function spawnMonster(eim, instance, mobid, x, y) {
    var map = eim.getMapInstance(instance);
    var mob = em.getMonster(mobid);
    eim.registerMonster(mob);
    if (map.getMobsSize(mobid) <= 0)
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
    if (mapid != 450008700 && mapid != 450008750 && mapid != 450008800 && mapid != 450008850 && mapid != 450008900 && mapid != 450008950) {
        player.setDeathCount(0);
        player.cancelEffectFromBuffStat(Packages.client.SecondaryStat.DebuffIncHp);
        eim.unregisterPlayer(player);
        eim.disposeIfPlayerBelow(0, 0);
    }
}
function playerDisconnected(eim, player) {
    return 0;
}

function monsterValue(eim, mobId) {
    var stage = parseInt(eim.getProperty("stage"));
    if ((mobId == 8880340 || mobId == 8880343 || mobId == 8880344) && stage == 1) {
        eim.setProperty("stage", "2");
        eim.schedule("WarptoNextStage", 5000);
    } else if (mobId == 8880341 && stage == 3) {
        eim.setProperty("stage", "4");
        eim.schedule("WarptoNextStage", 5000);
    } else if (mobId == 8880342 && stage == 5) {
        eim.restartEventTimer(300000);
    }
    return 1;
}

function playAnimation(player, eim) {
    var stage = parseInt(eim.getProperty("stage"));
    player.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
    if (stage == 0) {
        eim.setProperty("stage", "1");
        player.getClient().getSession().writeAndFlush(CField.showSpineScreen(false, false, true, "Effect/Direction20.img/bossWill/intro_spine/skeleton", "1", 0, true, "skeleton"));
        player.getClient().getSession().writeAndFlush(SLFCGPacket.playSE("Sound/SoundEff.img/esfera/boss/intro1"));
    } else if (stage == 2) {
        player.getClient().getSession().writeAndFlush(CField.showSpineScreen(false, false, true, "Effect/Direction20.img/bossWill/intro_spine/skeleton", "2", 0, true, "skeleton"));
        player.getClient().getSession().writeAndFlush(SLFCGPacket.playSE("Sound/SoundEff.img/esfera/boss/intro2"));
    } else if (stage == 4) {
        player.getClient().getSession().writeAndFlush(CField.showSpineScreen(false, false, true, "Effect/Direction20.img/bossWill/intro_spine/skeleton", "3", 0, true, "skeleton"));
        player.getClient().getSession().writeAndFlush(SLFCGPacket.playSE("Sound/SoundEff.img/esfera/boss/intro3"));
    }
    if (player.getParty().getLeader().getId() == player.getId()) {
        eim.schedule("WarptoNextStage", 6800);
    }
}

function WarptoNextStage(eim) {
    var stage = parseInt(eim.getProperty("stage"));
    var iter = eim.getPlayers().iterator();
    while (iter.hasNext()) {
        var pl = iter.next();
        map = eim.getMapInstance(stage);
        pl.changeMap(map.getId(), map.getPortal(map.getId() == 450008750 ? Randomizer.rand(1, 2) : 0));
        if (stage == 1) {
            pl.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(false, true, false, false));
            pl.getClient().getSession().writeAndFlush(MobPacket.BossWill.setMoonGauge(100, 45));
        } else if (stage == 2) {
            playAnimation(pl, eim);
        } else if (stage == 3) {
            pl.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(false, true, false, false));
            pl.getClient().getSession().writeAndFlush(MobPacket.BossWill.setMoonGauge(100, 50));
        } else if (stage == 4) {
            playAnimation(pl, eim);
            pl.cancelEffectFromBuffStat(SecondaryStat.DebuffIncHp);
        } else if (stage == 5) {
            pl.cancelEffectFromBuffStat(SecondaryStat.DebuffIncHp);
            pl.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(false, true, false, false));
            pl.getClient().getSession().writeAndFlush(MobPacket.BossWill.setMoonGauge(100, 25));
        }
    }
    if (stage == 0) {
        eim.setProperty("stage", "1");
    } else if (stage == 2) {
        eim.setProperty("stage", "3");
    } else if (stage == 4) {
        eim.setProperty("stage", "5");
    }
    stage = parseInt(eim.getProperty("stage"));
    if (stage == 1) {
        spawnMonster(eim, stage, 8880343, -4, 25);
        spawnMonster(eim, stage, 8880344, 352, -2020);
        spawnMonster(eim, stage, 8880340, 352, -2020);
        spawnMonster(eim, stage, 8880351, 352, 0);
        spawnMonster(eim, stage, 8880352, 352, -2020);
        spawnMonster(eim, stage, 8880355, 252, 0);
        spawnMonster(eim, stage, 8880356, 252, -2020);
    } else if (stage == 3) {
        spawnMonster(eim, stage, 8880341, 0, 215);
        spawnMonster(eim, stage, 8880323, 252, -2020);
        spawnMonster(eim, stage, 8880327, 352, 0);
    } else if (stage == 5) {
        eim.getMapInstance(stage).resetSpiderWeb();
        for (var i = 0; i < 35; i++) {
            eim.getMapInstance(stage).spawnSpiderWeb(new Packages.server.field.boss.will.SpiderWeb(i));
        }
        spawnMonster(eim, stage, 8880342, 80, 36);
        spawnMonster(eim, stage, 8880324, 352, 281);
        spawnMonster(eim, stage, 8880328, 252, 281);
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
        player.cancelEffectFromBuffStat(Packages.client.SecondaryStat.DebuffIncHp);
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