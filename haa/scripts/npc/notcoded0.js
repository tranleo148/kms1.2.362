function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
	if (cm.getPlayer().isGM()) {
	cm.sendOkS("#b아직 등록되지 않은 NPC입니다.\r\n#d코드 : " + cm.getNpc() + " 스크립트 : " + cm.getScript() + "\r\n#k스크립트가 있는 npc는 스크립트로만 작동해야 하는데 바꿔버렸음.", 2);
	} else {
	cm.sendOkS("#b칸[葉]#k에서 즐거운 시간 보내시길 바랍니다.", 4);
	}
	cm.dispose();
}