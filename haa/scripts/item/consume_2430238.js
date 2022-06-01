/*
제작자 : 퐁퐁(pongpong___@naver.com / unfix_@naver.com)
*/
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
	if (cm.getPlayer().getMapId() == 931000312) {
	cm.sendOk("감옥맵에선 사용할수 없습니다");
	cm.dispose()
	} else {
            var chat = "이동하실 맵을 선택해주세요.";
            chat += "\r\n#L99#헤네시스";
            chat += "\r\n#L1#리스항구";
            chat += "\r\n#L2#엘리니아";
            chat += "\r\n#L3#커닝시티";
            chat += "\r\n#L4#노틸러스";
            chat += "\r\n#L5#페리온";
            chat += "\r\n#L6#슬리피우드";
            chat += "\r\n#L7#여섯갈래길";
            chat += "\r\n#L8#오르비스";
            chat += "\r\n#L9#엘나스";
            chat += "\r\n#L10#루디브리엄";
            chat += "\r\n#L11#리프레";
            chat += "\r\n#L12#리엔";
            chat += "\r\n#L13#아리안트";
            chat += "\r\n#L14#마가티아";
            chat += "\r\n#L15#아쿠아리움";
            chat += "\r\n#L16#무릉";
            chat += "\r\n#L17#백초마을";
            chat += "\r\n#L18#에델슈타인";
            chat += "\r\n#L19#에레브";
            chat += "\r\n#L20#미래의 문";
            chat += "\r\n#L21#생명의동굴";
            chat += "\r\n#L22#시계탑의 근원";
            chat += "\r\n#L23#피아누스의 동굴";
            chat += "\r\n#L24#자쿰의 제단 입구";
            chat += "\r\n#L25#칸스 자쿰의 제단 입구";
            cm.sendSimple(chat);
}
            } else if (selection == 0) {
		cm.gainItem(2430238, -1);
                 cm.warp(209000000);
                cm.dispose();
            } else if (selection == 99) {
		cm.gainItem(2430238, -1);
                 cm.warp(209000000);
                cm.dispose();
            } else if (selection == 1) {
		cm.gainItem(2430238, -1);
                cm.warp(104000000);
                cm.dispose();
            } else if (selection == 2) {
		cm.gainItem(2430238, -1);
                cm.warp(101000000);
                cm.dispose();
            } else if (selection == 3) {
		cm.gainItem(2430238, -1);
                cm.warp(103000000);
                cm.dispose();
            } else if (selection == 4) {
		cm.gainItem(2430238, -1);
                cm.warp(120000000);
                cm.dispose();
            } else if (selection == 5) {
		cm.gainItem(2430238, -1);
                cm.warp(102000000);
                cm.dispose();
            } else if (selection == 6) {
		cm.gainItem(2430238, -1);
                cm.warp(105000000);
                cm.dispose();
            } else if (selection == 7) {
		cm.gainItem(2430238, -1);
                cm.warp(104020000);
                cm.dispose();
            } else if (selection == 8) {
		cm.gainItem(2430238, -1);
                cm.warp(200000000);
                cm.dispose();
            } else if (selection == 9) {
		cm.gainItem(2430238, -1);
                cm.warp(211000000);
                cm.dispose();
            } else if (selection == 10) {
		cm.gainItem(2430238, -1);
                cm.warp(220000000);
                cm.dispose();
            } else if (selection == 11) {
		cm.gainItem(2430238, -1);
                cm.warp(240000000);
                cm.dispose();
            } else if (selection == 12) {
		cm.gainItem(2430238, -1);
                cm.warp(140000000);
                cm.dispose();
            } else if (selection == 13) {
		cm.gainItem(2430238, -1);
                cm.warp(260000000);
                cm.dispose();
            } else if (selection == 14) {
		cm.gainItem(2430238, -1);
                cm.warp(261000000);
                cm.dispose();
            } else if (selection == 15) {
		cm.gainItem(2430238, -1);
                cm.warp(230000000);
                cm.dispose();
            } else if (selection == 16) {
		cm.gainItem(2430238, -1);
                cm.warp(250000000);
                cm.dispose();
            } else if (selection == 17) {
		cm.gainItem(2430238, -1);
                cm.warp(251000000);
                cm.dispose();
            } else if (selection == 18) {
		cm.gainItem(2430238, -1);
                cm.warp(310000000);
                cm.dispose();
            } else if (selection == 19) {
		cm.gainItem(2430238, -1);
                cm.warp(130000000);
                cm.dispose();
            } else if (selection == 20) {
                if (cm.getPlayer().getLevel() >= 160) {
		cm.gainItem(2430238, -1);
                cm.warp(271000000);
                cm.dispose();
		 }else {
		cm.sendOk("죄송하지만 미래의문은 160레벨 이상만 이동이 가능하답니다");
		cm.dispose();
}
	    } else if (selection == 21) {
			if (cm.haveItem(4000267,1) && cm.haveItem(4001078,1) && cm.haveItem(4001076,1) && cm.haveItem(2430457,1) && cm.haveItem(4001075,1) && cm.haveItem(4001077,10) && cm.getLevel() <120) {
            	cm.gainItem(2430457, -1);
	    	cm.gainItem(4001075, -1);
	    	cm.gainItem(4001077, -10);
	    	cm.gainItem(4001076, -1);
	    	cm.gainItem(4001078, -1);
	    	cm.gainItem(4000267, -1);
	    	cm.gainItem(2430238, -1);
		cm.warp(240060200);
		cm.dispose();
		    } else {
		        cm.sendOk("이동하시려면 혼테일 의 입장재료 6개와 레벨 120이 넘야아 합니다.");
		        cm.dispose();
}
	    } else if (selection == 22) {
		cm.gainItem(2430238, -1);
		cm.warp(220080001);
		cm.dispose();
	    } else if (selection == 23) {
		cm.gainItem(2430238, -1);
		cm.warp(230040420);
		cm.dispose();
	    } else if (selection == 24) {
		if (cm.getPlayer().getLevel() >120) {
		cm.gainItem(2430238, -1);
		cm.warp(211042400);
		cm.dispose();
		} else {
		cm.sendOkS("난 아직 120레벨이 안되니 갈수 없어",2);
		cm.dispose();
}
	    } else if (selection == 25) {
		if (cm.getPlayer().getLevel() >120) {
		cm.gainItem(2430238, -1);
		cm.warp(211042401);
		cm.dispose();
		} else {
		cm.sendOkS("난 아직 120레벨이 안되니 갈수 없어",2);
		cm.dispose();
}
		}
}
	}
