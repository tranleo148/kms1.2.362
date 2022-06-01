function enter(pi) {
    pi.warp(pi.getMapId() - 1000,"next02");
        if(pi.getMonsterCount(pi.getMapId()) == 0) {
pi.getPlayer().getMap().respawn(true);
       }
}