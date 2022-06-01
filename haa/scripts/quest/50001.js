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
        qm.sendNextS("자네를 위해 준비한 또 하나의 #b특별한 컨텐츠#k가 있지. 들어보겠나? 크크크.", 0x04, 9401232);
    } else if (status == 1) {
        qm.sendNextPrevS("[#b컨텐츠 시스템#k -> #r블랙 스킬#k]을 들어가면 자네의 성장에 필요한 다양한 #r스킬#k들이 있다네.", 0x04, 9401232);
    } else if (status == 2) {
        qm.sendNextPrevS("#r블랙 스킬#k들은 각각 다른 효과를 가지고 있고, 스킬을 업그레이드 하면 할 수록 #b효과#k가 #e#b증폭#n#k 된다네.", 0x04, 9401232);
    } else if (status == 3) {
        qm.sendYesNoS("서론이 길었군 크크. 자네가 직접 스킬을 구매 해보게나.", 0x04, 9401232);
    } else if (status == 4) {
        qm.forceStartQuest();
        qm.sendOkS("다시 한번 말하지만 [#b컨텐츠 시스템#k -> #r블랙 스킬#k]을 통해 강화를 진행하게나.", 4, 9401232);
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
            status--;
        }
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        qm.sendNextS("#b블랙 스킬#k은 마음에 드나? #r#i4310012:# #z4310012:##k을 이용하여 스킬들을 업그레이드 하면 분명 엄청난 성장을 할 수 있을거라네 크크.", 0x04, 9401232);
    } else if (status == 1) {
        말 = "블랙 코인을 모아서 다른 스킬도 한번 구매해보게나. 크크.\r\n\r\n"
        말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n\r\n";
        말 += "#i4310012:# #b#t4310012:# 30개\r\n";
        말 += "#i5200000:# #b15,000,000 메소\r\n";
        qm.sendNextS(말, 0x04, 9401232);
    } else if (status == 2) {
        leftslot = qm.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot();
        if (leftslot < 1) {
            qm.sendOkS("#r#h #! 기타창 한칸 이상을 비워두게!", 0x04, 9401232);
            qm.dispose();
            return;
        }
        var str = qm.getClient().getKeyValue("GrowQuest");
        var ab = str.split("");
        var fi = "";
        ab[1] = "1";
        for (var a = 0; a < ab.length; a++) {
            fi += ab[a];
        }
        qm.getClient().setKeyValue("GrowQuest", fi);
        qm.gainItem(4310012, 30);
        qm.gainMeso(15000000);
        qm.forceCompleteQuest();
        qm.dispose();
    }
}
