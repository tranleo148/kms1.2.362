importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.math);

var outmap = 100000000;
var time = 0;
var mob;
function init() {}

var unitLt = ['만', '억', '조', '경'];
function NtoKr(num) {
    var rslt = num;
    if (('' + num).length > 4) {
        rslt = '';
        var pos = 0;
        var nlt = ('' + num).split('');
        for (var i = 0; i < nlt.length; i++) {
            rslt = nlt[nlt.length - (i + 1)] + rslt;
            if ((i + 1) < nlt.length && (i % 4) == 3) {
                if (parseInt(rslt) != "0000") {
                    rslt = unitLt[pos++] + " " + rslt;
                } else {
                    rslt = unitLt[pos++]
                }
            }

        }
    }
    return rslt;
}

function setup(mapid) {
    map = parseInt(mapid);
    var eim = em.newInstance("UnionRaid");
    eim.setInstanceMap(map).resetFully();
    return eim;
}

function playerEntry(eim, player) {
    var map = eim.getMapInstance(0);
    player.changeMap(map, map.getPortal(0));
    spawnMonster(eim);
}

function spawnMonster(eim) {
    var map = eim.getMapInstance(0);
    mob = em.getMonster(9833101);
    mob2 = em.getMonster(9300183);

    getsecond = Math.floor((new Date().getTime() - eim.getPlayers().get(0).getKeyValue(20190622, "Union_Raid_Time_1"))/1000) // 나간 시점으로부터 몇초가 지났는지 계산
    mob2.setHp(Long.MAX_VALUE - (getsecond * eim.getPlayers().get(0).getKeyValue(20190622, "Union_Raid_Atk")));

    mob.setHp(Long.MAX_VALUE - eim.getPlayers().get(0).getKeyValue(20190622, "Union_Raid_Hp")); 

    eim.registerMonster(mob2);
    eim.registerMonster(mob);

    map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(2320,17));
    map.spawnMonsterOnGroundBelow(mob2, new java.awt.Point(2320,17));

    var schedule = Timer.MapTimer.getInstance().register(function () {
        if (eim.getPlayers().get(0).getMapId() != 921172000) {
            schedule.cancel(true);
        } else {
	    if (Long.MAX_VALUE - mob.getHp() > 10000000000000) {
		eim.getPlayers().get(0).dropMessage(6, "더 이상 보스몬스터에게 피해를 입힐 수 없습니다.");
		mob.setHp(Long.MAX_VALUE - 10000000000000);
		eim.getPlayers().get(0).getMap().updateMonsterController(mob);
	    } else if (Long.MAX_VALUE - mob.getHp() == 10000000000000) {
	    } else {
                eim.getPlayers().get(0).dropMessage(6, "입힌 데미지 : " + NtoKr(Long.MAX_VALUE - mob.getHp() - eim.getPlayers().get(0).getKeyValue(20190622, "Union_Raid_Hp")) + " / " + NtoKr(10000000000000 - eim.getPlayers().get(0).getKeyValue(20190622, "Union_Raid_Hp")));
	    }
            mob2.setHp(mob2.getHp() - eim.getPlayers().get(0).getKeyValue(20190622, "Union_Raid_Atk"));
	}
    }, 1000);
}

function playerRevive(eim, player) {
    return false;
}

function scheduledTimeout(eim) {
}

function changedMap(eim, player, mapid) {
    if (mapid != 921172200 && mapid != 921172000) {
        eim.unregisterPlayer(player);
        eim.disposeIfPlayerBelow(0, 0);
    }
}

function playerDisconnected(eim, player) {
    return 0;
}

function monsterValue(eim, mobId) {
    player = eim.getPlayers().get(0);
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