function enter(pi) {
	if (pi.getPlayer().getEventInstance().getProperty("RomeoAndJulietPQ_Gate") == 2) {
    pi.warpParty(926100300);
	pi.getPlayer().getEventInstance().setProperty("RomeoAndJulietPQ_Gate","0");
	} else {
}
}