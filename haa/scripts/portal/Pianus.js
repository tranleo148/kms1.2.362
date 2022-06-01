


/*

	퓨어 온라인 소스 스크립트 입니다.

	포탈이 있는 맵 : 위험한 동굴

	포탈 설명 : 피아누스의 동굴


*/

var map = 230040420;

function enter(pi) {
    if (!pi.checkTimeValue("Pianus_BattleStartTime", 3600 * 2)) {
        pi.getPlayer().message(5, "피아누스는 2시간마다 한번씩만 도전할 수 있습니다.");
        return false;
    }
    if (pi.getPlayerCount(230040420) >= 1) {
           pi.getPlayer().message(5, "이미 누군가가 피아누스를 잡고있습니다.");
           return false;
    }
    pi.setTimeValueCurrent("Pianus_BattleStartTime");
    pi.playPortalSE();
    pi.warp(map);
    pi.getPlayer().getMap().killAllMonsters(true);
    pi.getPlayer().getMap().respawn(true);
    
    return true;
}
