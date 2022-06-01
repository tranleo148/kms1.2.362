/*
 * ��ī�̷�
 */
function enter(pi) {
    var hard = true;
    var em = pi.getEventManager("Hard_Hillah");
    if (em == null) {
        hard = false;
        em = pi.getEventManager("Normal_Hillah");
    }
    var eim = pi.getPlayer().getEventInstance();
    if (em == null || eim == null) {
        return;
    }
    pi.warp(pi.getPlayer().getMapId() - 10);
    return false;
}
