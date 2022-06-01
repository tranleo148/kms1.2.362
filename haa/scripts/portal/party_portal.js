importPackage(java.util);
importPackage(java.lang);
function enter(pi) {
	var eim = pi.getPlayer().getEventInstance();
	var i = pi.getMapId() == 910340100 ? 1 :
		pi.getMapId() == 910340200 ? 3 :
		pi.getMapId() == 910340300 ? 5 : 
		pi.getMapId() == 910340400 ? 7 : 9;
	if (eim.getProperty("KerningPQ_Gate") == i) {
    pi.warpParty(pi.getMapId() + 100);
	eim.setProperty("KerningPQ_Gate", (Integer.parseInt(eim.getProperty("KerningPQ_Gate")) + 1) +"");
	} else {
}
} 