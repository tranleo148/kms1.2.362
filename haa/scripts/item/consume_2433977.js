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
	{'itemid' : 2450130, 'qty' : 3},
	{'itemid' : 2049704, 'qty' : 3},
	{'itemid' : 2431486, 'qty' : 3},
	{'itemid' : 4001715, 'qty' : 10},
	{'itemid' : 2431341, 'qty' : 5},
	{'itemid' : 2630755, 'qty' : 1},
	{'itemid' : 2439653, 'qty' : 2},
	{'itemid' : 2435719, 'qty' : 50},
	{'itemid' : 5068300, 'qty' : 2},
	{'itemid' : 4310065, 'qty' : 100},
	{'itemid' : 2630281, 'qty' : 10},
	{'itemid' : 4310218, 'qty' : 3},
	{'itemid' : 5060048, 'qty' : 3},
	{'itemid' : 5068304, 'qty' : 3}
	
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
		cm.gainItem(2433977, -1);
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