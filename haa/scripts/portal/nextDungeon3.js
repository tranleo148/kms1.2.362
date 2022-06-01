function enter(pi) {
    pi.warp(pi.getMapId() + 3000,"before02");
        if(pi.getMonsterCount(pi.getMapId()) == 0) {
pi.getPlayer().getMap().respawn(true);
       }
}