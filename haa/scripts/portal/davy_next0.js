importPackage(Packages.packet.creators);
function enter(pi) {
      if (pi.getPlayer().getEventInstance().getProperty("DavyzonePQ_Gate") == 1) {
         var em = pi.getEventManager("PartyQuest").readyInstance();
         pi.getEventInstance().unregisterAll();
         em.setProperty("Global_StartMap","925100100");
         em.setProperty("Global_ExitMap","925100700");
         em.startEventTimer(600000); // 420000
         em.registerParty(pi.getParty(),pi.getMap());
         pi.getEventInstance().setProperty("DavyzonePQ_Gate","0");
         pi.getEventInstance().setProperty("DavyzonePQ_Monster","9999");
	pi.getPlayer().getClient().getSession().write(UIPacket.AchievementRatio(25));
     } else {
         pi.playerMessage("모든 몬스터를 물리쳐야 다음 장소로 이동할 수 있습니다.");
     }
 }



