function act() {
    if (rm.getPlayer().getMap().getId() / 100 % 100 != 38) {
        rm.warp(rm.getPlayer().getMap().getId() + 100);
    }
}