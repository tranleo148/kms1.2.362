


/*

	오딘 KMS 팀 소스의 스크립트 입니다.

	포탈이 있는 맵 : 아리안트

	포탈 설명 : 자유시장입구로 이동


*/


function enter(pi) {
    pi.playPortalSE();
    pi.saveLocation("FREE_MARKET");
    pi.warp(910000000, "out00");
    return true;
}
