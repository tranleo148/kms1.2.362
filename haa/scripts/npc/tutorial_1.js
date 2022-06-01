var status = -1;

function start() {

    action(1, 0, 0);
}

function action(mode, type, selection) {
    dialogue = [
	        "#r칸#k 에서의 시간을 즐거우신가요? 제 작은 도움이 #b#h ##k 님에게 유용하다면 정말 기쁠거같네요.\r\n#r칸#k 에서 사용하실 수 있는 간단한 명령어와 몇가지 팁을 알려드릴게요!",
	        "#b#e<유저 명령어>#k#n\r\n\r\n#d#e@도움말#n : 유저가 사용할 수 있는 명령어리스트를 출력합니다\r\n#e@마을,광장#n : '칸 광장' 으로 이동합니다\r\n#e@동접#n : 현재 칸 에 접속한 유저를 출력합니다\r\n#e@힘,덱스,인트,럭#n : 기입한 숫자만큼 스탯을 추가합니다\r\n#e@인벤초기화#n : 장비/소비/설치/기타/캐시/장착 슬롯을 초기화합니다\r\n#e@명성치알림#n : 명성치 메세지 알림 on/off",
	        "#r#e<실용적인(?) 팁>#k#n\r\n\r\n#b[1]#k 채팅에 '~할말' 이라고 치면 전체 채팅을 입력할 수 있다(할말 부분에 하고싶은 말을 쓰면된다)\r\n#b[2]#k 키보드 #e'1'#n 좌측에 #e`#n 을 누르면 트레이드 창을 열 수 있다\r\n#b[3]#k부캐릭을 육성해 #b'링크, 유니온'#k 능력치는 필수로 챙기는것이 좋다\r\n#b[4]#k 200레벨 이후 #b아케인리버(여로, 츄츄, 레헬른 등)#k 사냥터는 #d아케인심볼#k이 필요하다"
	     ];
    if (mode == 1) {
        if (cm.getPlayer().getKeyValue(20190721, "tutostatus") == 0 && status == -1 && cm.itemQuantity(4031191) < 1) {
            cm.dispose();
            cm.openNpc(2074115);
            return;
        } else if (status == 3) {
            cm.dispose();
            cm.openNpc(2074115);
            return;
        } else {
            status++;
        }
    } else {
        cm.dispose();
        return;
    }
    if (cm.getPlayer().getKeyValue(20190721, "tutostatus") == -1) {
        if (status < dialogue.length) {
            cm.sendNext(dialogue[status]);
        } else if (status == 3) {
            cm.sendSimple("제가 드리는 여러가지 #b#e꿀팁!#k#n 잘 챙기셨나요? 그럼 숙지 다 하셨을거라 믿고 테스트 한번 해보겠습니다! 여러분의 테스트를 도와줄 #bNPC '고양이'#k를 소개해드릴게요!\r\n\r\n#d * #e@튜토리얼#n 명령어를 통해 언제든지 저를 호출하실 수 있습니다");
            cm.getPlayer().setKeyValue(20190721, "tutostatus", 0);
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
            cm.sendSimple("음.. 아직 제가 드린 숙제를 #r다 못하신거 같은데요? #b고양이 NPC#k가 내는 퀴즈를 전부 맞추고 돌아오세요!\r\n\r\n#d * #e@튜토리얼#n 명령어를 통해 언제든지 저를 호출하실 수 있습니다 " + status);
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
           if (cm.itemQuantity(4031191) >= 1) {
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