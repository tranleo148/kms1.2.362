function enter(pi) {
if (pi.getQuestStatus(30008) != 0) {
pi.warp(105200000);
} else {
pi.warp(910700200);
}
if(pi.getPlayer().getKeyValue("luta") == "start") {
pi.openNpc(1064001);
}

}
