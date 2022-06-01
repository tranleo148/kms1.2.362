var status;
importPackage(Packages.server);

function start() {
    status = -1;
    action(1, 1, 0);
}

function action(mode, type, selection) {
    itemlist = [[5062009, 400],[5062010, 300], [5062500, 200],[2435719, 50],[2049700, 5],[2048307, 5],[2630281, 10],[5068300, 10],[2438679, 20],[2438680, 20],[2438687, 10],[2438685, 20],[2438680, 20],[2438684, 10],[2450064, 5]]

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
	if (cm.getClient().getKeyValue(20190324, "sorry") == -1) {
	   cm.sendYesNo("#fs11#최근 잦은 점검 및 서버 초기화로 인해 불편을 겪으신 유저분들께 작은 보상을 마련하였습니다. 지금 상자를 여셔서 보상을 획득하시겠습니까?\r\n\r\n"
		+"#r#e(#fs11#점검 보상 상자는 계정당 1회만 사용하실 수 있으니 신중히 사용해 주세요.)#k#n");
        } else {
	   cm.sendOk("#fs11#점검 보상 상자는 계정당 1회만 사용하실 수 있습니다.");
	   cm.dispose();
	   return;
	}
    } else if (status == 1) {
	talk = "#fs11#아래와 같은 아이템을 획득하셨습니다.\r\n\r\n"
	for (i=0; i<itemlist.length; i++) {
	    cm.gainItem(itemlist[i][0], itemlist[i][1]);
	    talk += "#i"+itemlist[i][0]+"# #b#z"+itemlist[i][0]+"# "+itemlist[i][1]+"개#k\r\n"
	}
	talk+= "#i4031138# #b메소 3억#k\r\n";
	talk+= "#i4310261# #bDEEZ 코인 500개#k";
	cm.gainMeso(300000000);
	cm.getPlayer().AddStarDustCoin(500);
	cm.gainItem(2438682, -1);
	cm.sendNext(talk);
	cm.getClient().setKeyValue(20190324, "sorry", 1);
	cm.dispose();
	return;
    }
}
