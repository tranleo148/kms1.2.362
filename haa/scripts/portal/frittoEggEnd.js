var status = -1;
var idx, skill, req, sp;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
	stage = cm.getPlayer().getKeyValue(15042, "Stage");
	switch (stage) {
	    case 1:
		cm.sendNextNoESC("이런이런... 첫 번째 단계도 넘지 못하다니. 운이 없군 그래?", 9001060);
		break;
	    case 2:
		cm.sendNextNoESC("이런이런... 두 번째 단계에서 실패하다니. 다음에는 잘 해봐!", 9001060);
		break;
	    case 3:
		cm.sendNextNoESC("이런이런... 세 번째 단계에서 실패하다니. 다음에는 잘 해봐!", 9001060);
		break;
	    case 4:
		cm.sendNextNoESC("와~ 네 번째 단계까지 오다니. 아까웠어!", 9001060);
		break;
	    case 5:
		cm.sendNextNoESC("와~ 마지막 단계까지 오다니. 아까웠어!", 9001060);
		break;
	}
    } else if (status == 1) {
	cm.dispose();
	cm.warp(993000601);
    }
}