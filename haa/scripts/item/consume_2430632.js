importPackage(java.lang);
importPackage(Packages.handling.world);
importPackage(Packages.packet.creators);
importPackage(Packages.tools.RandomStream);

function start()
{
	St = -1;
	action(1, 0, 0);
}

function action(M, T, S)
{
	if(M != 1)
	{	
		cm.dispose();
		return;
	}

	if(M == 1)
	St++;
	else
	St--;

	if(St == 0) {
		var chat = " #b(신중하게 새롭게 시작할 직업군을 선택해보자...)\r\n#r[보조무기 증발방지를 위해 보조무기 장착을 해제해주세요.]\r\n\r\n";
		chat += "#L1#전사류#l　";
		chat += "#L2#법사류#l　";
		chat += "#L3#궁수류#l　";
		chat += "#L4#도적류#l　";
		chat += "#L5#해적류#l　";
		chat += "\r\n#L6##b메소#r로 전환한다.#l";
		chat += "\r\n#L7##b메이플 포인트#r로 전환한다.#l";
		cm.sendSimpleS(chat,2);
	} else if (St == 1) {
		var point = 0
		if (S < 6) {

		if(cm.getPlayer().getJob() == 3122 && cm.getPlayer().getRemainingAp() < 300){
			cm.sendOk("데몬어벤져는 스탯을 초기화 후 진행해주시기를 바랍니다.");
			cm.dispose();
			return;
		}

		if(cm.getPlayer().getStoneP() < 4 || cm.getPlayer().getLevel() < 150) {
			cm.sendOk("5성 이후 레벨 150이상 부터 직업변경이 가능합니다.");
			cm.dispose();
			return;
		}
		if(cm.getPlayer().getStoneP() == 7 && !cm.haveItem(2430632, 3)) {
			cm.sendOk("8성은 직변권이 3개 필요합니다.");
			cm.dispose();
			return;
		}

		if(cm.getPlayer().getStoneP() < 7)
		cm.gainItem(2430632, -1);

		if(cm.getPlayer().getStoneP() == 7)
		cm.gainItem(2430632, -3);

		cm.승급(S,false);
		}

		if (S == 6) {
		point = Randomizer.rand(8000000, 12000000);
		cm.gainMeso(point);
		cm.gainItem(2430632, -1);
		}

		if (S == 7) {
		point = Randomizer.rand(10000, 15000);
		cm.getPlayer().modifyCSPoints(2, point, false);
		cm.gainItem(2430632, -1);
		}

		cm.dispose();
	}
}