function enter(pi) {
if (pi.getQuestStatus(31348) == 2 && pi.getQuestStatus(31351) == 0) {
	pi.warp(240092101, 0);
	pi.playPortalSE();    
} else {
	pi.warp(240092100, 0);
}

}
