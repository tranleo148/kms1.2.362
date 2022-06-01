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
            qm.sendOkS("이런.. 마음이 바뀌면 다시 말을 걸어주게나.", 4, 9401232);
            qm.dispose();
            return;
        } else {
            status --;
        }
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        qm.sendNextS("반갑네 #h #! #b#eThe Black#n#k 페스티벌을 즐기러 자네도 왔구만!", 0x04, 9401232);
    } else if (status == 1) {
        qm.sendNextPrevS("처음 접하더라도 너무 걱정하지 말게! 내가 자네를 위해 준비해둔 것들이 많이 있으니. 차근차근 하다보면 어느순간 자네도 #b#e블.잘.알#n#k이 될걸세 크크.", 0x04, 9401232);
    } else if (status == 2) {
        qm.sendNextPrevS("먼저 #b더 블랙 페스티벌#k 광장에서 나를 찾아오면 자네에게 #r특별한 칭호#k를 주겠네!", 0x04, 9401232);
    } else if (status == 3) {
        qm.sendNextPrevS("#e\"@마을\"#n 명령어를 사용하면 #b더 블랙 페스티벌 광장#k으로 올 수 있다네!", 0x04, 9401232);
    } else if (status == 4) {
        qm.sendYesNoS("하지만 이번엔 내가 특별히 자네를 이쪽으로 안내 해주겠네 #b블랙 페스티벌 광장#k에서 이 슈피겔만 님을 찾게나 크크.\r\n\r\n#r※ 퀘스트 진행을 위해 맵이 이동 됩니다.", 4, 9401232);
    } else if (status == 5) {
        qm.warp(100000051, 10);
        qm.getPlayer().setKeyValue(50000, "1", "1");
        qm.forceStartQuest();
        qm.dispose();
    }
}

status = -1;
function end(mode, type, selection) {

    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        if (status == 4) {
            qm.sendOkS("나는 항상 같은자리에 있으니 언제라도 다시 말을 걸어주게.");
            qm.dispose();
            return;
        } else {
            status --;
        }
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        qm.sendNextS("오~ 잘 와주었네, #b#h ##k! 앞으로 나를 찾을일이 있으면 항상 이곳으로 나를 찾아오게나.", 0x04, 9401232);
    } else if (status == 1) {
        말 = "자, 이것이 바로 내가 준비한 약소한 선물이라네. 크크. 사양말고 받게나!\r\n\r\n"
        말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n\r\n";
        말 += "#i2633202:# #b#t2633202:# 1개\r\n";
        말 += "#i4310012:# #b#t4310012:# 300개";
        qm.sendOk(말, 9401232);
    } else if (status == 2) {
        leftslot = qm.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot();
        if (leftslot < 1) {
            qm.sendOkS("#r#h #! 소비창 한칸 이상을 비워두게!", 0x04, 9401232);
            qm.dispose();
            return;
        }
        var str = qm.getClient().getKeyValue("GrowQuest");
        var ab = str.split("");
        var fi = "";
        ab[0] = "1";
        for (var a = 0; a < ab.length; a++) {
            fi += ab[a];
        }
        qm.getClient().setKeyValue("GrowQuest", fi);
        qm.gainItem(2633202, 1);
        qm.gainItem(4310012, 300);
        qm.forceCompleteQuest();
        qm.dispose();
    }
}
