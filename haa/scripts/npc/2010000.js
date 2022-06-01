var status = -1;

function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
    if (cm.getPlayer().getMapId() == 100030301) {
        var john = "#fn나눔고딕 Extrabold#작전 명! 폐광 탐사 작전 이다! 오늘도 힘차게!\r\n그나저나 #b이지병장#k 은 왜 안보이는거지?\r\n\r\n";
    if (!cm.getQuestStatus(504) == 1) {
        john += "#fs11##fUI/UIWindow2.img/UtilDlgEx/list1#\r\n#fn굴림##L1##d찰리중사의 폐광 탐사 작전";
    } else {
        john += "#fs11##fUI/UIWindow2.img/UtilDlgEx/list3#\r\n#fn굴림##L2##d찰리중사의 폐광 탐사 작전";
    }
        cm.sendSimple(john);
    } else {
        cm.dispose();
    }
    } else if (status == 1) {
            if (selection == 2 ) {
	cm.sendOk("#fn나눔고딕 Extrabold##b이지병장#k !!.. 이 녀석... 아! 아무튼...\r\n이것은 내 작은 보답이라네..\r\n사양말고 받도록 하게나..!\r\n\\r\n#fUI/UIWindow2.img/QuestIcon/4/0#\r\n\r\n#i4021031# #b뒤틀린 시간의 정수#k #r100 개#k");
	cm.gainItem(4021031, 100);
        cm.forfeitQuest(504);
	cm.forceStartQuest(503);
	cm.showEffect(false,"monsterPark/clear");
        cm.playSound(false,"Field.img/Party1/Clear");
	cm.dispose();
        } else {
        cm.sendNextS("#fn나눔고딕 Extrabold##b에휴.. 괜한 호기심 영감 때문에.. 고생만 했네..\r\n다음분은.. #fs14#찰리중사#fs12# 라는 사람이였지..?",2);
        }
    } else if (status == 2) {
        cm.sendNextS("#fn나눔고딕 Extrabold##d(히익.. 무섭게 생기신 분이다. 분명 어려운 일 이겠지?..)#k\r\n\r\n#b안녕하세요. #e찰리중사#n님! 저는 요즘.. 가장 값 나가는.. 모험가!\r\n#h # (이)라고 합니다. 만나뵈서 정말 영광입니다.!#k",2);
    } else if (status == 3) {
        cm.sendNextPrev("#fn나눔고딕 Extrabold#어라!? 자넨 처음보는 얼굴이로군!! 흐으음..\r\n그래.. 생긴 것이 마음에 안들긴 하지만 기합 하나는 끝내주는구만!");
    } else if (status == 4) {
        cm.sendNextPrevS("#fn나눔고딕 Extrabold##d(생긴게 뭐? 죽일ㄱㅏ.. 아냐..참자.. 보상받자..보상받자..)#k\r\n\r\n#b하하! 원래 생긴것이 웃기다는 소리를 많이 들었습니다!\r\n그나저나, #fs14#찰리중사#fs12# 님께서 곤란한 일에 처해 있으시다고...?#k",2);
    } else if (status == 5) {
        cm.sendNextPrev("#fn나눔고딕 Extrabold#응? 그것을 어떻게 알았지? 실은.. #b이지병장#k 이..\r\n폐광을 탐사 하러 갔는데 지금까지 돌아오지 않고 있어서 말이야..\r\n그게.. 중사인 나로선 #b이지병장#k 을 꼭 찾아야 하지! 큰일이야 큰일...");
    } else if (status == 6) { 
       cm.sendNextS("#fn나눔고딕 Extrabold##b정.. 그러시다면... 제가 도와드리겠습니다.!#k", 2);
    } else if (status == 7) { 
       cm.sendYesNo("#fn나눔고딕 Extrabold#흠.. 생긴 것도 그렇고.. 위험 하지만 자네가 한다니, 어쩔수 없군..\r\n#b폐광#k 에 대해 설명 해주겠네.. #b폐광#k 은 총 2 단계로 되있고, 이제.. 그 안에서 #b이지병장#k 을 찾아 나에게 와주면 된다네..\r\n그 후에 나에게 오면 자네에게 알맞는 보상을 주도록 하지\r\n\r\n#fUI/UIWindow2.img/QuestIcon/3/0#\r\n#i4021031# #b뒤틀린 시간의 정수#k #r100 개#k\r\n\r\n#d그럼 지금 바로 폐광으로 출발 하겠어..??#k");
    } else if (status == 8) { 
        cm.warp(280020000,0);
        cm.sendOk("#fn나눔고딕 Extrabold##b이지병장#k 을 찾은 후에 나에게 오게나. 그럼 부탁하네 #b#h ##k !!");
      }
    }
