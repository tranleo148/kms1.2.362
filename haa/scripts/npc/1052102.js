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
        var sumi = "#fn나눔고딕 Extrabold#이런... 어떻게하지...? 친구 돈을 잃어버리고 말았어... \r\n어쩌지...? 누가 좀 도와줬으면 좋겠지만... 나로썬.. 도저히...\r\n\r\n";
    if (!cm.haveItem(4031039,1)) {
        sumi += "#fs11##fUI/UIWindow2.img/UtilDlgEx/list1#\r\n#fn굴림##L1##d슈미의 잃어버린 동전";
    } else {
        sumi += "#fs11##fUI/UIWindow2.img/UtilDlgEx/list3#\r\n#fn굴림##L2##d슈미의 잃어버린 동전";
    }
        cm.sendSimple(sumi);
    } else {
        cm.dispose();
    }
    } else if (status == 1) {
            if (selection == 2 ) {
	cm.sendOk("#fn나눔고딕 Extrabold#날 도와줘서 정말 고마워! 이건 내 작은 보답이야!\r\n\\r\n#fUI/UIWindow2.img/QuestIcon/4/0#\r\n#i4021031# #b뒤틀린 시간의 정수#k #r100 개#k");
	cm.gainItem(4031039,-1);
	cm.gainItem(4021031, 100);
	cm.forceStartQuest(500);
	cm.showEffect(false,"monsterPark/clear");
        cm.playSound(false,"Field.img/Party1/Clear");
	cm.dispose();
        } else {
        cm.sendNextS("#fn나눔고딕 Extrabold##d(엄청 곤란해 보이는 여자 아이가 있다. 무슨일인지 물어보자..)",2);
        }
    } else if (status == 2) {
        cm.sendNextPrevS("#fn나눔고딕 Extrabold##b저기…? 무슨 일 있어..?#k",2);
    } else if (status == 3) {
        cm.sendNextPrev("#fn나눔고딕 Extrabold#난 망했어.. 우에엥ㅠ_ㅠ 친구 돈을 그렇게 쉽게 잃어버리다니..\r\n하필 그.. 험난한.. #b지하철 역#k 에서 잃어버린 거야...ㅠㅠ\r\n대체.. 왜! ㅠㅠ… 하.. 누가 도와주지 않으려나..?");
    } else if (status == 4) {
        cm.sendNextS("#fn나눔고딕 Extrabold##d(얘기를 듣지 않아도 어떤 일인지 알 것 같다, 어떻게 할까?)#k\r\n\r\n#fs13##L1##b슈미를 도와준다.\r\n#L2#알 게 뭐야? 갈 길 간다.#k",2);
    } else if (status == 5) {
            if (selection == 2) {
	cm.sendOkS("#fn나눔고딕 Extrabold##b슈미가 죽던 말던 알빠 아니지! 하던거나 하자..#k",2);
	cm.dispose();
	} else {
        	cm.sendYesNo("#fn나눔고딕 Extrabold##fs15#정말..!? #fs12#정말 나를 도와주는거야 #b#h ##k !?\r\n신난다!! 고마워..!! 정말.. 정말 고마워!! ㅠㅠ..\r\n돈을 찾아와 준다면 내가 너에게 도움이 되는걸 줄게!\r\n\r\n#fUI/UIWindow2.img/QuestIcon/3/0#\r\n#i4021031# #b뒤틀린 시간의 정수#k #r100 개#k\r\n\r\n#d그럼 지금 바로 잃어버린 돈을 찾으러 갈래..??#k");
    }
    } else if (status == 6) { 
        cm.warp(910360000,0);
        cm.sendOk("#fn나눔고딕 Extrabold#내 #b동전#k 을 찾으면 다시 나에게 와줘! 그럼 부탁할게..!!");
      }
    }

