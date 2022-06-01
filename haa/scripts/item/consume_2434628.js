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
        cm.gainItem(2434628, -1);
        ItemInfo = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(1662141);
	ItemInfo.setReqLevel(90);
	ItemInfo.setStr(180);
	ItemInfo.setDex(180);
	ItemInfo.setInt(180);
	ItemInfo.setLuk(180);
	ItemInfo.setWatk(100);
	ItemInfo.setMatk(100);
	ItemInfo.setState(20);
	ItemInfo.setState(20);
	ItemInfo.setPotential1(40603);
	ItemInfo.setPotential2(40292);
	ItemInfo.setPotential3(40056);
	ItemInfo.setPotential4(30055);
	ItemInfo.setPotential5(30055);
	ItemInfo.setPotential6(30055);
        MapleInventoryManipulator.addFromDrop(cm.getClient(), ItemInfo, false);
	}
}