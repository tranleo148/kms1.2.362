importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.util);


function start() {
	St = -1;
	action(1, 0, 0);
}

function action(M, T, S) {
	if(M != 1) {
		cm.dispose();
		return;
	}

	if(M == 1)
	    St++;

	if(St == 0) {
		if(cm.getPlayer().getMapId() == 930000400 && cm.getPlayer().getMap().getMonstermarble() == 21){
			cm.sendNext("구해주셔서 감사합니다용! 지금까지는 독 때문에 괴인의 수하로 지냈습니다용! 하지만 그 덕분에 괴인의 행동을 보아왔으니, 여러분들을 안내드릴 수 있습니다용! 자, 그럼 더 깊은 숲으로 보내드릴게용!");
		} else if(cm.getPlayer().getMapId() == 930000500 && !cm.haveItem(4001163, 1)){
			cm.sendOk("#fs11#저 옆에 있는 독나무에서 만들어지는 보라색 마력석으로 괴인은 뭔가 연구를 하곤 했습니다용! 아마 만들어진 마력석은 괴인의 책상에 있을 겁니다용! 보라색 마력석을 가져와 주세용!");
			cm.dispose();
		} else if(cm.getPlayer().getMapId() == 930000500 && cm.haveItem(4001163, 1)){
			cm.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.environmentChange("Map/Effect.img/quest/party/clear", 4));
			cm.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.achievementRatio(75));
			cm.sendNext("보라색 마력석을 가져오셧군요 이제 괴인이 있는곳으로 이동시켜드릴게요.");
		}
	} else if(St == 1){
		if (cm.getPlayer().getMapId() == 930000400 && cm.getPlayer().getMap().getMonstermarble() == 21) {
			cm.resetMap(930000500);
			cm.warpParty(930000500);
			cm.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.achievementRatio(50));
			cm.getPlayer().getMap().setMonstermarble(0);
			cm.dispose();
			return;
		} else {
			cm.resetMap(930000600);
			cm.warpParty(930000600);
			cm.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.achievementRatio(75));
			cm.dispose();
			return;
		}
	}
}