importPackage(java.lang);

function ConvertNumber(number) { //모 블로그 참조함, 이 부분에 대해서는 키네시스(kinesis8@nate.com), 라피스#2519 에게 저작권이 없음
    var inputNumber  = number < 0 ? false : number;
    var unitWords    = ['', '만 ', '억 ', '조 ', '경 '];
    var splitUnit    = 10000;
    var splitCount   = unitWords.length;
    var resultArray  = [];
    var resultString = '';
    if (inputNumber == false) {
        cm.sendOk("오류가 발생하였습니다. 다시 시도해 주세요.\r\n(파싱오류)");
        cm.dispose();
        return;
    }
    for (var i = 0; i < splitCount; i++) {
        var unitResult = (inputNumber % Math.pow(splitUnit, i + 1)) / Math.pow(splitUnit, i);
        unitResult = Math.floor(unitResult);
        if (unitResult > 0){
            resultArray[i] = unitResult;
        }
    }
    for (var i = 0; i < resultArray.length; i++) {
        if(!resultArray[i]) continue;
        resultString = String(resultArray[i]) + unitWords[i] + resultString;
    }
    return resultString;
}

var status = -1;

var items = [ //강화수, 강화성공확률, 강화메소, 올스탯, 공마
    [0, 0, 0, 0, 0],
    [1, 95, 15000000, 5, 5],
    [2, 95, 15000000, 5, 5],
    [3, 90, 29500000, 5, 5],
    [4, 90, 37000000, 7, 5],
    [5, 80, 45000000, 7, 5],
    [6, 60, 120000000, 9, 5],
    [7, 50, 200000000, 11, 6],
    [8, 40, 250000000, 13, 9],
    [9, 30, 270000000, 15, 10],
    [10, 30, 550000000, 15, 15],
    [11, 25, 700000000, 20, 20],
    [12, 20, 900000000, 20, 20],
    [13, 20, 1100000000, 30, 25],
    [14, 15, 1500000000, 35, 30],
    [15, 9, 1700000000, 35, 30],
    [16, 7, 1900000000, 60, 30],
    [17, 5, 2100000000, 100, 50],
    [18, 5, 2400000000, 150, 100],
    [19, 3, 2700000000, 250, 150],
    [20, 2, 3000000000, 300, 250],
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

var keep = 3000000000; //파괴 방지 메소
var item, itemid, slot, choice, say;
var re = 0;

function start () {
    action (1, 0, 0);
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
        var say = "#fs11##b강화할 장비#k를 선택해 주세요\r\n\r\n#r스타포스와는 별개의 강화 시스템#k입니다 \r\n#r#e심볼 류는 강화 하지 마세요 능력치 적용 안됩니다.#n#k\r\n\r\n";
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
        if (item.getOwner().equals("20강")) {
            cm.sendOk("이미 20강까지 강화가 완료된 아이템 입니다.");
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