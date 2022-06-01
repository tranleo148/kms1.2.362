importPackage(java.lang);
importPackage(Packages.server);

var ItemList = [1012632, 1022278, 1132308, 1122430, 1182285, 1032316, 1113306, 1162080, 1162081, 1162082, 1162083];

var enter = "\r\n";

var need = 2439615, qty = 1;
var item;

var normal_allstat_min = 1, normal_allstat_max = 100;
var normal_atk_min = 1, normal_atk_max = 30;
var normal_dmg_min = 1, normal_dmg_max = 10;

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
		msg += "#r#i"+need +"##z"+need +"##k를 정말 사용하시겠습니까? "+ItemList.length+"개의 아이템 중 1가지가 랜덤으로 나옵니다.";
		cm.sendYesNo(msg);
	} else if (status == 1) {
		seld = sel;
		if (!cm.haveItem(need, qty)) {
			cm.sendOk("#b#i"+need+"##z"+need+"##k가 없는 것 같은데요?");
			cm.dispose();
			return;
		}
		selected = ItemList[Packages.server.Randomizer.nextInt(ItemList.length)];
		if (!cm.canHold(selected)) {
			cm.sendOk("장비창에 공간이 부족합니다.");
			cm.dispose();
			return;
		}
		allstat = Packages.server.Randomizer.rand(normal_allstat_min, normal_allstat_max);
		atk = Packages.server.Randomizer.rand(normal_atk_min, normal_atk_max);
		dmg = Packages.server.Randomizer.rand(normal_dmg_min, normal_dmg_max);

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
		var msg = "어때? #r#i"+selected+"##z"+selected+"##k아이템은 잘 받았어요? 정말 어메이징하지 않아요? 다음번에 또 #bMVP 전용 스페셜 캐시 선택박스#k가 생기면 나를 찾아와줘요!";
		cm.sendOk(msg);
		cm.dispose();
	}
}