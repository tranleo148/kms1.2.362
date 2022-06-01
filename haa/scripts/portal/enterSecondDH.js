function enter(pi) {
    for (i = 0; i < 5; i++) {
    if (pi.getQuestStatus(20201 + i) == 1) {
	pi.warp(913001000,0);
	return;
    }
}