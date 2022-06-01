importPackage(Packages.packet.creators);

function enter(pi) {
    var upstare = ["0","1","2","3"];
    if (pi.getPlayerCount(926100301) == 0) {
	for (x = 0; x < 5; x++) {
	    pi.getPlayer().setKeyValue("stare_0" + x + "",upstare[Math.floor(Math.random() * 4)] + "");
	}
	pi.warp(926100301,0);
	pi.getMap().broadcastMessage(MainPacketCreator.environmentChange("r1",2));
    } else {
	pi.playerMessage("대화가 없습니다.");
    }
}