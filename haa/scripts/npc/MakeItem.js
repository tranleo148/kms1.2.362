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
		var msg = "#fs11#안녕하세요 #e#b#h ##n#k 님! #b연금술#k에 관심이 있으신가요?\r\n";
		msg += "#fs12##fc0xFFFF9436##e #L9#[소비]#l    #L7#[안드로이드]#l#k#n\r\n\r\n";
		msg += "#L2##fs11##i1132800##b  초월한 보스 장신구#k 제작#l#n\r\n";
		msg += "#L3##fs11##i1004032##b 앱솔루트 이베이젼 헬름#k 제작#l#n\r\n";
		msg += "#L6##fs11# #i1123430##b 카오스 칠흑의 보스 세트#k 제작#l#n\r\n\r\n";
		msg += "#L8##fs11# #i1042392##fc0xFFF781D8# 초월한 파프니르 세트#k 제작#l#n\r\n";
		msg += "#L4##fs11##i1113305##fc0xFFF781D8# 마스테리아의 유산 세트#k 제작#l#n\r\n";

		cm.sendSimple(msg);
	} else if (status == 1) {
		seld = sel;
		switch (sel) {
			case 1:
				cm.dispose();
				cm.openNpcCustom(cm.getClient(), 2040050, "makeitem");
			break;
			case 2:
				cm.dispose();
				cm.openNpcCustom(cm.getClient(), 2040050, "boss_set");
			break;
			case 3:
				cm.dispose();
				cm.openNpcCustom(cm.getClient(), 2040050, "absolhat");
			break;
			case 4:
				cm.dispose();
				cm.openNpcCustom(cm.getClient(), 1540944, "Masteria_set");
			break;
			case 5:
				cm.dispose();
				cm.openNpc(3000002);
			break;
                                         case 6:
				cm.dispose();
				cm.openNpcCustom(cm.getClient(), 2040050, "Chaos_boss");
			break;
                                        case 7:
				cm.dispose();
				cm.openNpcCustom(cm.getClient(), 2560108, "makeand");
			break;
                                        case 8:
				cm.dispose();
				cm.openNpcCustom(cm.getClient(), 2040050, "royalPaf");
			break;
                                         case 9:
				cm.dispose();
				cm.openNpcCustom(cm.getClient(), 2040050, "makeconsume");
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