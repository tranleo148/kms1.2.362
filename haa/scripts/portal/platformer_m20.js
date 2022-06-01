function enter(pi) {
    pi.getPlayer().getStat().heal(pi.getPlayer())
    if (pi.getPlayer().getPosition().x > -830) {
        pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.CharReLocationPacket(-4129, -88));
        var m = (pi.getPlayer().getPosition().x + 830) / 100;
        if (m >= 15) {
            pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.SendPacket(714, "01 01 01 00"));
            pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.playSE("Sound/MiniGame.img/prize"));
            pi.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.environmentChange("monsterPark/clearF", 0x13));
            var schedule = Packages.server.Timer.MapTimer.getInstance().schedule(function () {
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.playSE("Sound/MiniGame.img/Catch"));
            }, 1000)
            pi.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.enforceMSG("스테이지 클리어다. 로비로 이동하지.", 212, 2000));
            pi.getPlayer().RegisterPlatformerRecord(12);
            pi.getPlayer().warpdelay(993001000, 2);
            var schedule = Packages.server.Timer.MapTimer.getInstance().schedule(function () {
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.SendPacket(714, "00 01"));
            }, 2000)
        }
        else {
            pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.SendPacket(694, "05 00 00 00 D0 07 00 00 00"));
            pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.OnYellowDlg(9070201, 3000, "네 점프 기록은 " + m + " 미터야. 다시 도전해 봐!", ""));
        }
        return;
    }
}