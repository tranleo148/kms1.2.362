var enter = "\r\n";
var seld = -1;

var maps = new Array(993000200, 993000300, 993000400);

// 993000200 : 독수리 사냥
// 993000300 : 알 훔치기
// 993000400 : 구애의 춤

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
        cm.sendNextS("안녕? 나는 현상금 사냥꾼 #b#e프리토#k#n라고해." + enter + "형 #r#e폴로#k#n와 함께 엄청난 명성을 떨치고 있지! 핫핫!", 1);
    } else if (status == 1) {
        cm.sendSimpleS("남들은 나를 어수룩하다고 생각하지만 사실 나는 엄청난 실력의 소유자야. 어때, 나와 함께 모험을 하지 않겠어?" + enter + "#b#L1#함께 한다." + enter + "#L2#함께하지 않는다.", 1);
    } else if (status == 2) {
        cm.dispose();
        switch (sel) {
            case 1:
                cm.getPlayer().addKV("poloFritto", cm.getPlayer().getMapId());
                cm.warp(maps[Math.floor(Math.random() * maps.length) - 1]);
                break;
            case 2:
                cm.sendNextS("너도 내가 어리다고 무시하는거야?", 1);
                break;
        }
    }
}