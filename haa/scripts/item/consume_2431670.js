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
        cm.gainItem(2431670, -1);
        ItemInfo = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(1002186);
	ItemInfo.setReqLevel(90);
	ItemInfo.setState(30);
	ItemInfo.setPotential1(40656);
	//ItemInfo.setPotential2(40656);
	//ItemInfo.setPotential3(40656);
	ItemInfo.setExpiration(System.currentTimeMillis() + (1 * 10 * 3600 * 1000));
        MapleInventoryManipulator.addFromDrop(cm.getClient(), ItemInfo, false);
	}
}