function enter(pi) {
    var lastshuttle = pi.getPlayer().getKeyValue(20190208, "lastshuttle");

    if (lastshuttle == 1 || lastshuttle == -1) {
        pi.getPlayer().setKeyValue(20190208, "lastshuttle", "0");
        pi.getPlayer().setKeyValue(20190208, "shuttlecount", (pi.getPlayer().getKeyValue(20190208, "shuttlecount") + 1) + "");
        var count = pi.getPlayer().getKeyValue(20190208, "shuttlecount");
        switch (count) {
            case 1:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.MaplePacket.OnYellowDlg(9070203, 1000, "좋아 시작이다! 하나!", ""));
                break;
            case 2:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.MaplePacket.OnYellowDlg(9070203, 1000, "둘!", ""));
                break;
            case 3:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.MaplePacket.OnYellowDlg(9070203, 1000, "셋!", ""));
                break;
            case 4:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.MaplePacket.OnYellowDlg(9070203, 1000, "넷!", ""));
                break;
            case 5:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.MaplePacket.OnYellowDlg(9070203, 1000, "다섯! 빛보다 빠르게!", ""));
                break;
            case 6:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.MaplePacket.OnYellowDlg(9070203, 1000, "여섯!", ""));
                break;
            case 7:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.MaplePacket.OnYellowDlg(9070203, 1000, "일곱!", ""));
                break;
            case 8:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.MaplePacket.OnYellowDlg(9070203, 1000, "여덟!", ""));
                break;
            case 9:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.MaplePacket.OnYellowDlg(9070203, 1000, "아홉!", ""));
                break;
            case 10:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.MaplePacket.OnYellowDlg(9070203, 1000, "열! 내 이름은 라이트닝 볼트!", ""));
                break;
            case 11:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.MaplePacket.OnYellowDlg(9070203, 1000, "열하나!", ""));
                break;
            case 12:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.MaplePacket.OnYellowDlg(9070203, 1000, "열둘!", ""));
                break;
            case 13:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.MaplePacket.OnYellowDlg(9070203, 1000, "열셋!", ""));
                break;
            case 14:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.MaplePacket.OnYellowDlg(9070203, 1000, "열넷!", ""));
                break;
            case 15:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.MaplePacket.OnYellowDlg(9070203, 1000, "열다섯! 힘들어도 근성이다!", ""));
                break;
            case 16:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.MaplePacket.OnYellowDlg(9070203, 1000, "열여섯!", ""));
                break;
            case 17:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.MaplePacket.OnYellowDlg(9070203, 1000, "열일곱!", ""));
                break;
            case 18:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.MaplePacket.OnYellowDlg(9070203, 1000, "열여덟!", ""));
                break;
            case 19:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.MaplePacket.OnYellowDlg(9070203, 1000, "열아홉!", ""));
                break;
            case 20:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.MaplePacket.OnYellowDlg(9070203, 1000, "스물! 엄마 생각 나지?!", ""));
                break;
            case 21:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.MaplePacket.OnYellowDlg(9070203, 1000, "스물하나!", ""));
                break;
            case 22:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.MaplePacket.OnYellowDlg(9070203, 1000, "스물둘!", ""));
                break;
            case 23:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.MaplePacket.OnYellowDlg(9070203, 1000, "스물셋!", ""));
                break;
            case 24:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.MaplePacket.OnYellowDlg(9070203, 1000, "스물넷!", ""));
                break;
            case 25:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.MaplePacket.OnYellowDlg(9070203, 1000, "스물다섯! 이제 다섯 개 남았다!", ""));
                break;
            case 26:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.MaplePacket.OnYellowDlg(9070203, 1000, "스물여섯!", ""));
                break;
            case 27:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.MaplePacket.OnYellowDlg(9070203, 1000, "스물일곱!", ""));
                break;
            case 28:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.MaplePacket.OnYellowDlg(9070203, 1000, "스물여덟!", ""));
                break;
            case 29:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.MaplePacket.OnYellowDlg(9070203, 1000, "스물아홉! 마지막 한 개!", ""));
                break;
            case 29:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.MaplePacket.OnYellowDlg(9070203, 1000, "서른! 좋아! 잘 했어! 성공이야!", ""));
                break;
        }
        if (count == 30) {
            pi.getPlayer().setKeyValue(20190208, "lastshuttle", "-1");
            pi.getPlayer().setKeyValue(20190208, "shuttlecount", "0");
            pi.getClient().getSession().writeAndFlush(Packages.tools.packet.MaplePacket.SendPacket(714, "01 01 01 00"));
            pi.getClient().getSession().writeAndFlush(Packages.tools.packet.MaplePacket.CharReLocationPacket(-510, 92));
            pi.getClient().getSession().writeAndFlush(Packages.tools.packet.MaplePacket.playSE("Sound/MiniGame.img/prize"));
            pi.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.environmentChange("monsterPark/clearF", 0x13));
            var schedule = Packages.server.Timer.MapTimer.getInstance().schedule(function () {
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.MaplePacket.playSE("Sound/MiniGame.img/Catch"));
            }, 1000)
            pi.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.enforceMSG("스테이지 클리어다. 로비로 이동하지.", 212, 2000));
            pi.getPlayer().RegisterPlatformerRecord(18);
            pi.getPlayer().warpdelay(993001000, 2);
            var schedule = Packages.server.Timer.MapTimer.getInstance().schedule(function () {
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.MaplePacket.SendPacket(714, "00 01"));
            }, 2000)
        }
    }
}
