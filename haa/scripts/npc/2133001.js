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
		if (cm.getPlayer().getMapId() == 930000300) {
			if (!checkPos(cm)) {
				cm.getPlayer().dropMessage(5, "거리가 너무 멀어 대화를 걸 수 없다.");
				cm.dispose();
				return;
			} else {
				cm.sendYesNo("휴우... 다행히 여기까지 왔구나. 안개 때문에 널 찾지 못해서 당황하고 있었어. 그럼 더 깊은 숲으로 갈래? 네가 가면 너의 파티원들도 마법으로 함께 보내줄게.");
			}
		} else if (cm.getPlayer().getMapId() == 930000400 && cm.getPlayer().getMap().getMonstermarble() != 20) {
			talk ="지금은 중독되어 버렸지만, 원래 스프라이트는 숲의 주민이었어. 몬스터를 정화할 수 있도록 중독된 스프라이트를 사냥하여 몬스터 구슬 20개를 모아줘.\r\n";
			talk +="#L0##b이곳에서 나가고 싶어요.#k\r\n";
			cm.sendSimple(talk);
		} else if(cm.getPlayer().getMapId() == 930000400 && cm.getPlayer().getMap().getMonstermarble() == 20 && cm.isLeader() && cm.getPlayer().getMap().getMonstermarble() == 20){
			cm.sendNext("몬스터 구슬을 모두 구해왔구나 좋아, 그럼 정화된 스프라이트를 한 명 깨워볼게! 더 깊은 숲의 상황은 그 녀석이 저 잘 알 테니, 녀석의 말을 따라줘!");
			cm.getPlayer().getMap().spawnNpc(2133004, new java.awt.Point(-282, -245));
			cm.getPlayer().getMap().setMonstermarble(cm.getPlayer().getMap().getMonstermarble()+1);
			cm.dispose();
			return;
		} else {
			talk ="우리들을 도와주지 않고 나갈 생각이야?\r\n";
			talk +="#L0##b이곳에서 나가고 싶어요.#k\r\n";
			cm.sendSimple(talk);
		}
	} else if(St == 1){
		if (cm.getPlayer().getMapId() == 930000300) {
			cm.resetMap(930000400);
			cm.warpParty(930000400);
			cm.getPlayer().getMap().setMonstermarble(0);
			cm.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.achievementRatio(45));
			cm.dispose();
			return;
		} else if (cm.getPlayer().getMapId() == 930000400 && cm.getPlayer().getMap().getMonstermarble() != 20) {
			cm.warp(300030010);
			cm.dispose();
			return;
		} else {
			cm.warp(300030010);
			cm.dispose();
			return;
		}
	}
}

function checkPos(cm) {
    var ltx = 861;
    var lty = 1069;
    var rbx = 1311;
    var rby = 1230;
    var curx = cm.getPlayer().getPosition().getX();
    var cury = cm.getPlayer().getPosition().getY();
    return curx >= ltx && cury >= lty && curx <= rbx && cury <= rby;
}