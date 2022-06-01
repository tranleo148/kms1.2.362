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
        var john = "#fn나눔고딕 Extrabold##b만드라고라#k... 참 매혹적인 생물이야..\r\n\r\n";
    if (!cm.haveItem(4034685,1)) {
        john += "#fs11##fUI/UIWindow2.img/UtilDlgEx/list1#\r\n#fn굴림##L1##d크리슈라마의 만드라고라";
    } else {
        john += "#fs11##fUI/UIWindow2.img/UtilDlgEx/list3#\r\n#fn굴림##L2##d크리슈라마의 만드라고라";
    }
        cm.sendSimple(john);
    } else {
        cm.dispose();
    }
    } else if (status == 1) {
            if (selection == 2 ) {
	cm.sendOk("#fn나눔고딕 Extrabold#역시 #b만드라고라#k 는 존재하지 않았군요..\r\n아쉽지만 어쩔수 없군요.\r\n이건 제 보답 입니다, 사양 말고 받도록 하세요.\r\n\\r\n#fUI/UIWindow2.img/QuestIcon/4/0#\r\n\r\n#i4021031# #b뒤틀린 시간의 정수#k #r100 개#k");
	cm.gainItem(4034685,-1);
	cm.gainItem(4021031, 100);
	cm.forceStartQuest(502);
	cm.showEffect(false,"monsterPark/clear");
        cm.playSound(false,"Field.img/Party1/Clear");
	cm.dispose();
        } else {
        cm.sendNextS("#fn나눔고딕 Extrabold##b루팡의 바나나... 으.. 최악이야!\r\n다음은... #fs14#크리슈라마#fs12# 라는 사람이였지..?#k",2);
        }
    } else if (status == 2) {
        cm.sendNextPrev("#fn나눔고딕 Extrabold#음.. 정말 궁금하단 말이야.. #b만드라고라#k 가 실존 하는 것일까?..");
    } else if (status == 3) {
        cm.sendNextPrevS("#fn나눔고딕 Extrabold##b혹시, #fs14#크리슈라마#fs12# 님 되시나요?#k", 2);
    } else if (status == 4) {
        cm.sendNextPrev("#fn나눔고딕 Extrabold#엇, 오셨군요! 음.. 제가 부탁드릴것은 #b만드라고라#k 라는 식물의\r\n존재 여부를 확인해 주시는것인데.. 그것이 #b끈기의 숲#k 정상에서\r\n자라고 있다고만 소문으로 널리 알려져 있답니다..\r\n그대가.. 그 위대한 진실을.. 파헤처 주시면 됩니다..");
    } else if (status == 5) {
        cm.sendYesNoS("#fn나눔고딕 Extrabold##d(갑작스럽지만.. 도와드려야 하긴 했어야 하니 도와드릴까..?)#k", 2);
    } else if (status == 6) { 
       cm.sendYesNo("#fn나눔고딕 Extrabold#그럼 지금 바로!.. 서둘러 #b끈기의 숲#k 끝자락..\r\n정상에 #b만드라고라#k 가 있는지 확인 해주세요.\r\n확인이 되신 후 오신다면 보상을 드리겠습니다.#k\r\n\r\n#fUI/UIWindow2.img/QuestIcon/3/0#\r\n#i4021031# #b뒤틀린 시간의 정수#k #r100 개#k\r\n\r\n#d그럼 지금 바로 끈기의 숲으로 출발 하시겠습니까..??#k");
    } else if (status == 7) { 
        cm.warp(910530200,0);
        cm.sendOk("#fn나눔고딕 Extrabold#정상을 확인 후에 저에게 와주세요. 그럼 부탁 드립니다..");
      }
    }
