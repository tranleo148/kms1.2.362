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
        if (status == 5) {
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
        leftslot = qm.getPlayer().getInventory(MapleInventoryType.CASH).getNumFreeSlot();
        if (leftslot < 1) {
            qm.sendOkS("#r#h #! 캐시창 한칸 이상을 비워두게!", 0x04, 9401232);
            qm.dispose();
            return;
        }
        qm.sendNextS("이번엔 #b마스터피스#k 컨텐츠에 대해 설명해주겠네.", 0x04, 9401232);
    } else if (status == 1) {
        qm.sendNextPrevS("우선, #b마스터피스#k는 두가지의 아이템이 존재한다네.\r\n#b#i5069000# #z5069000#와 #i5069001# #z5069001##k\r\n두 아이템의 차이점은 마스터피스의 결과물이 #r거래불가#k와 #b거래가능#k으로 나뉘어진다네.", 0x04, 9401232);
    } else if (status == 2) {
        qm.sendNextPrevS("#b마스터피스#k를 하기 위해 필요한 아이템이 있지.\r\n\r\n"+스라벨+" 라벨이 붙은 치장 아이템이 필요하네. 이 아이템은 #b스페셜라벨#k이라고 불리고 있으며, #r캐시샵에서 무료#k로 구매가 가능하다네!", 0x04, 9401232);
    } else if (status == 3) {
        qm.sendNextPrevS("이제 좀 느낌이 오나? #r#e마스터 피스#n#k란 두가지의 "+스라벨+" #b스페셜라벨#k 아이템을 합성하는 컨텐츠라네.\r\n\r\n#b마스터피스#k 아이템은 베이스 아이템의 종류와 동일하게 나온다네. 예를들면 #b모자 아이템#k이 베이스면 결과 #r아이템이 모자#k 아이템이 나오며, #b무기 아이템#k을 베이스로 하면 결과 #r아이템 무기#k로 나오지. 크크.", 0x04, 9401232);
    } else if (status == 4) {
        qm.sendNextPrevS("하지만 그냥 합성만 한다면 재미가 없겠지 마스터피스는 #r등급#k이 존재한다네. 크크.이 등급에 대하여 간단하게 설명 해 주겠네 먼저 아래를 보게나.\r\n\r\n#fs11##r※ 베이스 아이템은 "+스라벨+", "+레드라벨+"등급만 가능하며 합성 재료는 "+스라벨+"만 가능합니다.#k\r\n\r\n#e#b"+스라벨+" + "+스라벨+" = "+레드라벨+" 또는 "+마라벨+"\r\n#e"+레드라벨+" + "+스라벨+" = "+블랙라벨+" 또는 "+마라벨+"", 0x04, 9401232);
    } else if (status == 5) {
        qm.sendYesNoS("이번에는 특별히 내가 자네에게 #r#e마스터피스#n#k를 주겠네 크크. 백문이불여일견! #b마스터피스#k를 한번 사용해보게!", 4, 9401232);
    } else if (status == 6) {
        qm.sendOkS("슈피겔만씨가 #z5069001#를 하나 주셨다. 이걸로 마스터 피스를 진행해보자!\r\n\r\n#fs11##r※ 베이스 아이템은 "+스라벨+", "+레드라벨+"등급만 가능하며 합성 재료는 "+스라벨+"만 가능합니다.#k\r\n\r\n#e#b"+스라벨+" + "+스라벨+" = "+레드라벨+" 또는 "+마라벨+"\r\n#e"+레드라벨+" + "+스라벨+" = "+블랙라벨+" 또는 "+마라벨+"", 2);
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
        qm.sendNextS("어떤가. #b마스터라벨 아이템#k은 획득했나? 크크.\r\n마스터피스를 진행한 아이템은 #b추가 옵션과 기간#k도 붙는다는거 잊지말게!", 0x04, 9401232);
    } else if (status == 1) {
        말 = "마스터 라벨 아이템도 다양한 아이템이 있다네!\r\n\r\n"
        말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n\r\n";
        말 += "#i5069001:# #b#t5069001:# 3개\r\n";
        말 += "#i4310012:# #b#t4310012:# 150개\r\n";
        qm.sendNextPrevS(말, 0x04, 9401232);
    } else if (status == 2) {
        leftslot = qm.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot();
        leftslot1 = qm.getPlayer().getInventory(MapleInventoryType.CASH).getNumFreeSlot();
        if (leftslot < 1 && leftslot1) {
            qm.sendOkS("#r#h #! 기타창과 캐시창 한칸 이상을 비워두게!", 0x04, 9401232);
            qm.dispose();
            return;
        }
        var str = qm.getClient().getKeyValue("GrowQuest");
        var ab = str.split("");
        var fi = "";
        ab[8] = "1";
        for (var a = 0; a < ab.length; a++) {
            fi += ab[a];
        }
        qm.getClient().setKeyValue("GrowQuest", fi);
        qm.gainItem(5069001, 3);
        qm.gainItem(4310012, 150);
        qm.forceCompleteQuest();
        qm.dispose();
    }
}
