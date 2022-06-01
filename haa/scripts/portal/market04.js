


/*

	퓨어 소스 팩의 스크립트 입니다. (제작 : 주크블랙)

	포탈이 있는 맵 : 페리온

	포탈 설명 : 자유시장입구로 이동


*/


function enter(pi) {
    pi.saveLocation("FREE_MARKET");
    pi.warp(910000000, "out00");
    return true;
}
