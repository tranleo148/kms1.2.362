
function enter(pi) {
    if(pi.getPlayer().getMapId() == 922010100){
        if(pi.getPlayer().getEventInstance().getProperty("stage1r") == "true"){
            pi.resetMap(922010401);
            pi.resetMap(922010402);
            pi.resetMap(922010403);
            pi.resetMap(922010404);
            pi.resetMap(922010405);
            pi.resetMap(922010400);
            pi.warpParty(922010400);
        } else {
            pi.getPlayer().dropMessage(5, "미션을 클리어 해야 가실 수 있습니다.");
        }
    }
}