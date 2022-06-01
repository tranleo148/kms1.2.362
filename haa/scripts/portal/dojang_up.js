function enter(pi) {
	if ((pi.getPlayer().getSkillCustomValue0(92507) > 0 || pi.haveMonster()) && pi.getPlayer().getMap().getId() != 925070000) {
		pi.playerMessage("아직 몬스터가 남아있습니다.");
	} else {
		if (pi.getPlayer().getMap().getId() == 925078000) {
			pi.warp(925040000, 1);
		} else {
			pi.warp(pi.getPlayer().getMap().getId() + 100, 1);
			pi.doJoWarpMap(pi.getPlayer().getMap().getId());
			pi.getPlayer().setSkillCustomInfo(92507, 1, 4000);
		}
		if (pi.getPlayer().getMapId() != 925070100) {
			if (pi.getPlayer().getInfoQuest(3887) != "") {
				pi.getPlayer().updateInfoQuest(3887, "point=" + (parseInt(pi.getPlayer().getInfoQuest(3887).split("=")[1]) + 10));
			} else {
				pi.getPlayer().updateInfoQuest(3887, "point=10");
			}
			pi.playerMessage("10포인트를 획득하였습니다.");
			if (pi.getPlayer().getKeyValue(100466, "Score") < pi.getPlayer().getKeyValue(3, "dojo")) {
				pi.playerMessage("금주 최고 기록을 달성하였습니다.");
			}
		}
		
		pi.getPlayer().getMap().resetFully();
	}
}