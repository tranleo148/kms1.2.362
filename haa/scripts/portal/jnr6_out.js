function enter(pi) {
	var eim = pi.getPlayer().getEventInstance();
	if (eim.getProperty("RomeoAndJulietPQ_Gate") == 2) {
    pi.warp(926110300,0);
	eim.setProperty("RomeoAndJulietPQ_Gate","0");
	} else {
}
}