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
                     var msg = "#fs11##fn나눔고딕##fc0xFF000000#현재 #fc0xFF6600CC##h0# #fc0xFF000000#님의 정보 ::\r\n 성장패스 레벨 :#fc0xFFFF3300# " + cm.getPlayer().getKeyValue(100592, "point") + "레벨#l#n#fc0xFF000000#  / 파티포인트 : #r0 P #fc0xFF000000#/ 나의 랭크등급 :#r  " + cm.getPlayer().getKeyValue(190823, "grade") + " 레벨\r\n";
		msg += "                 #L1##fc0xFF990033#[Happy]#fc0xFF6600CC# 육성 다이어리 (필수)#l#n\r\n";
		msg += "                    #L7##fc0xFF990033#[Happy]#fc0xFF6600CC# 성장 패스 이용하기 (필수)#l#n\r\n";
		msg += "               #L103##fc0xFF00B700#슈피겔만의 게스트 하우스로 이동하기#k#l#n\r\n\r\n\r\n";
		msg += "#L8##fUI/UIWindow4.img/pointShop/100296/iconShop# #fc0xFF3162C7#메인 랭크 승급#l #fc0xFF990066##L13##fUI/UIWindow4.img/pointShop/3887/iconShop# 장비 메소강화#l#fc0xFF996600##L11##fUI/UIWindow4.img/pointShop/16393/iconShop# 기본악세 제작#l\r\n";
		msg += "#fc0xFF3162C7##L10##fUI/UIWindow4.img/pointShop/100296/iconShop# 보스 랭크 승급#l #fc0xFF990066##L14##fUI/UIWindow4.img/pointShop/3887/iconShop# 장비 옵션부여#l#fc0xFF996600##L12##fUI/UIWindow4.img/pointShop/16393/iconShop# 연금술 이용#l"+enter;
		msg += "#fc0xFF3162C7##L9##fUI/UIWindow4.img/pointShop/100296/iconShop# 스탯 랭크 승급#l#fc0xFF990066# #L15##fUI/UIWindow4.img/pointShop/3887/iconShop# 캐시 장비강화#l#fc0xFF990066#\r\n"+enter;
		cm.sendSimple(msg);
	} else if (status == 1) {
		seld = sel;
		switch (sel) {
			case 1:
				cm.dispose();
				cm.openNpc(9000368);
			break;
			case 2:
				var msg = "";
                                //msg += "#L9##fs11#  (공용)#r 퀘스트의 전당 이동 #k(서브퀘스트 클리어)#n#k"+enter;
				msg += "#fs12##b#e   [일일 퀘스트]#k#n\r\n"+enter;
				msg += "#L2##fs11##fUI/UIWindow.img/Quest/icon0#  (Lv. 190)#d 헤이븐 - #b긴급 지원#n#k"+enter;
				msg += "#L1##fs11##fUI/UIWindow.img/Quest/icon0#  (Lv. 190)#d 버려진 야영지 - #b어둠 퇴치#n#k"+enter;
				msg += "#L3##fUI/UIWindow.img/Quest/icon0#  (Lv. 200)#d 소멸의여로 - #b소멸의 여로 조사#n#k"+enter;
				msg += "#L4##fUI/UIWindow.img/Quest/icon0#  (Lv. 210)#d 츄츄아일랜드 - #b삐빅의 의뢰#n#k"+enter;
				msg += "#L5##fUI/UIWindow.img/Quest/icon0#  (Lv. 220)#d 레헬른 - #b마을촌장의 부탁#n#k"+enter;
				msg += "#L6##fUI/UIWindow.img/Quest/icon0#  (Lv. 235)#d 아르카나 - #b아르카나의 위기#n#k"+enter;
				msg += "#L7##fUI/UIWindow.img/Quest/icon0#  (Lv. 230)#d 모라스 - #b모라스의 안정을 위해#n#k"+enter;
				msg += "#L8##fUI/UIWindow.img/Quest/icon0#  (Lv. 235)#d 에스페라 - #b에스페라 정화#l#n#k"+enter;
				msg += "#L10##fUI/UIWindow.img/Quest/icon0#  (Lv. 260)#d 세르니움 - #b세르니움 정화#l#n#k\r\n"+enter;
				msg += "#fs12##b#e   [반복 퀘스트]#k#n"+enter;
				msg += "#L9##fs11##fUI/UIWindow.img/Quest/icon0#  잃어버린 크레파스 #b(메소수급)#n#k"+enter;
				cm.sendSimple(msg);
			break;
			case 3:
				cm.dispose();
				cm.openNpcCustom(cm.getClient(), 9062284, "LevelReward");
			break;
			case 4:
/*
				var msg = "";
				msg += "#L1##fs11##fs11##fUI/UIWindow8.img/EldaGauge/tooltip/23# #d레벨 랭킹"+enter;
				msg += "#L2##fs11##fUI/UIWindow8.img/EldaGauge/tooltip/23# 메소 랭킹"+enter;
				msg += "#L3##fs11##fUI/UIWindow8.img/EldaGauge/tooltip/23# 데미지 측정랭킹"+enter;
				msg += "#L4##fs11##fUI/UIWindow8.img/EldaGauge/tooltip/23# 길드 랭킹"+enter;
				msg += "#L5##fs11##fUI/UIWindow8.img/EldaGauge/tooltip/23# 무릉도장 랭킹"+enter;
				cm.sendSimple(msg);
*/
				cm.dispose();
				cm.openNpc(2040050);
			break;
			case 5:
				cm.dispose();
				cm.openNpc(1540110);
			break;
			case 99:
				cm.dispose();
				cm.warp(100030301, 0);
			break;
			case 100:
				cm.dispose();
				cm.warp(100030301, 0);
			break;
			case 103:
				cm.dispose();
				cm.warp(910002000, 0);
			break;
                        case 6:
				cm.dispose();
				cm.openNpc(3004525);
			break;
                        case 7:
				cm.dispose();
				cm.openNpcCustom(cm.getClient(), 9062294, "SeasonPass");
			break;
                        case 8:
				cm.dispose();
				cm.openNpcCustom(cm.getClient(), 9071000, "ZodiacRank");
			break;
                        case 9:
				cm.dispose();
				cm.openNpcCustom(cm.getClient(), 9071000, "StatRank");
			break;
                        case 10:
				cm.dispose();
				cm.openNpcCustom(cm.getClient(), 9071000, "BossRank");
			break;
                        case 11:
				cm.dispose();
				cm.openNpcCustom(cm.getClient(), 2040050, "BaseAcc");
			break;
                        case 12:
				cm.dispose();
				cm.openNpcCustom(cm.getClient(), 2040050, "MakeItem");
			break;
                        case 13:
				cm.dispose();
				cm.openNpcCustom(cm.getClient(), 3004434, "MesoEn");
			break;
                        case 14:
				cm.dispose();
				cm.openNpcCustom(cm.getClient(), 3004434, "BallEn");
			break;
                        case 15:
				cm.dispose();
				cm.openNpcCustom(cm.getClient(), 3004434, "CashEn");
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
						cm.openNpc(2082006);
					break;
                                        case 10:
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
						cm.openNpc(9062453);
					break;
					case 3:
						cm.dispose();
						cm.openNpc(3003381);
					break;
					case 4:
						cm.dispose();
						cm.openNpc(9074300);
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
						cm.warp(680000000, 1);
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
						cm.warp(680000000, 1);
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