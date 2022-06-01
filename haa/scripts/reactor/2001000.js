function act() {
    if (Math.random() > 0.6) {
        rm.spawnMonster(9300049);
    } else {
        rm.spawnMonster(9300048);
    }
}