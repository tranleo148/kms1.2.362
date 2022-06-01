importPackage(java.util);
importPackage(java.lang);
importPackage(Packages.client.items);
importPackage(Packages.provider);
importPackage(Packages.tools);
importPackage(Packages.client);
importPackage(Packages.server);

var status = 0;
var selectedType = -1;
var selectedItem = -1;
var item;
var mats;
var matQty;
var cost;
var qty;
var equip;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == 1)
	status++;
	else{
	cm.dispose();
	return;
	}

	if (status == 0 && mode == 1) {
	cm.sendSimpleS("그래. 장비 제작의 달인, 이 #b에이센#k님에게 원하는 게 뭐지?\r\n#b"
			+ "#L0##e장신구#n를 제작하고 싶습니다.#l\r\n"
			+ "#L1##e방어구#n를 제작하고 싶습니다.#l\r\n"
			+ "#L2##e고급 원석#n을 제련하고 싶습니다.#l\r\n"
			+ "#L3##e데미지 스킨#n을 제작하고 싶습니다.#l\r\n", 4, 9031003);

	}

	else if (status == 1 && mode == 1) {
	selectedType = selection;

		if (selectedType == 0) {
		var items = new Array (1012406, 1012407, 1012408, 1012409, 1012410, 1032200, 1032216, 1152155, 1113055, 1113068, 1113069, 1113070, 1114301, 1113089, 1113282, 1112763, 1112767, 1112771, 1112775, 1113231);
		var selStr = "#b장신구#k를 만들고 싶은거군! 어떤 장신구가 필요한가? \r\n\r\n#b";
		for (i = 0; i < items.length; selStr += "#b#L"+i+"##i"+items[i]+":# #z"+items[i]+"##l\r\n",i++); selStr += "\r\n\r\n";
		cm.sendSimple(selStr); equip = true;
		}

		else if (selectedType == 1) {
		var items = new Array (1032111, 1032112, 1032113);
		var selStr = "#b메이플 이어링#k을 만들고 싶으시군요! 제작을 원하시는 아이템을 선택해주세요. \r\n\r\n#b";
		for (i = 0; i < items.length; selStr += "#b#L"+i+"##i"+items[i]+"# #z"+items[i]+"##l\r\n",i++); selStr += "\r\n\r\n";
		cm.sendSimple(selStr); equip = true;
		}

		else if (selectedType == 2) {
		var items = new Array (1102468, 1102469, 1102470);
		var selStr = "#b메이플 망토#k를 만들고 싶으시군요! 제작을 원하시는 아이템을 선택해주세요. \r\n\r\n#b";
		for (i = 0; i < items.length; selStr += "#b#L"+i+"##i"+items[i]+"# #z"+items[i]+"##l\r\n",i++); selStr += "\r\n\r\n";
		cm.sendSimple(selStr); equip = true;
		}

		else if (selectedType == 3) {
		var items = new Array (1092030, 1092045, 1092046, 1092047);
		var selStr = "#b메이플 쉴드#k를 만들고 싶으시군요! 제작을 원하시는 아이템을 선택해주세요. \r\n\r\n#b";
		for (i = 0; i < items.length; selStr += "#b#L"+i+"##i"+items[i]+"# #z"+items[i]+"##l\r\n",i++); selStr += "\r\n\r\n";
		cm.sendSimple(selStr); equip = true;
		}


		if (equip) status++;
	}

	else if (status == 3 && mode == 1) {
		if (equip) { selectedItem = selection; qty = 1; }
		else qty = selection;

		if (selectedType == 0) {
		var itemSet   = new Array(1012406, 1012407, 1012408, 1012409, 1012410, 1032200, 1032216, 1152155, 1113055, 1113068, 1113069, 1113070, 1114301, 1113089, 1113282, 1112763, 1112767, 1112771, 1112775, 1113231);
		var matSet    = new Array(
					new Array(4021009),
					new Array(4021009),
					new Array(4021009),
					new Array(4021009),
					new Array(4021009),
					new Array(4021009),
					new Array(4021009),
					new Array(4021009),
					new Array(4021009),
					new Array(4021009),
					new Array(4021009),
					new Array(4021009),
					new Array(4021009),
					new Array(4021009),
					new Array(4021009),
					new Array(4021009)
				);

		var matQtySet = new Array(
					new Array(300),
					new Array(300),
					new Array(300),
					new Array(300),
					new Array(300),
					new Array(300),
					new Array(300),
					new Array(300),
					new Array(300),
					new Array(300),
					new Array(300),
					new Array(300),
					new Array(300),
					new Array(300),
					new Array(300),
					new Array(300),
					new Array(300),
					new Array(300),
					new Array(300),
					new Array(300)
				);

		var costSet   = new Array(500000000,
					  500000000,
					  500000000,
					  500000000,
					  500000000,
					  500000000,
					  500000000,
					  500000000,
					  500000000,
					  500000000,
					  500000000,
					  500000000,
					  500000000,
					  500000000,
					  500000000,
					  500000000
				);
		}


		else if (selectedType == 1) {
		var itemSet   = new Array(1032111, 1032112, 1032113);
		var matSet    = new Array(
					new Array(1032003, 4001126),
					new Array(1032111, 4001126),
					new Array(1032112, 4001126)
				)

		var matQtySet = new Array(
					new Array(1, 500),
					new Array(1, 1000),
					new Array(1, 1500)
				)

		var costSet   = new Array(
					  20000,
					  40000,
					  80000
				);
		}

		else if (selectedType == 2) {
		var itemSet   = new Array(1102468, 1102469, 1102470);
		var matSet    = new Array(
					4001126,
					new Array(1102468, 4001126),
					new Array(1102469, 4001126)
				)

		var matQtySet = new Array(
					400,
					new Array(1, 1000),
					new Array(1, 2000)
				)

		var costSet   = new Array(
					  0,
					  150000,
					  450000
				);
		}


		else if (selectedType == 3) {
		var itemSet   = new Array(1092030, 1092045, 1092046, 1092047);
		var matSet    = new Array(
					new Array(1092003, 4001126),
					new Array(1092030, 4001126),
					new Array(1092030, 4001126),
					new Array(1092030, 4001126)
				)

		var matQtySet = new Array(
					new Array(1, 900),
					new Array(1, 1500),
					new Array(1, 1500),
					new Array(1, 1500)
				)

		var costSet   = new Array(
					  10000,
					  1000000,
					  1000000,
					  1000000
				);
		}

		item = itemSet[selectedItem];
		mats = matSet[selectedItem];
		matQty = matQtySet[selectedItem];
		cost = costSet[selectedItem];

	var prompt = "어디보자…. #b#z"+item+"# "+qty+"개#k를 만드려면 아래에 있는 재료와 수수료를 준비해야 합니다.\r\n#b";
	if (mats instanceof Array){

		for(var i = 0; i < mats.length; i++){
//				if(cm.itemQuantity(mats[i]) < matQty[i]) {
				prompt += "\r\n#i"+mats[i]+"# #z"+mats[i]+"# (#r"+cm.itemQuantity(mats[i])+"개 #k/#b "+ matQty[i] * qty +"개)";
/*				} else {
				prompt += "\r\n#i"+mats[i]+"# #z"+mats[i]+"# (#b#e"+cm.itemQuantity(mats[i])+"개 #k#n/#b "+ matQty[i] * qty +"개)";
				}
*/		}
	}
	else {
		prompt += "\r\n#i"+mats+"# #z"+mats+"# " + matQty * qty + "개";
	}
		
	if (cost > 0)
			if(cost*qty != 0) {
		prompt += "\r\n#i4031138# #b"+cost * qty / 10000+"만 메소";
			}

	cm.sendYesNo(prompt);
	} else if (status == 4 && mode == 1) {
	var complete = false;
		
	if (cm.getMeso() < cost * qty) {
		cm.sendOk("메소#k 는 제대로 갖고 있는건가? 다시 한번 확인해보게.")
		cm.dispose();
		return;
	} else {
		if (mats instanceof Array) {
		for (var i = 0; i < mats.length; i++) {
			complete = cm.haveItem(mats[i], matQty[i] * qty);
			if (!complete) {
			break;
			}
		}
		} else {
		complete = cm.haveItem(mats, matQty * qty);
		}	
	}
			
	if (!cm.canHold(item)) {
		complete = false;
	}
	if (!complete)
		cm.sendOk("재료가 모두 없거나 인벤토리 공간이 부족한 것 같습니다. 수수료가 부족해도 만들 수 없어요!");
	else {
		if (mats instanceof Array) {
		for (var i = 0; i < mats.length; i++){
			cm.gainItem(mats[i], -matQty[i] * qty);
		}
		}
		else
		cm.gainItem(mats, -matQty * qty);
					
		if (cost > 0)
		cm.gainMeso(-cost * qty);
        
		real =  MapleItemInformationProvider.getInstance().getEquipById(item);

		real.setStr(real.getStr() + Math.floor(Math.random() * real.getStr()*3+1));
		real.setDex(real.getDex() + Math.floor(Math.random() * real.getDex()*3+1));
		real.setInt(real.getInt() + Math.floor(Math.random() * real.getInt()*3+1));
		real.setLuk(real.getLuk() + Math.floor(Math.random() * real.getLuk()*3+1));
			if(selectedType > 3) {
			real.setWatk(real.getWatk() + Math.floor(Math.random() * real.getWatk()/10));
			}

			if(selectedType > 3) {
			real.setMatk(real.getMatk() + Math.floor(Math.random() * real.getMatk()/10));
			}

		MapleInventoryManipulator.addbyItem(cm.getClient(), real, false);

		cm.sendOk("자아.. 다 됐다구. 역시 완벽한 아이템이 탄생했잖아? 다른 작업도 필요하다면 다시 나에게 찾아오라구.");
	}
	cm.dispose();
	}
}