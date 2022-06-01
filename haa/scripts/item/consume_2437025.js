importPackage(java.lang);
importPackage(Packages.server);

var ItemList = [1113070, 1032216, 1152155];

var enter = "\r\n";

var need = 2437025, qty = 1;
var item;

var allstat = 800;
var atk = 200;
var dmg = 20;

var selected, seld = -1;
var finallist;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, sel) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
    	}
	if (status == 0) {
		var msg = "#r[#z"+need +"#]#k"+enter;
        msg += "#r#i"+need +"##z"+need +"##k를 정말 사용하시겠습니까? "+ItemList.length+"개의 아이템 중 1가지를 선택하여 획득합니다.#b"+enter;
        for (i = 0; i < ItemList.length; i++) {
            msg += "#L"+i+"##i"+ItemList[i]+"##z"+ItemList[i]+"#" +enter;
        }
		cm.sendSimple(msg);
	} else if (status == 1) {
		seld = sel;
		if (!cm.haveItem(need, qty)) {
			cm.sendOk("#b#i"+need+"##z"+need+"##k가 없는 것 같은데요?");
			cm.dispose();
			return;
		}
		selected = ItemList[sel];
		if (!cm.canHold(selected)) {
			cm.sendOk("장비창에 공간이 부족합니다.");
			cm.dispose();
			return;
		}

		itemz = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(selected);
		itemz.setStr(allstat);
		itemz.setDex(allstat);
		itemz.setInt(allstat);
		itemz.setLuk(allstat);
		itemz.setWatk(atk);
		itemz.setMatk(atk);
		itemz.setTotalDamage(dmg);
		Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(), itemz, false);

		cm.gainItem(need, -qty);
		var msg = "어때? #r#i"+selected+"##z"+selected+"##k아이템은 잘 받았어요? 정말 어메이징하지 않아요? 다음번에 또 #b#i"+need+"##z"+need+"##k가 생기면 나를 찾아와줘요!";
		cm.sendOk(msg);
		cm.dispose();
	}
}
