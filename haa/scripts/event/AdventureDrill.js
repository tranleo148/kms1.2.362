importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.util);

var outmap = 993080400;
var outmap2 = 993080000;
var time = 0;

function init() {}

function setup(mapid) {
    map = parseInt(mapid);
    var eim = em.newInstance("AdventureDrill");
    eim.setInstanceMap(map).resetFully();
    return eim;
}

function playerEntry(eim, player) {
    eim.startEventTimer(60000);
    var map = eim.getMapInstance(0);
    player.changeMap(map, map.getPortal(0));
    em.setProperty("Monster", "0");
    em.setProperty("DrillStatus", "0");
    spawnMonster(eim);
}

function spawnMonster(eim) {
    var map = eim.getMapInstance(0);
    var mobx = [45, 240, 440, 635];
    var moby = [-357, -657, -957, -1257, -1557, -1857, -2157, -2457, -2757, -3057]
    for (i = 0; i < mobx.length; i++) {
        for (j = 0; j < moby.length; j++) {
            var mobid = 9833338 + (i * 10) + j;
            mob = em.getMonster(mobid);
            eim.registerMonster(mob);
            map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(mobx[i], moby[j]));
        }
    }
}

function playerRevive(eim, player) {
    return false;
}

function scheduledTimeout(eim) {
    switch (parseInt(em.getProperty("DrillStatus"))) {
        case 0:
            em.setProperty("DrillStatus", "1");
            var player = eim.getPlayers().get(0);
            map = Packages.handling.channel.ChannelServer.getInstance(player.getClient().getChannel()).getMapFactory().getMap(outmap);
            player.changeMap(map, map.getPortal(0));
            player.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.stopClock());
            break;
        case 1:
            end(eim);
        default:
            eim.getPlayers().get(0).dropMessage(5, "[오류] 호출되지 않아야 할 상태가 호출되었습니다. 운영자께 문의해 주세요.")
            end(eim);
            break;

    }
}

function changedMap(eim, player, mapid) {
    if (mapid != 993080200 && mapid != 993080400) {
        eim.unregisterPlayer(player);
        eim.disposeIfPlayerBelow(0, 0);
    }
}

function playerDisconnected(eim, player) {
    return 0;
}

function monsterValue(eim, mobId) {
    player = eim.getPlayers().get(0);
    if (mobId >= 9833338 && mobId <= 9833377) {

        getMobCount = Integer.parseInt(em.getProperty("Monster")) + 1;
        player.dropMessage(6, getMobCount);
        em.setProperty("Monster", getMobCount + "");
    }
    return 1;
}

function playerExit(eim, player) {
    eim.unregisterPlayer(player);
    eim.disposeIfPlayerBelow(0, 0);
}

function end(eim) {
    var player = eim.getPlayers().get(0);
    eim.disposeIfPlayerBelow(100, outmap2);
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