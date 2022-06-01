importPackage(java.lang);

var status = -1;
var item, itemid, slot, choice, say;
var re = 0;
var stat = 0;
var dmg = 0;
function start () {
    action (1, 0, 0);
}

var items = [ //강화수, 강화성공확률, 강화메소, 올스탯, 공마
    [0, 0, 0, 0, 0],
    [1, 100, 100000000, 87, 65],
    [2, 95, 30000000, 5, 5],
    [3, 90, 45000000, 5, 5],
    [4, 90, 50000000, 7, 5],
    [5, 80, 150000000, 7, 5],
    [6, 70, 150000000, 9, 5],
    [7, 70, 250000000, 11, 6],
    [8, 60, 250000000, 13, 9],
    [9, 50, 450000000, 15, 10],
    [10, 40, 550000000,15, 15],
    [11, 30, 550000000,15, 20],
    [12, 30, 500000000,20, 20],
    [13, 20, 500000000,30, 25],
    [14, 15, 500000000,35, 30],
    [15, 12, 500000000,35, 30],
    [16, 10, 1000000000,30, 30],
    [17, 5, 1000000000,50, 50],
    [18, 5, 1000000000,100, 50],
    [19, 3, 1200000000,150, 100],
    [20, 2, 1500000000,200, 150],
];

function getAddEnhance(item) {
	var owner = item.getOwner();
    return owner == "1강" ? 1 : 
	        owner == "2강" ? 2 : 
	        owner == "3강" ? 3 : 
	        owner == "4강" ? 4 : 
	        owner == "5강" ? 5 : 
	        owner == "6강" ? 6 : 
	        owner == "7강" ? 7 : 
	        owner == "8강" ? 8 : 
	        owner == "9강" ? 9 : 
	        owner == "10강" ? 10 : 
	        owner == "11강" ? 11 : 
	        owner == "12강" ? 12 : 
	        owner == "13강" ? 13 : 
	        owner == "14강" ? 14 : 
	        owner == "15강" ? 15 :
                    owner == "16강" ? 16 :
                    owner == "17강" ? 17 :
                    owner == "18강" ? 18 :
                    owner == "19강" ? 19 :
                    owner == "20강" ? 20 :
            0; 
}

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
        var say = "#fs11##b메소강화 초기화권#k을 사용할 장비를 선택 해주세요\r\n\r\n";
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
            if (getAddEnhance(item) == 0) {
            cm.sendOk("메소강화를 한 장비에만 사용 가능합니다.");
            cm.dispose();
            return;
        }
        itemid = item.getItemId();
        //강화수, 강화성공확률, 강화메소, 올스탯, 공마 
        var notice = "";
        say = "";
        say += "#fs 11#메소강화 초기화권을 사용하시겠습니까?\r\n\r\n\r\n ";
        cm.sendSimple(notice + say +
        "#L2##b사용하겠습니다.#k#k#l");
    } else if (status == 2) {
        if (re == 0) {
            choice = selection;
        }
        //강화수, 강화성공확률, 강화메소, 올스탯, 공마 
        say = "";
        say += "#fs 11#";
        say += "초기화권 사용시#b메소강화 옵션이#k사라집니다.\r\n";
        if (selection == 1 || choice == 1) {
            say += "\r\n";
        } else if (selection == 2 || choice == 2) {
            say += "";
        }   
        say += "#r<아이템 정보>\r\n";
        say += "초기화할 아이템 : #i" + itemid + "# #z" + itemid + "#\r\n";
        say += "STR : " + item.getStr() + "  |  DEX : " + item.getDex() + "  |  INT : " + item.getInt() + "  |  LUK " + item.getLuk() + "\r\n";
        say += "공격력 : " + item.getWatk() + "  |  마력 : " + item.getMatk() + "  | 스타포스 : " + item.getEnhance() + "성\r\n";
        say += "올 스탯 : " + item.getAllStat() + "%  |  총 데미지 : " + item.getTotalDamage() + "%  |  보스 공격력 : " + item.getBossDamage() + "%\r\n";
        say += "아이템 강화 횟수 : " + getAddEnhance(item) + "강#k\r\n\r\n\r\n";
        if (selection == 1 || choice == 1) {
            cm.sendYesNo(say + "정말로 " + ConvertNumber(keep) + "메소를 사용하여 미끄러짐을 방지 하시겠습니까?\r\n" +
            "성공/실패 여부와 상관없이 미끄러짐 방지 메소는 소모됩니다.\r\n\r\n#b총 필요 메소 : " + ConvertNumber(items[getAddEnhance(item) + 1][2] + keep) + "메소#k#l");
        } else if (selection == 2 || choice == 2) {
            cm.sendYesNo(say + "#b예#k를 누르면 초기화권이 사용됩니다.\r\n#b초기화권 사용에 필요한 수수료 : #b 1억 메소#k");
        }
    } else if (status == 3) {
        if (choice == 1) {
            if (cm.getPlayer().getMeso() >= 100000000) {
                cm.gainMeso(-100000000);
            } else {
                cm.sendOk("수수료가 부족하여 초기화권 사용이 불가능합니다.\r\n필요 수수료 : 1억메소");
                cm.dispose();
                return;
            }
        } else if (choice == 2) {
       	    	 if (cm.getPlayer().getMeso() >= 100000000) {
           	    	 cm.gainMeso(-100000000);
            } else {
                cm.sendOk("수수료가 부족하여 강화가 불가능합니다.");
                cm.dispose();
                return;
            }
        } else {
            cm.dispose();
            return;
        }        
        if (cm.getInventory(1).getItem(slot) != null) {
            var rand = Math.ceil(Math.random() * 100);
            if (rand >= 0) {
			if (item.getOwner().equals("1강")) { stat = 5; dmg = 5;}
			else if(item.getOwner().equals("2강")) { stat = 10; dmg = 10;}
			else if(item.getOwner().equals("3강")) { stat = 15; dmg = 15;}
			else if(item.getOwner().equals("4강")) { stat = 22; dmg = 20;}
			else if(item.getOwner().equals("5강")) { stat = 29; dmg = 25;}
			else if(item.getOwner().equals("6강")) { stat = 38; dmg = 30;}
			else if(item.getOwner().equals("7강")) { stat = 49; dmg = 36;}
			else if(item.getOwner().equals("8강")) { stat = 62; dmg = 45;}
			else if(item.getOwner().equals("9강")) { stat = 77; dmg = 55;}
			else if(item.getOwner().equals("10강")) { stat = 92; dmg = 70;}
			else if(item.getOwner().equals("11강")) { stat = 112; dmg = 90;}
			else if(item.getOwner().equals("12강")) { stat = 132; dmg = 110;}
			else if(item.getOwner().equals("13강")) { stat = 162; dmg = 135;}
			else if(item.getOwner().equals("14강")) { stat = 197; dmg = 165;}
			else if(item.getOwner().equals("15강")) { stat = 232; dmg = 195;}
			else if(item.getOwner().equals("16강")) { stat = 292; dmg = 225;}
			else if(item.getOwner().equals("17강")) { stat = 392; dmg = 275;}
			else if(item.getOwner().equals("18강")) { stat = 542; dmg = 375;}
			else if(item.getOwner().equals("19강")) { stat = 792; dmg = 525;}
			else if(item.getOwner().equals("20강")) { stat = 1092; dmg = 775;}
                item.setStr(item.getStr() - stat);
                item.setDex(item.getDex() - stat);
                item.setInt(item.getInt() - stat);
                item.setLuk(item.getLuk() - stat);
                item.setWatk(item.getWatk() - dmg);
                item.setMatk(item.getMatk() - dmg);
                item.setOwner("");
                cm.getPlayer().forceReAddItem(item, Packages.client.inventory.MapleInventoryType.EQUIP);
	    cm.gainItem(2430073, -1);
                say = "";     
                say += "#fs11##r<아이템 정보>\r\n";
                say += "강화할 아이템 : #i" + itemid + "# #z" + itemid + "#\r\n";
                say += "STR : " + item.getStr() + "  |  DEX : " + item.getDex() + "  |  INT : " + item.getInt() + "  |  LUK " + item.getLuk() + "\r\n";
                say += "공격력 : " + item.getWatk() + "  |  마력 : " + item.getMatk() + "  | 스타포스 : " + item.getEnhance() + "성\r\n";
                say += "올 스탯 : " + item.getAllStat() + "%  |  총 데미지 : " + item.getTotalDamage() + "%  |  보스 공격력 : " + item.getBossDamage() + "%\r\n";
                say += "아이템 강화 횟수 : " + getAddEnhance(item) + "강#k\r\n\r\n\r\n";
            cm.dispose();
            return;
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