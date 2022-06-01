
function enter(pi) {
if (pi.getPlayer().getMapId() == 105200100 || pi.getPlayer().getMapId() == 105200200 || pi.getPlayer().getMapId() == 105200300 || pi.getPlayer().getMapId() == 105200400) {
pi.warp(105200000);
} else if(pi.getPlayer().getMapId() == 105200000) {
pi.warp(100000000);
} else {
if(pi.getQuestStatus(30002) == 1) {
pi.openNpc(1064001);
} else if (pi.getPlayer().getKeyValue("lutat") == "start") {
pi.warp(105010200);
pi.openNpc(1064001);
} else {
pi.warp(100000000);
}
}
}