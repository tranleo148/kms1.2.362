function enter(pi) {
    if (pi.getPlayer().getMapId() == 932200200) {
        pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.CharReLocationPacket(-343, 2196));
    } else {
        pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.CharReLocationPacket(-1974, 2561));
    }
    return true;
}