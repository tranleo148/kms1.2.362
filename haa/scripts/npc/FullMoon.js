var status = -1;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
		return;
	}
	if (mode == 0) {
		cm.dispose();
		return;
	}
	if (mode == 1) {
		status++;
	}

	if (status == 0) {
		talk = "요정학원 엘리넬에서 #e#r특별한 파티#k#n를 열었다네. \r\n지금 바로 #e#b<풀 문 파티>#k#n로 이동하겠나?\r\n\r\n"
		cm.sendYesNo(talk);
	} else if (status == 1) {
		if (selection == 1) {
			cm.sendYesNo("DEEZ 코인을 #r#e700개#n#k 지불하고 #b#e<Hotel DEEZ 멤버십 포인트>#n#k를 구매하시겠어요?", 9062297);
		} else {
			cm.dispose();
		}
	} else if (status == 2) {
		cm.dispose();
		if (cm.getPlayer().getKeyValue(100592, "point") < 700) {
			cm.sendOk("DEEZ 코인을 #r#e1000개#n#k 가지고 계신가요? DEEZ 코인이 있어야만 구매하실 수 있습니다.", 9062297);
		} else if (cm.getPlayer().getKeyValue(501045, "point") >= 19 && cm.getPlayer().getKeyValue(501045, "lv") == 4) {
			cm.sendOk("더 이상 Hotel DEEZ VIP 멤버십 포인트를 구매하실 수 없습니다.", 9062297);
		} else {
			cm.getPlayer().AddStarDustCoin(-700);
			cm.getPlayer().setKeyValue(501045, "point", "" + (cm.getPlayer().getKeyValue(501045, "point") + 1));
			cm.getPlayer().setKeyValue(501045, "sp", "" + (cm.getPlayer().getKeyValue(501045, "sp") + 1));
			cm.getPlayer().setKeyValue(501045, "date", Packages.constants.GameConstants.getCurrentDate_NoTime());
			cm.getClient().setKeyValue("hotelMapleToday", "1");
			point = cm.getPlayer().getKeyValue(501045, "point"), lv = cm.getPlayer().getKeyValue(501045, "lv");
			if (point == 5 && lv == 1) {
				cm.getPlayer().setKeyValue(501045, "point", "0");
				cm.getPlayer().setKeyValue(501045, "lv", "2");
			} else if (point == 10 && lv == 2) {
				cm.getPlayer().setKeyValue(501045, "point", "0");
				cm.getPlayer().setKeyValue(501045, "lv", "3");
			} else if (point == 20 && lv == 3) {
				cm.getPlayer().setKeyValue(501045, "point", "0");
				cm.getPlayer().setKeyValue(501045, "lv", "4");
			}

			cm.playSound(false, "Sound/MiniGame.img/CrazySplash/revived");
			cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.detailShowInfo("Hotel DEEZ VIP 멤버십 포인트를 구매했습니다!", false));
			cm.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.OnYellowDlg(9062297, 2000, "#r#eHotel DEEZ VIP 멤버십 포인트#n#k 구매 정말 감사합니다.", ""));
			cm.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.OnYellowDlg(9062297, 2000, "#r Hotel DEEZ VIP 스킬 포인트#k를 \r\n드렸으니 잘 활용해보시죠.", ""));
		}
	}
}