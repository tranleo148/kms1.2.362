importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.io);

var mapid = [933011000, 933012000, 933013000, 933014000, 933015000, 933019000];
var stage2num = ["1110", "1101", "1011", "0111"];
var stage3num = ["11100", "11001", "10011", "00111", "01011", "01101", "01110", "10101"];
var ticket = 0;

var result = [0,0,0,0,0];
var strg = [];

var icon = "#fUI/UIWindow2.img/QuestIcon/3/0#"


function start() {
	St = -1;
	action(1, 0, 0);
}

function action(M, T, S) {
	if(M != 1) {
		cm.dispose();
		return;
	}

	if(M == 1)
	    St++;

	if(St == 0) {
        var getticket = cm.getPlayer().getKeyValue(210321, "ticket");
		if(cm.getPlayer().getMapId() == mapid[0]){
            if(cm.isLeader() && (cm.getPlayer().getMap().getKerningPQ()-2) != (cm.getParty().getMembers().size()-1) && (cm.getPlayer().getMap().getKerningPQ()-2) < (cm.getParty().getMembers().size()-1)){
                cm.sendNext("안녕하세요. 첫번째 스테이지에 오신 것을 환영합니다. 주변을 둘러보면 리게이터가 돌아다니고 있는 것을 볼 수 있을 겁니다. "
+"리게이터는 쓰러뜨리면 꼭 한개의 #b쿠폰#k을 떨어뜨립니다. 파티장을 제외한 파티원 전원은 각각 저에게 말을 걸어 문제를"
+"받고 문제의 답에 해당하는 수 만큼 리게이터가 주는 #b쿠폰#k을 모아와야 합니다.\r\n답만큼 #b쿠폰#k을 모아오면 미션을 완료하게 됩니다."
+"파티장을 제외한 모든 파티원이 #b개별 미션을 클리어#k 하면 스테이지를 클리어 하게 됩니다. 그럼 행운을 빕니다.");
                    cm.dispose();
                    return;
            } else if(cm.isLeader() && (cm.getPlayer().getMap().getKerningPQ()-2) == (cm.getParty().getMembers().size()-1)){
                    cm.sendNext("다음 스테이지로 통하는 포탈이 열렸습니다. 서둘러 주세요.");
                    cm.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.achievementRatio(15));
                    cm.environmentChange(true, "gate");
                    cm.getPlayer().getMap().setKerningPQ(cm.getPlayer().getMap().getKerningPQ()+1);
                    cm.dispose();
                    return;
            } else if(cm.isLeader() && (cm.getPlayer().getMap().getKerningPQ()-2) > (cm.getParty().getMembers().size()-1)){
                    cm.sendNext("다음 스테이지로 통하는 포탈이 열렸습니다. 서둘러 주세요.");
                    cm.dispose();
                    return;
        } else {
            if(getticket == 0){
                cm.sendNext("이곳에서는 개인별로 제가 내는 미션을 수행해야 합니다. 파티장을 제외한 모든 파티원이 #b개별 미션을 클리어#k하면 스테이지를 클리어 하게 됩니다. 되도록 빨리 해결해야 더 많은 스테이지에 도전할 수 있으므로 서둘러 주세요. 그럼 행운을 빕니다.");
            } else if(getticket != 0 && !cm.haveItem(4000886, getticket)){
                cm.sendNext("#r쿠폰 " + getticket + "장#k을 모아오세요. 쿠폰은 이 곳의 #r리게이터를 물리치면#k 얻을 수 있습니다.");
                cm.dispose();
                return;
            } else if(cm.haveItem(4000886, getticket) && getticket != -2){
                cm.sendNext("미션을 성공하여 #b통행권#k을 드렸습니다. 다른 파티원들이 통행권을 가져오는걸 도와주세요.");
                cm.gainItem(4000886, -getticket);
                cm.getPlayer().setKeyValue(210321, "ticket", -2);
                cm.getPlayer().getMap().broadcastMessage(CWvsContext.getTopMsg("통행권"+((cm.getPlayer().getMap().getKerningPQ()-2)+1)+"장을 모았습니다."));
                cm.getPlayer().getMap().setKerningPQ(cm.getPlayer().getMap().getKerningPQ()+1);
                if((cm.getPlayer().getMap().getKerningPQ()-2) == (cm.getParty().getMembers().size()-1)){
                    cm.getPlayer().getMap().broadcastMessage(CField.environmentChange("Map/Effect.img/quest/party/clear", 4));
                    cm.getPlayer().getMap().broadcastMessage(CField.startMapEffect("개별 미션을 모두 클리어 했습니다. 파티장은 저에게 말을 걸어 주세요.", 5120017, true));
                    cm.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.achievementRatio(15));
                }
                cm.dispose();
                return;
            } else if(getticket == -2){
                cm.sendNext("미션을 성공하셨습니다. 아직 미션을 수행하지 못한 다른 파티원이 있다면 그들의 미션을 도와주세요.");
                cm.dispose();
                return;
            }
        }
    } else if(cm.getPlayer().getMapId() == mapid[1]){
        var area1 = cm.getPlayer().getMap().getNumPlayersInArea(0);
        var area2 = cm.getPlayer().getMap().getNumPlayersInArea(1);
        var area3 = cm.getPlayer().getMap().getNumPlayersInArea(2);
        var area4 = cm.getPlayer().getMap().getNumPlayersInArea(3);
        var SCH = String(area1)+String(area2)+String(area3)+String(area4);
        if(cm.isLeader()){
            if(cm.getPlayer().getEventInstance().getProperty("stage2r") == null){
                cm.sendNext("안녕하세요. 두번째 스테이지에 오신 것을 환영합니다. 제 옆에 여러 개의 줄이 보일 것입니다. 이 줄 중에서 #b3개가 다음 스테이지로 향하는 포탈#k과 통해 있습니다. 파티원 중에서 #b3명이 정답 줄을 찾아 매달리면#k됩니다.\r\n단, 줄을 너무 아래쪽으로 잡고 매달리면 정답으로 인정되지 않으므로 줄을 잡고 충분히 위로 올라가 주시기 바랍니다. 그리고 반드시 3명만 줄을 잡고 있어야 합니다. 파티원이 줄에 매달리면 파티장은 #b저를 더블 클릭하여 정답인지 아닌지 확인#k하세요. 그럼 정답 줄을 찾아 주세요~!");
                var randomnum = Math.floor(Math.random() * stage2num.length);
                cm.getPlayer().getEventInstance().setProperty("stage2r", stage2num[randomnum]);
                cm.dispose();
                return;
            } else if(cm.getPlayer().getEventInstance().getProperty("stage2r") != null && cm.getPlayer().getEventInstance().getProperty("stage2r") != SCH && !cm.getPlayer().isGM()){// && !cm.getPlayer().isGM()
                cm.getPlayer().getMap().broadcastMessage(CField.environmentChange("Map/Effect.img/quest/party/wrong_kor", 4));
                cm.getPlayer().getMap().broadcastMessage(CField.playSound("Coconut/Failed"));
                //cm.getPlayer().dropMessage(5, cm.getPlayer().getEventInstance().getProperty("stage2r"));
                cm.dispose();
                return;
            } else if((cm.getPlayer().getEventInstance().getProperty("stage2r") != null && cm.getPlayer().getEventInstance().getProperty("stage2r") == SCH) || cm.getPlayer().isGM()){//
                cm.sendNext("다음 스테이지로 통하는 포탈이 열렸습니다. 서둘러 주세요.");
                if(cm.getPlayer().getEventInstance().getProperty("stage2r") != "true"){
                    cm.getPlayer().getMap().broadcastMessage(CField.environmentChange("Map/Effect.img/quest/party/clear", 4));
                    cm.getPlayer().getEventInstance().setProperty("stage2r", "true");
                    cm.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.achievementRatio(30));
                    cm.environmentChange(true, "gate");
                }
                cm.dispose();
                return;
            }
        } else {
            if(cm.getPlayer().getEventInstance().getProperty("stage2r") == "true"){
                cm.sendNext("다음 스테이지로 통하는 포탈이 열렸습니다. 서둘러 주세요.");
                cm.dispose();
                return;
            } else {
                cm.sendNext("안녕하세요. 두번째 스테이지에 오신 것을 환영합니다. 제 옆에 여러 개의 줄이 보일 것입니다. 이 줄 중에서 #b3개가 다음 스테이지로 향하는 포탈#k과 통해 있습니다. 파티원 중에서 #b3명이 정답 줄을 찾아 매달리면#k됩니다.\r\n단, 줄을 너무 아래쪽으로 잡고 매달리면 정답으로 인정되지 않으므로 줄을 잡고 충분히 위로 올라가 주시기 바랍니다. 그리고 반드시 3명만 줄을 잡고 있어야 합니다. 파티원이 줄에 매달리면 파티장은 #b저를 더블 클릭하여 정답인지 아닌지 확인#k하세요. 그럼 정답 줄을 찾아 주세요~!");
                cm.dispose();
                return;
            }
        }
    } else if(cm.getPlayer().getMapId() == mapid[2]){
        if(cm.isLeader()){
            if(cm.getPlayer().getEventInstance().getProperty("stage3r") != null){
            stage3area();
            var SCH2 = stage3area();
            var SCH3 = stage3check();
            }
            if(cm.getPlayer().getEventInstance().getProperty("stage3r") == null){
                cm.sendNext("안녕하세요. 세번째 스테이지에 오신 것을 환영합니다. 옆에 고양이가 들어있는 통이 놓인 발판들이 보일 것 입니다. 이 발판 중에서 #b3개가 다음 스테이지로 향하는 포탈#k과 통해 있습니다. 파티원 중에서 #b3명이 정답 발판을 찾아 위에 올라서면#k 됩니다.\r\n단, 발판 끝에 아슬 아슬하게 걸쳐서 거지 말고 발판 중간에 서야 정답으로 인정되니 이 점 주의 해주시기 바랍니다. 그리고 반드시 3명만 발판 위에 올라가 있어야 합니다.");
                var randomnum = Math.floor(Math.random() * stage3num.length);
                cm.getPlayer().getEventInstance().setProperty("stage3r", stage3num[randomnum]);
                cm.dispose();
                return;
            } else if(cm.getPlayer().getEventInstance().getProperty("stage3r") == "true"){
                cm.sendNext("다음 스테이지로 통하는 포탈이 열렸습니다. 서둘러 주세요.");
                cm.dispose();
                return;
            } else if(cm.getPlayer().getEventInstance().getProperty("stage3r") != null && cm.getPlayer().getEventInstance().getProperty("stage3r") != SCH2){// && !cm.getPlayer().isGM()
                cm.getPlayer().getMap().broadcastMessage(CField.environmentChange("Map/Effect.img/quest/party/wrong_kor", 4));
                cm.getPlayer().getMap().broadcastMessage(CField.playSound("Coconut/Failed"));
                cm.getPlayer().getMap().broadcastMessage(CWvsContext.getTopMsg("현재"+SCH3+"개의 정답 발판을 선택하셧습니다."));
                cm.getPlayer().getMap().broadcastMessage(CWvsContext.serverNotice(6, "", "현재"+SCH3+"개의 정답 발판을 선택하셧습니다."));
                cm.dispose();
                return;
            } else if((cm.getPlayer().getEventInstance().getProperty("stage3r") != null && cm.getPlayer().getEventInstance().getProperty("stage3r") == SCH2) ){//|| cm.getPlayer().isGM()
                cm.sendNext("다음 스테이지로 통하는 포탈이 열렸습니다. 서둘러 주세요.");
                if(cm.getPlayer().getEventInstance().getProperty("stage3r") != "true"){
                    cm.getPlayer().getMap().broadcastMessage(CField.environmentChange("Map/Effect.img/quest/party/clear", 4));
                    cm.getPlayer().getEventInstance().setProperty("stage3r", "true");
                    cm.getPlayer().getMap().broadcastMessage(CField.achievementRatio(50));
                    cm.environmentChange(true, "gate");
                }
                cm.dispose();
                return;
            }
        } else {
            if(cm.getPlayer().getEventInstance().getProperty("stage3r") == "true"){
                cm.sendNext("다음 스테이지로 통하는 포탈이 열렸습니다. 서둘러 주세요.");
                cm.dispose();
                return;
            } else {
                cm.sendNext("안녕하세요. 세번째 스테이지에 오신 것을 환영합니다. 옆에 고양이가 들어있는 통이 놓인 발판들이 보일 것 입니다. 이 발판 중에서 #b3개가 다음 스테이지로 향하는 포탈#k과 통해 있습니다. 파티원 중에서 #b3명이 정답 발판을 찾아 위에 올라서면#k 됩니다.\r\n단, 발판 끝에 아슬 아슬하게 걸쳐서 거지 말고 발판 중간에 서야 정답으로 인정되니 이 점 주의 해주시기 바랍니다. 그리고 반드시 3명만 발판 위에 올라가 있어야 합니다.");
                cm.dispose();
                return;
            }
        }
    } else if(cm.getPlayer().getMapId() == mapid[3]){
                cm.dispose();
                return;
    } else if(cm.getPlayer().getMapId() == mapid[4]){
            var em = cm.getEventManager("KerningPQ");
            if (em.getProperty("stage5") == null) {
                cm.sendNext("안녕하세요. 다섯번째, 마지막 스테이지에 오신 것을 환영합니다. 이번 스테이지는 이 곳의 최고 보스인 #r킹슬라임#k을 물리치면 됩니다. 그럼 힘내주세요!");
                cm.dispose();
                return;
            } else {
                var talk = "축하드립니다. 모든 스테이지를 클리어 하였습니다. 이제 이 곳에서 더 이상 볼 일이 없으시면, 밖으로 보내 드리겠습니다.";
                talk +="#r(보상이 지급 됩니다.)#k\r\n\r\n";
                talk +=icon+"\r\n";
                talk +="#L0# #i2437558# #z2437558#를 받는다.\r\n";
                talk +="#L1# #i4036661# #z4036661#를 받는다.\r\n";
                cm.sendSimple(talk);
            }
    }
	} else if(St == 1){
        var getticket = cm.getPlayer().getKeyValue(210321, "ticket");
        if(cm.getPlayer().getMapId() == mapid[0]){
            if(!cm.isLeader() && getticket == 0){
               var random = Math.floor(Math.random() * (10-5)+5);
               ticket = cm.getPlayer().setKeyValue(210321, "ticket", random);
               cm.sendNext("미션입니다. #r쿠폰 " + cm.getPlayer().getKeyValue(210321, "ticket") + "장#k을 모아오세요. 쿠폰은 이 곳의 #r리게이터를 물리치면#k 얻을 수 있습니다."); 
                cm.dispose();
               return;
            }
        } else if(cm.getPlayer().getMapId() == mapid[4]){
            if(S == 0){
                 cm.gainItem(2437558,1);
                cm.gainItem(4036661,1);
                cm.warp(mapid[5]);
                cm.dispose();
            } else {
                cm.gainItem(2437558,1);
                cm.gainItem(4036661,1);
                cm.warp(mapid[5]);
                cm.dispose();
            }
            return;
        }
    }
}

function stage3area(){
    var area = [];
    var Iarea = [];
    var SCH = "";
    var str = cm.getPlayer().getEventInstance().getProperty("stage3r");

    for(var i = 0; i<5; i++){
        area[i] = cm.getPlayer().getMap().getNumPlayersInArea(i);
        Iarea[i] = cm.getPlayer().getMap().getNumPlayersItemsInArea(i);
        if(area[i] == 1){
            result[i] = area[i];
        } else if(Iarea[i] == 1){
            result[i] = Iarea[i];
        }
        SCH +=String(result[i]);
        
        strg[i] = str.slice(i,i+1);
    }
    return SCH;
}

function stage3check(){
    var rr = 0;
    for(var i = 0; i<5; i++){
        if(result[i] == strg[i] && (result[i] !=0 || strg[i] != 0)){
            rr+=1;
        }
    }
    return rr;
}

