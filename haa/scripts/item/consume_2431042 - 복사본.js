
importPackage(Packages.client.items);
importPackage(java.lang);
importPackage(Packages.tools.RandomStream);
var status = 0;
var ran = 300;

function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    
    if (status == 0) {
        var leftslot = cm.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot();
        if (leftslot < 3) {
            cm.sendOk("기타창 인벤토리를 여유분3개는 남겨두세요.");
            cm.dispose();
            return;
        }
	ran = Randomizer.rand(300,300);
        if (Randomizer.nextInt(300) <= 30) {
            cm.gainItem(1302198, 1);
            cm.sendOk("아이템이 나왔습니다.");
        } else if (Randomizer.nextInt(300) <= 30) {
            cm.sendOk("아이템이 나왔습니다.");
            cm.gainItem(1312104, 1);
        } else if (Randomizer.nextInt(300) <= 30) {
            cm.sendOk("아이템이 나왔습니다.");
            cm.gainItem(1322144, 1);
        } else if (Randomizer.nextInt(300) <= 30) {
            cm.sendOk("아이템이 나왔습니다.");
            cm.gainItem(1332175, 1); // C급 끝
	} else if (Randomizer.nextInt(300) <= 30) {  
            cm.sendOk("아이템이 나왔습니다.");
            cm.gainItem(1342057, 1);
	} else if (Randomizer.nextInt(300) <= 30) {
              cm.sendOk("아이템이 나왔습니다."); 
            cm.gainItem(1372124, 1);
	} else if (Randomizer.nextInt(300) <= 30) {
            cm.sendOk("아이템이 나왔습니다.");
            cm.gainItem(1382150, 1);
	} else if (Randomizer.nextInt(300) <= 30) {
            cm.sendOk("아이템이 나왔습니다.");
            cm.gainItem(1402136, 1); // B급 끝
	} else if (Randomizer.nextInt(300) <= 30) {
              cm.sendOk("아이템이 나왔습니다.");
            cm.gainItem(1412092, 1); 
	} else if (Randomizer.nextInt(300) <= 30) {
            cm.sendOk("아이템이 나왔습니다.");
            cm.gainItem(1422095, 1); 
	} else if (Randomizer.nextInt(300) <= 30) {
            cm.sendOk("아이템이 나왔습니다.");
            cm.gainItem(1432124, 1); 
	} else if (Randomizer.nextInt(300) <= 30) {
            cm.sendOk("아이템이 나왔습니다.");
            cm.gainItem(1442162, 1); 
	} else if (Randomizer.nextInt(300) <= 30) {
            cm.sendOk("아이템이 나왔습니다.");
            cm.gainItem(1452154, 1); 
	} else if (Randomizer.nextInt(300) <= 30) {
            cm.sendOk("아이템이 나왔습니다.");
            cm.gainItem(1462144, 1); 
	} else if (Randomizer.nextInt(300) <= 30) {
            cm.sendOk("아이템이 나왔습니다.");
            cm.gainItem(1472166, 1); 
	} else if (Randomizer.nextInt(300) <= 30) {
            cm.sendOk("아이템이 나왔습니다.");
            cm.gainItem(1482127, 1); // S급 끝
	} else if (Randomizer.nextInt(300) <= 30) {
            cm.sendOk("아이템이 나왔습니다.");
            cm.gainItem(1522061, 1); 
	} else if (Randomizer.nextInt(300) <= 30) {
            cm.sendOk("아이템이 나왔습니다.");
            cm.gainItem(1482127, 1); 
	} else if (Randomizer.nextInt(300) <= 30) {
            cm.sendOk("아이템이 나왔습니다.");
            cm.gainItem(1532065, 1); 
	}
}

