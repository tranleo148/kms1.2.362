


/*

	히나 소스 팩 스크립트 입니다.

	포탈이 있는 맵 : 

	포탈 설명 : 골렘사원 입장스크립트


*/


function enter(pi) {
        if(pi.getPlayer().getMapId() == 240070202){
	pi.warp(240070203, 0);
        if(pi.getMonsterCount(240070203) == 0) {
pi.getPlayer().getMap().respawn(true);
       }
} else if(pi.getPlayer().getMapId() == 240070302){
	pi.warp(240070303, 0);
        if(pi.getMonsterCount(240070303) == 0) {
pi.getPlayer().getMap().respawn(true);
       }
} else if(pi.getPlayer().getMapId() == 240070402){
	pi.warp(240070403, 0);
        if(pi.getMonsterCount(240070403) == 0) {
pi.getPlayer().getMap().respawn(true);
       }
} else if(pi.getPlayer().getMapId() == 240070502){
	pi.warp(240070503, 0);
        if(pi.getMonsterCount(240070503) == 0) {
pi.getPlayer().getMap().respawn(true);
       }
} else if(pi.getPlayer().getMapId() == 240070602){
	pi.warp(240070603, 0);
        if(pi.getMonsterCount(240070603) == 0) {
        pi.getPlayer().getMap().respawn(true);
       }
}
	pi.playPortalSE();    
}
