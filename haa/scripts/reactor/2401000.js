function act() {
    rm.changeMusic("Bgm14/HonTale");
    rm.spawnMonster(8810026,71,260);
    rm.getMap().killMonster(rm.getMap().getMonsterById(8810026));
    rm.mapMessage(6, "동굴 깊은 곳에서 혼테일이 나타났습니다!");
}