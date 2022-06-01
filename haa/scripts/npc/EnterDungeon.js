function start() {
	cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.OnSetMirrorDungeonInfo(false));
	cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.openUI(152));
	cm.dispose();
}

function mirrorD_322_0_() {
	cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.OnSetMirrorDungeonInfo(true));
	cm.dispose();
	cm.openNpc(9062294);
}

function mirrorD_322_1_() {
	cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.OnSetMirrorDungeonInfo(true));
	cm.dispose();
	cm.openNpc(9010044);
}

function mirrorD_322_2_() {
	cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.OnSetMirrorDungeonInfo(true));
	cm.dispose();
	cm.openNpc(9062277);
}

function mirrorD_322_3_() {
	cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.OnSetMirrorDungeonInfo(true));
	cm.dispose();
	cm.openNpc(9062284);
}

function mirrorD_323_0_() {
	cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.OnSetMirrorDungeonInfo(true));
	cm.dispose();
	cm.openNpc(9401240);
}

function action(a, b, c) {
	cm.dispose();
}