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
	{'itemid' : 2430044, 'qty' : 1},
	{'itemid' : 4310308, 'qty' : 500},
	{'itemid' : 4310266, 'qty' : 1000},
	{'itemid' : 5068305, 'qty' : 10},
        {'itemid' : 4001716, 'qty' : 30}
	
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

		var msg = " 여름 한정 패키지 에서\r\n다음과같은 #b보상#k이 지급 되었습니다.#d\r\n\r\n";
		for (i = 0; i < itemlist.length; i ++) {
			cm.gainItem(itemlist[i]['itemid'],itemlist[i]['qty']);
			msg += "#i"+itemlist[i]['itemid']+"##z"+itemlist[i]['itemid']+"# "+itemlist[i]['qty']+"개 \r\n";
		}
                cm.gainItem(2430488, -1);
		cm.sendOk(msg);
		cm.dispose();
	}
}