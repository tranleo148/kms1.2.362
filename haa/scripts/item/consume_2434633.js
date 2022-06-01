var status;
importPackage(Packages.server);

item = [4001832, 2711000, 2711003, 2711004];

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
	cm.gainItem(2434633, -1);
	it = item[Math.floor(Math.random() * item.length)];
	cm.gainItem(it, it == 4001832 ? 100 : 10);
	cm.dispose();
	}
}
