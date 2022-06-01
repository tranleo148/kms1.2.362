var status;
importPackage(Packages.server);

item = [2049700, 2048716, 2470001, 4310266];

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
	cm.gainItem(2434635, -1);
	it = item[Math.floor(Math.random() * item.length)];
	cm.gainItem(it, 1);
	cm.dispose();
	}
}
