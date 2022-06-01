var St = -1;

var reward = [ 
	[ 10, 10, 4310237, 500, 4310237, 2000 ],
	[ 30, 30, 4009005, 50, 4009005, 500 ],
	[ 50, 50, 5060048, 5, 5060048, 15 ],
	[ 70, 70, 2049397, 2, 2049397, 5 ],
	[ 90, 90, 2049372, 1, 2049372, 5 ],
	[ 110, 110, 4310308, 200, 4310308, 500 ],
	[ 130, 130, 4001716, 10, 4001716, 50 ],
	[ 150, 150, 2430042, 1, 2430042, 1 ],
	[ 170, 170, 4310266, 700, 4310266, 2000 ],
	[ 200, 200, 4310266, 1000, 2430891, 1 ]
	];
var mine = [];

function start() {
    St = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        St++;
    } else {
        if (St == 0) {
            cm.dispose();
        }
        St--;
    }

    if (St == 0) {
	var msg = "#fs11##fc0xFF000000#    현재 #fc0xFF6600CC##h0# #fc0xFF000000#님의 성장패스 레벨 :#fc0xFFFF3300# " + cm.getPlayer().getKeyValue(100592, "point") + "\r\n";
	msg += "   #r성장패스는 일일 퀘스트를 통해 레벨업 할 수 있다네.\r\n";
	msg += "#Cgray#――――――――――――――――――――――――――――――――――――――――";
	msg += "#L1##b성장패스 보상을 수령하겠습니다.\r\n";
	msg += "#L2#성장패스의 보상이 궁금합니다.\r\n";
	cm.sendSimple(msg);
    } else if (St == 1) {
	if (selection == 1) {
		var list = "#fs11##fc0xFF000000#자네가 받을 수 있는 보상은 아래와 같다네.#b\r\n";
		var level = cm.getPlayer().getKeyValue(100592, "point");
		for (var i = 0; i < reward.length; i++) {
			var isExist = false;
			if (level >= reward[i][1]) {
				list += "#L" + mine.length +"# ";
				if (cm.getPlayer().getKeyValue(1234, reward[i][0]+"-0") == -1) {
					list += "#fc0xFF990033#[" + reward[i][0] + "레벨 보상 받기]\r\n\r\n#b일반 보상 : #i" + reward[i][2] + "# #z" + reward[i][2] + "# " + reward[i][3] + "개\r\n";
					isExist = true;

				}
				if (cm.haveItem(4001760)) {
					if (cm.getPlayer().getKeyValue(1234, reward[i][0]+"-1") == -1) {
						list += "#fc0xFFFF3300#프리미엄 보상 : #i" + reward[i][4] + "# #z" + reward[i][4] + "# " + reward[i][5] + "개#l\r\n";
						list += "#Cgray#――――――――――――――――――――――――――――――――――――――――";
						isExist = true;
					}
				}
				if (isExist)
					mine.push(i);
				list += "";
			}
		}
		if (mine.length > 0) {
			cm.sendSimple(list);
		} else {
			cm.sendOk("#fs11##fc0xFF000000#받을 수 있는 보상이 없다네.");
			cm.dispose();
			return;
		}
	} else if (selection == 2) {
		var list = "";
		for (var i = 0; i < reward.length; i++) {
			list += "#fs11##fc0xFF990033##e [성장패스 " + reward[i][1] +" 레벨 보상]#n \r\n\r\n#b일반 보상 : #i" + reward[i][2] + "# #z" + reward[i][2] + "# " + reward[i][3] + "개 \r\n#fc0xFFFF3300#프리미엄 보상 : #i" + reward[i][4] + "# #z" + reward[i][4] + "# " + reward[i][5] + "개 \r\n#Cgray#――――――――――――――――――――――――――――――――――――――――";
		}
		cm.sendOk(list);
            		cm.dispose();
           		return;
	}
    } else if (St == 2) {
	var sel = reward[mine[selection]];
	var msg = "";
	if (cm.getPlayer().getKeyValue(1234, sel[0]+"-0") == -1) {
		msg += "#b 일반 보상 : #i" + sel[2] + "# #z" + sel[2] + "# " + sel[3] + "개 획득\r\n";
		if (!cm.canHold(sel[2], sel[3])) {
			cm.sendOk("인벤토리에 공간이 부족하다네.");
			cm.dispose();
			return;
		}
		cm.gainItem(sel[2], sel[3]);
		cm.getPlayer().setKeyValue(1234, sel[0]+"-0", "1");
	}
	if (cm.haveItem(4001760)) {
		if (cm.getPlayer().getKeyValue(1234, sel[0]+"-1") == -1) {
			msg += "#fc0xFFFF3300#프리미엄 보상 : #i" + sel[4] + "# #z" + sel[4] + "# " + sel[5] + "개 획득 ";
			if (!cm.canHold(sel[4], sel[5])) {
				cm.sendOk("인벤토리에 공간이 부족하다네.");
				cm.dispose();
				return;
			}
			cm.gainItem(sel[4], sel[5]);
			cm.getPlayer().setKeyValue(1234, sel[0]+"-1", "1");
		}
	}
	cm.sendOk(msg);
            	cm.dispose();
           	return;
    }
}