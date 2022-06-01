function enter(pi) {
     if (pi.getQuestStatus(31248) == 1) {
          pi.openNpc(2134012);
          return; 
     }
     if (pi.getQuestStatus(31251) == 1) {
          pi.warp(301050200, 0);
          return; 
     }
     if (pi.getQuestStatus(31254) == 1) {
          pi.warp(301050300, 0);
          return; 
     }
    pi.playerMessage(5, "더 이상 진행할 수 없습니다.");
	pi.playPortalSE();    
}
