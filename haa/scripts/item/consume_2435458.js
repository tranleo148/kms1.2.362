importPackage(java.lang);
importPackage(Packages.server);

var Itemlist = [5062006];

var enter = "\r\n";

var need = 2435458, qty = 50;
var item;

var selected, seld = -1;

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
		var msg = "#b#i"+need+"##z"+need+"# "+qty+"개#k가 있으면 #b#i5062006##z5062006##k 3개랑 교환할 수 있습니다."+enter;
		//msg += "#L1#아이템 리스트보기"+enter;
		msg += "#L3#교환하기";
		cm.sendSimple(msg);
	} else if (status == 1) {
		seld = sel;
		if (sel == 1) {
			var msg = "골드애플 조각에선 다음과 같은 아이템들이 등장합니다.#b"+enter;
			for (i = 0; i < Itemlist.length; i++)
				msg += "#i"+Itemlist[i]+"##z"+Itemlist[i]+"#"+enter;

			cm.sendOk(msg);
			cm.dispose();
		} else {
			if (!cm.haveItem(need, qty)) {
				cm.sendOk("#b#i"+need+"##z"+need+"##k이 부족합니다.");
				cm.dispose();
				return;
			}
			cm.sendYesNo("정말 #b#i"+need+"##z"+need+"##k을 이용해 교환 하시겠습니까?");
		}
	} else if (status == 2) {
			if (!cm.haveItem(need, qty)) {
				cm.sendOk("#b#i"+need+"##z"+need+"##k이 부족합니다.");
				cm.dispose();
				return;
			}

			item = Itemlist[Math.floor(Math.random() * Itemlist.length)];

			if (!cm.canHold(item)) {
				cm.sendOk("캐시칸에 공간이 부족합니다.");
				cm.dispose();
				return;
			}
			if (item == 1114316) {
			    it = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(item);
			    Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(), it, false, false, false, true);
			} else {
			    cm.gainItem(item, 3);
			}
			cm.gainItem(need, -qty);
			var msg = "#r#i"+item+"##z"+item+"##k획득";
			cm.sendOk(msg);
			cm.dispose();
	}
}