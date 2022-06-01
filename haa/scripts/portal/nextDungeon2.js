function enter(pi) {
    pi.warp(pi.getMapId() + 2000,"before01");
        if(pi.getMonsterCount(pi.getMapId()) == 0) {
pi.getPlayer().getMap().respawn(true);
       }
}