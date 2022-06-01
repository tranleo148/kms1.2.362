importPackage(Packages.server.life);
importPackage(Packages.tools.RandomStream);
function enter(pi) {
	if (pi.getPlayer().getEventInstance().getProperty("OrbiPQ") == 1) {
	if (pi.getMapId() == 920010400) {
	var map = pi.getPlayer().getMap();
	pi.getPlayer().getEventInstance().setProperty("OrbiPQ","0");
	pi.resetMap(920010300);
	pi.warpParty(920010300);
	pi.getPlayer().spawn»ø¸®¿Â(-210,-504);
	pi.getPlayer().spawn»ø¸®¿Â(-216,-915);
	pi.getPlayer().spawn»ø¸®¿Â(-215,-1310);
	pi.getPlayer().spawn»ø¸®¿Â(128,-1121);
	pi.getPlayer().spawn»ø¸®¿Â(128,-717);
	} else if (pi.getMapId() == 920010300) {
	pi.resetMap(920010200);
	pi.warpParty(920010200);
	pi.getPlayer().getEventInstance().setProperty("OrbiPQ","0");
	pi.getPlayer().getEventInstance().setProperty("OrbiPQ_Piece","0");
} else {
	pi.resetMap(920010700);
	pi.warpParty(920010700);
	pi.getPlayer().getEventInstance().setProperty("OrbiPQ","0");
	var upstare = ["1","2","3","4"];
	for (x = 0; x < 8; x++) {
	    pi.getPlayer().getEventInstance().setProperty("stare_0" + x + "",upstare[Math.floor(Math.random() * 4)] + "");
	}
	var random1 = Randomizer.rand(0,1);
	var random2 = Randomizer.rand(0,1);
	var random3 = Randomizer.rand(0,1);
	pi.getPlayer().getEventInstance().setProperty("OrbiPQ_Lever1",random1+"");
	pi.getPlayer().getEventInstance().setProperty("OrbiPQ_Lever2",random2+"");
	pi.getPlayer().getEventInstance().setProperty("OrbiPQ_Lever3",random3+"");
	}
} else {
}
	
}