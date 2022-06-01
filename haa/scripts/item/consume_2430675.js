
var status = -1;
importPackage(Packages.tools.RandomStream);
importPackage(Packages.client.items);

function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    
    if (status == 0) {
        var leftslot = cm.getPlayer().getNX();
        if (leftslot >= 895000) {
            cm.sendOk("캐시 최대 한도는 90만 캐시 입니다. 현재 캐시 범위를 초과하여 이 상자를 열 수 없습니다.");
            cm.dispose();
            return;
        }
        cm.gainItem(2430675, -1);
        var cash = 500000;
        cm.getPlayer().modifyCSPoints(1, cash, true);
        cm.dispose();
    }
}

