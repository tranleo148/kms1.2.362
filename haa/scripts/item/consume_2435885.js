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
	{'itemid' : 2450130, 'qty' : 2},
	{'itemid' : 3994097, 'qty' : 1},
	{'itemid' : 2049704, 'qty' : 1},
	{'itemid' : 2431486, 'qty' : 1},
	{'itemid' : 5064400, 'qty' : 10},
	{'itemid' : 4001716, 'qty' : 1},
	{'itemid' : 4001715, 'qty' : 3},
	{'itemid' : 5062500, 'qty' : 50},
	{'itemid' : 5062009, 'qty' : 100},
	{'itemid' : 2435719, 'qty' : 10},
	{'itemid' : 4021031, 'qty' : 50},
	{'itemid' : 4310065, 'qty' : 100},
	{'itemid' : 2630281, 'qty' : 1},
	{'itemid' : 5121060, 'qty' : 1}
	
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
		후포 = Randomizer.rand(30000, 30000);
                cm.getPlayer().gainDPoint(후포);
		var msg = "베타 핫타임상자에서\r\n다음과같은 #b보상#k이 나왔습니다.#d\r\n\r\n";
		msg += "후원 포인트 +"+후포+"\r\n";
		for (i = 0; i < itemlist.length; i ++) {
			cm.gainItem(itemlist[i]['itemid'],itemlist[i]['qty']);
			msg += "#i"+itemlist[i]['itemid']+"##z"+itemlist[i]['itemid']+"# "+itemlist[i]['qty']+"개 \r\n";
		}
                cm.gainItem(2435885, -1);
		cm.sendOk(msg);
		cm.dispose();
	}
}