function action() {
	isok = false;
	list = ["김아지", "나영", "텔레포트", "몽몽", "곰이곰이"]
	for (i=0; i<list.length; i++) {
		if (cm.getPlayer().getName() == list[i]) {
			isok = true;
		}
	}
	if (isok == true) {
	if (cm.getPlayer().getLevel() < 999 || cm.getPlayer().getLevel() == 275) {
	    cm.gainItem(2430230, -1);
            cm.getPlayer().gainExp(cm.getPlayer().getNeededExp() - cm.getPlayer().getExp(), false, false, false);
	    cm.dispose();
        } else { 
            cm.sendOk("해당 레벨은 효과를 볼 수 없습니다.");
            cm.dispose();
		return;
        }
	} else {
		cm.sendOk("해당 폭풍 성장의 비약은 퀴즈 정답자에게만 적용됩니다.");
		cm.dispose();
		return;
	}
}