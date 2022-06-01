function enter(pi) {
    var portals = [
        [-1288, -775],
        [-1981, -1014],
        [-3245, -1615],
        [908, -774],
        [1719, -1015],
        [2899, -1617],
        [2887, -1375],
        [-3245, -1315],
        [113, -1431],
        [-466, -1438],
        [-2192, -536],
        [1908, -533]
    ];
    var rd = Math.floor(Math.random() * portals.length);
    pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.CharReLocationPacket(portals[rd][0], portals[rd][1]));
    return true;
}