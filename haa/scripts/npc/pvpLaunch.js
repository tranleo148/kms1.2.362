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
	text = "#fc0xFFFF9933#Pixi 유저 #h 0##k님 어서오세요~ 언제나 환영합니다!\r\n\r\n" +
	"#L28#훈장옵션#l    #L29#훈장잠재#l    #L31#훈장미션#l    #L30#훈장전승#l\r\n";
	cm.sendIllustSimple(text,0x24);
    } else if (status == 1) {
	cm.dispose();
	switch (selection) {
		case 28:
		cm.openNpc("backTuto_9062001");
		break;
		case 29:
		cm.openNpc("backTuto_9062002");
		break;
		case 30:
		cm.openNpc(2159000);
		break;
		case 31:
		cm.openNpc(9000066);
		break;
	}
    }
}

