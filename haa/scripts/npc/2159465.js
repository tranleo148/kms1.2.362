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

        var Lcoin = cm.itemQuantity(4033831);
	var chat = "#fn나눔고딕 Extrabold#현재 #b#h0##k 님이 획득하신 증표는 #r"+ Lcoin +" 개#k 입니다.#k\r\n";
	chat += "#L9999#그럼 아래에서 당신의 가치를 #d선택#k 해보세요.\r\n";
        chat += "\r\n#L1000#<수련의 증표 [#r100 개#k] 로 구매 가능한 아이템>\r\n\r\n";
	chat += "#L1# #i1190300# #b#t1190300##k\r\n　　#d올스탯 300 / 공, 마 20#k (장비 1 칸 필수)\r\n\r\n";

        chat += "\r\n#L1001#<수련의 증표 [#r200 개#k] 로 구매 가능한 아이템>\r\n\r\n";
	chat += "#L2# #i1190301# #b#t1190301##k\r\n　　#d올스탯 400 / 공, 마 40#k (장비 1 칸 필수)\r\n\r\n";

        chat += "\r\n#L1002#<수련의 증표 [#r300 개#k] 로 구매 가능한 아이템>\r\n\r\n";
	chat += "#L3# #i1190302# #b#t1190302##k\r\n　　#d올스탯 500 / 공, 마 60#k (장비 1 칸 필수)\r\n\r\n";

        cm.sendSimple(chat);

	    }  if (selection >= 999) {
		    cm.dispose();

	    } else if (selection == 1) {
		if (cm.haveItem(4033831, 100)) {
		    if (cm.canHold(1190300)) {
			cm.gainItem(4033831, -100);
			cm.setAllStat(1190300, 300, 20, 0);
			cm.warp(100030301,0);
                        cm.removeAll(4033831);
		        cm.sendOk("#fn나눔고딕 Extrabold##b수련의 증표#k 로 #i1190300# #r1 개#k 를 교환 하셨습니다.\r\n당신의 #r한계#k 는 매우.. 평범하네요..");
			cm.showEffect(false,"monsterPark/clear");
			cm.playSound(false,"Field.img/Party1/Clear");
			cm.dispose();
		    } else {
		        cm.sendOk("#fn나눔고딕 Extrabold##r장비칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fn나눔고딕 Extrabold##b수련의 증표#k 가 부족하신데요?\r\n제대로 #r한계#k 를 극복하신게 맞으신건가요..?");
		    cm.dispose();
}
	    } else if (selection == 2) {
		if (cm.haveItem(4033831, 200)) {
		    if (cm.canHold(1190301)) {
			cm.gainItem(4033831, -200);
			cm.setAllStat(1190301, 400, 40, 0);
			cm.warp(100030301,0);
                        cm.removeAll(4033831);
		        cm.sendOk("#fn나눔고딕 Extrabold##b수련의 증표#k 로 #i1190301# #r1 개#k 를 교환 하셨습니다.\r\n당신의 #r한계#k 는 나름.. 봐줄만하군요..");
			cm.showEffect(false,"monsterPark/clear");
			cm.playSound(false,"Field.img/Party1/Clear");
			cm.dispose();
		    } else {
		        cm.sendOk("#fn나눔고딕 Extrabold##r장비칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fn나눔고딕 Extrabold##b수련의 증표#k 가 부족하신데요?\r\n제대로 #r한계#k 를 극복하신게 맞으신건가요..?");
		    cm.dispose();
}
	    } else if (selection == 3) {
		if (cm.haveItem(4033831, 300)) {
		    if (cm.canHold(1190302)) {
			cm.gainItem(4033831, -300);
			cm.setAllStat(1190302, 500, 60, 0);
			cm.warp(100030301,0);
                        cm.removeAll(4033831);
		        cm.sendOk("#fn나눔고딕 Extrabold##b수련의 증표#k 로 #i1190302# #r1 개#k 를 교환 하셨습니다.\r\n당신의 #r한계#k 는 끝이 없군요.. 대단하세요..!!");
			cm.showEffect(false,"monsterPark/clear");
			cm.playSound(false,"Field.img/Party1/Clear");
			cm.dispose();
		    } else {
		        cm.sendOk("#fn나눔고딕 Extrabold##r장비칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
		} else {
		    cm.sendOk("#fn나눔고딕 Extrabold##b수련의 증표#k 가 부족하신데요?\r\n제대로 #r한계#k 를 극복하신게 맞으신건가요..?");
		    cm.dispose();
}
		}
	}
}
