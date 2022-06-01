function act() {
    var em = rm.getEventManager("Chaos_Zakum");
    var eim = rm.getPlayer().getEventInstance();
    if (em == null || eim == null) {
        rm.mapMessage(5, "정상적으로 입장을 하지 않아 리액터가 정상 작동하지 않습니다. 운영자께 문의해주세요.");
        return;
    }
    rm.getPlayer().getMap().spawnChaosZakum(rm.getPlayer(), -10, 86);
    rm.mapMessage(5, "원석의 힘으로 자쿰이 소환됩니다.");
}