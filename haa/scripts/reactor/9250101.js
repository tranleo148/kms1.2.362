importPackage(java.lang);
importPackage(java.util);
importPackage(Packages.tools.packet);

var re;

function act() {

	re = rm.getPlayer().getMap().getAllReactorsThreadsafe();
	var em = rm.getEventManager("KerningPQ");
	for(var i = 0; i<4; i++){
		if(re[i].getReactorId() == 9250101){
			if(re[i].getState() != 1){
				re[i].forceHitReactor(0,0);
				return;
			} else {
				em.setProperty("stage4r", "2");
				re[i].forceHitReactor(1,0);
				rm.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(5, "", "빼기로 바뀌었습니다."));
			}
		}
		if(re[i].getState() == 1 && re[i].getReactorId() != 9250101){
			re[i].forceHitReactor(0,0);
		}
	}
}