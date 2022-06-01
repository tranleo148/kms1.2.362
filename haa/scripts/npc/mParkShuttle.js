var status = 0;

 

function start() {
    status = -1;
    action(1, 0, 0);
}

 

function action(mode, type, selection) {
    if (mode == 1) 
        status++;
    else 
        status--;
    if (status == 0) {
        if (cm.getMapId() != 951000000) {
            cm.sendYesNo("사랑합니다 고객님. 항상 새로운 즐거움이 가득한 슈피겔만의 몬스터 파크로 이동하시겠습니까?");
        } else {
            cm.sendYesNo("안녕하세요. 고객만족을 위해 항상 최선을 다하고 있는 몬스터파크 셔틀입니다. 마을로 돌아가시겠습니까?");
        }
    } else if (status == 1) {
        if (cm.getMapId() != 951000000) {
            cm.warp(951000000,0);
        } else {
            cm.warp(100000000,0);
        }
        cm.dispose();
    }
}
