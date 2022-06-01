/*
블러디퀸 소환
*/

function act() {
    if (rm.getPlayer().getMapId() == 105200110)
    	rm.spawnMonster(8910100, 1);
    else
	rm.spawnMonster(8910000, 1);
}