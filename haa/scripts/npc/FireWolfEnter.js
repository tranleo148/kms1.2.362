var enter = "\r\n";
var seld = -1;

var map = 993000500;

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
        cm.sendNextS("나는 최고의 현상금 사냥꾼 #r#e폴로#k#n." + enter + "동생 #b#e프리토#k#n와 함께 마물들을 퇴치하고 있다.", 1);
    } else if (status == 1) {
        cm.sendSimpleS("우리 형제가 오랜 시간 추격하던 최강의 몬스터 #r#e불꽃늑대#k#n의 소굴을 마침내 찾아냈다. 녀석은 여행자들을 닥치는대로 약탈하는 아주 악랄한 놈이지... 어때, 나와 함께 녀석을 퇴치하러 가겠나?" + enter + "#b#L1#함께 한다." + enter + "#L2#함께하지 않는다.", 1);
    } else if (status == 2) {
        switch (sel) {
            case 1:
                cm.getPlayer().setKeyValue(20190330, "FireWolf_Return", "" + cm.getPlayer().getMapId());
                cm.warp(map);
                break;
            case 2:
                cm.sendOkS("#r#e불꽃늑대#k#n를 만난다는 건 쉬운 일이 아닌데...", 1);
                break;
        }
        cm.getPlayer().setFWolfPortal(false);
        cm.dispose();
    }
}