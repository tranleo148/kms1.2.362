function enter(pi) {
   if (!pi.haveItem(4032860, 1)) {
      pi.getPlayer().dropMessage(5, "열쇠가 없어 탈출할 수 없습니다.");
      return false;
   } else {
      pi.getMap().resetReactors(pi.getClient());
      if (pi.getPlayer().getMapId() == 211070350) {
         pi.warp(211070102, 0);
      } else if (pi.getPlayer().getMapId() == 211070450) {
         pi.warp(211070104, 0);
      } else if (pi.getPlayer().getMapId() == 211070550) {
         pi.warp(211070100, 0);
      }
      pi.removeAll(4032860);
      return true;
   }
}