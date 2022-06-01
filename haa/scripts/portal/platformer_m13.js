function enter(pi) {
    pi.getPlayer().getStat().heal(pi.getPlayer())
    if (pi.getPlayer().getPosition().x > -830) {
        pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.CharReLocationPacket(-4129, -88));
        var m = (pi.getPlayer().getPosition().x + 830) / 100;
        pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.SendPacket(694, "05 00 00 00 D0 07 00 00 00"));
        pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.OnYellowDlg(9070201, 3000, "네 점프 기록은 " + m + " 미터야. 다시 도전해 봐!", ""));
        return;
    }
}