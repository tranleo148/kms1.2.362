var status = -1;
	move1 = 0;
	move2 = 0;
	move3 = 0;
	h = 0;
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    dal = ["0100100.img","0100101.img","0130101.img"]
ccoin = 4310237; // 코인코드
    if (mode == 1) {
		if (status == 2) {
			if (move1 >= 50 || move2 >= 50 || move3 >= 50) {
				status ++;
			} else {
				move1 += Math.floor(Math.random() * 5);
				move2 += Math.floor(Math.random() * 5);
				move3 += Math.floor(Math.random() * 5);
			}
		} else {
            status++;
		}
    } else {
        cm.dispose();
        return;
    }
    if (status == 0) {
        말 = "#fs11##fn나눔고딕 Extrabold##d 제일 빨리 달리는 몬스터는 누굴까요?!\r\n제일 빠른 몬스터를 맞추시면 #i4310237 Fancy 코인을##r2배#k#d로 드려요!\r\n\r\n#rFancy 코인의 개수를 걸어주세요.)#n#k"
		if (cm.itemQuantity(ccoin) >= 1000) {
			limit = 1000;
		} else {
			limit = cm.itemQuantity(ccoin);
		}
		cm.sendGetNumber(말,1,1,limit);
	} else if (status == 1) {
		st = selection;
		if (st > cm.itemQuantity(ccoin) || st < 0) {
			cm.sendOk("#fn나눔고딕 Extrabold##fs11# #fc0xFF000000# 오류가 발생하였습니다.");
			cm.dispose();
			return;
		}
		말 = "#fs11##fn나눔고딕 Extrabold# #d 원하시는 몬스터를 선택해 주세요.\r\n\r\n"
		for (i=0; i<dal.length; i++) {
			말 += "#L"+i+"# #fMob/"+dal[i]+"/move/0#";
		}
		cm.sendSimple(말);
	} else if (status == 2) {
		if (h == 0) {
			st2 = selection;
			cm.gainItem(ccoin,-st);
		}
		h++;
		말 = "#fs11# #fc0xFF000000# 몬스터 레이스 "+h+"회차입니다.\r\n"
		말+= "#fs11# #fc0xFF000000# #r계속하시려면 엔터를 눌러주세요. ESC를 누를시 결과와 상관없이 코인이 지급되지 않습니다.#k#n\r\n\r\n";
		for (i=0; i<move1; i++) {
			말 += " ";
		}
		말+= "#fMob/"+dal[0]+"/move/0#\r\n"
		for (i=0; i<move2; i++) {
			말 += " ";
		}
		말+= "#fMob/"+dal[1]+"/move/0#\r\n"
				for (i=0; i<move3; i++) {
			말 += " ";
		}
		말+= "#fMob/"+dal[2]+"/move/0#\r\n\r\n"
		cm.sendOk(말);
	} else if (status == 3) {
		if (move1 >= move2 && move1 >= move3) {
			win = 0;
		}
		if (move2 >= move1 && move2 >= move3) {
			win = 1;
		}
		if (move3 >= move1 && move3 >= move2) {
			win = 2;
		}
		if (st2 == win) {
			cm.sendOk("#fn나눔고딕 Extrabold##fs11##d 승리하셨습니다. 코인을 2배로 드릴게요!");
			cm.gainItem(ccoin, st*2);
		} else {
			cm.sendOk("#fs11##fn나눔고딕 Extrabold##d 패배하셨습니다.");
		}
		cm.dispose();
    }
}