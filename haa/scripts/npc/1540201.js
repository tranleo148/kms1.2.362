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
		var msg = "#fn나눔고딕 Extrabold##r<미니게임 배틀리버스>#k\r\n배틀리버스 한판 하시겠어요? #b2인#k 파티 후 파티장이 #b입장하기#k를 누르시면 됩니다.\r\n"+enter;
		msg += "#L3#미니게임 배틀리버스 입장하기#k"+enter;
		cm.sendSimple(msg);
	} else if (status == 1) {
		seld = sel;
		switch (sel) {
			case 1:
				cm.dispose();
				cm.EnterCS;
				break;
			//case 2:
			//	cm.dispose();
			//			cm.openNpc(1540945);
			//		break;
			case 3:
				cm.dispose();
				if (cm.isLeader()) {
					if (cm.getPlayer().getParty().getMembers().size() >= 1 && cm.getPlayer().getParty().getMembers().size() <= 2) {
						Packages.server.games.BattleReverse.addQueue(cm.getPlayer(), true);
					} else {
						cm.sendOk("3, 4인 파티의 파티장만이 시도하실 수 있습니다.");
					}
				} else {
					cm.sendOk("3, 4인 파티의 파티장만이 시도하실 수 있습니다.");
				}
				break;
			case 4:
				var msg = "#e#b[아케인 리버 일일 퀘스트]#k#n"+enter;
				msg +="#L1#1. 소멸의 여로"+enter;
				msg +="#L2#2. 츄츄 아일랜드"+enter;
				msg +="#L3#3. 레헬른"+enter;
				msg +="#L4#4. 아르카나"+enter;
				msg +="#L5#5. 모라스"+enter;
				msg +="#L6#6. 에스페라"+enter;
				cm.sendSimple(msg);

				break;
			case 5:
				cm.dispose();
				cm.warp(926010000);
				break;
			case 6:
				var msg = "#e#b[디에즈 커플 시스템]#k#n"+enter;

				msg +="#L1#하우스 웨딩#b(준비 중 입니다.)"+enter+enter;
				msg +="#L2#커플링 시스템"+enter;
				//msg += "#L3#선택지3"+enter;
				//msg += "#L4#선택지4"+enter;
				//msg += "#L5#선택지5"+enter;
				cm.sendSimple(msg);
				break;
			case 7:
				cm.dispose();
				cm.warp(450005000);
				break;
			case 8:
				cm.dispose();
				cm.warp(450002023);
				break;
			case 9:
				cm.dispose();
				cm.warp(450004000);
				break;
			case 10:
				cm.dispose();
				cm.openNpc(2300001);
				break;
			case 13:
				cm.dispose();
				cm.openNpc(9000040);
				break;
			case 14:
				cm.dispose();
				cm.openNpc(3001652);
				break;
			case 15:
				cm.dispose();
				cm.warp(993014000);
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
						cm.openNpc(2155000);
						break;
					case 2:
						cm.dispose();
						cm.openNpc(3003104);
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
					case 9:
						cm.dispose();
						cm.openNpc(3003151);
						break;
					case 8:
						cm.dispose();
						cm.openNpc(3003381);
						break;
					case 10:
						cm.dispose();
						cm.warp(450004000, 0);
						break;
				}
				break;
			case 3:
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
			case 4:
				switch (sel) {
					case 1:
						cm.dispose();
						cm.openNpc(3003104);
						break;
					case 2:
						cm.dispose();
						cm.openNpc(3003161);
						break;
					case 3:
						cm.dispose();
						cm.openNpc(3003209);
						break;
					case 4:
						cm.dispose();
						cm.openNpc(3003322);
						break;
					case 5:
						cm.dispose();
						cm.openNpc(3003472);
						break;
					case 6:
						cm.dispose();
						cm.openNpc(3004202);
						break;
				}
				break;
			case 5:
				switch (sel) {
					case 1:
						cm.dispose();
						cm.openNpc("guild_proc");
						break;
					case 2:
						cm.dispose();
						cm.openNpc(2010009);
						break;
					case 3:
						cm.dispose();
						cm.openNpc(1540107);
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
			case 7:
				switch (sel) {
					case 1:
						cm.dispose();
						cm.warp(910130100, 0);
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
						cm.openNpc(2470039);
						break;
					case 4:
						cm.dispose();
						cm.openNpc(9000224);
						break;
					case 5:
						cm.dispose();
						cm.openNpc(9001153);
						break;
				}
				break;
		}
	}
}