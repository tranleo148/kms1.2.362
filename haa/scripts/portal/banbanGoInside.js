function enter(pi) {
    if (pi.getPlayer().getSkillCustomValue(8910000) != null) {
        pi.warp(pi.getPlayer().getMapId() + 10);
    }
    return true;
}