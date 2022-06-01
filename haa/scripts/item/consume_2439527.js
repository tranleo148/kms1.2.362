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
	{'itemid' : 2450134, 'qty' : 1},
	{'itemid' : 2431486, 'qty' : 1},
	{'itemid' : 4001716, 'qty' : 1},
	{'itemid' : 4036531, 'qty' : 1},
	{'itemid' : 5062500, 'qty' : 50},
	{'itemid' : 4021031, 'qty' : 50},
	{'itemid' : 2435719, 'qty' : 50},
	{'itemid' : 5068303, 'qty' : 2},
	{'itemid' : 2439653, 'qty' : 1},
	{'itemid' : 2435748, 'qty' : 2}
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
		var msg = "#b칸[葉]#k의 핫타임상자에서\r\n다음과같은 #b보상#k이 나왔습니다.#d\r\n\r\n";
		for (i = 0; i < itemlist.length; i ++) {
			cm.gainItem(itemlist[i]['itemid'],itemlist[i]['qty']);
			msg += "#i"+itemlist[i]['itemid']+"##z"+itemlist[i]['itemid']+"# "+itemlist[i]['qty']+"개 \r\n";
		}
                cm.gainItem(2439527, -1);
		cm.sendOk(msg);
		cm.dispose();
	}
}