importPackage(Packages.server);
var status;

function start() {
    status = -1;
    action(1, 1, 0);
}

function action(mode, type, selection) {
    if (mode <= 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
	talk = "코어슬롯 강화가 정상적으로 적용되었습니다.\r\n\r\n"
	level = cm.getPlayer().getLevel() - 200;
	for (i=0; i<99; i++) {
		cm.getPlayer().setKeyValue(23191, "upcore"+i, -1);
	}
	for (i=0; i<Math.floor(level/5); i++) {
		if (i<19) {
			cm.getPlayer().setKeyValue(23191, "upcore"+i, 5);
		}
	}
	if (Math.floor(level/5) < 19) {
	    cm.getPlayer().setKeyValue(23191, "upcore"+(Math.floor(level/5)), level%5);
	}
	cm.getPlayer().fakeRelog();
	cm.sendOk(talk);
	cm.dispose();
    }
}