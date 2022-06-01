var status;
importPackage(Packages.server);
importPackage(Packages.client.inventory);
importPackage(Packages.server);
importPackage(Packages.server.items);

var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
	if (status == 0) {
	cm.sendYesNo("어메이징 큐브가 #b1개 ~ 3개#k사이로 랜덤으로 지급됩니다.");
	} else if (status == 1) {
	cm.gainItem(2430026, -1);
	cm.gainItem(5062005, Randomizer.rand(1,3));
	cm.dispose();
	}
}
}