function start() {
	status = -1;
	action(1, 0, 0);
}
function action(mode, type, selection) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
    	}
	if (status == 0) {
		var eim = cm.getPlayer().getEventInstance();
		cm.sendSimple("Select animation you want to watch for test purpose\r\n\r\n"
			+"#b#L0# 1 phase#l\r\n"
			+"#b#L1# 2 phase#l\r\n"
			+"#b#L2# 3 phase#l\r\n"
			+"#b#L3# 4 phase#l\r\n"
			+"#b#L4# Fail\r\n"
			+"eim : "+eim.getProperty("stage"));
	} else if (status == 1) {
		if (selection == 0) {
			cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.showSpineScreen(false, false, true, "Effect/Direction20.img/bossBlackMage/start_spine/blasck_space", "animation", 0, false, ""));
		} else if (selection == 1) {
			cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.showSpineScreen(false, false, true, "Effect/Direction20.img/bossBlackMage/start2_spine/skeleton", "animation", 0, false, ""));
		} else if (selection == 2) {
			cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.showSpineScreen(false, false, true, "Effect/Direction20.img/bossBlackMage/space/blasck_space", "animation", 0, false, ""));
		} else if (selection == 3) {
			cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.showSpineScreen(false, false, true, "Effect/Direction20.img/bossBlackMage/start4_spine/black_Phase_3_4", "animation", 0, false, ""));
		} else {
			cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.showSpineScreen(false, false, true, "Effect/Direction20.img/bossBlackMage/fail_spine/black", "animation", 0, false, ""));
		}		
		cm.dispose();
		return;	
	}
}