function enter(pi) {
	var eim = pi.getPlayer().getEventInstance();
	if (eim.getProperty("RomeoAndJulietPQ_Gate") == 1) {
	pi.resetMap(926110100);
    pi.warp(926110100,0);
	eim.setProperty("RomeoAndJulietPQ_Gate","0");
	} else {
}
}