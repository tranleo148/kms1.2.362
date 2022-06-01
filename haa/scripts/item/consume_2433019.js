importPackage(Packages.packet.creators);
importPackage(Packages.launch.world);
importPackage(Packages.main.world);
importPackage(Packages.tools.RandomStream);
	function start() { Status = -1; action(1, 0, 0); }

	function action(M, T, S) {

	if (M == -1) { cm.dispose(); } else {
		if (M == 0) {cm.dispose(); return; }
		if (M == 1) Status++; else Status--;    

	if(Status == 0) {
	cm.sendYesNo("메소 럭키백을 열어보겠어? 100만 메소에서 1천만 메소 사이의 금액을 획득할 수 있어."
		+"\r\n\r\n#e#r인벤토리 기타 탭의 여유 공간을 2칸 이상 확보해주세요. 여유 공간이 없어서 아이템이 지급되지 않는 경우에 추가 지급이 어렵습니다.")
	}

	else if(Status == 1) {

		if(!cm.haveItem(2433019)) {
		cm.sendOk("#i2433019# #b#z2433019##k이 있다고 우겨서 되는게 아니야. 봐, 네 인벤토리에는 없잖아?");
		cm.dispose();
		} else {
		cm.gainItem(2433019, -1);
		itemSet = Randomizer.rand(1000000,10000000);
		cm.sendOk("축하해 메소 럭키백에서 "+itemSet+"메소를 얻었어. 지금 바로 인벤토리를 확인해 봐.#b"
			+ "\r\n\r\n #fUI/UIWindow.img/QuestIcon/7/0# "+itemSet+" 메소\r\n");
			cm.gainMeso(itemSet);
		if (itemSet >= 7500000) {
		WorldBroadcasting.broadcastMessage(MainPacketCreator.getGMText(20, cm.getPlayer().getName() + "님이 메소 럭키백에서 " + itemSet + " 메소를 얻었습니다 !!"));
		}
		cm.dispose();
		}
	}
	}
}



function Rullet() {
M = Math.floor(Math.random() * 10000);
	switch(M) {
	case 4873: case 9873: N = 4001208; Q = 1;  W = "천만"; break;

	default: N = M*1000; W = N; break;
	}
}