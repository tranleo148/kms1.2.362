/*
 * 프로젝트 : 1.2.214 SpiritStyle
 * Script Author : 하요(ifhayo)4001832
 * 이 주석은 지우지 않아주셨으면 좋겠습니다.
 *
 */


importPackage(Packages.server.life); 
importPackage(Packages.tools.RandomStream);

var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
	if (status == 0) {
	mC = Randomizer.rand(1,10);
		switch(mC) {
		case 1:
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		break;

		case 2:
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		break;

		case 3:
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		break;

		case 4:
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		break;

		case 5:
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		break;

		case 6:
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		break;

		case 7:
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		break;

		case 8:
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		break;

		case 9:
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		break;

		case 10:
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		cm.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeProvider.getMonster(8630038), cm.getPlayer().getPosition()); 
		break;
	}
	cm.gainItem(2431974, -1);
	cm.dispose();
	}
}
}