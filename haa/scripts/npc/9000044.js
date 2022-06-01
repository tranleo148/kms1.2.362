importPackage(Packages.constants);

var status = -1;

var 보스맵 = ServerConstants.worldbossmap;
var 돌아갈맵 = ServerConstants.worldbossfirstmap;
var 처치템 = ServerConstants.worldreward;

var 고정보상 = [[5062005, 3], [5062503, 3], [4001716, 3], [4036491, 1], [4310266, 50], [2048747, 30], [4001129, 50]];
var 막타보상 = [[5062005, 5], [5062503, 5], [4001716, 4], [4036491, 1], [4310266, 50], [2048747, 30], [4001129, 50]];
var 판매아이템 = [[5062005, 5, 50], [5062503, 5, 50], [4001716, 3, 50], [5068305, 1, 200], [4310266, 50, 50]];
var 구매시필요아이템 = 4001129;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else if (mode == 0 && type == 0) {
        status--;
    } else {
        cm.dispose();
        return;
    }
    if (status == 0) {
        if (보스맵 == cm.getPlayer().getMapId()) {
            if (cm.getMap().getNumMonsters() <= 0) {//&& cm.haveItem(처치템, 1)
                cm.sendYesNo("보상을 받고나가시겠습니까");
            } else {
                cm.sendOk("맵안에 아직 남은 몬스터가 있거나 보스 처치후 보상아이템을 소지하고있지않네요.");
                cm.dispose();
            }
        } else {
            if (cm.getPlayer().gethottimeboss() && cm.getPlayerCount(211070100) < 31 && cm.canHold(처치템 ,1)) {
                if (cm.getMap().getChannel() == 1 || cm.getMap().getChannel() == 2 || cm.getMap().getChannel() == 3 || cm.getMap().getChannel() == 4) {
                    cm.sendYesNo("월드 보스가 시작 되었습니다.");
                } else {
                    cm.sendOk("1,20세,2,3 채널에서만 입장 가능합니다. 다른채널을 이용해주세요 최대인원은 30명입니다. (기타창은 꼭 한칸 비우세요)");
                    cm.dispose();
                }
            } else {
                var text = "아직 #b#e월드보스 핫타임 이벤트#k#n가 시작되지 않았습니다. 월드보스가 시작되기전에 다시 찾아와주세요.\r\n현재는 #r#e월드보스 의 전리품#k#n#i" + 구매시필요아이템 + "#으로 상점을 이용할수 있는 시간입니다.!어떤 아이템을 구매하시고 싶으세요?\r\n\r\n";
                for (var i = 0; i < 판매아이템.length; i++) {
                    text += "#L" + i + "# #i" + 판매아이템[i][0] + ":##z" + 판매아이템[i][0] + ":# " + 판매아이템[i][1] + " 개 = #i" + 구매시필요아이템 + "#" + 판매아이템[i][2] + "개 필요  #l \r\n";
                }
                text += "";
                cm.sendSimple(text);
            }
        }

    } else if (status == 1) {
        if (보스맵 == cm.getPlayer().getMapId() || cm.getPlayer().gethottimeboss()) {
            if (보스맵 == cm.getPlayer().getMapId()) {
                var 보상여부 = false;
                for (var i = 0; i < 고정보상.length; i++) {
                    if (!cm.canHold(고정보상[i][0], 고정보상[i][1])) {
                        보상여부 = true;
                        break;
                    }
                }
                if (cm.getPlayer().gethottimebosslastattack()) {
                    for (var i = 0; i < 막타보상.length; i++) {
                        if (!cm.canHold(막타보상[i][0], 막타보상[i][1])) {
                            보상여부 = true;
                            break;
                        }
                    }
                }

                for (var i = 0; i < 고정보상.length; i++) {
                    cm.gainItem(고정보상[i][0], 고정보상[i][1]);
                }

                if (cm.getPlayer().gethottimebosslastattack()) {
                    for (var i = 0; i < 막타보상.length; i++) {
                        cm.gainItem(막타보상[i][0], 막타보상[i][1]);
                    }
                }
		cm.gainItem(처치템, -1);
		cm.removeAll(처치템);
                cm.getPlayer().sethottimeboss(false);
                cm.warp(돌아갈맵, 0);
                cm.dispose();
            } else {
                if (cm.getPlayer().gethottimeboss()) {
                    if (cm.getMap(보스맵).getCharactersSize() == 0) {
                        cm.getMap(보스맵).resetFully();
                    }
                    cm.warp(보스맵);
                    cm.dispose();
                }
            }
        } else {
            if (cm.haveItem(구매시필요아이템, 판매아이템[selection][2])) {
                if (cm.canHold(판매아이템[selection][2])) {
                    cm.sendOk("장비 공간이 부족합니다.");
                    cm.dispose();
                    return;
                }
                cm.gainItem(판매아이템[selection][0], 판매아이템[selection][1]);
                cm.gainItem(구매시필요아이템, -판매아이템[selection][2]);
                cm.sendOk("구매 완료");
                cm.dispose();
            } else {
                cm.sendOk("아이템이 부족합니다.");
                cm.dispose();
            }
        }
    }
}