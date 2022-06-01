/*
제작 : 판매요원 (267→295)
용도 : 판매요원 팩
*/

status = -1;
검정 = "#fc0xFF191919#"
파랑 = "#fc0xFF4641D9#"
남색 = "#fc0xFF4641D9#"
초록 = "#fc0xFF0BC904#"

importPackage(Packages.tools.packet);
importPackage(Packages.client.inventory);

function start(mode, type, selection) {

    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        if (status == 3) {
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
        qm.sendNextS("크크. #h #, #r#i4310012:# #z4310012:##k은 #b블랙 스킬#k 컨텐츠 뿐만 아니라 다양한 곳에도 사용되는 사실을 알고 있나?", 0x04, 9401232);
    } else if (status == 1) {
        qm.sendNextPrevS("물론 초반엔 빠르게 #b블랙 스킬#k을 업그레이드 하는것도 좋지만 #r#e치장 강화#n#k 컨텐츠도 있다는걸 알려주고 싶다네. 크크.", 0x04, 9401232);
    } else if (status == 2) {
        qm.sendNextPrevS("#b치장 강화#k 컨텐츠란 강화 1회당 #e#i4310012# #z4310012# 10개#n를 사용하여 강화 성공 시 #b올스탯 +1 공/마 +1 HP +10#k이 부여되지 하지만 실패 시 #r올스탯 -1 공/마 -1 HP -10#k이 차감된다네.", 0x04, 9401232);
    } else if (status == 3) {
        qm.sendYesNoS("어때 #h #, 한번 도전해보지 않겠나?\r\n\r\n#r#fs11#※ 치장 강화 컨텐츠의 성공 확률은 #e60%#n지만 성장 퀘스트를 수락 후 1회 한정 #e100%#n 확률로 성공합니다.", 4, 9401232);
    } else if (status == 4) {
        qm.sendOkS("치장 강화는 #r#e[컨텐츠 시스템 -> 강화 시스템 - > 치장 강화]#n#k에 있다네 참고 하게나!", 4, 9401232);
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
        qm.sendNextS("#b치장 아이템#k에도 #r강화#k를 할 수 있다니 놀랍지 않나? 원하는 장비 아이템에 강화를 하면 예쁜 아이템은 장착을 못하지 않냐고?\r\n크크. 나 #r슈피겔만#k 그런 세심함도 없어보이나? 예쁜 아이템의 외형도 적용 할 수 있는 #e캐시 모루#n 컨텐츠도 있으니 원하는 아이템에 마음껏 강화 해보게나.", 0x04, 9401232);
    } else if (status == 1) {
        말 = "자, 이번에도 고생 많았다네 이 아이템들이 자네에게 도움이 됐으면 좋겠구만.\r\n\r\n"
        말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n\r\n";
        말 += "#i4310012:# #b#t4310012:# 100개\r\n";
        말 += "#i4310310:# #b#t4310310:# 300개\r\n";
        qm.sendNextS(말, 0x04, 9401232);
    } else if (status == 2) {
        leftslot = qm.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot();
        if (leftslot < 1 && leftslot1 < 1) {
            qm.sendOkS("#r#h #! 기타창 한칸 이상을 비워두게!", 0x04, 9401232);
            qm.dispose();
            return;
        }
        var str = qm.getClient().getKeyValue("GrowQuest");
        var ab = str.split("");
        var fi = "";
        ab[2] = "1";
        for (var a = 0; a < ab.length; a++) {
            fi += ab[a];
        }
        qm.getClient().setKeyValue("GrowQuest", fi);
        qm.gainItem(4310012, 100);
        qm.getPlayer().AddBloomingCoin(300, null);
        qm.forceCompleteQuest();
        qm.dispose();
    }
}
