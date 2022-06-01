importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server); 
importPackage(Packages.scripting);
importPackage(Packages.client);

var status = -1;
var index = 0;
var skillid = 80003036;
function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
		return;
	}
	if (mode == 0) {
		status--;
	}
	if (mode == 1) {
		d = status;
		status++;
	}

	if (status == 0) {
		if (cm.getPlayer().getKeyValue(100794, "today") < 3000 && !cm.getPlayer().isGM()) {
			cm.sendOkS("히잉... 햇살이 충분치 않으면 꽃들을 깨울 수 없어여...\r\n오늘의 #b#e#i4310310# #t4310310##k#n을 모두 모으고 다시 와주세여!", 4, 9062508)
			cm.getPlayer().dropMessage(5, "<블루밍 플라워> 블루밍 코인 일일 제한량을 달성 후 다시 시도해 주세요.");
			cm.dispose();
			return;
		}
		if (cm.getPlayer().getKeyValue(100794, "bloom") > 18 && !cm.getPlayer().isGM()) {
			cm.dispose();
			return;
		}
		if (cm.getPlayer().getKeyValue(501367, "reward") == 1 && !cm.getPlayer().isGM()) {
			cm.sendOkS("오늘은 이미 꽃을 피워주셨네여?\r\n잠든 꽃들을 깨우는 건 #e#r하루에 한 번#k#n이면 충분해여!", 4, 9062508)
			cm.dispose();
			return;
		}
		up = (cm.getPlayer().getKeyValue(501367, "bloom") + 1) % 3 == 0 ? 3 : 1;
		cm.getPlayer().setKeyValue(501367, "bloom",(cm.getPlayer().getKeyValue(501367, "bloom")  + 1) +"");
		cm.getPlayer().setKeyValue(501367, "giveSun",(cm.getPlayer().getKeyValue(501367, "giveSun")  + 1) +"");
		cm.getPlayer().setKeyValue(501367, "reward", "1");
		cm.getPlayer().setKeyValue(501378, "sp",(cm.getPlayer().getKeyValue(501378, "sp")  + up) +"");
		//계정체크용
		cm.getClient().setKeyValue("Bloomingbloom", cm.getPlayer().getKeyValue(501367, "bloom")+"");
		cm.getClient().setKeyValue("BloominggiveSun", cm.getPlayer().getKeyValue(501367, "giveSun")+"");
		cm.getClient().setKeyValue("BloomingReward", "1");
		cm.getClient().setKeyValue("BloomingSkillPoint", cm.getPlayer().getKeyValue(501378, "sp")+"");
		if (up == 3) {
			cm.getPlayer().setKeyValue(501387, "flower",(cm.getPlayer().getKeyValue(501387, "flower")  + 1) +"");
			cm.getClient().setKeyValue("Bloomingflower", cm.getPlayer().getKeyValue(501387, "flower")+"");
			cm.getClient().send(CField.setMapOBJ("all", 0, 0, 0));
			cm.getClient().send(CField.setMapOBJ(cm.getClient().getKeyValue("Bloomingflower"), 1, 0, 0));
			cm.getPlayer().setKeyValue(501367, "week",(cm.getPlayer().getKeyValue(501367, "week")  + 1) +"");
			cm.getClient().setKeyValue("week", cm.getPlayer().getKeyValue(501367, "week")+"");
		}
		cm.sendNextS("#e#b#h0##k#n님 덕분에!\r\n잠든 꽃봉오리가 활짝 피어났네여!!\r\n\r\n블루밍 축복으로 #e#r"+up+" 스킬 포인트#k#n를 드렸어여!\r\n활짝 피어난 #e#r블루밍 플라워 보상#k#n도 확인해 주세여~!", 4, 9062508)
		cm.dispose();
	}
}