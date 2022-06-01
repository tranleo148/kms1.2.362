function enter(pi) {
    if (pi.getPlayer().getKeyValue(100793, "rMap") > 0) {
        map = parseInt(pi.getPlayer().getKeyValue(100793, "rMap"));
        pi.warp(map, 0);
    } else {
        pi.warp(100000000, 0);
    }
    return true;
}