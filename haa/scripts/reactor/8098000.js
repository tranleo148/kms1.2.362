function act() {
    var map = rm.getPlayer().getMapId();
    var b = Math.abs(rm.getPlayer().getMapId() - 809050005);
    if (map != 809050000 && map != 809050010) {
        rm.spawnMonster(9400217 - b,2);
        rm.spawnMonster(9400218 - b,3);
    } else {
        rm.spawnMonster(9400209,6);
        rm.spawnMonster(9400210,9);
    }
}