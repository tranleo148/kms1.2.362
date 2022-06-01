var enter = "\r\n";
var seld = -1;

var items = [1182069, 3014005, 2630281]

var box = 2436225;


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