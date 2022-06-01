
var status;
var select = -1;
var itemid  = new Array(1212115,1222109,1232109,1242120,1262017,1302333,1312199,1322250,1332274,1342101,1362135,1372222,1382259,1402251,1412177,1422184,1432214,1442268,1452252,1462239,1472261,1482216,1492231,1522138,1532144,1582017,1272016,1282016,1152174,1152176,1152177,1152178,1152179,1102775,1102794,1102795,1102796,1102797,1073030,1073032,1073033,1073034,1073035,1052882,1052887,1052888,1052889,1052890,1082636,1082637,1082638,1082639,1082640,1004422, 1004423, 1004424, 1004425, 1004426);

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
		cm.sendYesNoS("안녕하세요! 앱솔랩스 랜덤상자를 뜯어보시겠어요?",0x24);
	} else if (status == 1) {
		아이템1 = itemid[Math.floor(Math.random() * itemid.length)];
		//아이템2 = itemid[Math.floor(Math.random() * itemid.length)];
		//아이템3 = itemid[Math.floor(Math.random() * itemid.length)];
		cm.gainItem(아이템1, 1);
		//cm.gainItem(아이템2, 1);
		//cm.gainItem(아이템3, 1);
		cm.gainItem(2630291, -1);
                //Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, cm.getPlayer().getName()+"님께서 ["+cm.getItemName(itemid[아이템1])+"] 제작 성공하셨습니다."));
		//cm.cm.sendOkS("다음 아이템이 수령되었습니다:\r\n\r\n#i" + 아이템1 + "##z" + 아이템1 + "#\r\n#i" + 아이템2 + "##z" + 아이템2 + "#\r\n#i" + 아이템3 + "##z" + 아이템3 + "#",0x24);
		cm.dispose();
    	}
}
