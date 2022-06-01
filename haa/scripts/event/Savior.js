importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);

var outmap = 921172400;

var leftmobxy = [
    [-898, -731, 8644101],
    [-1645, -971, 8644102],
    [-2993, -1571, 8644103],
    [-2142, -1751, 8644104],
    [-1315, -1991, 8644105],
    [-830, -2231, 8644106]
];
var rightmobxy = [
    [488, -731, 8644107],
    [1369, -971, 8644108],
    [2661, -1571, 8644109],
    [1852, -1751, 8644110],
    [1096, -1991, 8644111],
    [563, -2231, 8644112]
];
var fullmobxy = [
    [-898, -731, 8644101],
    [-1645, -971, 8644102],
    [-2993, -1571, 8644103],
    [-2142, -1751, 8644104],
    [-1315, -1991, 8644105],
    [-830, -2231, 8644106],
    [488, -731, 8644107],
    [1369, -971, 8644108],
    [2661, -1571, 8644109],
    [1852, -1751, 8644110],
    [1096, -1991, 8644111],
    [563, -2231, 8644112]
];

function init() {}

function setup(mapid) {
    map = parseInt(mapid);
    var eim = em.newInstance("Savior");
    eim.setInstanceMap(map).resetFully();

    for (var i = 0; i < 5; i++) {
        em.setProperty("ObjectId" + i, "");
    }

    var temp = fullmobxy.sort(function () {
        return Math.random() - Math.random()
    }) //배열 랜덤
    
    for (var i = 0; i < 4; i++) {
        spawnMob(eim, temp[i][2], temp[i][0], temp[i][1]);
    }
    eim.startEventTimer(1000 * 60 * 3);
    eim.schedule("effect", 1000);
    return eim;
}

function effect(eim) {
    var player = eim.getPlayers().get(0);
    player.getClient().getSession().writeAndFlush(CField.environmentChange("event/start", 19));
    player.getClient().getSession().writeAndFlush(SLFCGPacket.playSE("Dojang/clear"));
}

function spawnReactor(eim, rid, x, y) {
    var reactor = new MapleReactor(MapleReactorFactory.getReactor(rid), rid);
    reactor.setDelay(-1);

    var iter = eim.getPlayers().iterator();
    while (iter.hasNext()) {
        var player = iter.next();
        player.getMap().spawnReactorOnGroundBelow(reactor, new java.awt.Point(x, y));
        break;
    }

}

function spawnMob(eim, mobid, x, y) {
    var map = eim.getMapInstance(0);
    var mob1 = em.getMonster(mobid);
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
    var x = 0,
        y = 0;
    var temp = [];
    var player = eim.getPlayers().get(0);
    var check = 0;
    for (var i = 0; i < fullmobxy.length; i++) {
        if (fullmobxy[i][2] == mobId) {
            spawnReactor(eim, 3600001, fullmobxy[i][0], fullmobxy[i][1]);
            temp = fullmobxy[i];
            break;
        }
    }

    var map = player.getMap();
    /*for (var i = 8644305; i >= 8644301; i--) {
        var mob = map.getMonsterById(i);

        if (8644305 == i && mob != null) {
            break;
        } else if (8644301 == i && mob == null) {
            var mob1 = Packages.server.life.MapleLifeFactory.getMonster(i);
            map.spawnMonsterOnGroundBelow(mob1, new java.awt.Point(-194, -1391));
            break;
        } else if (mob != null) {
            var mob1 = Packages.server.life.MapleLifeFactory.getMonster(i + 1);
            var pos = mob.getPosition();
            player.getMap().killMonster(mob);ss
            map.spawnMonsterOnGroundBelow(mob1, pos);
            break;
        }
    }*/

    var mob = map.getMonsterById(8644301);

    if (temp[0] < 0) {
        var temp2 = rightmobxy.sort(function () {
            return Math.random() - Math.random()
        });
        for (var i = 0; i < temp2.length; i++) {
            var mob = player.getMap().getMonsterById(temp2[i][2]);
            if (mob == null) {
                spawnMob(eim, temp2[i][2], temp2[i][0], temp2[i][1]);
                break;
            }
        }
    } else {
        var temp2 = leftmobxy.sort(function () {
            return Math.random() - Math.random()
        });
        for (var i = 0; i < temp2.length; i++) {
            var mob = player.getMap().getMonsterById(temp2[i][2]);
            if (mob == null) {
                spawnMob(eim, temp2[i][2], temp2[i][0], temp2[i][1]);
                break;
            }
        }
    }
    return 1;
}

function playerExit(eim, player) {
    eim.unregisterPlayer(player);
    eim.disposeIfPlayerBelow(0, 0);
}

function end(eim) {
    var player = eim.getPlayers().get(0);
    eim.disposeIfPlayerBelow(100, outmap);
    player.getClient().getSession().writeAndFlush(CField.environmentChange("Map/Effect2.img/event/gameover", 16));
    //eim.schedule("realend", 2000);
}

function realend(eim) {
    
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