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
	{'itemid' : 5068305, 'qty' : 1},
        {'itemid' : 4310261, 'qty' : 1000},
        {'itemid' : 5068304, 'qty' : 3},
	{'itemid' : 2630281, 'qty' : 1}
	
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
		//후포 = Randomizer.rand(30000, 30000);
                //cm.getPlayer().gainDPoint(후포);
		var msg = "#b칸[葉]#k의 베타 특별 상자에서\r\n다음과같은 #b보상#k이 나왔습니다.#d\r\n\r\n";
		//msg += "후원 포인트 +"+후포+"\r\n";
		for (i = 0; i < itemlist.length; i ++) {
			cm.gainItem(itemlist[i]['itemid'],itemlist[i]['qty']);
			msg += "#i"+itemlist[i]['itemid']+"##z"+itemlist[i]['itemid']+"# "+itemlist[i]['qty']+"개 \r\n";
		}
                cm.gainItem(2439532, -1);
		cm.sendOk(msg);
		cm.dispose();
	}
}