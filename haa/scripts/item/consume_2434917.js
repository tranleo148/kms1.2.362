var enter = "\r\n";
var seld = -1;

var maps = new Array(993000000, 993000100);

// 993000000 : 현상금 사냥
// 993000100 : 성벽 지키기
// 993000650 : 스톰윙 출몰지역

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
        cm.sendNextS("나는 메이플 월드 최고의 현상금 사냥꾼 #r#e폴로#k#n." + enter + "동생 #b#e프리토#k#n와 함께 마물들을 퇴치하고 있다.", 1, 9001059, 0);
    } else if (status == 1) {
        cm.sendSimpleS("이제 막 사냥을 떠나려던 길이였는데, 자네도 나와 함께하지 않겠나?" + enter + "#b#L1#함께 한다." + enter + "#L2#함께하지 않는다.", 1, 9001059, 0);
    } else if (status == 2) {
        cm.dispose();
        switch (sel) {
            case 1:
		map = maps[Math.random() * maps.length];
                cm.getPlayer().addKV("poloFritto", "" + cm.getPlayer().getMapId());
		if (cm.getClient().getChannelServer().getMapFactory().getMap(993000000).characterSize() == 0) {
		    cm.gainItem(2434917, -1);
		    cm.warp(993000000);
		} else if (cm.getClient().getChannelServer().getMapFactory().getMap(993000100).characterSize() == 0) {
		    cm.gainItem(2434917, -1);
		    cm.warp(993000100);
		} else {
		    cm.sendOk("현재 누군가가 진행중이군.", 9001059);
		}
                break;
            case 2:
                cm.sendNextS("그렇다면 어쩔수 없군.", 1, 9001059, 0);
                break;
        }
    }
}