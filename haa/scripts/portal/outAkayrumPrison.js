/*
 * 아카이럼
 */

importPacages()
function enter(pi) {
    var map = pi.getPlayer().getMap();
    if (pi.getMonsterCount(map.getId()) <= 0) {
        pi.warp(map.getId() - 100);
    } else {
        pi.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.startMapEffect("자신 속의 추악한 모습을 극복할 능력도 없는 피라미들은 이 곳을 나갈 수 없겠지요. 하하하.", 5120056, true));
    }
    return false;
}
