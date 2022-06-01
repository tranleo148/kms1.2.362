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
	{'itemid' : 4310261, 'qty' : 10000},
	{'itemid' : 4001715, 'qty' : 100},	
	{'itemid' : 2023287, 'qty' : 5},
        {'itemid' : 2436108, 'qty' : 1},
        {'itemid' : 3994385, 'qty' : 5},
        {'itemid' : 5068305, 'qty' : 5},
	
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
		cm.gainItem(2436720, -1);
		cm.sendOk(msg);
		a = new Date();
        fFile1 = new File("Log/핫타임/핫타임상자 개봉 "+ Number(a.getMonth() + 1)+"월 "+a.getDate()+"일 "+a.getHours()+"시 "+a.getMinutes()+"분 "+a.getSeconds()+"초 " + cm.getPlayer().getName() + ".log");
            if (!fFile1.exists()) {
                fFile1.createNewFile();
            }
        out1 = new FileOutputStream("Log/핫타임/핫타임상자 개봉 "+ Number(a.getMonth() + 1)+"월 "+a.getDate()+"일 "+a.getHours()+"시 "+a.getMinutes()+"분 "+a.getSeconds()+"초 " + cm.getPlayer().getName() + ".log", false);
        var msg = "캐릭터 : " + cm.getPlayer().getName() + "\r\n";
        msg += "개봉 시각 : "+a.getFullYear()+"년 "+Number(a.getMonth() + 1)+"월 "+a.getDate()+"일 "+a.getHours()+"시 "+a.getMinutes()+"분 "+a.getSeconds()+"초\r\n";
        out1.write(msg.getBytes());
        out1.close();
		cm.sendOk(msg);
		cm.dispose();
	}
}