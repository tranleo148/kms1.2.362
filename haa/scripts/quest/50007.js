/*
제작 : 판매요원 (267→295)
용도 : 판매요원 팩
*/

status = -1;
검정 = "#fc0xFF191919#"
파랑 = "#fc0xFF4641D9#"
남색 = "#fc0xFF4641D9#"
주황 = "#fc0xFFEDA900#"

importPackage(Packages.tools.packet);
importPackage(Packages.client.inventory);

function start(mode, type, selection) {

    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        if (status == 1) {
            qm.sendOkS("이런.. 언제라도 마음이 바뀌면 다시 찾아오게나.", 4, 9401232);
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
        qm.sendNextS("자 이제 어느정도 강해진 것 같군.\r\n여태껏 #b쌓아올린 노력의 결실#k을 여기서 맺어보게!", 0x04, 9401232);
    } else if (status == 1) {
        qm.sendYesNoS("#e'T'#n 또는 이동시스템에서 #e'보스'#n를 클릭해서 루타비스 보스\r\n#b피에르,반반,블러디 퀸,벨룸#k을 격파하고 돌아오게!", 4, 9401232);
    } else if (status == 2) {
        qm.sendOkS("#e'T'#n 또는 이동시스템에서 #e'보스'#n를 클릭해서 루타비스 보스\r\n#b피에르,반반,블러디 퀸,벨룸#k들을 격파하자.", 2);
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
        if (status == 1) {
            qm.sendOkS("나는 항상 같은자리에 있으니 언제라도 다시 말을 걸어주게.");
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
        qm.sendNextS("대단하군, #b루타비스 보스#k들을 벌써 격파 하고 오다니 보스를 격파하면 #r성장에 도움을 주는 아이템과 전용아이템#k을 드롭한다네!", 0x04, 9401232);
    } else if (status == 1) {
        말 = "자! 자네의 노력에 대한 보상이라네. 축하하네.\r\n\r\n"
        말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n\r\n";
        말 += "#i2434584:# #b#t2434584:# 5개\r\n";
        말 += "#i2434585:# #b#t2434585:# 5개\r\n";
        말 += "#i2434586:# #b#t2434586:# 5개\r\n";
        말 += "#i2434587:# #b#t2434587:# 15개\r\n";
        qm.sendNextS(말, 0x04, 9401232);
    } else if (status == 2) {
        leftslot = qm.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot();
        if (leftslot < 4) {
            qm.sendOkS("#r#h #! 소비창 네칸 이상을 비워두게!", 0x04, 9401232);
            qm.dispose();
            return;
        }
        var str = qm.getClient().getKeyValue("GrowQuest");
        var ab = str.split("");
        var fi = "";
        ab[7] = "1";
        for (var a = 0; a < ab.length; a++) {
            fi += ab[a];
        }
        qm.getClient().setKeyValue("GrowQuest", fi);
        qm.gainItem(2434584, 5);
        qm.gainItem(2434585, 5);
        qm.gainItem(2434586, 5);
        qm.gainItem(2434587, 15);
        qm.forceCompleteQuest();
        qm.dispose();
    }
}
