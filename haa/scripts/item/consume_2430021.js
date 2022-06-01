
function action (mode, type, selection) {
    if (mode != 1) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        var count = 0;
        var say = "#fs11##b강화할 장비#k를 선택해 주세요\r\n\r\n#r스타포스와는 별개의 강화 시스템#k으로 기존에 있는\r\n#r스타포스 또는 주문서 작 등의 영향#k을 받지 않습니다.#k\r\n\r\n";
        for (i = 0; i < cm.getInventory(1).getSlotLimit(); i++) {
            if (cm.getInventory(1).getItem(i) != null) {
                if (!Packages.server.MapleItemInformationProvider.getInstance().isCash(cm.getInventory(1).getItem(i).getItemId())) {
                    say += "#L" + i + "##e#b#i" + cm.getInventory(1).getItem(i).getItemId() + "# #z" + cm.getInventory(1).getItem(i).getItemId() + "# (" + i + "번째 슬롯)#l\r\n";
                    count++;
                }
            }
        }
        if (count <= 0) {
            cm.sendOk("강화할 장비를 소지하고 있는지 확인해 주세요.");
            cm.dispose();
            return;
        }
        cm.sendSimple(say);
    } else if (status == 1) {
        if (re == 0) {
            slot = selection;
            item = cm.getInventory(1).getItem(selection);
        }
        if (item.getOwner().equals("15강")) {
            cm.sendOk("이미 15강까지 강화가 완료된 아이템 입니다.");
            cm.dispose();
            return;
        }
        itemid = item.getItemId();
        //강화수, 강화성공확률, 강화메소, 올스탯, 공마 
        var notice = "";
        say = "";
        say += "#fs 11#";
        say += "#r강화 : " + getAddEnhance(item) + "강 -> " + (getAddEnhance(item) + 1) + "강#k\r\n";
        say += "강화 성공 시 #b올스탯 +" + items[getAddEnhance(item) + 1][3] + ", 공마 +" + items[getAddEnhance(item) + 1][4] + "#k 증가\r\n";
        say += "기본강화에 필요한 메소 :#b " + items[getAddEnhance(item) + 1][2] + " 메소#k\r\n";
        say += "#b성공확률 " + items[getAddEnhance(item) + 1][1] + "%#k, 실패 시 아이템 옵션 하락\r\n";
        say += "단, #b" + ConvertNumber(keep) + "메소#k를 소모하여 아이템 옵션 보호 가능\r\n\r\n\r\n";        
        say += "#r<아이템 정보>\r\n";
        say += "강화할 아이템 : #i" + itemid + "# #z" + itemid + "#\r\n";
        say += "STR : " + item.getStr() + "  |  DEX : " + item.getDex() + "  |  INT : " + item.getInt() + "  |  LUK " + item.getLuk() + "\r\n";
        say += "공격력 : " + item.getWatk() + "  |  마력 : " + item.getMatk() + "  | 스타포스 : " + item.getEnhance() + "성\r\n";
        say += "올 스탯 : " + item.getAllStat() + "%  |  총 데미지 : " + item.getTotalDamage() + "%  |  보스 공격력 : " + item.getBossDamage() + "%\r\n";
        say += "아이템 강화 횟수 : " + getAddEnhance(item) + "강#k\r\n\r\n\r\n";
        cm.sendSimple(notice + say +
        "#L1##b" + ConvertNumber(keep) + "메소를 사용하여 파괴, 미끄러짐을 방지 하겠습니다.#l\r\n" +
        "#L2#파괴, 미끄러짐 방지를 사용하지 않고 강화하겠습니다.#k#l");
    } else if (status == 2) {
        if (re == 0) {
            choice = selection;
        }
        if (item.getOwner().equals("20강")) {
            cm.sendOk("이미 20강까지 강화가 완료된 아이템 입니다.");
            cm.dispose();
            return;
        }
        //강화수, 강화성공확률, 강화메소, 올스탯, 공마 
        say = "";
        say += "#fs 11#";
        say += "강화 : #b" + getAddEnhance(item) + "강 -> " + (getAddEnhance(item) + 1) + "강#k\r\n";
        say += "강화 성공 시 #b올스탯 +" + items[getAddEnhance(item) + 1][3] + ", 공마 +" + items[getAddEnhance(item) + 1][4] + "#k 증가\r\n";
        say += "기본강화에 필요한 메소 : #b" + items[getAddEnhance(item) + 1][2] + "메소#k\r\n";
        say += "#b성공확률 " + items[getAddEnhance(item) + 1][1] + "%#k";
        if (selection == 1 || choice == 1) {
            say += "\r\n";
        } else if (selection == 2 || choice == 2) {
            say += ", 실패 시 아이템 옵션 하락\r\n\r\n";
        }   
        say += "#r<아이템 정보>\r\n";
        say += "강화할 아이템 : #i" + itemid + "# #z" + itemid + "#\r\n";
        say += "STR : " + item.getStr() + "  |  DEX : " + item.getDex() + "  |  INT : " + item.getInt() + "  |  LUK " + item.getLuk() + "\r\n";
        say += "공격력 : " + item.getWatk() + "  |  마력 : " + item.getMatk() + "  | 스타포스 : " + item.getEnhance() + "성\r\n";
        say += "올 스탯 : " + item.getAllStat() + "%  |  총 데미지 : " + item.getTotalDamage() + "%  |  보스 공격력 : " + item.getBossDamage() + "%\r\n";
        say += "아이템 강화 횟수 : " + getAddEnhance(item) + "강#k\r\n\r\n\r\n";
        if (selection == 1 || choice == 1) {
            cm.sendYesNo(say + "정말로 " + ConvertNumber(keep) + "메소를 사용하여 미끄러짐을 방지 하시겠습니까?\r\n" +
            "성공/실패 여부와 상관없이 미끄러짐 방지 메소는 소모됩니다.\r\n\r\n#b총 필요 메소 : " + ConvertNumber(items[getAddEnhance(item) + 1][2] + keep) + "메소#k#l");
        } else if (selection == 2 || choice == 2) {
            cm.sendYesNo(say + "정말로 " + ConvertNumber(keep) + "메소를 사용하지 않고 강화를 시도하시겠습니까?\r\n" +
            "강화 실패시 강화 등급이 하락합니다.");
        }
    } else if (status == 3) {
        if (choice == 1) {
            if (cm.getPlayer().getMeso() >= (items[getAddEnhance(item) + 1][2] + keep)) {
                cm.gainMeso(-(items[getAddEnhance(item) + 1][2] + keep));
            } else {
                cm.sendOk("메소가 부족하여 보호 강화가 불가능합니다.\r\n총 필요 강화 메소 : " + ConvertNumber(items[getAddEnhance(item) + 1][2] + keep) + "메소");
                cm.dispose();
                return;
            }
        } else if (choice == 2) {
            if (cm.getPlayer().getMeso() >= items[getAddEnhance(item) + 1][2]) {
                cm.gainMeso(-items[getAddEnhance(item) + 1][2]);
            } else {
                cm.sendOk("메소가 부족하여 강화가 불가능합니다.");
                cm.dispose();
                return;
            }
        } else {
            cm.dispose();
            return;
        }        
        if (cm.getInventory(1).getItem(slot) != null) {
            var rand = Math.ceil(Math.random() * 100);
            if (rand >= 0 && rand <= items[getAddEnhance(item) + 1][1]) {
                item.setStr(item.getStr() + items[getAddEnhance(item) + 1][3]);
                item.setDex(item.getDex() + items[getAddEnhance(item) + 1][3]);
                item.setInt(item.getInt() + items[getAddEnhance(item) + 1][3]);
                item.setLuk(item.getLuk() + items[getAddEnhance(item) + 1][3]);
                item.setWatk(item.getWatk() + items[getAddEnhance(item) + 1][4]);
                item.setMatk(item.getMatk() + items[getAddEnhance(item) + 1][4]);
                item.setOwner(""+(getAddEnhance(item)+1)+"강");
                cm.getPlayer().forceReAddItem(item, Packages.client.inventory.MapleInventoryType.EQUIP);
                say = "";     
                say += "#fs11##r<아이템 정보>\r\n";
                say += "강화할 아이템 : #i" + itemid + "# #z" + itemid + "#\r\n";
                say += "STR : " + item.getStr() + "  |  DEX : " + item.getDex() + "  |  INT : " + item.getInt() + "  |  LUK " + item.getLuk() + "\r\n";
                say += "공격력 : " + item.getWatk() + "  |  마력 : " + item.getMatk() + "  | 스타포스 : " + item.getEnhance() + "성\r\n";
                say += "올 스탯 : " + item.getAllStat() + "%  |  총 데미지 : " + item.getTotalDamage() + "%  |  보스 공격력 : " + item.getBossDamage() + "%\r\n";
                say += "아이템 강화 횟수 : " + getAddEnhance(item) + "강#k\r\n\r\n\r\n";
                cm.sendYesNo("#b강화에 성공#k하였습니다.\r\n계속 강화하시려면 '예'를 눌러 주세요.\r\n\r\n" + say);

                re = 1;
                status = 1;
            } else {
                if (choice == 1 || getAddEnhance(item) == 0) {
                    say = "";
                    say += "#fs11##r<아이템 정보>\r\n";
                    say += "강화할 아이템 : #i" + itemid + "# #z" + itemid + "#\r\n";
                    say += "STR : " + item.getStr() + "  |  DEX : " + item.getDex() + "  |  INT : " + item.getInt() + "  |  LUK " + item.getLuk() + "\r\n";
                    say += "공격력 : " + item.getWatk() + "  |  마력 : " + item.getMatk() + "  | 스타포스 : " + item.getEnhance() + "성\r\n";
                    say += "올 스탯 : " + item.getAllStat() + "%  |  총 데미지 : " + item.getTotalDamage() + "%  |  보스 공격력 : " + item.getBossDamage() + "%\r\n";
                    say += "아이템 강화 횟수 : " + getAddEnhance(item) + "강#k\r\n\r\n\r\n";
                    cm.sendYesNo("#r강화에 실패#k하였지만" + ConvertNumber(keep) + " 메소를 사용하여 #r강화 등급#k이 보호되었습니다.\r\n계속 강화하시려면 '예'를 눌러 주세요.\r\n\r\n" + say);
                    re = 1;
                    status = 1;
                } else if (choice == 2) {
                    item.setStr(item.getStr() - items[getAddEnhance(item)][3]);
                    item.setDex(item.getDex() - items[getAddEnhance(item)][3]);
                    item.setInt(item.getInt() - items[getAddEnhance(item)][3]);
                    item.setLuk(item.getLuk() - items[getAddEnhance(item)][3]);
                    item.setWatk(item.getWatk() - items[getAddEnhance(item)][4]);
                    item.setMatk(item.getMatk() - items[getAddEnhance(item)][4]);
                    if (getAddEnhance(item) > 1 && getAddEnhance(item) < 15) {
                        item.setOwner(""+(getAddEnhance(item)-1)+"강");
                    } else if (getAddEnhance(item) == 1) {
                        item.setOwner("");
                    }                    
                    cm.getPlayer().forceReAddItem(item, Packages.client.inventory.MapleInventoryType.EQUIP);
                    say = "";     
                    say += "#r<아이템 정보>\r\n";
                    say += "강화할 아이템 : #i" + itemid + "# #z" + itemid + "#\r\n";
                    say += "STR : " + item.getStr() + "  |  DEX : " + item.getDex() + "  |  INT : " + item.getInt() + "  |  LUK " + item.getLuk() + "\r\n";
                    say += "공격력 : " + item.getWatk() + "  |  마력 : " + item.getMatk() + "  | 스타포스 : " + item.getEnhance() + "성\r\n";
                    say += "올 스탯 : " + item.getAllStat() + "%  |  총 데미지 : " + item.getTotalDamage() + "%  |  보스 공격력 : " + item.getBossDamage() + "%\r\n";
                    say += "아이템 강화 횟수 : " + getAddEnhance(item) + "강#k\r\n\r\n\r\n";
                    cm.sendYesNo("#r강화에 실패#k하여 #r강화 등급#k이 하락하였습니다.\r\n계속 강화하시려면 '예'를 눌러 주세요.\r\n\r\n" + say);
                    re = 1;
                    status = 1;
                } else {
                    cm.dispose();
                    return;
                }
            }
        } else {
            cm.dispose();
            return;
        }
    } else {
        cm.dispose();
        return;
    }
}