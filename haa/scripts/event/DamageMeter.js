importPackage(Packages.database);
importPackage(java.lang);

var Time = new Date();
var Year = Time.getFullYear() + "";
var Month = Time.getMonth() + 1 + "";
var Date = Time.getDate() + "";
if (Month < 10) {
    Month = "0" + Month;    
}
if (Date < 10) {
    Date = "0" + Date;    
}
var Today = parseInt(Year + Month + Date);

function init() {
    em.setProperty("entry", "true");
}

function monsterValue(eim, mobId) {
    return 1;
}

function setup(eim) {
    em.getProperties().clear();
    var eim = em.newInstance("DamageMeter");
    var map = eim.setInstanceMap(450002250); //측정맵
    map.resetFully();
    map.killMonster(9833376); //몬스터코드
    var mob = em.getMonster(9833376); //몬스터코드
    //mob.setHp(9000000000000000000);
    eim.registerMonster(mob);
    map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(200, 100)); //좌표
    eim.startEventTimer(120000); //시간 (1s = 1000)
    return eim;
}

function playerEntry(eim, player) {
    var map = eim.getMapFactory().getMap(450002250); //측정맵
    player.changeMap(map, map.getPortal(0));
    em.setProperty("entry", "false");
}

function changedMap(eim, player, mapid) {
    if (mapid != 450002250) { //측정맵
        em.setProperty("entry", "true");
        eim.unregisterPlayer(player);
        eim.dispose();
    }
}

function playerRevive(eim, player) {}

function end(eim) {
    try {
        em.setProperty("entry", "true");
        var player = eim.getPlayers().get(0);
        var map = eim.getMapFactory().getMap(100000000); //나갈맵
        player.changeMap(map, map.getPortal(0));
        //if (player.getGMLevel() > 5) {
            var con = null;
            var ps = null;
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("DELETE FROM `DamageMeter` WHERE `date` = " + Today + " AND `characterid` = " + player.getId());
            ps.executeUpdate();
            ps.close();
            ps = con.prepareStatement("INSERT INTO `DamageMeter` (`characterid`, `name`, `damage`, `date`) VALUES (?, ?, ?, ?)");
            ps.setInt(1, player.getId());
            ps.setString(2, player.getName());
            ps.setLong(3, player.DamageMeter);
            ps.setInt(4, Today);          
            ps.executeUpdate();
            ps.close();
            con.close();
        //}
        eim.unregisterPlayer(player);
        eim.dispose();
        return;
    } catch (e) {
        cm.sendOk("오류가 발생하였습니다.\r\n데미지 미터 측정을 다시 수행해 주세요.\r\n\r\n" + e);
        cm.dispose();
        return;
    } finally {
        if (ps != null) {
            try {
                ps.close();
            } catch (e) {

            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (e) {

            }
        }
    }
}

function scheduledTimeout(eim) {
    var player = eim.getPlayers().get(0);
    eim.broadcastPlayerMsg(2, "[시스템] : 제한시간이 지나 데미지 기록이 저장되었습니다.   총 누적 데미지 : " + player.DamageMeter);
    end(eim);
}

function playerDead(eim, player) {}
function playerDisconnected(eim, player) {
    end(eim);
    return 0;
}
function allMonstersDead(eim) {}
function cancelSchedule() {}
function leftParty(eim, player) {}
function disbandParty(eim, player) {}
