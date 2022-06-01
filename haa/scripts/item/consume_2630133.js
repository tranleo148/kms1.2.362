
var status;
var select = -1;
var itemid  = new Array(1212120, 1222113, 1232113, 1242121, 1262039, 1302343, 1312203, 1322255, 1332279, 1342104, 1362140, 1372228, 1382265, 1402259, 1412181, 1422189, 1432218, 1442274, 1452257, 1462243, 1472265, 1482221, 1492235, 1522143, 1532150, 1582023, 1272017, 1282017
);

function start() {
    status = -1;
    action(1, 1, 0);
}

function action(mode, type, selection) {
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
		cm.sendYesNoS("안녕하세요! 아케인셰이드 무기 상자를 뜯어보시겠어요?",0x24);
	} else if (status == 1) {
		아이템1 = itemid[Math.floor(Math.random() * itemid.length)];
		//아이템2 = itemid[Math.floor(Math.random() * itemid.length)];
		//아이템3 = itemid[Math.floor(Math.random() * itemid.length)];
		cm.gainItem(아이템1, 1);
		//cm.gainItem(아이템2, 1);
		//cm.gainItem(아이템3, 1);
		cm.gainItem(2630133, -1);
                //Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, cm.getPlayer().getName()+"님께서 ["+cm.getItemName(itemid[아이템1])+"] 제작 성공하셨습니다."));
		//cm.cm.sendOkS("다음 아이템이 수령되었습니다:\r\n\r\n#i" + 아이템1 + "##z" + 아이템1 + "#\r\n#i" + 아이템2 + "##z" + 아이템2 + "#\r\n#i" + 아이템3 + "##z" + 아이템3 + "#",0x24);
		cm.dispose();
    	}
}
