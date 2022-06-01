


/*

	오딘 KMS 팀 소스의 스크립트 입니다.

	포탈이 있는 맵 : 900000000

	포탈 설명 : 시작맵 포탈차단


*/


function enter(pi) {
    rand = Math.floor(Math.random() * 1);
    pi.teleport(rand < 1 ? 321 : -855, rand < 1 ? 1027 : -842);
    return false;
}
