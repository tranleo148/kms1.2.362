
importPackage(java.lang);

var enter = "\r\n";
var seld = -1;

var isStart = false;

var guild1 = null;
var guild2 = null;

var damage1 = 0;
var damage2 = 0;

var winner;

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

	if (cm.getPlayer().getGuild() == null) {
		cm.sendOk("길드가 없다면 이용할 수 없는 컨텐츠입니다.");
		cm.dispose();
		return;
	}
	if (cm.getChannelServer().getGuildWar() == null) {
		cm.getChannelServer().initMapleGuildWar();
	}

	damage1 = cm.getChannelServer().getGuildWar().getDamage(1);
	damage2 = cm.getChannelServer().getGuildWar().getDamage(2);

	guild1 = cm.getMapleGuildWar(1);
	guild2 = cm.getMapleGuildWar(2);


	winner = damage1 > damage2 ? guild1 : guild2;

	if (status == 0) {

		var msg = "#fn나눔고딕##fs13#";
		msg += "[길드 대항전 결과 :: #b"+guild1.getName()+"#k VS #r" + guild2.getName()+"#k]"+enter;
		msg += "#b"+guild1.getName()+"#k 길드의 누적 데미지 : #b"+damage1+"#k"+enter;
		msg += "#b"+guild2.getName()+"#k 길드의 누적 데미지 : #b"+damage2+"#k"+enter;
		msg += "---------------------------------------------"+enter;
		msg += "축하합니다! "+winner.getName()+" 길드의 승리입니다!"+enter;
		msg += "참여자 전원은 패배 길드의 배팅 금액을 나눠 갖게됩니다.";
		cm.sendOk(msg);
		cm.dispose();
	}
}