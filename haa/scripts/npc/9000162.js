
function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, sel) {
	cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.OnSetMirrorDungeonInfo(false));
	cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.openUI(152));
	cm.dispose();
}

function mirrorD_322_0_() {
	cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.OnSetMirrorDungeonInfo(true));
	cm.dispose();
	cm.openNpc(1012124);
}

function mirrorD_322_1_() {
	cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.OnSetMirrorDungeonInfo(true));
	cm.dispose();
	cm.openNpc(3003324);
}

function mirrorD_322_3_() {
	cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.OnSetMirrorDungeonInfo(true));
	cm.dispose();
	cm.openNpc(3002026);
}


function mirrorD_322_2_() {
	cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.OnSetMirrorDungeonInfo(true));
	cm.dispose();
	cm.openNpc(9000382);
}


function mirrorD_323_0_() {
	cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.OnSetMirrorDungeonInfo(true));
	cm.dispose();
	cm.openNpc(2510022);
}


function mirrorD_323_1_() {
	cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.OnSetMirrorDungeonInfo(true));
	cm.dispose();
	cm.openNpc(3003310);
}

function action(a, b, c) {
	cm.dispose();
}