function enter(pi) {
    if (Math.random() * 100 + 1 > 50) {
	pi.warp(993000300, 10);
	pi.getPlayer().getFrittoEgg().updateStage(pi.getPlayer().getClient(), 5);
    } else {
	pi.getPlayer().getFrittoEgg().finishStage(pi.getPlayer().getClient(), 5);
    }
}