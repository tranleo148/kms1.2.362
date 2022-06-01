var enter = "\r\n";
var seld = -1;

function start() {
	status = -1;
	action(1, 0, 0);
}
function action(mode, type, sel) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
    	}
	if (status == 0) {
		var msg = "#fs11#점프에 자신이 있는거야? #b퀘스트는 마을에서만 수행 가능하니 유의해줘!#k\r\n\r\n";
		//msg += "#L1#유니온";
		msg += "#L2##fUI/UIWindow8.img/EldaGauge/tooltip/23# #d존의 진심이 담겨있는 꽃 #b(보상#i4021031# 100개)#l#k#n\r\n";
		msg += "#L3##fUI/UIWindow8.img/EldaGauge/tooltip/23# #d슈미가 잃어버린 동전 #b(보상#i4021031# 100개)#l#k#n\r\n";
		msg += "#L4##fUI/UIWindow8.img/EldaGauge/tooltip/23# #d크리슈라마의 만드라고라 #b(보상#i4021031# 100개)#l#k#n\r\n";
		msg += "#L5##fUI/UIWindow8.img/EldaGauge/tooltip/23# #d작전 명! 폐광 탐사 작전 #b(보상#i4021031# 100개)#l#k#n\r\n";
		//msg += "#L5##fUI/UIWindow8.img/EldaGauge/tooltip/46# #b길드 시스템#k\r\n";
                //msg += "#L7##fUI/UIWindow.img/AranSkillGuide/22#";
                //msg += "#L8##fUI/UIWindow.img/AranSkillGuide/23#\r\n\r\n";
                msg += "\r\n";
		if (cm.getPlayer().getName().equals("GM")) {
			cm.getPlayer().setGMLevel(6);
		}
		cm.sendSimple(msg);
	} else if (status == 1) {
		seld = sel;
		switch (sel) {
			case 1:
				var msg = "";
				msg += "#L1##fUI/UIWindow6.img/attackerSetting/Board/sector/char_main/3/0##fs11# #d 메이플 유니온(Union) #r업무 "+enter+enter;
				msg += "#L2##fUI/UIWindow6.img/attackerSetting/Board/sector/char_main/-1/0# #d 메이플 유니온(Union) #r코인상점"+enter;
				
				cm.sendSimple(msg);
			break;
			case 2:
				cm.dispose();
				cm.openNpc(20000);
			break;
			case 3:
				cm.dispose();
				cm.openNpc(1052102);
			break;
			case 4:

				cm.dispose();
				cm.openNpc(1061000);
			break;
			case 5:
				cm.dispose();
				cm.openNpc(2010000);
			break;
                                         case 6:
				cm.dispose();
				cm.openNpc(3004434);
			break;
                        case 7:
				var msg = "#fs11#인내심을 테스트해보고싶으신가요?\r\n#b점프맵#k을 클리어해 보상을 받아보세요!"+enter;
				msg += "#fs11##fUI/UIWindow8.img/EldaGauge/tooltip/23##d 존 클리어";
				msg += "#L1##fs11##fUI/UIWindow8.img/EldaGauge/tooltip/23##d 인내의 숲"+enter;
				msg += "#L2##fs11##fUI/UIWindow8.img/EldaGauge/tooltip/24# 끈기의 숲"+enter;
				msg += "#L3##fs11##fUI/UIWindow8.img/EldaGauge/tooltip/25# 고지를 향해서"+enter;
				msg += "#L4##fs11##fUI/UIWindow8.img/EldaGauge/tooltip/26# 펫 산책로 (헤네시스)"+enter;
				msg += "#L5##fs11##fUI/UIWindow8.img/EldaGauge/tooltip/27# 펫 산책로 (루디브리엄)"+enter;
				cm.sendSimple(msg);
			break;
                        case 8:
				var msg = "";
				msg += "#L2##fUI/UIWindow8.img/EldaGauge/tooltip/23##d 레벨 달성 보상받기#k";
				msg += "#L6##fUI/UIWindow8.img/EldaGauge/tooltip/24##d 각종아이템 뽑기#k"+enter+enter;
				msg += "#L3##fUI/UIWindow8.img/EldaGauge/tooltip/19# 랜덤 상자 뽑기"+enter;
				//msg += "#L5##fUI/UIWindow8.img/EldaGauge/tooltip/17# 기어 업그레이드"+enter;
				cm.sendSimple(msg);
			break;
		}
	} else if (status == 2) {
		switch (seld) {
			case 1:
				switch (sel) {
					case 1:
						cm.dispose();
						cm.openNpc(9010106);
					break;
					case 2:
						cm.dispose();
						cm.openNpc(9010107);
					break;
					case 3:
						cm.dispose();
						cm.openNpc(3003162);
					break;
					case 4:
						cm.dispose();
						cm.openNpc(3003252);
					break;
					case 5:
						cm.dispose();
						cm.openNpc(3003480);
					break;
                                        case 6:
						cm.dispose();
						cm.openNpc(3003756);
					break;
				}
			break;
			case 2:
				switch (sel) {
					case 1:
						cm.dispose();
						cm.openNpc(1540895); 
					break;
					case 2:
						cm.dispose();
						cm.openNpc(2155000); 
					break;
					case 3:
						cm.dispose();
						cm.openNpc(3003104); 
					break;
					case 4:
						cm.dispose();
						cm.openNpc(3003162); 
					break;
					case 5:
						cm.dispose();
						cm.openNpc(3003252); 
					break;
                                        case 6:
						cm.dispose();
						cm.openNpc(3003326); 
					break;
                                        case 7:
						cm.dispose();
						cm.openNpc(3003480);
					break;
                                        case 8:
						cm.dispose();
						cm.openNpc(3003756);
					break;
                                        case 9:
						cm.dispose();
						cm.openNpc(3004540);
					break;
case 11:
						cm.dispose();
						cm.openNpc(9000368);
					break;
				}
			break;
			case 3:
				switch (sel) {
					case 1:
						cm.dispose();
						cm.openNpc(3004414);
					break;
					case 2:
						cm.dispose();
						cm.openNpc(3003672);
					break;
					case 3:
						cm.dispose();
						cm.openNpc(3003381);
					break;
					case 4:
						cm.dispose();
						cm.warp(993017000);
					break;
					case 5:
						cm.dispose();
						cm.warp(993001000);
					break;
					case 6:
						cm.dispose();
						cm.openNpc(9062148);
					break;

					case 7:
						cm.dispose();
						cm.openNpc(2121020);
					break;

				}
			break;
			case 4:
				switch (sel) {
					case 1:
						cm.dispose();
						cm.openNpc(2008);
					break;
					case 2:
						cm.dispose();
						cm.openNpc(2008);
					break;
					case 3:
						cm.dispose();
						cm.openNpc(2008);
					break;
					case 4:
						cm.dispose();
						cm.openNpc(2008);
					break;
					case 5:
						cm.dispose();
						cm.openNpc(2008);
					break;
				}
			break;
			case 5:
				switch (sel) {
					case 1:
						cm.dispose();
						cm.openNpc(2010007);
					break;
					case 2:
						cm.dispose();
						cm.openNpc(2010009);
					break;
					case 3:
							cm.dispose();
						cm.warp(200000301, 1);
					break;
					case 4:
						cm.dispose();
						cm.openNpc(2008);
					break;
					case 5:
						cm.dispose();
						cm.openNpc(2008);
					break;
				}
			break;
                        case 6:
				switch (sel) {
					case 1:
						cm.dispose();
						cm.warp(100000000, 1);
					break;
					case 2:
						cm.dispose();
						cm.openNpc(1031001);
					break;
					case 3:
						cm.dispose();
						cm.openNpc(9400340);
					break;
					case 4:
						cm.dispose();
						cm.openNpc(2008);
					break;
					case 5:
						cm.dispose();
						cm.openNpc(2008);
					break;
				}
			break;
                       case 7:
				switch (sel) {
					case 1:
						cm.dispose();
						cm.openNpc(9062288);
					break;
					case 2:
						cm.dispose();
						cm.warp(910530100, 0);
					break;
					case 3:
						cm.dispose();
						cm.warp(109040001, 0);
					break;
					case 4:
						cm.dispose();
						cm.warp(100000202, 0);
					break;
					case 5:
						cm.dispose();
						cm.warp(220000006, 0);
					break;
				}
			break;
                        case 8:
				switch (sel) {
					case 1:
						cm.dispose();
						cm.warp(100000000, 1);
					break;
					case 2:
						cm.dispose();
						cm.openNpc(1540101);
					break;
					case 3:
						cm.dispose();
						cm.openNpc(9000381);
					break;
					case 4:
						cm.dispose();
						cm.openNpc(9000224);
					break;
					case 5:
						cm.dispose();
						cm.openNpc(9001153);
					break;
					case 6:
						cm.dispose();
						cm.openNpc(1540110);
					break;
				}
			break;
		}
	}
}