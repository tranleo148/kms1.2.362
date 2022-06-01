var enter = "\r\n";
var seld = -1;

var maps = [];

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
                if (cm.getClient().getChannelServer().getMapFactory().getMap(993000300).characterSize() == 0) {
                    maps.push(993000300);
                }
                if (cm.getClient().getChannelServer().getMapFactory().getMap(993000200).characterSize() == 0) {
                    maps.push(993000200);
                }
                if (cm.getClient().getChannelServer().getMapFactory().getMap(993000400).characterSize() == 0) {
                    maps.push(993000400);
                }
                if (maps.length == 0) {
                    cm.sendOk("지금 진행중인 사람이 많네.. 잠시 후에 시도해줄래?");
                    return;
                }
                cm.warp(maps[Packages.server.Randomizer.nextInt(maps.length)], 0);
                break;
            case 2:
                cm.sendOk("너도 내가 어리다고 무시하는거야?");
                break;
        }
    }
}