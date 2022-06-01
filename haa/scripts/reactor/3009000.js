importPackage(java.lang);
importPackage(java.util);
importPackage(Packages.tools.packet);

var check = true;

function act() {
	rm.getPlayer().getMap().broadcastMessage(CField.environmentChange("Map/Effect.img/quest/party/clear", 4));
	rm.getPlayer().getMap().broadcastMessage(CField.achievementRatio(25));
}
