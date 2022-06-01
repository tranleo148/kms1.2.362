importPackage(Packages.constants);

var status = 0;
var invs = Array(1, 5);
var invv;
var selected;
var slot_1 = Array();
var slot_2 = Array();
var statsSel;
var 별 = "#fUI/FarmUI.img/objectStatus/star/whole#";

function start() {
	action(1,0,0);
}

function action(mode, type, selection) {
	if (mode != 1) {
		cm.dispose();
		return;
	}
	status++;
	if (status == 1) {
		var txt = "안녕하세요. 캐쉬아이템을 사리지지않고 버릴 수 있게 해드립니다.#k\r\n#fs12#";
		txt += "\r\n#d* 드롭에는 1000 만 메소가 필요합니다.#k\r\n";
		txt += "#L1##r캐쉬 아이템#k 을 드롭하겠습니다.#k\r\n";
		cm.sendSimple(txt);
	} else if (status == 2) {
		var ok = false;
		var selStr = "#fn나눔고딕 Extrabold##d드롭하고 싶으신 아이템을 선택해주세요.#k\r\n";
		for (var x = 0; x < invs.length; x++) {
			var inv = cm.getInventory(invs[x]);
			for (var i = 0; i <= inv.getSlotLimit(); i++) {
				if (x == 0) {
					slot_1.push(i);
				} else {
					slot_2.push(i);
				}
				var it = inv.getItem(i);
				if (it == null) {
					continue;
				}
				if (selection == 1){
				var itemid = it.getItemId();
				}else if (selection == 2){
				if (cm.isCash(it.getItemId())){
				var itemid = 0;
				}else{
				var itemid = it.getItemId();
				}
				}

				if (selection == 1){
				if (!cm.isCash(itemid)) {
					continue;
				}
				}else if (selection == 2){
				if (!GameConstants.isEquip(itemid)) {
					continue;
				}
				}
				ok = true;
				selStr += "#L" + (invs[x] * 1000 + i) + "##v" + itemid + "##t" + itemid + "##l\r\n";
			}
		}
		if (!ok) {
			cm.sendOk("#fn나눔고딕 Extrabold##r인벤토리 창에 아이템이 하나도 없으신 것 같아요..#k");
			cm.dispose();
			return;
		}
		cm.sendSimple(selStr + "#k");
	} else if (status == 3) {
		invv = selection / 1000;
		selected = selection % 1000;
		var inzz = cm.getInventory(invv);
		if (invv == invs[0]) {
			statsSel = inzz.getItem(slot_1[selected]);
		} else {
			statsSel = inzz.getItem(slot_2[selected]);
		}
		if (statsSel == null) {
			cm.sendOk("#fn나눔고딕 Extrabold##r오류 안내\r\n\r\n다시 시도를 해주세요.#k");
			cm.dispose();
			return;
		}
		cm.sendGetNumber("#fn나눔고딕 Extrabold##v" + statsSel.getItemId() + "##t" + statsSel.getItemId() + "#\r\n\r\n#d드롭하실 갯수를 적어주세요.#k", 1, 1, statsSel.getQuantity());
	} else if (status == 4) {
	if (cm.getMeso()>=10000){
		if (statsSel.getItemId() !== 1143032 && statsSel.getItemId() !== 1142373 && statsSel.getItemId() !== 1112750 && statsSel.getItemId() !== 1182058 && statsSel.getItemId() !== 1142551 && statsSel.getItemId() !== 1182062 && statsSel.getItemId() !== 1182063 && statsSel.getItemId() !== 1182064 && statsSel.getItemId() !== 1182192){
		cm.gainMeso(-10000);
		if (!cm.dropItem(selected, invv, selection)) {
			cm.sendOk("#fn나눔고딕 Extrabold##r[오류 안내]#k\r\n\r\n다시 시도를 해주세요.");
		}
                } else {
				cm.sendOk("#fn나눔고딕 Extrabold##r해당 아이템은 드롭하실 수 없습니다.#k");
				cm.dispose();
                }
	} else {
	cm.sendOk("#fn나눔고딕 Extrabold##r드롭을 위한 메소가 부족합니다.#k");
	cm.dispose();
	}
		cm.dispose();
	}
}
