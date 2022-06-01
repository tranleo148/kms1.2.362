var enter = "\r\n";
var seld = -1;

var key = "levelreward";
var selr;
var reward = [
         {'lvl' : 200, 'ap' : 200, 'item' : [
		{'itemid' : 1712001, 'qty' : 1},
		{'itemid' : 1712002, 'qty' : 1},
		{'itemid' : 1712003, 'qty' : 1},
		{'itemid' : 1712004, 'qty' : 1},
		{'itemid' : 1712005, 'qty' : 1},
		{'itemid' : 1712006, 'qty' : 1},
                {'itemid' : 5062500, 'qty' : 60},
                {'itemid' : 5062010, 'qty' : 150},
	]},     
        {'lvl' : 220, 'ap' : 270, 'item' : [
		{'itemid' : 2435719, 'qty' : 100},
                {'itemid' : 5062500, 'qty' : 200},
                {'itemid' : 5062010, 'qty' : 400},
                {'itemid' : 4310237, 'qty' : 300},
        ]},
        {'lvl' : 230, 'ap' : 280, 'item' : [
		{'itemid' : 2450064, 'qty' : 2},
                {'itemid' : 2437760, 'qty' : 10},
                {'itemid' : 2435719, 'qty' : 100},
                {'itemid' : 2431940, 'qty' : 2},
        ]},
	{'lvl' : 240, 'ap' : 300, 'item' : [
                {'itemid' : 4001716, 'qty' : 1},
                {'itemid' : 2437760, 'qty' : 10},
                {'itemid' : 2049360, 'qty' : 3},
                {'itemid' : 4310266, 'qty' : 500},
                {'itemid' : 4310237, 'qty' : 2000},
        ]},
            {'lvl' : 250, 'ap' : 320, 'item' : [
                {'itemid' : 2430885, 'qty' : 1},
		{'itemid' : 4310266, 'qty' : 500},
              	{'itemid' : 2048717, 'qty' : 200},
                {'itemid' : 2450064, 'qty' : 5},
                {'itemid' : 2023072, 'qty' : 3},
                {'itemid' : 2430042, 'qty' : 2},
        ]},
            {'lvl' : 260, 'ap' : 330, 'item' : [
                {'itemid' : 2430886, 'qty' : 1},
		{'itemid' : 2430043, 'qty' : 2},
		{'itemid' : 2048753, 'qty' : 20},
		{'itemid' : 2633336, 'qty' : 10},
		{'itemid' : 4001716, 'qty' : 5},
        ]},
            {'lvl' : 275, 'ap' : 350, 'item' : [
                {'itemid' : 2430887, 'qty' : 1},
                {'itemid' : 5062005, 'qty' : 2},
                {'itemid' : 4001716, 'qty' : 10},
                {'itemid' : 3014028, 'qty' : 1},
                {'itemid' : 2430044, 'qty' : 1},
            ]},
            {'lvl' : 290, 'ap' : 351, 'item' : [
                {'itemid' : 2430889, 'qty' : 1},
                {'itemid' : 5062005, 'qty' : 4},
                {'itemid' : 4001716, 'qty' : 15},
            ]},
            {'lvl' : 300, 'ap' : 370, 'item' : [
                {'itemid' : 2430890, 'qty' : 1},
                {'itemid' : 2438145, 'qty' : 1},
                {'itemid' : 5062005, 'qty' : 5},
                {'itemid' : 5062503, 'qty' : 5},
                {'itemid' : 2430218, 'qty' : 1},
                {'itemid' : 4001716, 'qty' : 20},
            ]},

         ]
function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, sel) {
	if (mode == -1) {
		cm.dispose();
	}
	if (mode == 0) {
		cm.dispose();
		return;
	}
	if (mode == 1) {
		status++;
	}
	if (status == 0) {
		var msg = "#fs11# #d수령 가능한 레벨 보상은 아래와 같습니다.#k"+enter;
		if (cm.getPlayer().getLevel() >= 250) {
			check = getk(250) == -1 ? "수령 가능" : "수령 완료"
			//msg += "#L250##b250레벨 보상 #e("+check+")#n#k"+enter;
		} else {
			//msg += "#L250##r250레벨 보상 (수령불가)#k"+enter;
		}
		if (cm.getPlayer().getLevel() >= 275) {
			check = getk(275) == -1 ? "수령 가능" : "수령 완료"
			//msg += "#L275##b275레벨 보상 #e("+check+")#n#k"+enter;
		} else {
			//msg += "#L275##r275레벨 보상 (수령불가)#k"+enter;
		}
		for (i = 0; i < reward.length; i++) {
			if (cm.getPlayer().getLevel() >= reward[i]['lvl']) {
				check = getk(reward[i]['lvl']) == -1 ? "수령 가능" : "수령 완료"
				msg += "#L"+i+"##b"+reward[i]['lvl']+"레벨 보상 #e("+check+")#n#k"+enter;
			} else {
				msg += "#L"+i+"##r"+reward[i]['lvl']+"레벨 보상 (수령 불가)#k"+enter;
			}
		}
		cm.sendSimple(msg);
	} else if (status == 1) {
		seld = sel;
		if (seld > 200) {
			if (getk(seld) > -1) {
				cm.sendOk("#fs11##r이미 수령하신 보상입니다.");
				cm.dispose();
				return;
			}
			if (seld > cm.getPlayer().getLevel()) {
				cm.sendOk("#fs11##r레벨이 부족합니다.#k");
				cm.dispose();
				return;
			}
			if (seld == 250) {
				cm.forceCompleteQuest(6500);
				cm.forceCompleteQuest(12394);
				cm.forceCompleteQuest(12395);
				cm.forceCompleteQuest(12396);
				cm.setInnerStats(1);
				cm.setInnerStats(2);
				cm.setInnerStats(3);
			}
			if (seld == 275) {
				cm.getPlayer().gainAp(100); // 275레벨 스탯보상
				cm.getPlayer().levelUp();
			}
			setk(seld, "1");
			cm.sendOk("#fs11##r레벨업 달성보상이 지급되었습니다.");
			cm.dispose();
		} else {
			selr = reward[seld];
			if (getk(selr['lvl']) > -1) {
				cm.sendOk("#fs11##r이미 수령하신 보상입니다."+enter+enter+getRewardList());
				cm.dispose();
				return;
			}
			if (selr['lvl'] > cm.getPlayer().getLevel()) {
				cm.sendOk("#fs11##r이 보상을 받기엔 레벨이 모자랍니다."+enter+enter+getRewardList());
				cm.dispose();
				return;
			}
			//cm.getPlayer().gainAp(selr['ap']);
			gainReward(selr['lvl']);
			setk(selr['lvl'], "1");
			cm.sendOk("#fs11##r보상 지급이 완료되었습니다.");
			cm.dispose();
		}
		
	}
}

function gainReward(level) {
	for (p = 0; p < selr['item'].length; p++) {
		if (Math.floor(selr['item'][p]['itemid'] / 1000000) == 1) {
			gainItemall(selr['item'][p]['itemid'], selr['item'][p]['allstat'], selr['item'][p]['atk']);
		} else {
			cm.gainItem(selr['item'][p]['itemid'], selr['item'][p]['qty']);
		}
	}
}

function getRewardList() {
	var msg = "#b"+selr['lvl']+"#fs11#레벨#k 달성 보상리스트 입니다.#fs11#"+enter;
	msg += ""+enter;
	for (p = 0; p < selr['item'].length; p++) {
		if (Math.floor(selr['item'][p]['itemid'] / 1000000) == 1) {
			msg += "#i"+selr['item'][p]['itemid']+"##b#z"+selr['item'][p]['itemid']+"# "+selr['item'][p]['qty']+"개#k" +enter;
		} else {
			msg += "#i"+selr['item'][p]['itemid']+"##b#z"+selr['item'][p]['itemid']+"# "+selr['item'][p]['qty']+"개#k" + enter;
		}
	}
	return msg;
}

function gainItemall(itemid, allstat, atk) {
	item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(itemid);
	/*item.setStr(allstat);
	item.setDex(allstat);
	item.setInt(allstat);
	item.setLuk(allstat);
	item.setWatk(atk);
	item.setMatk(atk);*/
	Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(), item, false);
}

function getk(level) {
	return cm.getPlayer().getKeyValue(201820, key+"_"+level);
}

function setk(level, value) {
	cm.getPlayer().setKeyValue(201820, key+"_"+level, value);
}