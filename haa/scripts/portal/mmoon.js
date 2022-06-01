
function enter(pi) {
     if (pi.getQuestStatus(3519) == 2) {
          pi.warp(271000000, 0);
          pi.playerMessage(5, "시간의 문 안으로 들어왔다. 여기는 어디지?");
if (pi.getQuestStatus(31102) == 1) {
pi.openNpc(2143002);
}
          return; 
     }
        pi.playerMessage(5, "이 문 너머에는 무엇이 있을까?");
 	pi.playPortalSE();    
}
