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
	status--;
	cm.dispose();
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
	cm.sendGetNumber("캐릭터 ID 입력.", 0, 0, 100000000);
    } else if (status == 1) {
	cm.dispose();
	var id = cm.getText();
	var player = Packages.handling.world.World.getChar(id);
	if (player != null) {
		Packages.constants.ResetKeyValue.ResetOnline(player, true);
	} else {
		Packages.constants.ResetKeyValue.ResetOffline(id, true);
	}
	cm.sendOk("ok");
    }
}