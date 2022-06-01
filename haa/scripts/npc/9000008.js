var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
		var jessica = "#fn나눔고딕 Extrabold##rHappy 서버#k의 특별 사냥터!\r\n입장티켓을 내고 #r강화된 사냥터#k에서 혜택을 맛보세요!\r\n\r\n";
		jessica += "#b* 필요 아이템 : #i4033235# #z4033235##k\r\n";
		jessica += "#r* 제한 시간 : 30 분#k\r\n";
		jessica += "#L300##d특별 사냥터 입장하기#k\r\n";
		cm.sendSimple(jessica);
        } else if (status == 2) {
       } else if (selection == 300) {

	if (!cm.haveItem(4033235, 1)) {
	cm.sendOk("#fn나눔고딕 Extrabold##r현재 사냥터에 입장할 재료가 없는거같다.#k");
	cm.dispose();
	} else {
		cm.TimeMoveMap(0, 100000000, 600);
		cm.gainItem(4033235, -1);
		cm.sendOk("#fn나눔고딕 Extrabold#즐거운 사냥 하시기 바랍니다...");
		cm.dispose();

		
}
}
}
}