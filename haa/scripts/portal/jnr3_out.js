function enter(pi) {
    if (pi.getMap().getReactorByName("jnr3_out3").getState() > 0) {
	pi.warp(926110203,0);
	pi.openNpc(2112010);
    }
}