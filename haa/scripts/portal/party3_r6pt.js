importPackage(Packages.packet.creators);

function enter(pi) {
	var em = pi.getPlayer().getEventInstance();
	if (em.getProperty("OrbiPQ_Half") != 1) {
    if (pi.getPortal().getName().startsWith("rp01" + em.getProperty("stare_00"))) {
	pi.currentPortal(6);
	pi.getMap().broadcastMessage(MainPacketCreator.environmentChange("an1" + em.getProperty("stare_00"),2));

    } else if (pi.getPortal().getName().startsWith("rp02" + em.getProperty("stare_01"))) {
	pi.currentPortal(7);
	pi.getMap().broadcastMessage(MainPacketCreator.environmentChange("an2" + em.getProperty("stare_01"),2));

    } else if (pi.getPortal().getName().startsWith("rp03" + em.getProperty("stare_02"))) {
	pi.currentPortal(8);
	pi.getMap().broadcastMessage(MainPacketCreator.environmentChange("an3" + em.getProperty("stare_02"),2));

    } else if (pi.getPortal().getName().startsWith("rp04" + em.getProperty("stare_03"))) {
	pi.currentPortal(9);
	pi.getMap().broadcastMessage(MainPacketCreator.environmentChange("an4" + em.getProperty("stare_03"),2));
	em.setProperty("OrbiPQ_Half","1");

	} else {
	pi.currentPortal(22);
	}

	} else {
// Áß°£ Ãþ

    	 if (pi.getPortal().getName().startsWith("rp05" + em.getProperty("stare_04"))) {
	pi.currentPortal(10);
	pi.getMap().broadcastMessage(MainPacketCreator.environmentChange("an5" + em.getProperty("stare_04"),2));

	} else if (pi.getPortal().getName().startsWith("rp06" + em.getProperty("stare_05"))) {
	pi.currentPortal(11);
	pi.getMap().broadcastMessage(MainPacketCreator.environmentChange("an6" + em.getProperty("stare_05"),2));

	} else if (pi.getPortal().getName().startsWith("rp07" + em.getProperty("stare_06"))) {
	pi.currentPortal(12);
	pi.getMap().broadcastMessage(MainPacketCreator.environmentChange("an7" + em.getProperty("stare_06"),2));
	
	} else if (pi.getPortal().getName().startsWith("rp08" + em.getProperty("stare_07"))) {
	pi.currentPortal(13);
	pi.getMap().broadcastMessage(MainPacketCreator.environmentChange("an8" + em.getProperty("stare_07"),2));
	
    } else {
	pi.currentPortal(9);
    }
}
}