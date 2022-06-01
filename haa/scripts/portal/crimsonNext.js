importPackage (java.util);
importPackage (Packages.tools);
importPackage (Packages.server.quest);
importPackage(java.awt);
importPackage(java.lang);
importPackage(Packages.tools.RandomStream);
importPackage(Packages.packet.creators);
importPackage(Packages.server.life);

function enter(pi) {
if (pi.getQuestStatus(31258) == 1) {
	pi.warp(301070010, "sp");
   var mobid = 8148012;
            var mob = MapleLifeProvider.getMonster(mobid);
            mob.disableDrops();
            pi.getPlayer().getMap().spawnMonsterOnGroundBelow(mob, new java.awt.Point(138, 38));
}
	pi.playPortalSE();    
}
