
var status;
var select = -1;
var book  = new Array(2591187,2591188,2591189,2591190,2591191,2591192,2591193);

function start() {    status = -1;
    action(1, 1, 0);
}

function action(mode, type, selection) {
    if (mode <= 0) {
        cm.dispose();
    	return;
    } else {
        if (mode == 1)
            status++;
        if (status == 0) {
	    cm.dispose();
	    if (cm.canHold(2630281, 100)) {
		cm.gainItem(2630281, 100);
		cm.gainItem(2631878, -1);
	    } else {
		cm.sendOk("소비창에 공간이 부족합니다.");
	    }
	}
    }
}
