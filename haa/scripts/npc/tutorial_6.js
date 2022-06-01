var status = -1;

function start() {

    action(1, 0, 0);
}

function action(mode, type, selection) {
    dialogue = [
                        "자 여기까지 따라오신 #b#h ##k 님에게 박수! 짝짝짝! 이쯤에서 슬슬 보스몬스터를 잡아볼까요?",
	        "오늘 잡을 보스는 #b동네북 '자쿰'#k 입니다!\r\n워프 시스템의 보스메뉴를 이용하여 #b자쿰#k을 처치하러 가시면됩니다!"
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
            cm.sendOk("자쿰을 잡고서 나오는 #i4033302# #b#z4033302##k 을(를) 가져오시면됩니다!\r\n\r\n#d * #e@튜토리얼#n 명령어를 통해 언제든지 저를 호출하실 수 있습니다");
            cm.getPlayer().setKeyValue(20190721, "tutostatus", 0);
            cm.dispose();
        }
    } else {
        getq = cm.getPlayer().getKeyValue(20190721, "tutorial") + 1;
        if (isQuestCompleteable(getq)) {
            cm.sendOk("#b#e수고하셨습니다!#k#n\r\n보상으로 포인트를 드릴게요!\r\n\r\n#fUI/UIWindow2.img/QuestIcon/4/0#\r\n#r#e30 칸 포인트#n#k");
            cm.getPlayer().setKeyValue(20190721, "tutorial", cm.getPlayer().getKeyValue(20190721, "tutorial") + 1);
            cm.getPlayer().setKeyValue(20190721, "tutostatus", -1);
            itembyQuest(getq);
            cm.dispose();
            return;
        } else {
            cm.sendOk("음.. 아직 제가 드린 숙제를 #r다 못하신거 같은데요?#k 자쿰을 잡고서 나오는 #i4033302# #b#z4033302##k 을(를) 가져오시면됩니다!\r\n\r\n#d * #e@튜토리얼#n 명령어를 통해 언제든지 저를 호출하실 수 있습니다");
            cm.dispose();
        }
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
            if (cm.itemQuantity(4033302) >= 1) { // 자쿰 이벤트스크립트확인
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