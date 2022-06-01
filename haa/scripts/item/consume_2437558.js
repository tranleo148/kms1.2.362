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

		var msg = "#b칸#k 후원포인트 교환권에서\r\n다음과같은 #b보상#k이 나왔습니다.#d\r\n\r\n";
                as = Randomizer.rand(100000, 100000);
		for (i = 0; i < itemlist.length; i ++) {
			cm.gainItem(itemlist[i]['itemid'],itemlist[i]['qty']);
			msg += "#i"+itemlist[i]['itemid']+"##z"+itemlist[i]['itemid']+"# "+itemlist[i]['qty']+"개 \r\n";
		}
msg += "#b후원포인트#k : 100000 #rP#k \r\n";
                cm.getPlayer().gainDonationPoint(as);
                cm.gainItem(2437558, -1);
		cm.sendOk(msg);
		cm.dispose();
	}
}