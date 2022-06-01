/*
제작 : 판매요원 (267→295)
용도 : 판매요원 팩
*/

status = -1;
검정 = "#fc0xFF191919#"
파랑 = "#fc0xFF4641D9#"
남색 = "#fc0xFF4641D9#"

importPackage(Packages.tools.packet);
importPackage(Packages.client.inventory);

function start(mode, type, selection) {

    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        if (status == 4) {
            qm.sendOkS("#fs11#나는 항상 같은자리에 있으니 언제라도 다시 말을 걸어주게.");
            qm.dispose();
            return;
        } else {
            status--;
        }
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        leftslot = qm.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot();
        if (leftslot < 1) {
            qm.sendOkS("#fs11##r#h #! 소비창 한칸 이상을 비워두게!", 0x04, 9401232);
            qm.dispose();
            return;
        }
        qm.sendYesNoS("#fs11#운영자인 #e블랙#n으로 부터 전언이 왔다... 확인해볼까?", 2);
    } else if (status == 1) {
        qm.Entertuto(false, false, true);
    } else if (status == 2) {
        qm.sendScreenText("To. #h #", false);
    } else if (status == 3) {
        qm.sendScreenText("안녕하세요. 여행자님 " + 남색 + "더 블랙#k입니다.", false);
    } else if (status == 4) {
        qm.sendScreenText("200레벨을 달성하여 블랙에서 준비한 소정의 선물 상자를 드립니다.", false);
    } else if (status == 5) {
        qm.sendScreenText("블랙 페스티벌에서 즐거운 시간 보내시길 바랍니다.", true);
    } else if (status == 6) {
        var str = qm.getClient().getKeyValue("LevelUpGive");
        var ab = str.split("");
        var fi = "";
        ab[4] = "1";
        for (var a = 0; a < ab.length; a++) {
            fi += ab[a];
        }
        qm.getClient().setKeyValue("LevelUpGive", fi);
        qm.gainItem(2430015, 1);
        qm.forceStartQuest();
        qm.forceCompleteQuest();
        qm.Endtuto(false)
        qm.dispose();
    }
}
