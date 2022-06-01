importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.util);

var outmap = 450012500;
var time = 0;

function init() { }

function setup(mapid) {
    var a = Randomizer.nextInt();
    map = parseInt(mapid);
    while (em.getInstance("Black_Mage" + a) != null) {
        a = Randomizer.nextInt();
    }
    var eim = em.newInstance("Black_Mage" + a);
    eim.setInstanceMap(map).resetFully();
    eim.setInstanceMap(map + 100).resetFully();
    eim.setInstanceMap(map + 200).resetFully();
    eim.setInstanceMap(map + 300).resetFully();
    eim.setInstanceMap(map + 400).resetFully();
    eim.setInstanceMap(map + 500).resetFully();
    eim.setInstanceMap(map + 600).resetFully();
    eim.setInstanceMap(map + 700).resetFully();
    eim.setInstanceMap(map + 750).resetFully();
    return eim;
}

function playerEntry(eim, player) {
    eim.startEventTimer(3600000);
    eim.setProperty("stage", "0");
    var map = eim.getMapInstance(0);
    player.setDeathCount(12);
    player.changeMap(map, map.getPortal(0));
    eim.setProperty("stage", "1");
    player.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
    player.getClient().getSession().writeAndFlush(SLFCGPacket.MakeBlind(1, 0xff, 0x0, 0x0, 0x0, 0, 0));
    player.getClient().getSession().writeAndFlush(CField.musicChange("Bgm00.img/Silence"));
    player.getClient().getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("", 1, 100));




    if (player.getParty().getLeader().getId() == player.getId()) {
        eim.schedule("showEffectPhase1", 500);
        eim.schedule("blackOut", 5500);
        eim.schedule("removeblackOut", 6500);
        eim.schedule("WarptoNextStage", 7000);
    }
}

function showEffectPhase1(eim) {
    var iter = eim.getPlayers().iterator();
    while (iter.hasNext()) {
        var player = iter.next();
        player.getClient().getSession().writeAndFlush(CField.showSpineScreen(false, false, true, "Effect/Direction20.img/bossBlackMage/start_spine/blasck_space", "animation", 0, true, "intro"));
        player.getClient().getSession().writeAndFlush(CField.playSound("Sound/SoundEff.img/BM3/boss_start"));
        player.getClient().getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("", 1, 5000));

    }
}

function blackOut(eim) {
    var iter = eim.getPlayers().iterator();
    while (iter.hasNext()) {
        var player = iter.next();
        player.getClient().getSession().writeAndFlush(CField.showBlackOutScreen(1000, "BlackOut", "Map/Effect2.img/BlackOut", 13, 4, -1));
        player.getClient().getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("", 1, 1000));
    }
}

function removeblackOut(eim) {
    var iter = eim.getPlayers().iterator();
    while (iter.hasNext()) {
        var player = iter.next();
        player.getClient().getSession().writeAndFlush(CField.removeIntro("intro", 100));
        player.getClient().getSession().writeAndFlush(CField.removeBlackOutScreen("BlackOut", 100));
        player.getClient().getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("", 1, 700));
    }
}

function showEffectPhase2(eim) {
    var iter = eim.getPlayers().iterator();
    while (iter.hasNext()) {
        var player = iter.next();
        player.getClient().getSession().writeAndFlush(CField.showSpineScreen(false, false, true, "Effect/Direction20.img/bossBlackMage/start2_spine/skeleton", "animation", 0, true, "intro"));
        player.getClient().getSession().writeAndFlush(CField.playSound("Sound/SoundEff.img/BM3/boss_start2"));
        player.getClient().getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("", 1, 5000));
    }
}

function showEffectPhase3(eim) {
    var iter = eim.getPlayers().iterator();
    while (iter.hasNext()) {
        var player = iter.next();
        player.getClient().getSession().writeAndFlush(CField.showSpineScreen(false, false, true, "Effect/Direction20.img/bossBlackMage/space/blasck_space", "animation", 0, true, "intro"));
        player.getClient().getSession().writeAndFlush(CField.playSound("Sound/SoundEff.img/BM3/boss_start3"));
        player.getClient().getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("", 1, 5000));
    }
}

function showEffectPhase4(eim) {
    var iter = eim.getPlayers().iterator();
    while (iter.hasNext()) {
        var player = iter.next();
        player.getClient().getSession().writeAndFlush(CField.showSpineScreen(false, false, true, "Effect/Direction20.img/bossBlackMage/start4_spine/black_Phase_3_4", "animation", 0, true, "intro"));
        player.getClient().getSession().writeAndFlush(CField.playSound("Sound/SoundEff.img/BM3/boss_start4"));
        player.getClient().getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("", 1, 5000));
    }
}
function spawnMonster(eim, instance, mobid, x, y) {
    var map = eim.getMapInstance(instance);
    var mob = em.getMonster(mobid);
    if (mobid == 8880502) {
        map.spawnMonsterOnGroundBelowBlackMage(mob, new java.awt.Point(x, y));
    } else {
        map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(x, y));
    }
    map.broadcastMessage(CField.UIPacket.openUI(1204));
}

function message(eim, instance, msg) {
    var map = eim.getMapInstance(instance);
    map.broadcastMessage(CField.enforceMSG(msg, 265, 3000));
}

function messagePlayer(eim, instance, msg) {
    var map = eim.getMapInstance(instance);
    map.broadcastMessage(SLFCGPacket.OnYellowDlg(0, 3000, msg, ""));
}

function playerRevive(eim, player) {
    if (parseInt(eim.getProperty("stage")) == 7) {
        player.getClient().getSession().writeAndFlush(CField.blackMageTamporarySkill(5));
        player.giveBlackMageBuff();
    }
    return false;
}

function scheduledTimeout(eim) {
    end(eim);
}

function changedMap(eim, player, mapid) {
    stage = parseInt(eim.getProperty("stage"));
    if (mapid == 450013700) {
        player.giveBlackMageBuff();
    }
    if (!(mapid >= 450013000 && mapid <= 450013800)) {
        player.setDeathCount(0);
        eim.unregisterPlayer(player);
        eim.disposeIfPlayerBelow(0, 0);
        player.getClient().send(CField.getSelectPower(6, 39));
    }
}

function playerDisconnected(eim, player) {
    return 0;
}

function monsterValue(eim, mobId) {
    stage = parseInt(eim.getProperty("stage"));
    if (mobId == 8880505 && stage == 1) {
        eim.setProperty("stage", "2");
        eim.schedule("WarptoNextStage", 3000);
    } else if (mobId == 8880502 && stage == 3) {
        eim.setProperty("stage", "4");
        eim.schedule("WarptoNextStage", 3000);
    } else if (mobId == 8880503 && stage == 5) {
        eim.setProperty("stage", "6");
        eim.schedule("WarptoNextStage", 3000);
    } else if (mobId == 8880519 && stage == 7) {
        map = eim.getMapInstance(7);
        map.killAllMonsters(true);
        eim.setProperty("stage", "8");
        eim.schedule("WarptoNextStage", 3000);
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
        if (stage % 2 == 1 && stage != 8) {
            player.setSkillCustomInfo(45001310, 0, 5000);
            player.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(false, true, false, false));
        } else if (stage == 2) {
            player.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
            player.getClient().getSession().writeAndFlush(SLFCGPacket.MakeBlind(1, 0xff, 0x0, 0x0, 0x0, 0, 0));
            player.getClient().getSession().writeAndFlush(CField.musicChange("Bgm50/ThroneOfDarkness"));
            player.getClient().getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("", 1, 100));

            if (player.getParty().getLeader().getId() == player.getId()) {
                eim.schedule("showEffectPhase2", 500);
                eim.schedule("blackOut", 5500);
                eim.schedule("removeblackOut", 6000);

                eim.schedule("WarptoNextStage", 7000);
                eim.setProperty("stage", "3");
            }
        } else if (stage == 4) {
            player.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
            player.getClient().getSession().writeAndFlush(SLFCGPacket.MakeBlind(1, 0xff, 0x0, 0x0, 0x0, 0, 0));
            player.getClient().getSession().writeAndFlush(CField.musicChange("Bgm50/WorldHorizon"));
            player.getClient().getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("", 1, 100));

            if (player.getParty().getLeader().getId() == player.getId()) {
                eim.schedule("showEffectPhase3", 500);
                eim.schedule("blackOut", 5500);
                eim.schedule("removeblackOut", 6000);

                eim.schedule("WarptoNextStage", 7000);
                eim.setProperty("stage", "5");
            }
        } else if (stage == 6) {
            player.getClient().getSession().writeAndFlush(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
            player.getClient().getSession().writeAndFlush(SLFCGPacket.MakeBlind(1, 0xff, 0x0, 0x0, 0x0, 0, 0));
            player.getClient().getSession().writeAndFlush(CField.musicChange("Bgm50/WorldHorizon"));
            player.getClient().getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("", 1, 100));

            if (player.getParty().getLeader().getId() == player.getId()) {
                eim.schedule("showEffectPhase4", 500);
                eim.schedule("blackOut", 5500);
                eim.schedule("removeblackOut", 6000);

                eim.schedule("WarptoNextStage", 7000);
                eim.setProperty("stage", "7");
            }
        } else if (stage == 8) {
            if (player.getParty().getLeader().getId() == player.getId()) {
                eim.setProperty("stage", "8");
                spawnMonster(eim, stage, 8880518, 0, 85);
            }
            eim.getMapInstance(8).broadcastMessage(CField.enforceMSG("창세의 알을 파괴하여 기나긴 싸움을 마무리 하자.", 265, 3000));
        }
    }
    if (stage == 1) {
        eim.getMapInstance(stage).setCustomInfo(45001310, 0, 30000);
        eim.getMapInstance(stage).setCustomInfo(45001311, 0, 63000);
        eim.getMapInstance(stage).setCustomInfo(45001312, 0, 51000);
        spawnMonster(eim, stage, 8880500, -350, 85);
        spawnMonster(eim, stage, 8880501, 350, 85);
        spawnMonster(eim, stage, 8880505, 0, 85);
        spawnMonster(eim, stage, 8880512, 0, 85);
        spawnMonster(eim, stage, 8645064, 0, 85);
        message(eim, stage, "검은 마법사와 대적하기 위해서는 그를 호위하는 창조와 파괴의 기사들을 물리쳐야 한다.");
    } else if (stage == 3) {
        eim.getMapInstance(stage).setCustomInfo(45001310, 0, 60000);//다크폴링쿨타임
        eim.getMapInstance(stage).setCustomInfo(45001313, 0, 50000);//파멸의 눈
        eim.getMapInstance(stage).setCustomInfo(45001314, 0, 65000);//모닝스타폴
        eim.getMapInstance(stage).setCustomInfo(45001315, 0, 75000);//검은번개
        spawnMonster(eim, stage, 8880502, 0, 88);
        spawnMonster(eim, stage, 8880512, 0, 88);
        spawnMonster(eim, stage, 8880516, 0, 88);
        spawnMonster(eim, stage, 8645064, 0, 88);
        message(eim, stage, "드디어 검은 마법사의 앞에 바로 섰다. 모든 힘을 다해 그를 물리치자.");
    } else if (stage == 5) {
        eim.getMapInstance(stage).broadcastMessage(MobPacket.SpawnFieldSummon(3, 2, new java.awt.Point(-271, -303)));
        eim.getMapInstance(stage).setCustomInfo(45001327, 0, -271);
        eim.getMapInstance(stage).setCustomInfo(45001328, 0, -303);
        eim.getMapInstance(stage).setCustomInfo(45001326, 0, 10000);
        eim.getMapInstance(stage).setCustomInfo(45001310, 0, 50000);//다크폴링쿨타임
        eim.getMapInstance(stage).setCustomInfo(45001314, 0, 12000);//모닝스타폴
        eim.getMapInstance(stage).setCustomInfo(45001320, 0, 10000);//십자 레이저
        eim.getMapInstance(stage).setCustomInfo(45001321, 0, 10000);//발판
        eim.getMapInstance(stage).setCustomInfo(45001322, 0, 40000);//빨간구체
        eim.getMapInstance(stage).setCustomInfo(45001323, 0, 44000);//파란구체
        eim.getMapInstance(stage).setCustomInfo(45001324, 0, 75000);//창조 파괴
        eim.getMapInstance(stage).setCustomInfo(45021328, 0, 120000);//천사
        spawnMonster(eim, stage, 8880503, 0, 85);
        spawnMonster(eim, stage, 8880512, 0, 85);
        spawnMonster(eim, stage, 8645064, 0, 85);
        message(eim, stage, "저 모습은 마치 신의 권능이라도 얻은 것 같다. 설사 상대가 신이라고 할지라도 모두를 위해 여기서 저지해야 한다.");
    } else if (stage == 7) {
        eim.getMapInstance(stage).setCustomInfo(45004444, 0, 10000);
        eim.getMapInstance(stage).setCustomInfo(45004445, 0, 40000);
        spawnMonster(eim, stage, 8880504, 0, 208);
        spawnMonster(eim, stage, 8880512, 0, 208);
        spawnMonster(eim, stage, 8880519, 0, 208);
        spawnMonster(eim, stage, 8645064, 0, 208);
        messagePlayer(eim, stage, "#face1# 아무 것도 없는 공간...... 나 혼자 남은 것인가...");
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
        player.getClient().getSession().writeAndFlush(CField.blackMageTamporarySkill(6));
        player.setDeathCount(0);
        player.changeMap(outmap, 0);
        eim.unregisterPlayer(player);
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