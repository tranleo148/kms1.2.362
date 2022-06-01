function enter(pi) {
    var mapid = [0,0,0,100,100,100,200,200,300,300,300,400,400,400];
    var quest = [1431,1432,1433,1435,1436,1437,1439,1440,1442,1443,1447,1445,1446,1448];
    for (j = 0; j < 14; j++) {
	if (pi.getQuestStatus(quest[j]) == 1) {
	    for (i = 0; i < 10; i++) {
		if (pi.getPlayerCount(910540100 + mapid[j] + i) == 0) {
		    pi.warp(910540100 + mapid[j] + i,0);
		    pi.getPlayer().spawnAll();
		    return;
		}
	    }
	}
    }
}