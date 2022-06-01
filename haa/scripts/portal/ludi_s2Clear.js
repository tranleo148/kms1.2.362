function enter(pi) {
    if(pi.getPlayer().getMapId() == 922010400){
        if(pi.getPlayer().getEventInstance().getProperty("stage2r") == "true"){
	        pi.resetMap(922010600);
            pi.warpParty(922010600);
        } else {
            pi.getPlayer().dropMessage(5, "미션을 클리어 해야 가실 수 있습니다.");
        }
    }
}