var status = -1;

function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
	if (cm.getClient().getChannelServer().getMapFactory().getMap(cm.getMapId()).getMobsSize() > 0) {
		cm.sendOk("이미 소환된 몬스터가 있습니다.");
	} else if (cm.getPlayer().getKeyValue(10, "Clear_NVanreon" > 0) || cm.getPlayer().getKeyValue(10, "Clear_HVanreon" > 0)) {
		cm.sendOk("이미 클리어했습니다.");
	} else {
		cm.spawnMob(cm.getMapId() == 211070100 ? 8840000 : 8840014, cm.getPlayer().getTruePosition().getX(), cm.getPlayer().getTruePosition().getY());
		cm.setDeathcount(selection == 8840000 ? 5 : 15);
	}
	cm.dispose();
    }
}

