importPackage(java.lang);
importPackage(Packages.server);

var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }
    if (status == 0) {
        cm.sendYesNo("메소 1억~100억원 랜덤 럭키백");

    } else if (status == 1) {
       var rand = Randomizer.rand(100000000, 10000000000);
       cm.gainMeso(rand);
       cm.gainItem(2433979, -1);
       cm.sendOk("메소 럭키백에서 #b" + rand + " 메소#k를 획득하였습니다.");
       cm.dispose();
    }
}