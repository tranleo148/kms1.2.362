function enter(pi) {
	if (pi.getMap().getReactorByName("beaker1").getState() + pi.getMap().getReactorByName("beaker2").getState() + pi.getMap().getReactorByName("beaker3").getState() == 21) {
	pi.resetMap(926100200);
    pi.warpParty(926100200);
	pi.getPlayer().getEventInstance().setProperty("RomeoAndJulietPQ_Gate","0");
	} else {
}
}