function action() {
	if (cm.getPlayer().getLevel() < 999 || cm.getPlayer().getLevel() == 275) {
	    cm.gainItem(2430218, -1);
            cm.getPlayer().gainExp(cm.getPlayer().getNeededExp() - cm.getPlayer().getExp(), false, false, false);
	    cm.dispose();
        } else { 
            cm.sendOk("해당 레벨은 효과를 볼 수 없습니다.");
            cm.dispose();
		return;
        }
}