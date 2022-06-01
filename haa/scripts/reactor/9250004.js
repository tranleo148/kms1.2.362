/*
벨룸 소환
*/
importPackage(java.lang);
importPackage(java.util);
importPackage(Packages.tools.packet);
var re;
var EtcTimer = Packages.server.Timer.EtcTimer;
var m1 = 0, m2 = 0, m3 = 0, m4 = 0, m5 = 0, m6 = 0, m7 = 0;

function act() {
	re = rm.getPlayer().getMap().getAllReactorsThreadsafe();
	rm.gainItem(4000884, -1);
	rm.getPlayer().dropMessage(5,"9250003\r\n");
	for(var i =0; i<7; i++){
		if(re[i].getReactorId() == 9250000){
			m1 = i
		}
		if(re[i].getReactorId() == 9250001){
			m2 = i
		}
		if(re[i].getReactorId() == 9250002){
			m3 = i
		}
		if(re[i].getReactorId() == 9250003){
			m4 = i
		}
		if(re[i].getReactorId() == 9250004){
			re[i].setState(1);
			rm.getPlayer().dropMessage(5,"i : "+i+"/ re : "+ re[i].getReactorId());
			m5 = i
		}
		if(re[i].getReactorId() == 9250005){
			m6 = i
		}
		if(re[i].getReactorId() == 9250090){
			m7 = i
		}
	}
	if(re[m1].getState() == 1 && re[m2].getState() == 1 && re[m3].getState() == 1 && re[m4].getState() == 1 && re[m5].getState() == 1 && re[m6].getState() == 1){
		re[m7].forceHitReactor(5,0);
		re[m7].forceHitReactor(6,0);
		rm.getMap().killAllMonsters(true);
		rm.getMap().partyrespawn();
		rm.getMap().spawnMonsterOnGroundBelow(Packages.server.life.MapleLifeFactory.getMonster(9300902), new java.awt.Point(-194, -188));
		rm.getMap().spawnMonsterOnGroundBelow(Packages.server.life.MapleLifeFactory.getMonster(9300906), new java.awt.Point(-213, -192));
		//mobspawn();
		var tick = 0;
		schedule = EtcTimer.getInstance().register(function () {
		if(tick == 5){
			schedule.cancel(true);
			return;
		} else {
			if(tick == 4){
				mobspawn();
			}
			rm.getMap().broadcastMessage(CField.startMapEffect("", 0, false));
			rm.getMap().broadcastMessage(CField.startMapEffect("월묘는 공격을 받지 않는 상태에서만 떡을 찧을 수 있다네! 월묘의 떡 80개를 모아주게!", 5120016, true));
		}
			tick++;
		}, 1000)
	}
}

function mobspawn(){
	var Mmobid = [9300903, 9300904, 9300905];
	var Mmobid1x = [585, -919, 599, -879];
	var Mmobid2x = [-838, 693, -835, 568];
	var Mmobid3x = [659, -925, -947, 587];
	
	var Mmobid1y = [-356, -267, -536, -539];
	var Mmobid2y = [-448, -374, -640, -627];
	var Mmobid3y = [-444, -495, -387, -263];

	for (var i = 0; i < 4; i++) {
			rm.getMap().spawnMonsterOnGroundBelow(Packages.server.life.MapleLifeFactory.getMonster(Mmobid[0]), new java.awt.Point(Mmobid1x[i], Mmobid1y[i]));
			rm.getMap().spawnMonsterOnGroundBelow(Packages.server.life.MapleLifeFactory.getMonster(Mmobid[1]), new java.awt.Point(Mmobid2x[i], Mmobid2y[i]));
			rm.getMap().spawnMonsterOnGroundBelow(Packages.server.life.MapleLifeFactory.getMonster(Mmobid[2]), new java.awt.Point(Mmobid3x[i], Mmobid3y[i]));
	}
}