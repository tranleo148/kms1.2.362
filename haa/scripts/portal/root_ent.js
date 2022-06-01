/*
 * 퓨어온라인 소스 스크립트 입니다.
 * 
 * 포탈위치 : 
 * 포탈설명 : 
 * 
 * 제작 : 주크블랙
 * 
 */

function enter(pi) {
	if (pi.getPlayer().getMapId() == 105200100 || pi.getPlayer().getMapId() == 105200200 || pi.getPlayer().getMapId() == 105200300 || pi.getPlayer().getMapId() == 105200400) {
		pi.warp(105200000);
	} else if(pi.getPlayer().getKeyValue("luta") == null) {
	pi.openNpc(1103005);
	} else {
	pi.warp(105010200);
	pi.openNpc(1103005);
	}
}