importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.util);

var EtcTimer = Packages.server.Timer.EtcTimer;

var outmap = 933000000;
var outmap2 = 993189200;
var time = 0;

function init() {}

function setup(mapid) {
    map = parseInt(mapid);
    var eim = em.newInstance("MoonPQ");
    eim.setInstanceMap(map).resetFully();
    return eim;
}

function playerEntry(eim, player) {
    eim.startEventTimer(1200000);
    var map = eim.getMapInstance(0);
    player.changeMap(map, map.getPortal(0));
    player.map.killAllMonsters(true);
    player.map.setPartyCount(1);
    player.map.setMoonCake(0);
    var tick = 0;
    schedule = EtcTimer.getInstance().register(function () {
	if(tick == 8){
        if(tick > 8){
            schedule.cancel(true);
            return;
        }
        schedule.cancel(true);
        return;
	} else {
        if(tick == 7){
            spawnMonster(eim);
        }
        player.map.broadcastMessage(CField.startMapEffect("돼지가 훔쳐 먹은 달맞이꽃 씨앗 6개를 되찾아야 해.", 5120016, true));
	}
        tick++;
    }, 1000);
}

function monsterValue(eim, mobId) {
    var stage = parseInt(eim.getProperty("stage"));
    return 1;
}

function spawnMonster(eim) {
    var map = eim.getMapInstance(0);
    var Mmobid = [9300900, 9300901];
    var Mmobx = [-902, -759, -553, -416, -185, -42, 155, 329, 544, 700];
    var Mmoby = [213, 273];
    for(var i = 0; i < Mmobx.length; i++){
        for(var j = 0; j < Mmoby.length-1; j++){
            map.spawnMonsterOnGroundBelow(Packages.server.life.MapleLifeFactory.getMonster(Mmobid[0]), new java.awt.Point(Mmobx[i], Mmoby[j]));
            map.spawnMonsterOnGroundBelow(Packages.server.life.MapleLifeFactory.getMonster(Mmobid[1]), new java.awt.Point(Mmobx[i], Mmoby[j]));
        }
    }
}

function playerRevive(eim, player) {
    return false;
}

function scheduledTimeout(eim) {
    end(eim);
}

    

function changedMap(eim, player, mapid) {
    if (mapid != 933001000) {
        eim.unregisterPlayer(player);
        eim.disposeIfPlayerBelow(0, 0);
    }
}

function playerDisconnected(eim, player) {
    return 0;
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