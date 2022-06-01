function enter(pi) {
	if (pi.getPlayer().getEventInstance().getProperty("RomeoAndJulietPQ_Gate") == 1) {
	pi.resetMap(926110001);
    pi.warp(926110001,0);
	pi.getPlayer().getEventInstance().setProperty("RomeoAndJulietPQ_Gate","0");
	} else {
}
}