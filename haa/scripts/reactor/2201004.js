function act() {
    if (rm.getPlayer().getMapId() == 220080200) {
        rm.spawnMonster(8500010, 0, 179);
    } else if (rm.getPlayer().getMapId() == 220080100) {
        rm.spawnMonster(8500000, 0, 179);
    } else {
        rm.spawnMonster(8500020, 0, 179);
    }
    rm.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.musicChange("Bgm09/TimeAttack"));
}