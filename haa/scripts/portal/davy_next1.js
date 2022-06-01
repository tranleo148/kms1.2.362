importPackage(Packages.packet.creators);
function enter(pi) {
     if (pi.getPlayer().getEventInstance().getProperty("DavyzonePQ_Gate") == 1) {
         var em = pi.getEventManager("PartyQuest").readyInstance();
         pi.getEventInstance().unregisterAll();
         em.setProperty("Global_StartMap","925100400");
         em.setProperty("Global_ExitMap","925100700");
         em.startEventTimer(300000);
         em.registerParty(pi.getParty(),pi.getMap());
         pi.getEventInstance().setProperty("DavyzonePQ_Gate","0");
	pi.getPlayer().getClient().getSession().write(UIPacket.AchievementRatio(50));
     } else {
         pi.playerMessage("지금은 포탈이 봉인되어 있습니다.");
     }
 }


