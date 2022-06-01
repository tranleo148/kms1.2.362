importPackage(java.sql);
importPackage(java.util);
importPackage(java.lang);
importPackage(java.io);
importPackage(java.awt);
importPackage(Packages.database);
importPackage(Packages.constants);
importPackage(Packages.client.items);
importPackage(Packages.client.inventory);
importPackage(Packages.server.items);
importPackage(Packages.server);
importPackage(Packages.tools);
importPackage(Packages.server.life);

var itemlist = [
	{'itemid' : 4001395, 'qty' : 1},
	{'itemid' : 2450134, 'qty' : 1},
	{'itemid' : 4001715, 'qty' : 3},
	{'itemid' : 2431341, 'qty' : 1},
	{'itemid' : 5122015, 'qty' : 5},
	{'itemid' : 2520001, 'qty' : 10}

]
var 추뎀 = 0;

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
		var msg = "눈꽃 정령의 상자에서\r\n다음과같은 #b보상#k이 나왔습니다.#d\r\n\r\n";
		for (i = 0; i < itemlist.length; i ++) {
			cm.gainItem(itemlist[i]['itemid'],itemlist[i]['qty']);
			msg += "#i"+itemlist[i]['itemid']+"##z"+itemlist[i]['itemid']+"# "+itemlist[i]['qty']+"개 \r\n";
		}
                cm.gainItem(2430217, -1);
		cm.sendOk(msg);
		cm.dispose();
	}
}