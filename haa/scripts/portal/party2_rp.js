var random0, random1, random2, random3, random4, random5, random6, random7, random8, random9;
	if(random0 == null){
		random0 = Packages.server.Randomizer.rand(0, 2);
		random1 = Packages.server.Randomizer.rand(0, 2);
		random2 = Packages.server.Randomizer.rand(0, 2);
		random3 = Packages.server.Randomizer.rand(0, 2);
		random4 = Packages.server.Randomizer.rand(0, 2);
		random5 = Packages.server.Randomizer.rand(0, 2);
		random6 = Packages.server.Randomizer.rand(0, 2);
		random7 = Packages.server.Randomizer.rand(0, 2);
		random8 = Packages.server.Randomizer.rand(0, 2);
		random9 = Packages.server.Randomizer.rand(0, 2);
		pi.getPlayer().getMap().setrpportal(random0);
	}

function enter(pi) {
    if (pi.getPortal().getName().startsWith("pt0" + random0)) {
		if(pi.getPlayer().isGM()){
			pi.getPlayer().dropMessage(5, random0+"/"+random1+"/"+random2+"/"+random3+"/"+random4+"/"+random5+"/"+random6+"/"+random7+"/"+random8+"/"+random9);
		}
		pi.getPlayer().getClient().getSession().writeAndFlush(Packages.tools.packet.CField.instantMapWarp(pi.getPlayer(), pi.getPlayer().getMap().getPortal("pt10").getId()));
		pi.getPlayer().getMap().movePlayer(pi.getPlayer(), new java.awt.Point(pi.getPlayer().getMap().getPortal("pt10").getPosition()));
		pi.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.environmentChange("an0"+ random0, 2));
		pi.getPlayer().getMap().setrpportal(random1);
    } else if (pi.getPortal().getName().startsWith("pt1" + random1)) {
		pi.getPlayer().getClient().getSession().writeAndFlush(Packages.tools.packet.CField.instantMapWarp(pi.getPlayer(), pi.getPlayer().getMap().getPortal("pt20").getId()));
		pi.getPlayer().getMap().movePlayer(pi.getPlayer(), new java.awt.Point(pi.getPlayer().getMap().getPortal("pt20").getPosition()));
		pi.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.environmentChange("an1"+ random1, 2));
		pi.getPlayer().getMap().setrpportal(random2);
    } else if (pi.getPortal().getName().startsWith("pt2" + random2)) {
		pi.getPlayer().getClient().getSession().writeAndFlush(Packages.tools.packet.CField.instantMapWarp(pi.getPlayer(), pi.getPlayer().getMap().getPortal("pt30").getId()));
		pi.getPlayer().getMap().movePlayer(pi.getPlayer(), new java.awt.Point(pi.getPlayer().getMap().getPortal("pt30").getPosition()));
		pi.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.environmentChange("an2"+ random2, 2));
		pi.getPlayer().getMap().setrpportal(random3);
    } else if (pi.getPortal().getName().startsWith("pt3" + random3)) {
		pi.getPlayer().getClient().getSession().writeAndFlush(Packages.tools.packet.CField.instantMapWarp(pi.getPlayer(), pi.getPlayer().getMap().getPortal("pt40").getId()));
		pi.getPlayer().getMap().movePlayer(pi.getPlayer(), new java.awt.Point(pi.getPlayer().getMap().getPortal("pt40").getPosition()));
		pi.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.environmentChange("an3"+ random3, 2));
		pi.getPlayer().getMap().setrpportal(random4);
    } else if (pi.getPortal().getName().startsWith("pt4" + random4)) {
		pi.getPlayer().getClient().getSession().writeAndFlush(Packages.tools.packet.CField.instantMapWarp(pi.getPlayer(), pi.getPlayer().getMap().getPortal("pt50").getId()));
		pi.getPlayer().getMap().movePlayer(pi.getPlayer(), new java.awt.Point(pi.getPlayer().getMap().getPortal("pt50").getPosition()));
		pi.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.environmentChange("an4"+ random4, 2));
		pi.getPlayer().getMap().setrpportal(random5);
    } else if (pi.getPortal().getName().startsWith("pt5" + random5)) {
		pi.getPlayer().getClient().getSession().writeAndFlush(Packages.tools.packet.CField.instantMapWarp(pi.getPlayer(), pi.getPlayer().getMap().getPortal("pt60").getId()));
		pi.getPlayer().getMap().movePlayer(pi.getPlayer(), new java.awt.Point(pi.getPlayer().getMap().getPortal("pt60").getPosition()));
		pi.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.environmentChange("an5"+ random5, 2));
		pi.getPlayer().getMap().setrpportal(random6);
    } else if (pi.getPortal().getName().startsWith("pt6" + random6)) {
		pi.getPlayer().getClient().getSession().writeAndFlush(Packages.tools.packet.CField.instantMapWarp(pi.getPlayer(), pi.getPlayer().getMap().getPortal("pt70").getId()));
		pi.getPlayer().getMap().movePlayer(pi.getPlayer(), new java.awt.Point(pi.getPlayer().getMap().getPortal("pt70").getPosition()));
		pi.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.environmentChange("an6"+ random6, 2));
		pi.getPlayer().getMap().setrpportal(random7);
    } else if (pi.getPortal().getName().startsWith("pt7" + random7)) {
		pi.getPlayer().getClient().getSession().writeAndFlush(Packages.tools.packet.CField.instantMapWarp(pi.getPlayer(), pi.getPlayer().getMap().getPortal("pt80").getId()));
		pi.getPlayer().getMap().movePlayer(pi.getPlayer(), new java.awt.Point(pi.getPlayer().getMap().getPortal("pt80").getPosition()));
		pi.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.environmentChange("an7"+ random7, 2));
		pi.getPlayer().getMap().setrpportal(random8);
    } else if (pi.getPortal().getName().startsWith("pt8" + random8)) {
		pi.getPlayer().getClient().getSession().writeAndFlush(Packages.tools.packet.CField.instantMapWarp(pi.getPlayer(), pi.getPlayer().getMap().getPortal("pt90").getId()));
		pi.getPlayer().getMap().movePlayer(pi.getPlayer(), new java.awt.Point(pi.getPlayer().getMap().getPortal("pt90").getPosition()));
		pi.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.environmentChange("an8"+ random8, 2));
		pi.getPlayer().getMap().setrpportal(random9);
    } else if (pi.getPortal().getName().startsWith("pt9" + random9)) {
		pi.warpParty(pi.getPlayer().getMapId(), 10);
		pi.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.environmentChange("Map/Effect.img/quest/party/clear", 4));
		pi.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.achievementRatio(35));
    } else {
		pi.getPlayer().getClient().getSession().writeAndFlush(Packages.tools.packet.CField.instantMapWarp(pi.getPlayer(), pi.getPlayer().getMap().getPortal("st00").getId()));
		pi.getPlayer().getMap().movePlayer(pi.getPlayer(), new java.awt.Point(pi.getPlayer().getMap().getPortal("st00").getPosition()));
    }
}
