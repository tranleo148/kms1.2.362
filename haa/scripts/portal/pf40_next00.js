function enter(pi) {
    pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.CharReLocationPacket(-3847, -159));
}