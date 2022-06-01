var mapid = [933011000, 933012000, 933013000, 933014000, 933015000];

var conn;

function enter(pi) {
    if(pi.getPlayer().getMapId() == mapid[0]){
        if((pi.getPlayer().getMap().getKerningPQ()-2) > (pi.getParty().getMembers().size()-1)){
            pi.resetMap(933012000);
            pi.warpParty(933012000);
        } else {
            pi.getPlayer().dropMessage(5, "클래토의 미션을 클리어 해야 가실 수 있습니다.");
        }
    } else if(pi.getPlayer().getMapId() == mapid[1]){
        if(pi.getPlayer().getEventInstance().getProperty("stage2r") == "true"){
            pi.resetMap(933013000);
            pi.warpParty(933013000);
        } else {
            pi.getPlayer().dropMessage(5, "클래토의 미션을 클리어 해야 가실 수 있습니다.");
        }
    } else if(pi.getPlayer().getMapId() == mapid[2]){
        if(pi.getPlayer().getEventInstance().getProperty("stage3r") == "true"){
            pi.resetMap(933014000);
            pi.warpParty(933014000);
        } else {
            pi.getPlayer().dropMessage(5, "클래토의 미션을 클리어 해야 가실 수 있습니다.");
        }
    } else if(pi.getPlayer().getMapId() == mapid[3]){
        var stage4r1 = pi.getPlayer().getEventInstance().getProperty("stage4M");
        var stage4r2 = pi.getPlayer().getMap().getKerningPQ();
        if(stage4r1 == stage4r2){
            pi.resetMap(933015000);
            pi.warpParty(933015000);
            pi.getPlayer().getMap().spawnMonsterWithEffectBelow(Packages.server.life.MapleLifeFactory.getMonster(9300912), new java.awt.Point(4, -435), 15);
        } else {
            pi.getPlayer().dropMessage(5, "클래토의 미션을 클리어 해야 가실 수 있습니다.");
        }
    }
}