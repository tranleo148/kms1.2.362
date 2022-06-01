function enter(pi) {
if (pi.getMap().getReactorByName("beaker1").getState() + pi.getMap().getReactorByName("beaker2").getState() + pi.getMap().getReactorByName("beaker3").getState() == 21) {
	pi.resetMap(926110200);
    pi.warp(926110200,0);
	} else {
}
}