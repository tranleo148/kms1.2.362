function enter(pi) {
    if (pi.getMonsterCount(pi.getMapId()) == 0 && pi.getMapId() != 921160350) {
        if (pi.getMapId() == 921160350) {
	    pi.warpParty(pi.getMapId() + 50);
    	} else {
	    pi.warpParty(pi.getMapId() + 100);
	}
    }
}