/*
벨룸 소환
*/
importPackage(java.lang);
importPackage(java.util);
importPackage(Packages.tools.packet);
var re;

function act() {
	re = rm.getPlayer().getMap().getAllReactorsThreadsafe();
	var key1 = rm.getPlayer().getEventInstance().getProperty("key1");
	var key2 = rm.getPlayer().getEventInstance().getProperty("key2");
	var key3 = rm.getPlayer().getEventInstance().getProperty("key3");
	var key4 = rm.getPlayer().getEventInstance().getProperty("key4");
	rm.gainItem(4001528, -1);
	for(var i =0; i<re.length; i++){
		if(re[i].getState() == 1 && re[i].getName() == "key1" && key1 != "1"){
			rm.getPlayer().getEventInstance().setProperty("key1", "1");
		}
		if(re[i].getState() == 1 && re[i].getName() == "key2" && key2 != "1"){
			rm.getPlayer().getEventInstance().setProperty("key2", "1");
		}
		if(re[i].getState() == 1 && re[i].getName() == "key3" && key3 != "1"){
			rm.getPlayer().getEventInstance().setProperty("key3", "1");
		}
		if(re[i].getState() == 1 && re[i].getName() == "key4" && key4 != "1"){
			rm.getPlayer().getEventInstance().setProperty("key4", "1");
		}
		
		if(key1 != null && key2 != null && key3 != null){
			rm.getMap().broadcastMessage(CField.environmentChange("Map/Effect.img/quest/party/clear", 4));
			rm.getMap().broadcastMessage(CField.achievementRatio(70));
			rm.getMap().broadcastMessage(CField.startMapEffect("휴우. 덕분에 이 답답한 감옥을 벗어나게 되었군요.", 5120053, true));
			rm.getPlayer().getEventInstance().setProperty("stage5", "true");
			rm.getPlayer().getMap().spawnNpc(9020006, new java.awt.Point(2806, -139));
			return;
		}
	}
	rm.getPlayer().dropMessage(5, key1+"/"+key2+"/"+key3+"/"+key4);
}