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
	if(pi.getPlayer().getKeyValue("luta") == null) {
	cm.sendNextS("흠.. 원래 이런곳에 포탈이 있엇나?",2);
	pi.dispose();
	} else {
	pi.warp(105010200);
	if(pi.getPlayer().getKeyValue("luta") == "start") {
	pi.sendNextS("사방에 안개가 자욱해서 시야를 구분할 수 없어. 뭐가 나올지 모르니 조심해야겠다.");
	}
	pi.dispose();
	}
}