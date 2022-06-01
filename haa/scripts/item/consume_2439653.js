var enter = "\r\n";
var seld = -1;

var items = [2048717, 2048717, 2048717, 2048717, 2048717, 2048717,  2048717, 2048717, 2048717, 2048717]

var box = 2439653;


function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, sel) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
    	}
	if (status == 0) {
		for (i = 0; i < items.length; i++) cm.gainItem(items[i], 1);
		cm.gainItem(box, -1);
		cm.dispose();
	}
}