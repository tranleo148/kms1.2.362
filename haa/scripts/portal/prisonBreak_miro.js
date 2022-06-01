function enter(pi) {
    if (pi.getMonsterCount(pi.getMapId()) == 0) {
	if (Math.random() > 0.9) {
	    pi.warpParty(921160400);
	} else {
	    pi.warp(921160300 + ((Math.floor(Math.random() * 6) | 0) * 10),0);
	}
    } else {
	pi.playerMessage("대화가 없습니다.");
    }
}