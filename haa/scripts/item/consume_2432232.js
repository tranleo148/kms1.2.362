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
        cm.gainItem(2432232, -1);
        ItemInfo = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(1082102);
	ItemInfo.setReqLevel(00);
	ItemInfo.setState(20);
           ItemInfo.setStr(1000);
           ItemInfo.setDex(1000);
           ItemInfo.setInt(1000);
	ItemInfo.setLuk(1000);
	ItemInfo.setWatk(500);
	ItemInfo.setMatk(500);
	//ItemInfo.setPotential1(40650);
	//ItemInfo.setPotential2(40650);
	//ItemInfo.setPotential3(40650);
           //ItemInfo.setPotential4(40650);
           //ItemInfo.setPotential5(40650)
           //ItemInfo.setPotential6(40650)
	//ItemInfo.setExpiration(System.currentTimeMillis() + (999 * 24 * 3600 * 1000));
        MapleInventoryManipulator.addFromDrop(cm.getClient(), ItemInfo, false);
	}
}