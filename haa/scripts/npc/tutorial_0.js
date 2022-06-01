var status = -1;

function start() {

    action(1, 0, 0);
}

function action(mode, type, selection) {
    dialogue = [
	        "#b#h ##k 님 안녕하세요? 이 튜토리얼은 #b#h ##k 님이 #r칸#k에 적응하기 쉽게 도와드리는 역할을 하고 있습니다. 지금부터 저와 함께 간단한 퀘스트들을 통해서 칸의 모든 시스템을 알아보겠습니다!",
	        "\r\n#r칸#k는 #e'하자지향서버'#n 으(로) 사냥과 레벨업이 기본적인 컨텐츠 입니다!\r\n레벨업 부터 시작해야하는 당신! 첫 시작은 간단하게 #e몬스터 사냥#n 부터 시작해볼까요?"
	     ];
    if (mode == 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }
    if (cm.getPlayer().getKeyValue(20190721, "tutostatus") == -1) {
        if (status < dialogue.length) {
            cm.sendNext(dialogue[status]);
        } else {
            cm.sendOk("#b#o5130101##k 을(를) 처치해 #i4000022# #b#z4000022# 30개#k 을(를) 모아와 주세요!\r\n\r\n#d * #e@튜토리얼#n 명령어를 통해 언제든지 저를 호출하실 수 있습니다");
            cm.getPlayer().setKeyValue(20190721, "tutostatus", 0);
            cm.dispose();
        }
    } else {
        getq = cm.getPlayer().getKeyValue(20190721, "tutorial") + 1;
        if (isQuestCompleteable(getq)) {
            cm.sendOk("#b#e수고하셨습니다!#k#n\r\n몬스터를 사냥해서 #b전리품#k을 모아오기. 자주 등장하는 퀘스트니 잊지말도록해요.\r\n\r\n#fUI/UIWindow2.img/QuestIcon/4/0#\r\n#e#r30 칸 포인트#k#n");
            cm.getPlayer().setKeyValue(20190721, "tutorial", cm.getPlayer().getKeyValue(20190721, "tutorial") + 1);
            cm.getPlayer().setKeyValue(20190721, "tutostatus", -1);
            itembyQuest(getq);
        } else {
            cm.sendOk("음.. 아직 제가 드린 숙제를 #r다 못하신거 같은데요?#k\r\n#b#o5130101##k 을(를) 처치해 #i4000022# #b#z4000022# 30개#k 을(를) 모아와 주세요!\r\n\r\n#d * #e@튜토리얼#n 명령어를 통해 언제든지 저를 호출하실 수 있습니다");
        }
        cm.dispose();
        return;
    }
}

function isQuestCompleteable(getq) {
    switch (getq) {
        case 0:
            if (cm.itemQuantity(4000022) >= 30) {
                return true;
            }
            return false;
            break;
        case 1:
            if (퀴즈) {
                return true;
            }
            return false;
            break;
        case 2:
            if (cm.itemQuantity(4033622) >= 1) {
                return true;
            }
            return false;
            break;
        case 3:
            if (cm.itemQuantity(5000708) >= 1) {
                return true;
            }
            return false;
            break;
        case 4:
            if (미니게임) {
                return true;
            }
            return false;
            break;
        case 5:
            if (cm.itemQuantity(1082102) >= 1) {
                return true;
            }
            return false;
            break;
        case 6:
            if (cm.getPlayer().getKeyValue(20190721, "tutostatus") == 1 && cm.itemQuantity(4033302) >= 1) { // 자쿰 이벤트스크립트확인
            return true;
            }
            return false;
            break;
        case 7:
            if (cm.getPlayer().getLevel() >= 200) {
                return true;
            }
            return false;
            break;
        default:
            return true;
            break;
    }
}

function itembyQuest(getq) {
    switch (getq) {
         case 0:
             cm.getPlayer().AddStarDustCoin(50);
             cm.gainItem(4000022, -30);
             break;
         case 1:
             cm.getPlayer().AddStarDustCoin(50);
             cm.gainItem(4031191, -1);
             break;
         case 2:
             cm.getPlayer().AddStarDustCoin(50);
             cm.gainItem(4033622, -1);
             break;
         case 3:
             cm.getPlayer().AddStarDustCoin(50);
             break;
         case 4:
             cm.getPlayer().AddStarDustCoin(50);
             break;
         case 5:
             cm.getPlayer().AddStarDustCoin(50);
             break;
         case 6:
             cm.getPlayer().AddStarDustCoin(50);
             cm.gainItem(4033302,-1);
             break;
         case 7:
             cm.gainItem(2435719,10);
             cm.gainItem(1712001,1);
             cm.gainItem(2439302,1);
             cm.gainItem(2450130,2);
             cm.gainItem(4001715,1);
             break;
         default:
         break;
    }
}