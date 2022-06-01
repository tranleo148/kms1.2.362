function enter(pi) {
    if (!pi.isLeader()) {
	pi.playerMessage(5, "파티장 만이 다음 스테이지로 이동 할 수 있습니다.");
    } else {
	if (pi.getMap().getAllMonstersThreadsafe().size() != 0) {
	    pi.playerMessage(5, "맵의 모든 몬스터를 퇴치해야만 다음 스테이지로 이동이 가능합니다.");
	    return;
	}
	if (((pi.getMapId() % 10) | 0) == 4) { //last stage
	    if (!pi.getPlayer().isGM()){
	    if (pi.getMap().getReactorByName("switch0").getState() < 1 || pi.getMap().getReactorByName("switch1").getState() < 1) {
		pi.playerMessage(5, "모든 스위치를 작동시켜야 다음 스테이지로 이동이 가능합니다.");
		return;
	    }
	    }
	    var bossroom = pi.getMapId() + 66;//90-14 = 76, 90-24=66
	    if (((bossroom % 100) | 0) != 90) {
		bossroom += 10;
	    }
	    pi.warpParty(bossroom, 0);
	} else {
	    pi.warpParty(pi.getMapId() + 1, ((pi.getMapId() % 10) | 0) == 3 ? 1 : 2);
	}
    }
}
