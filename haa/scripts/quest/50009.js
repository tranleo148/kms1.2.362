/*
제작 : 판매요원 (267→295)
용도 : 판매요원 팩
*/

status = -1;
검정 = "#fc0xFF191919#"
파랑 = "#fc0xFF4641D9#"
남색 = "#fc0xFF4641D9#"
스라벨 = "#fUI/CashShop.img/CashItem_label/0#";
레드라벨 = "#fUI/CashShop.img/CashItem_label/1#";
블랙라벨 = "#fUI/CashShop.img/CashItem_label/2#";
마라벨 = "#fUI/CashShop.img/CashItem_label/3#";

importPackage(Packages.tools.packet);
importPackage(Packages.client.inventory);

function start(mode, type, selection) {

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
        leftslot = qm.getPlayer().getInventory(MapleInventoryType.CODY).getNumFreeSlot();
        if (leftslot < 2) {
            qm.sendOkS("#r#h #! 치창 두칸 이상을 비워두게!", 0x04, 9401232);
            qm.dispose();
            return;
        }
        qm.sendNextS("#d마스터라벨#k 아이템은 뽑은건가? 크크.\r\n이번엔 #b치장 아이템 모루#k 컨텐츠에 대해서 알려주겠네!", 0x04, 9401232);
    } else if (status == 1) {
        qm.sendNextPrevS("#b치장 아이템 모루#k란 일반 장비 모루와 같은 개념이라네.", 0x04, 9401232);
    } else if (status == 2) {
        qm.sendNextPrevS("단! 모루를 진행할 베이스 아이템은 #r마스터 라벨#k 아이템만 가능하다네!\r\n캐시샵에서 구매해서 모루를 해도 되지만, #b마스터피스를 통해 획득한 아이템#k에 모루를 하는게 일석이조겠지? 크크크.\r\n", 0x04, 9401232);
    } else if (status == 3) {
        qm.sendNextPrevS("그리고 모루를 하려면 #e#r30,000,000만 메소#n#k가 필요하다네!\r\n자네에게 이번만 #b모루비용과 베이스 아이템, 모루 아이템#k을 하나씩 주겠네.", 0x04, 9401232);
    } else if (status == 4) {
        qm.sendYesNoS("자! 그럼 #b치장 모루#k를 이용해보게!\r\n\r\n#r#fs11#※ [컨텐츠 시스템 -> 캐시모루]란을 이용해주세요.", 4, 9401232);
    } else if (status == 5) {
        qm.sendOkS("슈피겔만씨가 모루 아이템을 주셨다..\r\n#b컨텐츠시스템#k에서 #r치장 모루#k를 이용해보자!\r\n\r\n#r#fs11#※ [컨텐츠 시스템 -> 캐시모루]란을 이용해주세요.", 2);
        qm.gainItem(1005619, 1);
        qm.gainItem(1005633, 1);
        qm.gainMeso(30000000);
        qm.gainItem(5069001, 1);
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
        qm.sendNextS("치장 아이템에도 모루라니. 얼마나 혁신적인 시스템이 아닌가! 크크크.", 0x04, 9401232);
    } else if (status == 1) {
        qm.sendNextS("자! 내가 준비한 성장 퀘스트는 여기까지라네! 앞으로 자네가 만들어가는 블랙 페스티벌을 지켜보고 있겠다네!", 0x04, 9401232);
    } else if (status == 2) {
        말 = "고생했네! 자네 노력에 대한 보상이라네.\r\n\r\n"
        말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n\r\n";
        말 += "#i2049750:# #b#t2049750:# 1개\r\n";
        말 += "#i4310012:# #b#t4310012:# 300개\r\n";
        말 += "#i5200002:# #b100,000,000 메소#k\r\n";
        qm.sendNextPrevS(말, 0x04, 9401232);
    } else if (status == 3) {
        leftslot = qm.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot();
        leftslot1 = qm.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot();
        if (leftslot < 1 && leftslot1) {
            qm.sendOkS("#r#h #! 소비창과 기타창 한칸 이상을 비워두게!", 0x04, 9401232);
            qm.dispose();
            return;
        }
        var str = qm.getClient().getKeyValue("GrowQuest");
        var ab = str.split("");
        var fi = "";
        ab[9] = "1";
        for (var a = 0; a < ab.length; a++) {
            fi += ab[a];
        }
        qm.getClient().setKeyValue("GrowQuest", fi);
        qm.gainItem(2049750, 1);
        qm.gainItem(4310012, 300);
        qm.gainMeso(100000000);
        qm.forceCompleteQuest();
        qm.dispose();
    }
}
