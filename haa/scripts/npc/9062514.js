


/*

	* 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

	* (Guardian Project Development Source Script)

	신궁 에 의해 만들어 졌습니다.

	엔피시아이디 : 9062513

	엔피시 이름 : 나무의 정령

	엔피시가 있는 맵 : 블루밍 포레스트 : 꽃 피는 숲 (993192000)

	엔피시 설명 : 블루밍 쉐어 코인샵


*/

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
        if (cm.getPlayer().getKeyValue(100790, "shopTutoP") == 1) {
            //오픈샵
            cm.openShop(24);
            cm.dispose();
            return;
        }
        cm.sendNextS("... 음? 이방인이군.", 4, 9062514);
    } else if (status == 1) {
        cm.sendNextPrevS("이곳에서 만난 것도 분명 뭔가의 인연.\r\n특별히 내가 가진 #b#e성장#n#k에 도움이 되는 특별한 아이템과 #b#e반지#n#k, 너를 #b#e예쁘게#n#k 꾸며 줄 아이템들을 보여줄까?", 4, 9062514);
    } else if (status == 2) {
        cm.sendNextPrevS("#b#e#i4310310:# #t4310310:##n#k을 내게 주면 이 특별한 아이템들을 주도록 하지.\r\n\r\n#e※블루밍 퍼스널 코인샵 이용 기간#r\r\n6월 20일 오후 11시 59분까지#k#n", 4, 9062514);
    } else if (status == 3) {
        cm.getPlayer().setKeyValue(100790, "shopTutoP", "1");
        cm.openShop(24);
        cm.dispose();
        //상점오픈
    }
}
