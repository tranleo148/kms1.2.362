importPackage(Packages.server);
importPackage(Packages.client.inventory);
importPackage(Packages.constants);
importPackage(java.lang);
importPackage(java.io);
importPackage(Packages.packet.creators);
importPackage(Packages.client.items);
importPackage(Packages.server.items);
importPackage(Packages.launch.world);
importPackage(Packages.main.world);
importPackage(Packages.database);
importPackage(java.lang);
importPackage(Packages.server);
importPackage(Packages.handling.world);
importPackage(Packages.tools.packet);

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
        cm.dispose();
        cm.gainItem(2437113, -1);
	item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(1712001);
	item.setStr(2200);
	item.setDex(2200);
	item.setInt(2200);
	item.setLuk(2200);
	item.setArcLevel(20);
	item.setArc(220);
	Packages.server.MapleInventoryManipulator.addbyItem(cm.getClient(), item, false);
        cm.dispose();

	}
}