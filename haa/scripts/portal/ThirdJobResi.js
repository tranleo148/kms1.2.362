function enter(pi) {
    if (pi.getQuestStatus(23033) == 1 || pi.getQuestStatus(23034) == 1 || pi.getQuestStatus(23035) == 1) {
	if (pi.getPlayerCount(931000200) == 0) {
	    pi.resetMap(931000200);
	    pi.warp(931000200,0);
	} else {
	    pi.playerMessage("대화가 없습니다.");
	}
    }
}