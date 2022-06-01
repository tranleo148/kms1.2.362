importPackage (java.util);
importPackage (Packages.tools);
importPackage (Packages.server.quest);
importPackage(java.awt);
importPackage(java.lang);
importPackage(Packages.tools.RandomStream);
importPackage(Packages.packet.creators);
importPackage(Packages.server.life);

function enter(pi) {
if (pi.getQuestStatus(31259) == 2) {
	pi.warp(301060000, "sp");
}
	pi.playPortalSE();    
}
