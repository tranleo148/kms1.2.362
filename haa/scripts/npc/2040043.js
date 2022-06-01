importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.io);

var St = -1;
var seld = -1;
var icon = "#fUI/UIWindow2.img/QuestIcon/3/0#"

var quize = [
["20*30+15", "100011000"],
["30*10+98", "001000011"],
["900/2+98", "000100011"],
["892-369", "011010000"],
["50*10+80-4", "000011100"],
["5*60+5*5", "011010000"],
];

var quize1;
var SCH2, SCH3;

var result = [0,0,0,0,0,0,0,0,0];
var strg = [];

//루피 파퀘 퇴장
function start() {
	St = -1;
	action(1, 0, 0);
}

function action(M, T, S) {
	if(M == -1) {
		cm.dispose();
		return;
	}
	if (M == 0) {
        St--;
		cm.dispose();
		return;
    }
	if(M == 1){
	    St++;
	}
	if(St == 0) {
		if(cm.getPlayer().getEventInstance().getProperty("stage5r") == "true"){
			cm.sendOk("다음 스테이지로 이동해주세요.");
			cm.dispose();
			return;
		}
		var talk = "";
		if(cm.getPlayer().getEventInstance().getProperty("stage5r") == null) {
			talk += "다섯번째 스테이지에 대해 설명해 드리겠습니다. \r\n";
			talk +="이곳에는 여러개의 발판이 있습니다. 이 발판 중에서 #b다음 스테이지로 향하는 포탈#k과 통해 있습니다.";
			talk +="파티원 중에서 #b정답 발판을 찾아 위에 올라서면#k 됩니다.\r\n";
			talk +="단, 발판 끝에 아슬아슬하게 걸쳐서 서지 말고  발판 중간에 서야 정답으로 인정되니 이점 주의해 주시기 바랍니다.";
			talk +="그리고 반드시 발판 위에 올라가 있어야 합니다. 파티원이 발판에 올라서면 #b파티장은 저를 더블클릭하여 정답인지 아닌지 확인#k해야 합니다.";
			talk +="그럼 힘내 주세요!\r\n";
			cm.getPlayer().getEventInstance().setProperty("stage5r", "1");
		} else if(cm.getPlayer().getEventInstance().getProperty("stage5r") == "2") {
			if(cm.getPlayer().getEventInstance().getProperty("stage5r") == 2){
				stage5area();
				SCH2 = stage5area();
				//var SCH3 = stage5check();//안씀
            }
			SCH3 = cm.getPlayer().getEventInstance().getProperty("stage5quize");
            if(cm.getPlayer().getEventInstance().getProperty("stage5r") != null && quize[SCH3][1] != SCH2){// && !cm.getPlayer().isGM()
				cm.getPlayer().getMap().broadcastMessage(CField.environmentChange("Map/Effect.img/quest/party/wrong_kor", 4));
                cm.getPlayer().getMap().broadcastMessage(CField.playSound("Coconut/Failed"));
				cm.getPlayer().getMap().broadcastMessage(CField.startMapEffect(""+quize[SCH3][0]+"", 5120018, true));
                cm.dispose();
                return;
            } else if((cm.getPlayer().getEventInstance().getProperty("stage5r") != null && quize[SCH3][1] == SCH2) ){//|| cm.getPlayer().isGM()
                cm.sendNext("다음 스테이지로 통하는 포탈이 열렸습니다. 서둘러 주세요.");
                if(cm.getPlayer().getEventInstance().getProperty("stage5r") != "true"){
                    cm.getPlayer().getMap().broadcastMessage(CField.environmentChange("Map/Effect.img/quest/party/clear", 4));
                    cm.getPlayer().getEventInstance().setProperty("stage5r", "true");
                    cm.getPlayer().getMap().broadcastMessage(CField.achievementRatio(80));
                    cm.environmentChange(true, "gate");
                }
                cm.dispose();
                return;
            }
		}
		cm.sendNext(talk);
    } else {
		if(St == 1){
			if(cm.getPlayer().getEventInstance().getProperty("stage5r") == "1"){
				var talk = "";
				quize1 = Math.floor(Math.random() * quize.length);
				cm.getPlayer().getEventInstance().setProperty("stage5quize", ""+quize1);
				talk +="제가 내는 퀴즈에 해당하는 3가지 숫자가 #r다음 스테이지로 향하는 포탈을 여는 열쇠#k입니다.\r\n";
				talk +="#r"+quize[quize1][0]+"= ?#k\r\n";
				talk +="정답을 맞춰주세요.\r\n";
				cm.getPlayer().getEventInstance().setProperty("stage5r", "2");
				cm.sendOk(talk);
				cm.dispose();
                return;
			} else {
				cm.dispose();
			}
			
			
		}
	}
}


function stage5area(){
    var area = [];
    var Iarea = [];
    var SCH = "";
    var str = cm.getPlayer().getEventInstance().getProperty("stage5quize");

    for(var i = 0; i<9; i++){
        area[i] = cm.getPlayer().getMap().getNumPlayersInArea(i); // 플레이어
        Iarea[i] = cm.getPlayer().getMap().getNumPlayersItemsInArea(i); //아이템
        if(area[i] == 1){
            result[i] = area[i];
        } else if(Iarea[i] == 1){
            result[i] = Iarea[i];
        }
        SCH +=String(result[i]);
		
        
        //strg[i] = str.slice(i,i+1);
    }
	
    return SCH;
}

function stage5check(){
    var rr = 0;
    for(var i = 0; i<9; i++){
        if(result[i] == strg[i] && (result[i] !=0 || strg[i] != 0)){
            rr+=1;
        }
    }
    return rr;
}