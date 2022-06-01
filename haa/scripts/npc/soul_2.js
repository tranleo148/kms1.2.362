importPackage(Packages.server);
importPackage(java.lang);
importPackage(Packages.handling.world);
importPackage(Packages.tools.packet);

var enter = "\r\n";
var seld = -1;

var need = 2434470, qty = 10;

var basesoul = 2591509; // 제일 첫번째 소울

var special = 2591517; // 위대한 소울
var chance = 100; // 0.1%

var v1 = false;

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
		if (!cm.haveItem(need, qty)) {
			cm.sendOk("#b#i"+need+"##z"+need+"##k을 #b"+qty+"개#k 모아오면 소울로 바꿀 수 있지..");
			cm.dispose();
			return;
		}

		asd = Randomizer.rand(1, 1000);
		if (asd <= chance)
			v1 = true;
		cm.sendYesNo("#b#i"+need+"##z"+need+"##k "+qty+"개를 소비하여 소울 아이템으로 교환할거야?");
	} else if (status == 1) {
		if (!cm.haveItem(need, qty)) {
			cm.sendOk("#b#i"+need+"##z"+need+"##k을 #b"+qty+"개#k 모아오면 소울로 바꿀 수 있지..");
			cm.dispose();
			return;
		}
		cm.gainItem(need, -qty);
		if (v1) {
			cm.gainItem(special, 1);
			cm.sendOk("오오.. #b#i"+special+"##z"+special+"##k이 나왔군! 이건 다른 소울보다 강력한 아이템이니 소중히 간직하도록 하렴");
			World.Broadcast.broadcastMessage(CWvsContext.serverNotice(11, cm.getPlayer().getName()+"님께서 "+cm.getItemName(special)+"을 획득하셨습니다!"));
		} else {
			asd2 = Randomizer.rand(0,7);
			asd3 = basesoul + asd2;

			cm.gainItem(asd3, 1);
			cm.sendOk("오호~! #b#i"+asd3+"##z"+asd3+"##k이 나왔구나! 요긴하게 쓰도록 하렴. 크크크...");
		}
		cm.dispose();
	}
}