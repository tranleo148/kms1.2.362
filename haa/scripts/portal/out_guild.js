


/*

	오딘 KMS 팀 소스의 스크립트 입니다.

	포탈이 있는 맵 : 200000301

	포탈 설명 : 길드본부에서 나감.


*/


function enter(pi) {
    if (pi.getClient().getChannelServer().getMapFactory().getMap(pi.getSavedLocation("GUILD")).getPortal("guildMove00") == null) {
        pi.warp(pi.getSavedLocation("GUILD"));
    } else {
        pi.warp(pi.getSavedLocation("GUILD"), "guildMove00");
    }
    return true;
}
