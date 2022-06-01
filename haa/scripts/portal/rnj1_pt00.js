function enter(pi) {
	if (pi.getPlayer().getEventInstance().getProperty("RomeoAndJulietPQ_Gate") == 1) {
	pi.resetMap(926100001);
    pi.warpParty(926100001);
	pi.getPlayer().getEventInstance().setProperty("RomeoAndJulietPQ_Gate","0");
	} else {
	}
}