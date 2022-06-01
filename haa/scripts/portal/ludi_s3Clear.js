function enter(pi) {
    if(pi.getPlayer().getMapId() == 922010700){
        if(pi.getPlayer().getEventInstance().getProperty("stage4r") == "true"){
            pi.resetMap(922010800);
            pi.warpParty(922010800);
        } else {
            pi.getPlayer().dropMessage(5, "미션을 클리어 해야 가실 수 있습니다.");
        }
    }
}