function enter(pi) {
    if (pi.getMap().getReactorByName("rnj3_out3").getState() > 0) {
	pi.warpParty(926100203);
	pi.openNpc(2112010);
    }
}