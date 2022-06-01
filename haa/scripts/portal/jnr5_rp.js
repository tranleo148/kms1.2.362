importPackage(Packages.packet.creators);

function enter(pi) {
    if (pi.getPortal().getName().startsWith("pt0" + pi.getPlayer().getKeyValue("stare_00"))) {
	pi.currentPortal(3);
	pi.getMap().broadcastMessage(MainPacketCreator.environmentChange("an0" + pi.getPlayer().getKeyValue("stare_00"),2));
    } else if (pi.getPortal().getName().startsWith("pt1" + pi.getPlayer().getKeyValue("stare_01"))) {
	pi.currentPortal(4);
	pi.getMap().broadcastMessage(MainPacketCreator.environmentChange("an1" + pi.getPlayer().getKeyValue("stare_01"),2));
    } else if (pi.getPortal().getName().startsWith("pt2" + pi.getPlayer().getKeyValue("stare_02"))) {
	pi.currentPortal(5);
	pi.getMap().broadcastMessage(MainPacketCreator.environmentChange("an2" + pi.getPlayer().getKeyValue("stare_02"),2));
    } else if (pi.getPortal().getName().startsWith("pt3" + pi.getPlayer().getKeyValue("stare_03"))) {
	pi.currentPortal(7);
	pi.getMap().broadcastMessage(MainPacketCreator.environmentChange("an3" + pi.getPlayer().getKeyValue("stare_03"),2));
    } else if (pi.getPortal().getName().startsWith("pt4" + pi.getPlayer().getKeyValue("stare_04"))) {
	pi.currentPortal(12);
	pi.getMap().broadcastMessage(MainPacketCreator.environmentChange("an4" + pi.getPlayer().getKeyValue("stare_04"),2));
    } else {
	pi.currentPortal(13);
    }
}