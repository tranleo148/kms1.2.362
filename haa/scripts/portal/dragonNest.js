function enter(pi) {
    if (pi.haveItem(4001094,1)) {
        if (pi.getPlayerCount(240040611) == 0) {
            pi.resetMap(240040611);
            pi.warp(240040611,0);
        } else {
	    pi.showinfoMessage("이미 누군가가 안에 있습니다.");
    } else {
	pi.showinfoMessage("나인스피릿의 알이 없으면 입장할 수 없습니다.");
    }
}