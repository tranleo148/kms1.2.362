function enter(pi) {
    var map = pi.getPlayer().getMapId();
    var check = map%10;
    var em = check == 0 ? pi.getEventManager("Normal_Horntail") : pi.getEventManager("Chaos_Horntail");
    var eim = pi.getPlayer().getEventInstance();
    if (em == null || eim == null) {
        pi.getPlayer().dropMessage(5, "비정상적인 값입니다. 운영자께 문의해 주세요.");
    }
    if (map == 240060000 || map == 240060001) {
        if (eim.getProperty("stage") == "0") {
            pi.getPlayer().dropMessage(5, "지금은 포탈이 작동하지 않습니다.");
        } else if (!pi.isLeader()) {
	pi.getPlayer().dropMessage(5, "파티장만이 입장을 시도할 수 있습니다.");
        } else {
            pi.warpParty(map + 100);
	eim.changedMap(pi.getPlayer(), map + 100);
        }
    } else if (map == 240060100 || map == 240060101) {
        if (eim.getProperty("stage") == "1") {
            pi.getPlayer().dropMessage(5, "지금은 포탈이 작동하지 않습니다.");
        } else if (!pi.isLeader()) {
	pi.getPlayer().dropMessage(5, "파티장만이 입장을 시도할 수 있습니다.");
        } else {
            pi.warpParty(map + 100);
	eim.changedMap(pi.getPlayer(), map + 100);
        }
    }
    return false;
}
