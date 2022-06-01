var status = -1;

function start() {

    action(1, 0, 0);
}

function action(mode, type, selection) {
    dialogue = [
	        "퀴즈가 어렵진않았죠? 계속해서 이번엔 #e'빠른이동'#n 에 대해서 알아볼게요.\r\n\r\n#e'빠른이동' 란?#n\r\n#r캐시샵(`)#k 을 누르면 나오는 인터페이스 창을 통칭 빠른이동(트레이드) 라고 부릅니다. 5개의 메뉴로 이루어져 있으며 간단히 설명을 해드릴게요",
                        "#e#b<트레이드>#n#k\r\n\r\n #d#e- 코디 시스템#n : 헤어&성형 변경, 검색 캐시 등 캐릭터를 꾸미는데 도움을 주는 시스템이다.\r\n #d#e- 워프 시스템#n : 다른 맵으로 편리하게 이동할때 사용한다. 사냥터, 마을 등 필수적인 맵을 갈때 빠르게 이동할 수 있다.\r\n #d#e- 편의 시스템#n : 각종 편의 기능과 강화, 다양한혜택을 이용할 수 있다.\r\n #d#e- 상점 시스템#n : 다양한 장비, 아이템 , 제작을 이용할 수 있다.\r\n #d#e- 컨텐츠 시스템#n : 사냥이 지루할 때 한번씩 해볼 수 있는 재밌는 컨텐츠를 이용할 수 있다."
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
            cm.sendOk("트레이드에 대해서 숙지 잘 하셨나요? 그럼 워프시스템을 이용해서 #b골드비치#k 에 있는 #r'토푸' NPC#k 에게 #b장난감 공#k 을 받아와주세요!\r\n\r\n#d * #e@튜토리얼#n 명령어를 통해 언제든지 저를 호출하실 수 있습니다");
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
            cm.sendOk("음.. 아직 제가 드린 숙제를 #r다 못하신거 같은데요?#k 워프시스템을 이용해서 #b골드비치#k 에 있는 #r'토푸' NPC#k 에게 #b장난감 공#k 을 받아와주세요!\r\n\r\n#d * #e@튜토리얼#n 명령어를 통해 언제든지 저를 호출하실 수 있습니다");
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