importPackage(java.lang);
importPackage(Packages.server);

var seld = -1;
var enter = "\r\n";

var hairs = [39030, 39070, 39200, 39210, 39100, 39180, 39110];

var itemid = 2430022;
var price = 1;

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
		var msg = "#fUI/GuildMark.img/Mark/Letter/00005014/4##fs11# #eDEEZ 스페셜 헤어쿠폰(여) B#n를 획득하셨군요! (현재 7종)\r\n#fUI/GuildMark.img/Mark/Letter/00005014/4# 헤어쿠폰 사용 전 주의사항이 있으니 꼭 읽어주세요."+enter+enter;
                msg += "#d- 해당 헤어는 믹스염색이 불가능합니다. (일반 염색 이용) \r\n- 헤어 쿠폰 사용 전 반드시 일반 염색을 적용해주세요.\r\n- 스페셜 헤어는 일반 마네킹에 저장이 불가합니다.\r\n- 반드시 DEEZ 스페셜 헤어 전용 마네킹을 이용해주세요.#n"+enter+enter;
		//msg += "#L1#헤어쿠폰에서 뜨는 헤어 목록 확인하기"+enter;
		msg += "#L2##e#bDEEZ 스페셜 헤어 랜덤쿠폰(여)B를 사용하겠습니다.";
		cm.sendSimple(msg);
	} else if (status == 1) {
		seld = sel;
		switch (sel) {
			case 1:
				var msg = "";
				for (i = 0; i < hairs.length; i++) {
					if (i != 1 && i % 4 == 0) msg += enter;
					msg += "#fCharacter/Hair/000"+hairs[i]+".img/backDefault/backHair#";
				}
				cm.sendOk(msg);
				cm.dispose();
			break;
			case 2:
				if (cm.itemQuantity(itemid) < price) {
					cm.sendOk("#i"+itemid+"##z"+itemid+"#가 부족합니다.");
					cm.dispose();
					return;
				}
				cm.sendYesNo("정말 DEEZ 스페셜 헤어쿠폰B를 사용하시겠어요?\r\n7종의 헤어 중 랜덤한 하나의 헤어가 적용됩니다.");
 			break;
		}
	} else if (status == 2) {
		if (cm.itemQuantity(itemid) < price) {
			cm.sendOk("#i"+itemid+"##z"+itemid+"#가 부족합니다.");
			cm.dispose();
			return;
		}
		var color = cm.getPlayer().getHair() % 10;
		if (color > 0 || cm.getPlayer().getBaseColor() != -1 || cm.getPlayer().getAddColor() != 0) {
			cm.getPlayer().setBaseColor(-1);
			cm.getPlayer().setAddColor(0);
			cm.getPlayer().setBaseProb(0);
			cm.setAvatar(2430022, cm.getPlayer().getHair() - color);
		}
		ind = Randomizer.rand(0, (hairs.length - 1));
		cm.gainItem(itemid, -price);
		cm.setAvatar(2430022, hairs[ind]);
		cm.sendOk("새로운 헤어는 마음에 드시나요? DEEZ만의 특별한 헤어로 멋진 스타일을 뽐내보세요!");
		cm.dispose();
	}
}