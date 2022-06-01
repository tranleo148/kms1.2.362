importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server);
importPackage(Packages.scripting);

var status = -1;
var npc1 = 0;
var check = false;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
		return;
	}
	if (mode == 0) {
		status--;
	}
	if (mode == 1) {
		d = status;
		status++;
	}

	if (status == 0) {
		cm.sendNextS("아! 살랑살랑~ 포근한 봄바람~\r\n#b#h0##k님은 블루밍 스킬이 처음이시군여?!", 4, 9062508);
	} else if (status == 1) {
		cm.sendNextPrevS("#r#e<블루밍 스킬>#n#k은 블루밍 축복을 받은 분들만\r\n사용하실 수 있는 #r특별한 능력치#k 스킬이에여!", 4, 9062508);
	} else if (status == 2) {
		cm.sendNextPrevS("꽃의 정령들과 함께 따뜻한 #b햇살#k을 충분히 모아서\r\n아직 잠들어 있는 #b꽃을 활짝#k 피워주실 때마다~?\r\n\r\n블루밍 축복으로 #r#e1 블루밍 스킬 포인트#k#n를 드릴게여!", 4, 9062508);
	} else if (status == 3) {
		cm.sendNextPrevS("그리고 #r#e세 번째 꽃#k#n이 필 때마다 #b더 많은 스킬 포인트#n#k를\r\n드리지여!\r\n\r\n#r※ 블루밍 플라워 진행 시 1/1/3개의 블루밍 스킬 포인트를 받을 수 있습니다.", 4, 9062508);
	} else if (status == 4) {
		cm.sendNextPrevS("원하는 능력치 스킬을 선택해서 #b블루밍 스킬 포인트#k를 사용하면 #b스킬 레벨#k을 올릴 수 있어여!", 4, 9062508);
	} else if (status == 5) {
		cm.sendNextPrevS("모든 스킬은 최대 #r#e3레벨#n#k까지 올릴 수 있고,\r\n스킬 레벨이 오를수록 업그레이드에 #b필요한 스킬 포인트가 점점 많아져여!#k\r\n\r\n\r\n#e[블루밍 스킬 업그레이드]\r\n - 1 레벨 : #r1#k 포인트 필요\r\n - 2 레벨 : #r2#k 포인트 필요\r\n - 3 레벨 : #r3#k 포인트 필요#n", 4, 9062508);
	} else if (status == 6) {
		cm.sendNextPrevS("한 번 사용한 #r스킬 포인트#k는 다시 #r돌려받을 수 없으니#k,\r\n#b#h0##k님께 가장 필요한 스킬부터 올려보세여~!", 4, 9062508);
	} else if (status == 7) {
		cm.getPlayer().setKeyValue(501378, "tuto", "1");
		cm.getClient().setKeyValue("BloomingSkilltuto", "1");
		cm.dispose();
		cm.openNpc(2007, "BloomingForest_Skill");
	}
}