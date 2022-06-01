importPackage(Packages.server);

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
        status --;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
	cm.sendOkS("흰색 꽃 무더기에서 #i4310225##z4310225# 아이템을 발견했다.", 2);
    } else if (status == 1) {
	var rand = Randomizer.rand(10,20);
	cm.warp(1000000, 0);
	cm.gainItem(4310225, rand);
	cm.sendOkS("#i4310225##z4310225# 아이템 " + rand + "개 획득!", 2);
    }
}