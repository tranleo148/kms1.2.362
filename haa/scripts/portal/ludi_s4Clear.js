function enter(pi) {
    if(pi.getPlayer().getMapId() == 922010800){
        if(pi.getPlayer().getEventInstance().getProperty("stage5r") == "true"){
            pi.resetMap(922010900);
            pi.warpParty(922010900);
        } else {
            pi.getPlayer().dropMessage(5, "미션을 클리어 해야 가실 수 있습니다.");
        }
    }
}