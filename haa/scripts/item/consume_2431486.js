var status;
one = Math.floor(Math.random() * 5) + 1 // 최소 10 최대 35 , 혼테일
function start() {
    status = -1;
    action(1, 1, 0);
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
		cm.gainItem(2049360, one);
		cm.sendOkS("#i2049360##z2049360# 획득!", 2);
		cm.gainItem(2431486, -1);
		cm.dispose();
	}
}
