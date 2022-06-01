

var enter = "\r\n";
var seld = -1, seld2 = -1;

var key = "thanks_"

var items = [
    {'itemid' : 2435719, 'qty' : 200, 'select' : false},
    {'itemid' : 2630281, 'qty' : 50, 'select' : false},
    {'itemid' : [5002079, 5002080, 5002081, 5000930, 5000931, 5000932], 'name' : "자석 펫 선택", 'qty' : 1, 'select' : true},
    {'itemid' : 5062009, 'qty' : 300, 'select' : false},
    {'itemid' : 5062010, 'qty' : 300, 'select' : false},
    {'itemid' : 5062500, 'qty' : 300, 'select' : false},
    {'itemid' : 4310291, 'qty' : 2000, 'select' : false},
    {'itemid' : [2438698, 2438699, 2438700, 2438701], 'name' : "스페셜 캐시 랜덤 상자 선택",  'qty' : 1, 'select' : true}
]

var canuse = -1;

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
	canuse = items.length;
        for (i = 0; i < items.length; i++) {
		if (cm.getClient().getKeyValue(key+i) != null)
			canuse--;
	}

        var msg = "#e#b디에즈 초기화 보상상자#n#k 입니다.\r\n생성된 계정당 #r#e1회#n#k만 사용가능하니 신중히 선택해주세요."+enter;
        for (i = 0; i < items.length; i++) {
		//cm.getClient().setKeyValue(key+i, null);
            if (items[i]['select']) {
                if (cm.getClient().getKeyValue(key+i) == null)
                    msg += "#L"+i+"##b#i"+items[i]['itemid'][0]+"#"+items[i]['name']+" (수령가능)#k"+enter;
                else
                    msg += "#L"+i+"##i"+items[i]['itemid'][0]+"#"+items[i]['name']+" (수령완료)"+enter;
            } else {
                if (items[i]['itemid'] == 4310291) {
                    if (cm.getClient().getKeyValue(key+i) == null)
                        msg += "#L"+i+"##b#i4310291#DEEZ 코인 "+items[i]['qty']+"개 (수령가능)#k"+enter;
                    else
                        msg += "#L"+i+"##b#i4310291#DEEZ 코인 "+items[i]['qty']+"개 (수령완료)#k"+enter;
                } else {
                    if (cm.getClient().getKeyValue(key+i) == null)
                        msg += "#L"+i+"##b#i"+items[i]['itemid']+"##z"+items[i]['itemid']+"# "+items[i]['qty']+"개 (수령가능)#k"+enter;
                    else
                        msg += "#L"+i+"##i"+items[i]['itemid']+"##z"+items[i]['itemid']+"# "+items[i]['qty']+"개 (수령완료)"+enter;
                }
            }
        }

        cm.sendSimple(msg);
    } else if (status == 1) {
        seld = sel;
        if (cm.getClient().getKeyValue(key+seld) != null) {
            cm.sendOk("이미 수령한 적 있는 보상입니다.");
            cm.dispose();
            return;
        }
        if (items[seld]['select']) {
            var msg = "어떤 아이템을 받을지 선택해주세요.#b"+enter;
            for (i = 0; i < items[seld]['itemid'].length; i++) {
                msg += "#L"+i+"##i"+items[seld]['itemid'][i]+"##z"+items[seld]['itemid'][i]+"# "+items[seld]['qty']+"개"+enter;
            }

            cm.sendSimple(msg);
        } else {
            if (items[seld]['itemid'] == 4310291) {
                cm.sendYesNo("정말 DEEZ 코인 "+items[seld]['qty']+"개를 이 캐릭터에서 수령하시겠습니까?");
            } else
                cm.sendYesNo("정말 #i"+items[seld]['itemid']+"##b#z"+items[seld]['itemid']+"# "+items[seld]['qty']+"개#k를 이 캐릭터에서 수령하시겠습니까?");
        }
    } else if (status == 2) {
        if (cm.getClient().getKeyValue(key+seld) != null) {
            cm.sendOk("이미 수령한 적 있는 보상입니다.");
            cm.dispose();
            return;
        }
        if (items[seld]['select']) {
            seld2 = sel;
            cm.sendYesNo("정말 #i"+items[seld]['itemid'][seld2]+"##b#z"+items[seld]['itemid'][seld2]+"# "+items[seld]['qty']+"개#k를 이 캐릭터에서 수령하시겠습니까?");
        } else {
		if (!cm.canHold(items[seld]['itemid'], items[seld]['qty'])) {
			if (items[seld]['itemid'] != 4310291) {
				cm.sendOk("인벤토리에 공간을 확보해주세요.");
				cm.dispose();
				return;
			}
		}
            if (items[seld]['itemid'] == 4310291) {
                cm.getPlayer().AddStarDustCoin(items[seld]['qty']);
            } else
                cm.gainItem(items[seld]['itemid'], items[seld]['qty']);

		if ((canuse - 1) == 0) {
			cm.gainItem(2431549, -1);
		}
            cm.getClient().setKeyValue(key+seld, "1");
            cm.sendOk("수령이 완료되었습니다.");
            cm.dispose();
        }
    } else if (status == 3) {
        if (cm.getClient().getKeyValue(key+seld) != null) {
            cm.sendOk("이미 수령한 적 있는 보상입니다.");
            cm.dispose();
            return;
        }
		if (!cm.canHold(items[seld]['itemid'][seld2], items[seld]['qty'])) {
			if (items[seld]['itemid'] != 4310291) {
				cm.sendOk("인벤토리에 공간을 확보해주세요.");
				cm.dispose();
				return;
			}
		}
	if (Math.floor(items[seld]['itemid'][seld2] / 10000) == 500)
		Packages.server.MapleInventoryManipulator.addId_Item(cm.getClient(), items[seld]['itemid'][seld2], 1, "", Packages.client.inventory.MaplePet.createPet(items[seld]['itemid'][seld2], -1), 30, "", false);
	else
        	cm.gainItem(items[seld]['itemid'][seld2], items[seld]['qty']);

		if ((canuse - 1) == 0) {
			cm.gainItem(2431549, -1);
		}
        cm.getClient().setKeyValue(key+seld, "1");
        cm.sendOk("수령이 완료되었습니다.");
        cm.dispose();

    }
}