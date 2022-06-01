function start() {
 action(1, 0, 0);
}
var status = -1;
function action(mode, type, selection) {
 if (mode == 1) {
  status++;
 } else {
  status--;
  cm.dispose();
 }
 if (status == 0) {
		var cps = "#r#fs11##k#e경험치 시스템#n#fs 11#입니다. 최대 중첩가능한 시간은 #e#b12시간#k#n 이며,\r\n10초마다 #e0.02%#n 씩 쌓이게됩니다.\r\n\r\n#l#L1##b아무것도 안했는데 경험치 현황을 보겠습니다.\r\n\r\n#L2#아무것도 안했는데 남은 시간을 보겠습니다." ;	
		cm.sendSimple(cps);
 } else if(status == 1) {
	if(cm.getPlayer().getLevel() <= 400) {
	if(selection == 1) {
		cm.getClient().getSession().writeAndFlush(Packages.tools.packet.ContentsPacket.NoPlayerExpInfo(cm.getPlayer()));
		cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.openUI(1207));
		cm.dispose();
	} else if(selection == 2) {
		cm.getClient().getSession().writeAndFlush(Packages.tools.packet.ContentsPacket.NoPlayerExpInfo(cm.getPlayer()));
		cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.openUI(1208));
		cm.dispose();
	}
	} else {
		cm.sendOk("현재 시스템은 레벨 400까지만 이용 가능합니다.");
		cm.dispose();
	}
 }
}