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
	for (i = 0; i < cm.getClient().getUnions().size(); ++i)
	{
	if (cm.getClient().getUnions().get(i).getPosition() != -1) {
		cm.getPlayer().dropMessage(6, cm.getClient().getUnions().get(i).getName() + " 캐릭터의 포지션 : " + cm.getClient().getUnions().get(i).getPosition());
	}
	}
	cm.dispose();
    }
}

