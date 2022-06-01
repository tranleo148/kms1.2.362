importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.util);

var outmap = 921171100;
var schedulesg;
var orgel = [
    [9833080, 9833070],
    [9833081, 9833071],
    [9833082, 9833072],
    [9833083, 9833073],
    [9833084, 9833074]
];
var etcmonster = [
    [9833090, 9833091],
    [9833092, 9833093],
    [9833094, 9833095],
    [9833096, 9833097],
    [9833098, 9833099]
];
var pos = [
    [792, -457],
    [813, -1963],
    [3107, -237],
    [838, 1485],
    [-1450, -237]
]

var time = 0;

/*
792,-457 센터
813- 1963 위
3107,-237 오른쪽
838, 1485 아래
-1450, -237 왼쪽
*/

// mobid에서 10을 나눈뒤 10으로 나머지가 7이면 노란색 8이면 보라색

// 보라     노랑      클리너   가고일
//9833080 (9833070) (9833090) (9833091) - 센터
//9833081 (9833071) (9833092) (9833093) - 위
//9833082 (9833072) (9833094) (9833095) - 오른쪽
//9833083 (9833073) (9833096) (9833097) - 아래
//9833084 (9833074) (9833098) (9833099) - 왼쪽

//캐릭 리스폰 816,-457

function init() {}

function setup(mapid) {
    map = parseInt(mapid);
    var eim = em.newInstance("DreamBreaker");
    eim.setInstanceMap(map).resetFully();
    em.setProperty("Gauge", "500");
    em.setProperty("TimerOn", "0");
    eim.schedule("asdf", 1000);
    return eim;
}

function asdf(eim) {
    em.setProperty("TimerOn", "0");
    time = 0;
    var player = eim.getPlayers().get(0);
    var em2 = player.getEventInstance();
    em2.setProperty("GaugeHold", "0");
    em2.setProperty("StopSpawn", "0");
    player.getClient().getSession().writeAndFlush(SLFCGPacket.SetDreamBreakerUI(Integer.parseInt(player.getKeyValue(15901, "stage"))));
    player.getClient().getSession().writeAndFlush(SLFCGPacket.DreamBreakerDisableTimer(true, 180 * 1000));
    player.getClient().getSession().writeAndFlush(SLFCGPacket.DreamBreakerCountdown(Integer.parseInt(player.getKeyValue(15901, "stage"))));
    eim.schedule("startgame", 3000);
}

function TimerGo(eim) {
    var player = eim.getPlayers().get(0);
    var schedule = Timer.EtcTimer.getInstance().register(function () {
        if (player.getMapId() != 921171000) {
            schedule.cancel(true);
        }
        if (em.getProperty("TimerOn") == "1") {
            time++;
        } else {
            schedule.cancel(true);
        }
    }, 1000);
}

function startgame(eim) {
    var player = eim.getPlayers().get(0);
    player.getClient().getSession().writeAndFlush(SLFCGPacket.DreamBreakerDisableTimer(false, 180 * 1000));
    em.setProperty("TimerOn", "1");
    TimerGo(eim);
    var purpleorgel = [0, 1, 2, 3, 4];
    var yelloworgel = [];
    for (i = 0; i < 2; i++) {
        rd = Math.floor(Math.random() * purpleorgel.length);
        yelloworgel.push(purpleorgel[rd]);
        purpleorgel.splice(rd, 1);
    }
    for (i = 0; i < purpleorgel.length; i++) {
        spawnMob(eim, orgel[purpleorgel[i]][0], pos[purpleorgel[i]][0], pos[purpleorgel[i]][1]);
    }
    for (i = 0; i < yelloworgel.length; i++) {
        spawnMob(eim, orgel[yelloworgel[i]][1], pos[yelloworgel[i]][0], pos[yelloworgel[i]][1]);
    }
    for (i = 0; i < 5; i++) {
        for (j = 0; j < 2; j++) {
            for (h = 0; h < 3; h++) {
                spawnMob(eim, etcmonster[i][j], pos[i][0], pos[i][1]);
            }
        }
    }
    eim.schedule("updateGauge", 0);
    if (schedulesg != null) {
        schedulesg.cancel(true);
    }
    respawnMonster(eim);

}

function updateGauge(eim) {
    var player = eim.getPlayers().get(0);
    var em2 = player.getEventInstance();
    var schedule = Timer.EtcTimer.getInstance().register(function () {
        if (player.getMapId() != 921171000) {
            schedule.cancel(true);
        }
        var hold = Integer.parseInt(em2.getProperty("GaugeHold"));
        if (hold > 0) {
            em2.setProperty("GaugeHold", (hold - 1) + "");
        } else {
            var purple = 0;
            var yellow = 0;
            for (i = 0; i < eim.getMapInstance(921171000).getAllMonstersThreadsafe().size(); i++) {
                mobid = eim.getMapInstance(921171000).getAllMonstersThreadsafe().get(i).getId()
                if (mobid >= 9833080 && mobid <= 9833084) {
                    purple++;
                } else if (mobid >= 9833070 && mobid <= 9833074) {
                    yellow++;
                }
            }
            var stage = Integer.parseInt(player.getKeyValue(15901, "stage"));
            var calc = yellow - purple;
	    var m = Math.max(10, parseInt(stage / 15));
            var speed = calc * -m;
            em.setProperty("Gauge", (Integer.parseInt(em.getProperty("Gauge")) + speed) + "");
            player.getClient().getSession().writeAndFlush(SLFCGPacket.DreamBreakerGaugePacket(Integer.parseInt(em.getProperty("Gauge"))));
            if (Integer.parseInt(em.getProperty("Gauge")) <= 0) {
                em.setProperty("TimerOn", "0");
                player.getClient().getSession().writeAndFlush(SLFCGPacket.DreamBreakerDisableTimer(true, (180 - time) * 1000));
                if (stage == Integer.parseInt(player.getKeyValue(15901, "best"))) {
                    var besttime = Integer.parseInt(player.getKeyValue(15901, "besttime"));
                    if (besttime > time) {
                        player.setKeyValue(15901, "besttime_b", "" + besttime);
                        player.setKeyValue(15901, "best_b", "" + stage);
                        player.setKeyValue(15901, "besttime", "" + time);
                    }
                } else if (stage >= Integer.parseInt(player.getKeyValue(15901, "best"))) {
                    player.setKeyValue(15901, "besttime_b", "" + player.getKeyValue(15901, "besttime"));
                    player.setKeyValue(15901, "best_b", "" + player.getKeyValue(15901, "best"));
                    player.setKeyValue(15901, "best", "" + stage);
                    player.setKeyValue(15901, "besttime", "" + time);
                }
                var pluspoint = 0;
                if (stage < 10) {
                    pluspoint = 10;
		} else if (stage >= 100) {
		    pluspoint = 100;
                } else {
                    pluspoint = stage - (stage % 10);
                }
                player.getClient().getSession().writeAndFlush(SLFCGPacket.DreamBreakerMsg("드림포인트 " + pluspoint + " 획득!"));
                player.setKeyValue(15901, "dream", (Integer.parseInt(player.getKeyValue(15901, "dream")) + pluspoint) + "");
                player.setKeyValue(15901, "clearTime", "" + time);
                player.AddStarDustCoin(stage);
                em.setProperty("Gauge", "500");
                player.getClient().getSession().writeAndFlush(SLFCGPacket.DreamBreakerGaugePacket(Integer.parseInt(em.getProperty("Gauge"))));
                player.getMap().killAllMonsters(true);
                player.setKeyValue(15901, "stage", ++stage + "");
                
                player.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.DreamBreakerResult(time));
                var rank = DreamBreakerRank.getRank(player.getName());
                player.setKeyValue(15901, "rank_b", "" + rank);
                if (schedulesg != null) {
                    schedulesg.cancel(true);
                }
                eim.schedule("asdf", 3000);
                schedule.cancel(true);
            } else if (Integer.parseInt(em.getProperty("Gauge")) >= 1000) {
                em.setProperty("TimerOn", "0");
                player.getClient().getSession().writeAndFlush(SLFCGPacket.DreamBreakerDisableTimer(true, (180 - time) * 1000));
                var temp2 = parseInt(player.getKeyValue(15901, "selectedStage"));
                player.warp(outmap);
                if (stage == temp2) {
                    player.getClient().getSession().writeAndFlush(CField.environmentChange("Map/Effect2.img/event/gameover", 16));
                } else if (stage > temp2) {
                    player.getClient().getSession().writeAndFlush(CField.environmentChange("Map/Effect3.img/hungryMuto/Clear", 16));
                    player.getClient().getSession().writeAndFlush(SLFCGPacket.DreamBreakerMsg((stage - 1) + "스테이지 클리어!"));
                }
            }
        }

    }, 1000);
}

function respawnMonster(eim) {
    var player = eim.getPlayers().get(0);
    var em2 = player.getEventInstance();
    schedulesg = Timer.EtcTimer.getInstance().register(function () {
        if (player.getMapId() != 921171000) {
            schedulesg.cancel(true);
        }
        if (Integer.parseInt(em2.getProperty("StopSpawn")) > 0) {
            em2.setProperty("StopSpawn", (Integer.parseInt(em2.getProperty("StopSpawn")) - 1) + "");
        } else {
            var count = 3 - player.getMap().countMonsterById(9833090);
            for (var i = 0; i < count; i++) {
                spawnMob(eim, 9833090, 792, -457);
            }
            count = 3 - player.getMap().countMonsterById(9833091);
            for (var i = 0; i < count; i++) {
                spawnMob(eim, 9833091, 792, -457);
            }

            count = 3 - player.getMap().countMonsterById(9833092);
            for (var i = 0; i < count; i++) {
                spawnMob(eim, 9833092, 813, -1963);
            }
            count = 3 - player.getMap().countMonsterById(9833093);
            for (var i = 0; i < count; i++) {
                spawnMob(eim, 9833093, 813, -1963);
            }

            count = 3 - player.getMap().countMonsterById(9833094);
            for (var i = 0; i < count; i++) {
                spawnMob(eim, 9833094, 3107, -237);
            }
            count = 3 - player.getMap().countMonsterById(9833095);
            for (var i = 0; i < count; i++) {
                spawnMob(eim, 9833095, 3107, -237);
            }

            count = 3 - player.getMap().countMonsterById(9833096);
            for (var i = 0; i < count; i++) {
                spawnMob(eim, 9833096, 838, 1485);
            }
            count = 3 - player.getMap().countMonsterById(9833097);
            for (var i = 0; i < count; i++) {
                spawnMob(eim, 9833097, 838, 1485);
            }

            count = 3 - player.getMap().countMonsterById(9833098);
            for (var i = 0; i < count; i++) {
                spawnMob(eim, 9833098, -1450, -237);
            }
            count = 3 - player.getMap().countMonsterById(9833099);
            for (var i = 0; i < count; i++) {
                spawnMob(eim, 9833099, -1450, -237);
            }
        }
    }, 10000);
}

function spawnMob(eim, mobid, x, y) {
    var player = eim.getPlayers().get(0);
    var stage = Integer.parseInt(player.getKeyValue(15901, "stage"));
    var map = eim.getMapInstance(0);
    var mob1 = em.getMonster(mobid);
    if (!(mobid >= 9833070 && mobid <= 9833074)) {
        var hp = GameConstants.getDreamBreakerHP(stage);
        mob1.setHp(hp);
        mob1.getStats().setHp(hp);
    }
    eim.registerMonster(mob1);
    map.spawnMonsterOnGroundBelow(mob1, new java.awt.Point(x, y));
}

function playerEntry(eim, player) {
    var map = eim.getMapInstance(0);
    player.changeMap(map, map.getPortal(0));
}

function playerRevive(eim, player) {
    return false;
}

function scheduledTimeout(eim) {
    end(eim);
}

function changedMap(eim, player, mapid) {
    if (mapid != eim.getMapInstance(0).getId()) {
        eim.unregisterPlayer(player);

        if (eim.disposeIfPlayerBelow(0, 0)) {
            //em.setProperty("state", "0");
        }
    }
}

function playerDisconnected(eim, player) {
    return 0;
}

function monsterValue(eim, mobId) {
    return 1;
}

function playerExit(eim, player) {
    eim.unregisterPlayer(player);
    eim.disposeIfPlayerBelow(0, 0);
}

function end(eim) {
    var player = eim.getPlayers().get(0);
    eim.disposeIfPlayerBelow(100, outmap);
    player.getClient().getSession().writeAndFlush(CField.environmentChange("Map/Effect3.img/hungryMuto/Clear", 16));
}


function clearPQ(eim) {
    end(eim);
}


function disposeAll(eim) {
    var iter = eim.getPlayers().iterator();
    while (iter.hasNext()) {
        var player = iter.next();
        eim.unregisterPlayer(player);
        player.changeMap(eim.getMapInstance(1), eim.getMapInstance(1).getPortal(0));
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