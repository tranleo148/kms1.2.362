function start() {
	cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.OnSetMirrorDungeonInfo(false));
	cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.openUI(152));
	cm.dispose();
}

function action(a, b, c) {
	cm.dispose();
}